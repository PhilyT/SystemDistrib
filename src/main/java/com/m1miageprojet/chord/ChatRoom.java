package com.m1miageprojet.chord;

import java.util.ArrayList;

public class ChatRoom {
	private ArrayList<ChordPeer> noeuds;
	private byte[] message;
	
	public ChatRoom(ChordPeer noeud,byte[] message){
		this.noeuds.add(noeud);
		this.message=message;
		
	}
	public ArrayList<ChordPeer> getNoeuds() {
		return noeuds;
	}
	public void setNoeuds(ArrayList<ChordPeer> noeuds) {
		this.noeuds = noeuds;
	}
	public byte[] getMessage() {
		return message;
	}
	public void setMessage(byte[] message) {
		this.message = message;
	}

}
