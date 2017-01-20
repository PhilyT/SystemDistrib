package com.m1miageprojet.chord;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.m1miageprojet.tcpcommunication.ConnexionListener;
import com.m1miageprojet.tcpcommunication.Request;

public class ChordPeer {

    public static int KEY_LENGTH = 64;
    private int myId;
    private int port;
    private String ip;
    private ChordPeer succ;
    private ChordPeer pred;
    private int maxKeyValue;
    private GestionSalon gestionSalon;
    private FingerTable fingerTable = new FingerTable(this);
    private ConnexionListener listener;

    /**
     * Constructor for the Main Peer
     *
     * @param maxKeyValue
     * @param port
     */
    public ChordPeer(int maxKeyValue, int port) {
        try {
            this.ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.maxKeyValue = maxKeyValue;
        this.port = port;
        this.myId = new Random().nextInt(maxKeyValue);
        this.succ = this;
        this.pred = this;
        this.setGestionSalon(new GestionSalon(this));
        /*finger = new FingerTable[(int) Math.log(maxKeyValue)];

        for (int i = 0; i < finger.length; i++) {
            finger[i] = new FingerTable();
            finger[i].setId((int) (myId + Math.pow(2, i) % maxKeyValue));
            //finger[i].setIp(findkey(finger[i].getId()).getIp());
        }*/

    }

    /**
     * Constructor for recipients peers
     *
     * @param maxKeyValue
     * @param ip
     * @param port
     */
    public ChordPeer(int maxKeyValue, String ip, int port) {
        this.ip = ip;
        this.maxKeyValue = maxKeyValue;
        this.port = port;
        this.myId = new Random().nextInt(maxKeyValue);
        this.succ = this;
        this.pred = this;
        /*finger = new FingerTable[(int) Math.log(maxKeyValue)];

        for (int i = 0; i < finger.length; i++) {
            finger[i] = new FingerTable();
            finger[i].setId((int) (myId + Math.pow(2, i) % maxKeyValue));
            //finger[i].setIp(findkey(finger[i].getId()).getIp());
        }*/
    }

    /**
     * Constructor with JSON object
     *
     * @param json
     */
    public ChordPeer(JSONObject json) {
        try {
            this.ip = json.getString("ip");
            this.port = json.getInt("port");
            this.maxKeyValue = json.getInt("maxKeyValue");
            this.myId = json.getInt("key");
            if (json.has("succ") && json.has("pred") && json.getJSONObject("pred").getInt("key") == json.getJSONObject("succ").getInt("key")) {
                ChordPeer sameChordPeer = new ChordPeer(json.getJSONObject("succ"), this, this);
                this.succ = sameChordPeer;
                this.pred = sameChordPeer;
            } else {
                if (json.has("succ")) {

                    this.succ = new ChordPeer(json.getJSONObject("succ"), this, this);
                } else {
                    this.succ = this;
                }
                if (json.has("pred")) {
                    this.pred = new ChordPeer(json.getJSONObject("pred"), this, this);
                } else {
                    this.pred = this;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ChordPeer(JSONObject json, ChordPeer base, ChordPeer chordPeer) {
        try {
            this.ip = json.getString("ip");
            this.port = json.getInt("port");
            this.maxKeyValue = json.getInt("maxKeyValue");
            this.myId = json.getInt("key");
            if (json.has("succ")) {
                if (json.getJSONObject("succ").getInt("key") == base.getMyId()) {
                    this.succ = base;
                } else {
                    this.succ = new ChordPeer(json.getJSONObject("succ"), base, this);
                }
            } else {
                this.succ = chordPeer;
            }
            if (json.has("pred")) {
                if (json.getJSONObject("pred").getInt("key") == base.getMyId()) {
                    this.pred = base;
                } else {
                    this.pred = new ChordPeer(json.getJSONObject("pred"), base, this);
                }
            } else {
                this.pred = chordPeer;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param key
     * @return the ip's node responsible for the key
     */
    public ChordPeer findkey(int key) {
        if (key >= maxKeyValue) {
            return null;
        }

        if (pred == this) {
            return this;
        }

        int predecessorId = pred.myId;
        if (predecessorId < this.myId && key > predecessorId && key <= this.myId) {
            return this;
        } else if (predecessorId > this.myId && key <= this.myId) {
            return this;
        } else if (predecessorId > this.myId && key > predecessorId) {
            return this;
        } else {
            /*for (int i = finger.length-1; i >= 0; i--) {
                if (finger[i].getId() > myId && finger[i].getId() < key) {
                    return findkey(finger[i].getId());
                }
            }*/
            return succ.findkey(key);
        }
    }

    /**
     * joins the chord
     *
     * @param chordPeerHandle
     */
    public void joinChord(ChordPeer peer) {
        this.setSucc(peer.findkey(getMyId()));
        this.setPred(peer.findkey(succ.getPred().getMyId()));
        pred.setSucc(this);
        succ.setPred(this);
        ChordPeer preceding = this.getSucc().getPred();
        //Each node runs a “stabilization” protocol periodically in the background to update successor pointer and finger table
        this.stabilize();
        if (preceding == null) {
            this.getSucc().stabilize();
        } else {
            preceding.stabilize();
        }
        //periodically calls fix fingers to make sure its finger table entries are correct.
        this.fixFingers();
        this.showFingerTable();
    }

    /**
     * leaves the chord
     */
    public void leaveChord() {
        succ.pred = this.pred;
        pred.succ = this.succ;
        pred = this;
        succ = this;
    }

    /**
     * sends data to a given chord peer
     *
     * @param data is some data to communicate
     */
    public void sendData(byte[] data) {
        Request req = new Request(this);
        req.sendRequest(data, succ);
    }

    /**
     * sends message by a given chord peer
     *
     * @param msg as a String
     * @param sender
     */
    public void sendData(String msg, ChordPeer sender) {
        Request req = new Request(this);
        req.sendRequest(msg, sender, succ);
    }

    /**
     * run the ConnexionListener
     *
     * @param key
     */
    public void runListener(Request req) {
        // ChordPeer destinationPeer = findkey(key);
        listener = new ConnexionListener(this);
        listener.listen(req);
    }

    public void stopListener() {
        listener.stopConnection();
    }

    /**
     * forward the message if peer is not expe
     *
     * @param msg as a string
     * @param expe as the sender
     */
    public void forwardMessage(String msg, ChordPeer expe) {
        if (!equals(expe)) {
            sendData(msg, expe);
        }
    }

    @Override
    public boolean equals(Object expe) {
        return ((ChordPeer) expe).getMyId() == this.getMyId();
    }

    /**
     * @return the myId
     */
    public int getMyId() {
        return myId;
    }

    /**
     * @param myId the myId to set
     */
    public void setMyId(int myId) {
        this.myId = myId;
    }

    /**
     * @return the adresse ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the succ
     */
    public ChordPeer getSucc() {
        return succ;
    }

    /**
     * @param succ the succ to set
     */
    public void setSucc(ChordPeer succ) {
        this.succ = succ;
    }

    /**
     * @return the pred
     */
    public ChordPeer getPred() {
        return pred;
    }

    /**
     * @param pred the pred to set
     */
    public void setPred(ChordPeer pred) {
        this.pred = pred;
    }

    /**
     * @return the gestionSalon
     */
    public GestionSalon getGestionSalon() {
        return gestionSalon;
    }

    /**
     * @param gestionSalon the gestionSalon to set
     */
    public void setGestionSalon(GestionSalon gestionSalon) {
        this.gestionSalon = gestionSalon;
    }

    /**
     * Stabilize method
     *
     */
    public void stabilize() {
        ChordPeer peer = succ.getPred();
        if (peer != null) {
            long key = peer.getMyId();
            if ((this.equals(succ)) || (key > this.myId && key < succ.getMyId())) {
                succ = peer;
            }
        }
        succ.notifyPred(this);
    }

    /**
     *
     * @param peer
     */
    private void notifyPred(ChordPeer peer) {
        long key = peer.getMyId();
        if (pred == null || (key > pred.getMyId() && key < this.myId)) {
            pred = peer;
        }
    }

    /**
     * fix fingers
     */
    public void fixFingers() {

        for (int i = 0; i < KEY_LENGTH; i++) {
            Finger finger = fingerTable.getFingerList(i);
            long key = finger.getStart();
            //finger.setPeer(findSuccessor(key));
        }
    }

    /**
     *
     * @param key
     * @return
     */
    public ChordPeer findSuccessor(long key) {
        if (this == succ) {
            return this;
        }
        if ((key > this.myId && key < succ.getMyId()) || key == succ.getMyId()) {
            return succ;
        } else {
            ChordPeer peer = closestPrecedingPeer(key);
            if (peer == this) {
                return succ.findSuccessor(key);
            }
            return peer.findSuccessor(key);
        }
    }

    /**
     *
     * @param key
     * @return
     */
    public ChordPeer closestPrecedingPeer(long key) {
        for (int i = KEY_LENGTH - 1; i >= 0; i--) {
            Finger finger = fingerTable.getFingerList(i);
            long fingerKey = finger.getPeer().getMyId();
            if (fingerKey > this.myId && fingerKey < key) {
                return finger.getPeer();
            }
        }
        return this;
    }

    public void showFingerTable() {
        System.out.println("finger table du pair: " + this.toString());
        System.out.println(this.fingerTable.toString());
    }

    /**
     * @return the className with value of myId
     */
    @Override
    public String toString() {
        return "ChordPeer [myId=" + myId + "; succ=" + succ.getMyId() + "; pred=" + pred.getMyId() + "]";
    }

    /**
     *
     * @param chordPeerBase
     * @return JSONObject
     */
    public JSONObject toJSON(ChordPeer chordPeerBase, boolean b) {
        ArrayList<ChordPeer> chordPeersVisits = new ArrayList<ChordPeer>();
        chordPeersVisits.add(chordPeerBase);
        JSONObject json = new JSONObject();
        try {
            json.put("ip", getIp());
            json.put("port", getPort());
            json.put("key", getMyId());
            json.put("maxKeyValue", maxKeyValue);
            if (!succ.equals(this) && !pred.equals(this) && b) {
                json.put("succ", succ.toJSON(chordPeersVisits));
                chordPeersVisits.clear();
                chordPeersVisits.add(chordPeerBase);
                json.put("pred", pred.toJSON(chordPeersVisits));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     *
     * @param chordPeersVisits as the list of ChordPeers have visited
     * @return JSONObject
     */
    public JSONObject toJSON(ArrayList<ChordPeer> chordPeersVisits) {
        ArrayList<ChordPeer> iter = chordPeersVisits;
        iter.add(this);
        JSONObject json = new JSONObject();
        try {
            json.put("ip", getIp());
            json.put("port", getPort());
            json.put("key", getMyId());
            json.put("maxKeyValue", maxKeyValue);
            if (!chordPeersVisits.contains(succ)) {
                json.put("succ", succ.toJSON(iter));
            } else if (chordPeersVisits.get(0).equals(succ)) {
                json.put("succ", succ.toJSON(succ, false));
            }
            if (!chordPeersVisits.contains(pred)) {
                json.put("pred", pred.toJSON(iter));
            } else if (chordPeersVisits.get(0).equals(pred)) {
                json.put("pred", pred.toJSON(pred, false));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

}
