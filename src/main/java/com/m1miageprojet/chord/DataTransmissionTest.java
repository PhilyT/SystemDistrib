/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m1miageprojet.chord;

import com.m1miageprojet.chord.ChordPeer;
import java.util.Scanner;

/**
 *
 * @author MAROUANE
 */
public class DataTransmissionTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = null;

        System.out.print("def un port source: ");
        String ports = sc.nextLine();
        System.out.print("def un port destination: ");
        String portd = sc.nextLine();
        System.out.print("def un key source: ");
        String keys = sc.nextLine();
        
        System.out.print("def un key destination: ");
        String keyd = sc.nextLine();
        /*if (sc.nextLine().matches("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$")) {
				System.out.println("Found good SSN: " + ssn);
			}*/
        try {
            ChordPeer peerN = new ChordPeer(101, Integer.parseInt(ports));
            peerN.setMyId(Integer.parseInt(keys));
            peerN.establishConnection(Integer.parseInt(portd/*keyd*/));
            System.out.println("chatter");
            while ((line = sc.nextLine()) != "\n") {
                String data = peerN.getMyId() + " : " + line;
                peerN.sendData(data.getBytes());
            }
            
        } catch (NumberFormatException | NullPointerException e) {
            System.err.println("port incorrecte");
        }

        
    }

}
