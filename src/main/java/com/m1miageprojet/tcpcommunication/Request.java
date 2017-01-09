/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m1miageprojet.tcpcommunication;

import com.m1miageprojet.chord.ChordPeer;

/**
 *
 * @author MAROUANE
 */
public class Request {
    private ChordPeer peer;

    private DataSender ds;
    private ConnexionListener cl;

    public Request(ChordPeer peer, int HandeledPeerPort) {
        this.peer = peer;
        this.ds = new DataSender(peer);
    }
    //create a new peer with port of chord 0
    public void sendRequest(String req, ChordPeer distPeer) {
        
        if ("JOIN".equals(req)) {
            
            System.out.println("Requete: jointure du pair " + peer.getMyId());
            ds.send(("JOIN_" + peer.getMyId()+"_"+peer.getPort()).getBytes(), "localhost", distPeer.getPort());
            peer.joinChord(distPeer);
            
        } else if ("LEAVE".equals(req)) {
            
            System.out.println("Requete: le pair " + peer.getMyId() + " va quitter le Chord");
            ds.send(("LEAVE_" + peer.getMyId()).getBytes(), "localhost", distPeer.getPort());
            peer.leaveChord();
            
        }

    }

    public void processRequest(String req) {
        String request = req.split("_")[0];
        String peerId = req.split("_")[1];
        int port = Integer.parseInt(req.split("_")[2]);
        if (request.equals("JOIN")) {
        	ChordPeer nouveau = new ChordPeer(101, port);
        	nouveau.setMyId(Integer.parseInt(peerId));
        	peer.joinChord(nouveau);
            System.out.println("un peer vient de joindre le Chord avec l'Id: " + peerId);
        } else if (request.equals("LEAVE")) {
            System.out.println("un peer " + peerId + " vient de joindre le Chord");
        }

    }
}
