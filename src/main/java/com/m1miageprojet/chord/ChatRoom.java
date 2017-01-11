package com.m1miageprojet.chord;

import java.util.ArrayList;

public class ChatRoom {
	private ArrayList<ChordPeer> noeuds = new ArrayList<ChordPeer>();
	private int id;
	private ArrayList<String> messages;
	
	public ChatRoom(ChordPeer noeud,int id){
		this.noeuds.add(noeud);
		this.setId(id);
		this.messages = new ArrayList<String>();
		
	}
	public ArrayList<ChordPeer> getNoeuds() {
		return noeuds;
	}
	public void setNoeuds(ArrayList<ChordPeer> noeuds) {
		this.noeuds = noeuds;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ArrayList<String> getMessages() {
		return messages;
	}
	public void setMessages(ArrayList<String> messages) {
		this.messages = messages;
	}
	
}
