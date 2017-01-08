package com.m1miageprojet.tcpcommunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnexionListener extends Thread {

    int port;
    ServerSocket serverSocket;

    /**
     * constructor
     */
    public ConnexionListener() {}
    
    /**
     * listen a port
     * @param port 
     */
    public void listen(int port){
        
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
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
                    System.out.println(msg);
                }
                
            }
            
        } catch (IOException ex) {
            System.err.println("Erreur : lecture de données echoué.");
        }
    }

}
