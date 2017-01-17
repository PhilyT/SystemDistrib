package com.m1miageprojet.chord;

import java.util.ArrayList;
import java.util.Hashtable;

public class GestionSalon {

	private Hashtable<Integer, ChatRoom> salles;
	private ChordPeer peer;

	public GestionSalon(ChordPeer peer) {

		this.salles = new Hashtable<>();
		this.peer= peer;

	}

	public Hashtable<Integer, ChatRoom> getChatRoomList() {
		return salles;
	}

	public void joinChatRoom(int chatKey, ChordPeer newNoeud) {
		if (salles.containsKey(chatKey)) {

			salles.get(chatKey).getNoeuds().add(newNoeud);
		} else {
			System.out.println("la salle nexiste pas il faut la cree");

		}

	}

	public void creatChatRoom(ChordPeer noeud, int keyChatRoom) {
		ChatRoom newChat = new ChatRoom(noeud, keyChatRoom);
		salles.put( keyChatRoom, newChat);

	}

	public void sendToChatRoom(String s, int chatKey, ChordPeer noeud) {
		if(salles.containsKey(chatKey)){
			if (salles.get(chatKey).getNoeuds().contains(noeud)) {
				salles.get(chatKey).getMessages().add(s);

			}
			else{
				System.out.println("rejoins le salon pou pouvoir envoyer ton message");
			}
			
		}
		else
			System.out.println("le salon nexiste pas");
		
		
		

	}

	public ArrayList<String> readChatRoom(int chatKey) {
		 ArrayList<String>s = new  ArrayList<String>();
		if(salles.containsKey(chatKey)){
			if (salles.get(chatKey).getNoeuds().contains(this.peer)) {
				s=salles.get(chatKey).getMessages();
			}
			else
				System.out.println("rejoins le salon pou pouvoir lir les messages");
			
		}
		else
			System.out.println("le salon nexiste pas");
			
		
			
		return s;
	}
	public String readlastMessage(int chatKey) {
		 String s = null;
		if(salles.containsKey(chatKey)){
			if (salles.get(chatKey).getNoeuds().contains(this.peer)) {
				s=salles.get(chatKey).getMessages().get(salles.get(chatKey).getMessages().size()-1);
			}
			else
				System.out.println("rejoins le salon pou pouvoir lir les messages");
			
		}
		else
			System.out.println("le salon nexiste pas");
			
		
			
		return s;
	}

	

}
