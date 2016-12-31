package com.m1miageprojet.chord;

import java.util.Random;

import com.m1miageprojet.tcpcommunication.ConnexionListener;
import com.m1miageprojet.tcpcommunication.DataSender;

/**
 * 
 * @author Tom
 *
 */
public class ChordPeer {
	private int myId;
	private int port;
	private String ip;
	private ChordPeer succ;
	private ChordPeer pred;

	public ChordPeer() {
		this.myId = new Random().nextInt(101);
		this.succ = this;
		this.pred = this;
	}

	/**
	 * 
	 * @param key
	 * @return the node responsible for the key
	 */
	public ChordPeer findkey(int key) {
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
	 * @param ChordPeerhandle
	 */
	public void joinChord(ChordPeer ChordPeerhandle) {
		ChordPeer s = ChordPeerhandle.findkey(myId);
		ChordPeer pred = s.pred;
		s.pred = this;
		this.pred = pred;
		pred.succ = this;
		this.succ = s;
	}

	/**
	 * leaves the chord
	 */
	public void leaveChord() {
		succ.pred = this.pred;
		pred.succ = this.succ;
	}

	/**
	 * sends data to a given chord peer
	 *
	 * @param key
	 *            to target the wanted peer
	 * @param data
	 *            is some data to communicate
	 */
	public void sendData(byte[] data) {
		DataSender sender = new DataSender(data, this.ip, this.port);
		sender.start();
	}

	/**
	 * establishes a connection to the peer using his key in the chord network
	 *
	 * @param key
	 */
	public void establishConnection(int key) {
		ChordPeer destinationPeer = findkey(key);
		ConnexionListener listener = new ConnexionListener(destinationPeer.port);
		listener.start();
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
	 * @return the className with value of myId
	 */
	@Override
	public String toString() {
		return "ChordPeer [myId=" + myId + "]";
	}
}