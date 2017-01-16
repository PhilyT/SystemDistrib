package com.m1miageprojet.tcpcommunication;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.m1miageprojet.chord.ChordPeer;

public class Request {
	private ChordPeer peer;

	public Request(ChordPeer peer) {
		this.peer = peer;
	}

	public void sendRequest(String req, ChordPeer distPeer) {
		sendRequest(req, peer, distPeer);
	}
	
	/**
	 * Send a message to a recipient
	 * @param req as a String
	 * @param sender
	 * @param distPeer as the recipient
	 */
	public void sendRequest(String req, ChordPeer sender, ChordPeer distPeer) {
		DataSender ds = new DataSender();
		JSONObject jsonReq;
		try {
			jsonReq = new JSONObject(req);
		} catch (JSONException e1) {
			jsonReq = new JSONObject();
			try {
                                jsonReq.put("expe", sender.toJSON(1));
                                jsonReq.put("dest", distPeer.toJSON(1));
                                if ("JOIN".equals(req)) {
                                        jsonReq.put("req", "JOIN");					
                                } else if ("LEAVE".equals(req)) {
                                        jsonReq.put("req", "LEAVE");
                                        System.out.println("Requete: le pair " + peer.getMyId() + " va quitter le Chord");
                                        peer.leaveChord();
                                }
				else if ("REP_JOIN".equals(req))
				{
					jsonReq.put("req", "REP_JOIN");
				}
			} catch (JSONException e2) {
				e2.printStackTrace();
			}
		}
		ds.send(jsonReq.toString().getBytes(), distPeer.getIp(), distPeer.getPort());
	}
	
	/**
	 * Send a message to a recipient
	 * @param req as a byte array
	 * @param distPeer as the recipient
	 */
	public void sendRequest(byte[] req, ChordPeer distPeer) {
		DataSender ds = new DataSender();
		JSONObject jsonReq = new JSONObject();
		try {
			jsonReq.put("expe", peer.toJSON(1));
			jsonReq.put("dest", distPeer.toJSON(1));
			jsonReq.put("req", "chat");
			JSONArray jsonByteArray = new JSONArray();
			for(byte b : req)
			{
				jsonByteArray.put(b);
			}
			jsonReq.put("message", jsonByteArray);
			ds.send(jsonReq.toString().getBytes(), distPeer.getIp(), distPeer.getPort());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void processRequest(String req) {
		try {
			JSONObject jsonReq = new JSONObject(req);
			ChordPeer expe = new ChordPeer(jsonReq.getJSONObject("expe"));
			ChordPeer dest = new ChordPeer(jsonReq.getJSONObject("dest"));
			String reqName = jsonReq.getString("req");
			if(!reqName.equals("REP_JOIN") && !peer.equals(expe))
			{
				if (reqName.equals("JOIN")) {
					if(peer.equals(dest))
					{
						sendRequest("REP_JOIN", expe);
					}
					expe.joinChord(peer);
					System.out.println("un peer vient de joindre le Chord avec l'Id: " + expe.getMyId());
				} else if (reqName.equals("LEAVE")) {
					System.out.println("un peer " + expe.getMyId() + " vient de quitter le Chord");
				}
				else if (reqName.equals("chat")){
					JSONArray jsonByteArray = jsonReq.getJSONArray("message");
					byte[] msgByteArray = new byte[jsonByteArray.length()];
					for(int i = 0; i < jsonByteArray.length(); i++)
					{
						msgByteArray[i] = Byte.parseByte(jsonByteArray.getString(i));
					}
					System.out.println(new String(msgByteArray));
				}
				peer.forwardMessage(req, expe);
			}
			else if(reqName.equals("REP_JOIN"))
			{
				
				System.out.println("Requete: jointure du pair " + peer.getMyId());
				peer.joinChord(expe);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
