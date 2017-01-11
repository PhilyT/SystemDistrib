package com.m1miageprojet.annuaire;

import com.m1miageprojet.chord.ChatRoom;
import com.m1miageprojet.chord.ChordPeer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Annuaire {

    private int port = 2345;
    private String host = "127.0.0.1";
    private ServerSocket server;
    private boolean isRunning = true;
    public ArrayList<ChordPeer> listUsers;
    public ArrayList<ChordPeer> listDisconnectedUsers;
    public ArrayList<ChatRoom> listChatRooms;


    public Annuaire() throws IOException {
        this.listUsers= new ArrayList<ChordPeer>();
        this.listDisconnectedUsers = new ArrayList<ChordPeer>();
        this.listChatRooms = new ArrayList<ChatRoom>();
        this.server = new ServerSocket(this.port);
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

    public synchronized void Maj() {

    }

    public void run() {
        Socket clientSocket;
        try {
            while ((clientSocket = server.accept()) != null) {

                InputStream is = clientSocket.getInputStream();
                BufferedReader buffReader = new BufferedReader(new InputStreamReader(is));
                String msg = buffReader.readLine();
                if (msg != null) {
                    System.out.println(msg);
                }
            }
        } catch (IOException ex) {
            System.err.println("Erreur : lecture de données echoué.");
        }
    }
}
