package com.m1miageprojet.annuaire;

import com.m1miageprojet.chord.ChatRoom;
import com.m1miageprojet.chord.ChordPeer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by ch-ha_000 on 11/01/2017.
 */
public class Annuaire {

    ServerSocket serveur;
    ArrayList<Thread> threadsClients;
    private ArrayList<ChordPeer> listUsers;
    private ArrayList<ChordPeer> listDisconnectedUsers;
    private ArrayList<ChatRoom> listChatRooms;

    public ChordPeer getLastConnected() {
        return lastConnected;
    }

    public void setLastConnected(ChordPeer lastConnected) {
        this.lastConnected = lastConnected;
    }

    private ChordPeer lastConnected;

    public Annuaire(int port) throws IOException {
        serveur = new ServerSocket(port);
        threadsClients = new ArrayList<Thread>();
        this.listUsers= new ArrayList<ChordPeer>();
        this.listDisconnectedUsers = new ArrayList<ChordPeer>();
        this.listChatRooms = new ArrayList<ChatRoom>();
    }

    public synchronized void addNewUser(ChordPeer newUser) {
        listUsers.add(newUser);
        System.out.println("Nouvel utilsateur ajouté");
    }

    public ArrayList<ChordPeer> getListUsers() {
            return listUsers;
    }


    public Socket waitClient() throws IOException {return serveur.accept();}

    public ArrayList<Thread> getThreadsClients() {return threadsClients;}

    public static void main(String[] args) {
        try
        {
            int portNumber = 2000;
            Annuaire servertest = new Annuaire(portNumber);
            System.out.println("Serveur ecoute "+ portNumber + "...");
            int cpt = 0;
            while(true)
            {
                Socket clientSocket = servertest.waitClient();
                servertest.getThreadsClients().add(new Thread(new Gestionnaire(clientSocket, servertest)));
                servertest.getThreadsClients().get(cpt).start();
                cpt++;
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("Serveur en arret !");
        }
        catch(NumberFormatException nbe)
        {
            nbe.printStackTrace();
            System.out.println("Serveur en arret !");
        }
    }



}


