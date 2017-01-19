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

        for (int i = 0; i < finger.length; i++) {
            finger[i] = new FingerTable();
            finger[i].setId((int) (myId + Math.pow(2, i) % maxKeyValue));
            //finger[i].setIp(findkey(finger[i].getId()).getIp());
        }
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
        finger = new FingerTable[(int) Math.log(maxKeyValue)];

        for (int i = 0; i < finger.length; i++) {
            finger[i] = new FingerTable();
            finger[i].setId((int) (myId + Math.pow(2, i) % maxKeyValue));
            ///finger[i].setIp(findkey(finger[i].getId()).getIp());
        }
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
            if(json.has("succ") && json.has("pred") && json.getJSONObject("pred").getInt("key") == json.getJSONObject("succ").getInt("key"))
        	{
        		ChordPeer sameChordPeer = new ChordPeer(json.getJSONObject("succ"), this, this);
        		this.succ = sameChordPeer;
        		this.pred = sameChordPeer;
        	}
            else
            {
            	if (json.has("succ")) {
                	
                    this.succ = new ChordPeer(json.getJSONObject("succ"), this, this);
                } else {
                    this.succ = this;
                }
                if(json.has("pred")){
                	this.pred = new ChordPeer(json.getJSONObject("pred"), this, this);
                } else{
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
            	if(json.getJSONObject("succ").getInt("key") == base.getMyId())
            	{
            		this.succ = base;
            	}else{
            		this.succ = new ChordPeer(json.getJSONObject("succ"), base, this);
            	}
            } else {
                this.succ = chordPeer;
            }
            if(json.has("pred")){
            	if(json.getJSONObject("pred").getInt("key") == base.getMyId())
            	{
            		this.pred = base;
            	}else{
            		this.pred = new ChordPeer(json.getJSONObject("pred"), base, this);
            	}
            } else{
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
            for (int i = finger.length-1; i >= 0; i--) {
                if (finger[i].getId() > myId && finger[i].getId() < key) {
                    return findkey(finger[i].getId());
                }
            }
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
        this.setPred(succ.pred);
        pred.setSucc(this);
        succ.setPred(this);
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
     * @param data
     *            is some data to communicate
     */
    public void sendData(byte[] data) {
        Request req = new Request(this);
        req.sendRequest(data, succ);
    }

    /**
     * sends message by a given chord peer
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
            if(!succ.equals(this)&& !pred.equals(this)&& b)
        	{
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
            if(!chordPeersVisits.contains(succ))
        	{
        		json.put("succ", succ.toJSON(iter));
        	}
            else if(chordPeersVisits.get(0).equals(succ))
            {
            	json.put("succ", succ.toJSON(succ, false));
            }
            if(!chordPeersVisits.contains(pred))
        	{
        		json.put("pred", pred.toJSON(iter));
        	}
            else if(chordPeersVisits.get(0).equals(pred))
            {
            	json.put("pred", pred.toJSON(pred, false));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

}
