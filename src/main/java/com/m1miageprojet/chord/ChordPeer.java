package com.m1miageprojet.chord;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import com.m1miageprojet.tcpcommunication.ConnexionListener;
import com.m1miageprojet.tcpcommunication.DataSender;
import com.m1miageprojet.tcpcommunication.Request;

public class ChordPeer {

    private int myId;
    private int port;
    private String ip;
    private ChordPeer succ;
    private ChordPeer pred;
    private int maxKeyValue;
    private byte[] data;

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
    }

    /**
     * Constructor for succ and prec.
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
     *
     * @param key
     * @return the ip's node responsible for the key
     */
    public ChordPeer findkey(int key) {
        if (key >= maxKeyValue) {
            return null;
        }

        if (pred == this) {
            return this;//this.ip+":"+this.port+";"+pred.getIp()+":"+pred.getPort();
        }

        int predecessorId = pred.myId;
        if (predecessorId < this.myId && key > predecessorId && key <= this.myId) {
            return this;//.ip+":"+this.port+";"+pred.getIp()+":"+pred.getPort();
        } else if (predecessorId > this.myId && key <= this.myId) {
            return this;//.ip+":"+this.port+";"+pred.getIp()+":"+pred.getPort();
        } else if (predecessorId > this.myId && key >= predecessorId) {
            return this;//.ip+":"+this.port+";"+pred.getIp()+":"+pred.getPort();
        } else if (succ.myId < this.myId) {
            return succ;//.getIp()+":"+succ.getPort()+";"+this.ip+":"+this.port;
        } else {
            /*DataSender sender = new DataSender(this);
			sender.send(("FindMainChord "+key).getBytes(), ip, port);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new String(data);*/
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
        succ.pred = this.pred;
        pred.succ = this.succ;
        Request reqPred = new Request(this, pred.getPort());
        reqPred.sendRequest("LEAVE", pred);
        Request reqSucc = new Request(this, succ.getPort());
        reqSucc.sendRequest("LEAVE", succ);
        
    }

    /**
     * sends data to a given chord peer
     *
     * @param data is some data to communicate
     */
    public void sendData(byte[] data) {
        DataSender sender = new DataSender(this);
        System.out.println("print port succ : " + succ.getPort());
        sender.send(data, succ.getIp(), succ.getPort());
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
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * @return the className with value of myId
     */
    @Override
    public String toString() {
        return "ChordPeer [myId=" + myId + "]";
    }
}
