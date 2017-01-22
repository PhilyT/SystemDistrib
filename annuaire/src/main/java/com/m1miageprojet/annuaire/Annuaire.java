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
        return listUsers.get(listUsers.size()-1);
    }

    public Annuaire(int port) throws IOException {
        serveur = new ServerSocket(port);
        threadsClients = new ArrayList<Thread>();
        this.listUsers= new ArrayList<ChordPeer>();
        this.setListDisconnectedUsers(new ArrayList<ChordPeer>());
        this.setListChatRooms(new ArrayList<ChatRoom>());
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

	/**
	 * @return the listChatRooms
	 */
	public ArrayList<ChatRoom> getListChatRooms() {
		return listChatRooms;
	}

	/**
	 * @param listChatRooms the listChatRooms to set
	 */
	public void setListChatRooms(ArrayList<ChatRoom> listChatRooms) {
		this.listChatRooms = listChatRooms;
	}

	/**
	 * @return the listDisconnectedUsers
	 */
	public ArrayList<ChordPeer> getListDisconnectedUsers() {
		return listDisconnectedUsers;
	}

	/**
	 * @param listDisconnectedUsers the listDisconnectedUsers to set
	 */
	public void setListDisconnectedUsers(ArrayList<ChordPeer> listDisconnectedUsers) {
		this.listDisconnectedUsers = listDisconnectedUsers;
	}



}


