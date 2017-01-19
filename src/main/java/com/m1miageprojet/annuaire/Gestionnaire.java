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
    private ArrayList<ChordPeer> listUsers;
    private ArrayList<ChordPeer> listDisconnectedUsers;
    private ArrayList<ChatRoom> listChatRooms;
    private ChordPeer lastConnected;


    public Gestionnaire(Socket socket) {
        this.client = socket;
        this.listUsers= new ArrayList<ChordPeer>();
        this.listDisconnectedUsers = new ArrayList<ChordPeer>();
        this.listChatRooms = new ArrayList<ChatRoom>();
    }

    public synchronized void addNewUser(ChordPeer newUser) {
        listUsers.add(newUser);
        System.out.println("Nouvel utilsateur ajouté");
    }

    public synchronized void addDisconnectedUser(ChordPeer cp) {
        listDisconnectedUsers.add(cp);
    }

    public ArrayList<ChordPeer> getListUsers() {
        return listUsers;
    }

    public ArrayList<ChordPeer> getListDisconnectedUsers() {
        return listDisconnectedUsers;
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
                listUsers.add(newUser);
                if (lastConnected == null) {
                    lastConnected = newUser;
                    return;
                }
                else {
                    newUser.joinChord(lastConnected);
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


    public void sendJSON(JSONObject jsonObject) throws IOException {
        JSONObject jsonObject2 = new JSONObject();
        OutputStream out = client.getOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(out);
        o.writeObject(jsonObject2);
        out.flush();

    }
}
