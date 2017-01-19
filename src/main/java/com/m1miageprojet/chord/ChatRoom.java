package com.m1miageprojet.chord;

import java.util.ArrayList;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ChatRoom {
	private ArrayList<ChordPeer> noeuds = new ArrayList<ChordPeer>();
	private long id;
	private ArrayList<String> messages;

	public ChatRoom(ChordPeer noeud, long id) {
		this.noeuds.add(noeud);
		this.setId(id);
		this.messages = new ArrayList<String>();

	}

	public ChatRoom(JSONObject o) {
		try {
			JSONArray noeudsJSON = o.getJSONArray("noeuds");
			JSONArray messagesJSON = o.getJSONArray("messages");
			this.id = o.getInt("id");
			this.messages = new ArrayList<String>();
			for (int i = 0; i < noeudsJSON.length(); i++) {
				
				this.noeuds.add(new ChordPeer(noeudsJSON.getJSONObject(i)));
			}
			for (int i = 0; i < messagesJSON.length(); i++) {
				
				this.messages.add(messagesJSON.getString(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<ChordPeer> getNoeuds() {
		return noeuds;
	}

	public void setNoeuds(ArrayList<ChordPeer> noeuds) {
		this.noeuds = noeuds;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ArrayList<String> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<String> messages) {
		this.messages = messages;
	}

	public JSONObject toJson() {

		JSONObject json = new JSONObject();
		try {
			json.put("id", getId());
			JSONArray noeudsJSON = new JSONArray();
			JSONArray messagesJSON = new JSONArray();
			for (ChordPeer c : noeuds) {
				noeudsJSON.put(c.toJSON(c, true));
			}
			for (String s : messages) {
				noeudsJSON.put(s);
			}
			json.put("noeuds", noeudsJSON);
			json.put("messages", messagesJSON);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;

	}

}
