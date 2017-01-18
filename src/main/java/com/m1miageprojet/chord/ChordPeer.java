package com.m1miageprojet.chord;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.m1miageprojet.tcpcommunication.ConnexionListener;
import com.m1miageprojet.tcpcommunication.Request;

public class ChordPeer {

    private int myId;
    private int port;
    private String ip;
    private ChordPeer succ;
    private ChordPeer pred;
    private int maxKeyValue;
    private GestionSalon gestionSalon;
    private FingerTable[] finger;

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
        finger = new FingerTable[(int) Math.log(maxKeyValue)];
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
    }

    /**
     * Constructor with JSON object
     * 
     * @param json
     */
    public ChordPeer(JSONObject json) {
        try {
            int profondeur = json.getInt("profondeur");
            this.ip = json.getString("ip");
            this.port = json.getInt("port");
            this.maxKeyValue = json.getInt("maxKeyValue");
            this.myId = json.getInt("key");
            if (profondeur > 0) {
                this.succ = new ChordPeer(json.getJSONObject("succ"));
                this.pred = new ChordPeer(json.getJSONObject("pred"));
                ;
            } else {
                this.succ = this;
                this.pred = this;
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
        } else if (predecessorId > this.myId && key >= predecessorId) {
            return this;
        } else if (succ.myId < this.myId) {
            return succ;
        } else {
            return succ.findkey(key);
        }
    }

    /**
     * joins the chord
     *
     * @param chordPeerHandle
     */
    public void joinChord(ChordPeer peer) {

        ChordPeer successeur = peer.findkey(getMyId());
        ChordPeer predecesseur = successeur.getPred();
        successeur.setPred(this);
        this.setPred(predecesseur);
        pred.setSucc(this);
        this.setSucc(successeur);
    }

    /**
     * leaves the chord
     */
    public void leaveChord() {
        // succ.pred = this.pred;
        // pred.succ = this.succ;
        // notfier : LEAVE CHORD
        Request req = new Request(this);
        req.sendRequest("LEAVE", succ);
    }

    /**
     * sends data to a given chord peer
     *
     * @param data
     *            is some data to communicate
     */
    public void sendData(byte[] data) {
        Request req = new Request(this);
        req.sendRequest(data, succ);
    }

    /**
     * sends message to a given chord peer
     * 
     * @param msg
     *            as a String
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
        ConnexionListener listener = new ConnexionListener(this);
        listener.listen(req);
    }

    /**
     * forward the message if peer is not expe
     * 
     * @param msg
     *            as a string
     * @param expe
     *            as the sender
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
     * @param myId
     *            the myId to set
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
     * @param ip
     *            the ip to set
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
     * @param port
     *            the port to set
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
     * @param succ
     *            the succ to set
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
     * @param pred
     *            the pred to set
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
     * @param gestionSalon
     *            the gestionSalon to set
     */
    public void setGestionSalon(GestionSalon gestionSalon) {
        this.gestionSalon = gestionSalon;
    }

    /**
     * @return the className with value of myId
     */
    @Override
    public String toString() {
        return "ChordPeer [myId=" + myId + "]";
    }

    /**
     * 
     * @param peer's
     *            profondeur
     * @return JSONObject
     */
    public JSONObject toJSON(int profondeur) {
        JSONObject json = new JSONObject();
        try {
            json.put("ip", getIp());
            json.put("port", getPort());
            json.put("key", getMyId());
            json.put("profondeur", profondeur);
            json.put("maxKeyValue", maxKeyValue);
            if (profondeur > 0) {
                json.put("succ", succ.toJSON(profondeur - 1));
                json.put("pred", pred.toJSON(profondeur - 1));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

}
