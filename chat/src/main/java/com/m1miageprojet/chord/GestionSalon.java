package com.m1miageprojet.chord;

import java.util.ArrayList;
import java.util.Hashtable;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GestionSalon {

	private Hashtable<Long, ChatRoom> salles;
	private ChordPeer peer;

	public GestionSalon(ChordPeer peer) {

		this.salles = new Hashtable<>();
		String s=""+peer.getMyId()+peer.getPort();
		creatChatRoom(peer, Integer.parseInt(s));
		this.peer = peer;

	}

	public Hashtable<Long, ChatRoom> getChatRoomList() {
		return salles;
	}
	
	public void setChatRoom(JSONObject o){
		try {
			JSONArray json= o.getJSONArray("salons");
			this.salles = new Hashtable<Long,ChatRoom>();
			for(int i=0; i<json.length();i++){
				ChatRoom chat=new ChatRoom(json.getJSONObject(i));
				salles.put(chat.getId(), chat);
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}

	public void joinChatRoom(long chatKey, ChordPeer newNoeud) {
		if (salles.containsKey(chatKey)) {

			salles.get(chatKey).getNoeuds().add(newNoeud);
		} else {
			System.out.println("la salle nexiste pas il faut la cree");

		}

	}

	public void creatChatRoom(ChordPeer noeud, long keyChatRoom) {
		ChatRoom newChat = new ChatRoom(noeud, keyChatRoom);
		salles.put(keyChatRoom, newChat);

	}

	public void sendToChatRoom(String s,  long chatKey, ChordPeer noeud) {
		if (salles.containsKey(chatKey)) {
			if (salles.get(chatKey).getNoeuds().contains(noeud)) {
				salles.get(chatKey).getMessages().add(s);

			} else {
				System.out.println("rejoins le salon pou pouvoir envoyer ton message");
			}

		} else
			System.out.println("le salon nexiste pas");

	}

	public ArrayList<String> readChatRoom(long chatKey) {
		ArrayList<String> s = new ArrayList<String>();
		if (salles.containsKey(chatKey)) {
			if (salles.get(chatKey).getNoeuds().contains(this.peer)) {
				s = salles.get(chatKey).getMessages();
			} else
				System.out.println("rejoins le salon pour pouvoir lire les messages");

		} else
			System.out.println("le salon nexiste pas");

		return s;
	}

	public String readlastMessage(long chatKey) {
		String s = null;
		if (salles.containsKey(chatKey)) {
			if (salles.get(chatKey).getNoeuds().contains(this.peer)) {
				s = salles.get(chatKey).getMessages().get(salles.get(chatKey).getMessages().size() - 1);
			} else
				System.out.println("rejoins le salon pour pouvoir lire les messages");

		} else
			System.out.println("le salon nexiste pas");

		return s;
	}

	public JSONObject toJSON() {

		JSONObject json = new JSONObject();
    	try 
		{
			JSONArray salonJSON = new JSONArray();
			for(ChatRoom c : salles.values()){
				salonJSON.put(c.toJson());
			}
			
			json.put("salons", salonJSON);
			
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
    	return json;

	}

}
