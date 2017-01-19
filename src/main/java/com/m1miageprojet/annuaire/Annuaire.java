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

    public Annuaire(int port) throws IOException {
        serveur = new ServerSocket(port);
        threadsClients = new ArrayList<Thread>();
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
                servertest.getThreadsClients().add(new Thread(new Gestionnaire(clientSocket)));
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


