package com.m1miageprojet.chord;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.m1miageprojet.tcpcommunication.Request;

public class ChatApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = null;

        System.out.print("def un port source: ");
        String ports = sc.nextLine();
        System.out.print("def un port destination: ");
        String portd = sc.nextLine();
        System.out.print("def un key source: ");
        String keys = sc.nextLine();

        /*System.out.print("def un key destination: ");
		String keyd = sc.nextLine();*/
        
        try 
        {
            ChordPeer peerN = new ChordPeer(101, Integer.parseInt(ports));

            peerN.setMyId(Integer.parseInt(keys));
            Request req = new Request(peerN, peerN.getPort());
            peerN.runListener(req);
            if(!portd.isEmpty())
            {
            	ChordPeer dest = new ChordPeer(101, Integer.parseInt(portd));
            	req.sendRequest("JOIN", dest);
            }
            System.out.println("chatter");
            while (!(line = sc.nextLine()).equals("\n")) {
                if (!line.trim().isEmpty()) {
                    String data = peerN.getMyId() + " >> " + line;
                    peerN.sendData(data.getBytes());
                }
            }
            sc.close();

        } catch (NumberFormatException e) {
            System.err.println("port incorrecte");
        } catch (NullPointerException e) {
            System.err.println("port incorrecte");
        } /*catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }
}
