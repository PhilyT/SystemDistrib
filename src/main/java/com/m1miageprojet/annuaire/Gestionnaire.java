package com.m1miageprojet.annuaire;

import com.m1miageprojet.chord.ChatRoom;
import com.m1miageprojet.chord.ChordPeer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.awt.print.Paper;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Gestionnaire implements Runnable {

    private Socket client;
    private Socket server;
    private Annuaire annuaire;


    public Gestionnaire(Socket socket, Annuaire annuaire) {
        this.client = socket;
    }



    public void lireClient(BufferedReader bufferClient) throws IOException, JSONException {
        int cpt = 1;
        String s = "";
        String inputLine = bufferClient.readLine();
        System.out.println(inputLine);
        while ((inputLine = bufferClient.readLine()) != null)
        {
            s += inputLine;
            System.out.println(inputLine);
            if (inputLine.isEmpty())
            {
                ChordPeer newUser = new ChordPeer(new JSONObject(inputLine));
                server = new Socket(newUser.getIp(), newUser.getPort());
                annuaire.addNewUser(newUser);
                if (annuaire.getLastConnected() == null) {
                    annuaire.setLastConnected(newUser);
                    return;
                }
                else {
                    newUser.joinChord(annuaire.getLastConnected());
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


    public void sendJSON() throws IOException {
        JSONObject jsonObject2 = annuaire.getLastConnected().toJSON(annuaire.getLastConnected(), true);
        server.getOutputStream().write(jsonObject2.toString().getBytes());
        server.close();
    }
}
