package com.m1miageprojet.chord;

import com.m1miageprojet.chord.ChordPeer;

import java.util.ArrayList;


public class Annuaire {

    public ArrayList<ChordPeer> listUsers;
    public ArrayList<ChordPeer> listConnectedUsers;
    public ArrayList<ChordPeer> listDisconnectedUsers;

    public Annuaire() {
        this.listUsers= new ArrayList<ChordPeer>();
        this.listConnectedUsers = new ArrayList<ChordPeer>();
        this.listDisconnectedUsers = new ArrayList<ChordPeer>();
    }

    public synchronized void addNewUser(ChordPeer newUser) {
        listConnectedUsers.add(newUser);
        System.out.println("Nouvel utilsateur ajouté");
    }

    public synchronized void Maj(ChordPeer user) {

    }
}
