package com.m1miageprojet.tcpcommunication;

import java.io.IOException;
import java.net.Socket;

public class DataSender extends Thread {

    int port;
    String ip;
    byte[] data;

    /**
     *
     * @param data
     * @param ip
     * @param port
     */
    public DataSender(byte[] data, String ip, int port) {

        this.port = port;
        this.ip = ip;
        this.data = data;

    }

    @Override
    public void run() {

        try {

            Socket socket = new Socket(ip, port);
            socket.getOutputStream().write(data);
            socket.close();

        } catch (IOException ex) {
            System.err.println("connexion refusé sur le port " + port);
        }

    }
}
