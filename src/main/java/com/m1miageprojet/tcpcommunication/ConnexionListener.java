package com.m1miageprojet.tcpcommunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.m1miageprojet.chord.ChordPeer;

public class ConnexionListener extends Thread {
    private ServerSocket serverSocket;
    private ChordPeer peer;

    /**
     * constructor
     */
    public ConnexionListener(ChordPeer peer) {
    	this.peer = peer;
    }
    
    /**
     * listen a port
     * @param port 
     */
    public void listen(){
        try {
            serverSocket = new ServerSocket(peer.getPort());
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
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
                BufferedReader buffReader = new BufferedReader(new InputStreamReader(is));
                String msg = buffReader.readLine();
                if (msg != null) {
                	String[] contenu = msg.split(" ");
                	if(isFindMainChord(contenu))
                	{
                		System.out.println("passe par ici");
                		out.write(peer.findkey(Integer.parseInt(contenu[1])));
                	}
                    System.out.println(msg);
                }
                
            }
            
        } catch (IOException ex) {
            System.err.println("Erreur : lecture de données echoué.");
        }
    }

    /**
     * 
     * @param msg
     * @return true if msg is FindMainChord
     */
    private boolean isFindMainChord(String[] contenu)
    {
    	return contenu.length == 2 && contenu[0].equals("FindMainChord");
    }
}
