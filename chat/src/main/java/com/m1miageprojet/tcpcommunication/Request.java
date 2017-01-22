package com.m1miageprojet.tcpcommunication;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.m1miageprojet.chord.ChatRoom;
import com.m1miageprojet.chord.ChordPeer;

public class Request {
	private ChordPeer peer;
	private String annuaireIP;
	private int annuairePort;

	public Request(ChordPeer peer) {
		this.peer = peer;
	}
	
	public Request(ChordPeer peer, String annuaireIP, int port) {
		this.peer = peer;
		this.annuaireIP = annuaireIP;
		this.annuairePort = port;
	}

	/**
	 * Send a request to a recipient
	 * @param req
	 *            as a String
	 * @param distPeer
	 *            as the recipient
	 */
	public void sendRequest(String req, ChordPeer distPeer) {
		sendRequest(req, peer, distPeer);
	}

	/**
	 * Send a request to a recipient
	 * 
	 * @param req
	 *            as a String
	 * @param sender
	 * 				as the sender
	 * @param distPeer
	 *            as the recipient
	 */
	public void sendRequest(String req, ChordPeer sender, ChordPeer distPeer) {
		DataSender ds = new DataSender();
		JSONObject jsonReq;
		JSONObject annuaireReq;
		try {
			jsonReq = new JSONObject(req);
		} catch (JSONException e1) {
			jsonReq = new JSONObject();
			annuaireReq = new JSONObject();
			try {
				jsonReq.put("expe", sender.toJSON(sender, true));
				jsonReq.put("dest", distPeer.toJSON(distPeer, true));
				if ("JOIN".equals(req)) {
					jsonReq.put("salons", peer.getGestionSalon().toJSON());
					jsonReq.put("req", "JOIN");
				} else if ("LEAVE".equals(req)) {
					jsonReq.put("req", "LEAVE");
					System.out.println("Requete: quitter le Chord ..");
					peer.leaveChord();
					if(annuaireIP != null)
					{
						DataSender dsAnnuaire = new DataSender();
						annuaireReq.put("req", "LEAVE");
						annuaireReq.put("chordpeer", peer.toJSON(peer, true));
						dsAnnuaire.send(annuaireReq.toString().getBytes(), annuaireIP, annuairePort);
					}
				} else if ("REP_JOIN".equals(req)) {
					jsonReq.put("salons", peer.getGestionSalon().toJSON());
					jsonReq.put("req", "REP_JOIN");
				}
				if ("JOIN_SALON".equals(req)) {
					jsonReq.put("salons", peer.getGestionSalon().toJSON());
					jsonReq.put("req", "JOIN_SALON");
				} 
				else if("SMS_DANS_SALON".equals(req)){
					jsonReq.put("salons", peer.getGestionSalon().toJSON());
					jsonReq.put("messages", peer.getGestionSalon().toJSON());
					jsonReq.put("req", "SMS_DANS_SALON");
					
				}
				else if("CREAT_SALON".equals(req)){
					jsonReq.put("creation", peer.getGestionSalon().toJSON());
					jsonReq.put("req", "CREAT_SALON");
					
				}
			} catch (JSONException e2) {
				e2.printStackTrace();
			}
		}
		ds.send(jsonReq.toString().getBytes(), distPeer.getIp(), distPeer.getPort());
	}

	/**
	 * Send a message to a recipient
	 * 
	 * @param req
	 *            as a byte array
	 * @param distPeer
	 *            as the recipient
	 */
	public void sendRequest(byte[] req, ChordPeer distPeer) {
		DataSender ds = new DataSender();
		JSONObject jsonReq = new JSONObject();
		try {
			jsonReq.put("expe", peer.toJSON(peer, true));
			jsonReq.put("dest", distPeer.toJSON(peer, true));
			jsonReq.put("req", "chat");
			JSONArray jsonByteArray = new JSONArray();
			for (byte b : req) {
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
			ChordPeer expe = null;
			if(jsonReq.has("expe"))
			{
				expe= new ChordPeer(jsonReq.getJSONObject("expe"));
			}
			ChordPeer dest = new ChordPeer(jsonReq.getJSONObject("dest"));
			String reqName = jsonReq.getString("req");
			if (!reqName.equals("REP_JOIN") && expe != null && !peer.equals(expe)) {
				
				if (reqName.equals("JOIN")) {
					if(expe.equals(peer.findkey(expe.getMyId())) && dest.equals(peer))
					{
						return;
					}
					ChatRoom chat = new ChatRoom (jsonReq.getJSONObject("salons").getJSONArray("salons").getJSONObject(0));//on recupere le salon crer par lexp
					peer.getGestionSalon().getChatRoomList().put(chat.getId(), chat);
					expe.joinChord(peer);
					System.out.println("un peer vient de joindre le Chord : " + expe);
					
				} else if (reqName.equals("LEAVE")) {
					/*System.out.println("peer :" + peer);
					System.out.println("succ :" + peer.getSucc());
					System.out.println("pred :" + peer.getPred());
					System.out.println("suc suc suc :" + peer.getSucc().getSucc().getSucc());*/
					if(peer.equals(dest) && !peer.getPred().equals(expe))
					{
						return;//Casse la boucle infini d'envoi de messages
					}
					peer.findkey(expe.getMyId()).leaveChord();
					System.out.println("un peer " + expe + " vient de quitter le Chord");
					
				} else if (reqName.equals("chat")) {
					
					JSONArray jsonByteArray = jsonReq.getJSONArray("message");
					byte[] msgByteArray = new byte[jsonByteArray.length()];
					for (int i = 0; i < jsonByteArray.length(); i++) {
						msgByteArray[i] = Byte.parseByte(jsonByteArray.getString(i));
					}
					System.out.println(new String(msgByteArray));
				}

				else if(reqName.equals("JOIN_SALON")){
					peer.getGestionSalon().setChatRoom(jsonReq.getJSONObject("salons"));
				
					
				}
				else if(reqName.equals("SMS_DANS_SALON")){
					//peer.joinChord(expe);
					//peer.getGestionSalon().setChatRoom(jsonReq.getJSONObject("salons"));
					System.out.println("nouveau message dans le salon");
					peer.getGestionSalon().setChatRoom(jsonReq.getJSONObject("salons"));
					
					
				}
				else if(reqName.equals("CREAT_SALON")){
					//peer.joinChord(expe);
					//peer.getGestionSalon().setChatRoom(jsonReq.getJSONObject("salons"));
					
					System.out.println("creation nouvelle salle");
					peer.getGestionSalon().setChatRoom(jsonReq.getJSONObject("salons"));
					
					
				}
				peer.forwardMessage(req, expe);
			} else if (reqName.equals("REP_JOIN")) {
				if(expe != null)
				{
					sendRequest("JOIN", expe);
				}
				if(jsonReq.has("salons"))
				{
					peer.getGestionSalon().setChatRoom(jsonReq.getJSONObject("salons"));
				}	
			}
			else if (peer.equals(expe)&& reqName.equals("JOIN")) {
				if(expe != null)
				{
					if(!dest.findkey(peer.getMyId()).equals(peer))
					{
						peer.joinChord(dest);
						peer.forwardMessage(req, expe);
					}
				}	
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
