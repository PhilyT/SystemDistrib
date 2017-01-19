package com.m1miageprojet.annuaire;

import com.m1miageprojet.chord.ChatRoom;
import com.m1miageprojet.chord.ChordPeer;
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
        int cpt = 1;
        String s = "";
        String inputLine;
        while ((inputLine = bufferClient.readLine()) != null)
        {
            s += inputLine;
            System.out.println(inputLine);
            if (!inputLine.isEmpty())
            {
                ChordPeer newUser = new ChordPeer(new JSONObject(inputLine));
                server = new Socket(newUser.getIp(), newUser.getPort());
                annuaire.addNewUser(newUser);
                if (annuaire.getLastConnected() == null) {
                	sendJSON(new JSONObject(inputLine));
                    annuaire.setLastConnected(newUser);
                    return;
                }
                else {
                	sendJSON(new JSONObject(inputLine));
                	newUser.joinChord(annuaire.getLastConnected());
                    return;
                }
            }
        }
    }

    @Override
    public void run() {
        try
        {
            System.out.println("Client connecte !");
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            lireClient(in);
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("Serveur en arret !");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void sendJSON(JSONObject c) throws IOException {
        JSONObject jsonObject = new JSONObject();
        try {
        	if(annuaire.getLastConnected() != null){
                JSONObject chord = annuaire.getLastConnected().toJSON(annuaire.getLastConnected(), true);
                jsonObject.put("expe", chord);
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
