package com.m1miageprojet.tcpcommunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.m1miageprojet.chord.ChordPeer;

public class ConnexionListener extends Thread {

    private ServerSocket serverSocket;
    private ChordPeer peer;
    private Request req;

    /**
     * constructor
     * @param peer
     */
    public ConnexionListener(ChordPeer peer) {
        this.peer = peer;
    }

    /**
     * listen a port
     * @param req
     */
    public void listen(Request req) {
        try {
            serverSocket = new ServerSocket(peer.getPort());
            this.req = req;
        } catch (IOException ex) {

        }
        this.start();
    }

    @Override
    public void run() {

        Socket clientSocket;

        try {

            while ((clientSocket = serverSocket.accept()) != null) {

                InputStream is = clientSocket.getInputStream();
                BufferedReader buffReader = new BufferedReader(new InputStreamReader(is));
                String msg = buffReader.readLine();
                if (msg != null) {
                    req.processRequest(msg);
                }

            }

        } catch (IOException ex) {
            System.err.println("Erreur : lecture de données echoué.");
        }
    }
}
