package com.m1miageprojet.annuaire;

import com.m1miageprojet.chord.ChatRoom;
import com.m1miageprojet.chord.ChordPeer;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class Gestionnaire implements Runnable {

	private Socket client;
	private Socket server;
	private Annuaire annuaire;

	public Gestionnaire(Socket socket, Annuaire annuaire) {
		this.client = socket;
		this.annuaire = annuaire;
	}

	public void lireClient(BufferedReader bufferClient) throws IOException, JSONException {
		String inputLine = bufferClient.readLine();
		boolean running = true;
		while (inputLine != null && running) {
			System.out.println(inputLine);
			if (!inputLine.isEmpty()) {
				JSONObject obj = new JSONObject(inputLine);
				if (obj.getString("req").equals("JOIN")) {
					ChordPeer newUser = new ChordPeer(obj.getJSONObject("chordpeer"));
					ChatRoom cahtroom = new ChatRoom(obj.getJSONObject("chatroom"));
					server = new Socket(newUser.getIp(), newUser.getPort());
					annuaire.getListChatRooms().add(cahtroom);
					ChordPeer oldLastConnected = null;
					if (!annuaire.getListUsers().isEmpty()) {
						oldLastConnected = annuaire.getLastConnected();
					}
					sendJSON(obj.getJSONObject("chordpeer"));

					annuaire.addNewUser(newUser);
					for (ChordPeer c : annuaire.getListUsers()) {
						if (!c.equals(newUser)) {
							newUser.joinChord(c);
						} else if (oldLastConnected != null) {
							oldLastConnected.joinChord(c);
						}
					}
					running = false;
				} else if (obj.getString("req").equals("LEAVE")) {
					ChordPeer discoUser = new ChordPeer(obj.getJSONObject("chordpeer"));
					for (ChordPeer c : annuaire.getListUsers()) {
						c.findkey(discoUser.getMyId()).leaveChord();
					}
					annuaire.getListUsers().remove(discoUser);
					annuaire.getListDisconnectedUsers().add(discoUser);
					running = false;
				}
			}
		}
	}

	@Override
	public void run() {
		try {
			System.out.println("Client connecte !");
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			lireClient(in);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Serveur en arret !");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void sendJSON(JSONObject c) throws IOException {
		JSONObject jsonObject = new JSONObject();
		try {
			if (!annuaire.getListUsers().isEmpty()) {
				JSONObject chord = annuaire.getLastConnected().toJSON(annuaire.getLastConnected(), true);
				jsonObject.put("expe", chord);
			}
			if (!annuaire.getListChatRooms().isEmpty()) {
				JSONObject s = new JSONObject();
				JSONArray salons = new JSONArray();
				for (ChatRoom chatroom : annuaire.getListChatRooms()) {
					salons.put(chatroom.toJson());
				}
				s.put("salons", salons);
				jsonObject.put("salons", s);
			}
			jsonObject.put("req", "REP_JOIN");
			jsonObject.put("dest", c);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		server.getOutputStream().write(jsonObject.toString().getBytes());
		server.close();
	}
}
