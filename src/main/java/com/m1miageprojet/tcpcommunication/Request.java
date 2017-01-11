package com.m1miageprojet.tcpcommunication;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.m1miageprojet.chord.ChordPeer;

public class Request {
	private ChordPeer peer;

	private DataSender ds;

	public Request(ChordPeer peer, int HandeledPeerPort) {
		this.peer = peer;
		this.ds = new DataSender();
	}

	public void sendRequest(String req, ChordPeer distPeer) {
		JSONObject jsonReq = new JSONObject();
		try {
			jsonReq.put("expe", peer.toJSON(1));
			jsonReq.put("dest", distPeer.toJSON(1));
			if ("JOIN".equals(req)) {
				jsonReq.put("req", "JOIN");
				System.out.println("Requete: jointure du pair " + peer.getMyId());
				ds.send(jsonReq.toString().getBytes(), distPeer.getIp(), distPeer.getPort());
				peer.joinChord(distPeer);

			} else if ("LEAVE".equals(req)) {
				jsonReq.put("req", "LEAVE");
				System.out.println("Requete: le pair " + peer.getMyId() + " va quitter le Chord");
				ds.send(jsonReq.toString().getBytes(), distPeer.getIp(), distPeer.getPort());
				peer.leaveChord();

			}
			else
			{
				jsonReq.put("req", "chat");
				jsonReq.put("message", req);
				ds.send(jsonReq.toString().getBytes(), distPeer.getIp(), distPeer.getPort());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void sendRequest(byte[] req, ChordPeer distPeer) {
		JSONObject jsonReq = new JSONObject();
		try {
			jsonReq.put("expe", peer.toJSON(1));
			jsonReq.put("dest", distPeer.toJSON(1));
			jsonReq.put("req", "chat");
			jsonReq.put("message", new String(req));
			ds.send(jsonReq.toString().getBytes(), distPeer.getIp(), distPeer.getPort());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void processRequest(String req) {
		try {
			JSONObject jsonReq = new JSONObject(req);
			ChordPeer expe = new ChordPeer(jsonReq.getJSONObject("expe"));
			if (jsonReq.getString("req").equals("JOIN")) {
				expe.joinChord(peer);
				System.out.println("un peer vient de joindre le Chord avec l'Id: " + expe.getMyId());
			} else if (jsonReq.getString("req").equals("LEAVE")) {
				System.out.println("un peer " + expe.getMyId() + " vient de quitter le Chord");
			}
			else if (jsonReq.getString("req").equals("chat")){
				System.out.println(jsonReq.getString("message"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
