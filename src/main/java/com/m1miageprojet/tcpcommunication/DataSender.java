package com.m1miageprojet.tcpcommunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.m1miageprojet.chord.ChordPeer;

public class DataSender extends Thread {

    private int port;
    private String ip;
    private byte[] data;
    private ChordPeer peer;

    public DataSender(ChordPeer peer) {
    	this.peer = peer;
    }
    
    /**
     *
     * @param data
     * @param ip
     * @param port
     */
    public synchronized void send(byte[] data, String ip, int port){
        this.port = port;
        this.ip = ip;
        this.data = data;
        this.start();
    }
    
    /**
     * run ..
     */
    @Override
    public void run() {

        try {

            Socket socket = new Socket(ip, port);
            //InputStream is = socket.getInputStream();
            //BufferedReader buffReader = new BufferedReader(new InputStreamReader(is));
            socket.getOutputStream().write(data);
            /*String msg = buffReader.readLine();
            if (msg != null) {
        		System.out.println("passe par ici");
                peer.setData(msg.getBytes());
            }*/
            socket.close();

        } catch (IOException ex) {
            System.err.println("connexion refusé sur le port " + port);
        }

    }
}
