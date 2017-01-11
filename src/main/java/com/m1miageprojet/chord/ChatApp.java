package com.m1miageprojet.chord;

import java.util.Scanner;

import com.m1miageprojet.tcpcommunication.Request;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        try {
            ChordPeer peerN = new ChordPeer(101, Integer.parseInt(ports));

            peerN.setMyId(Integer.parseInt(keys));
            Request req = new Request(peerN, peerN.getPort());
            peerN.runListener(req);

            if (!portd.isEmpty()) {
                ChordPeer dest = new ChordPeer(101, Integer.parseInt(portd));
                req.sendRequest("JOIN", dest);
            }

            System.out.println("Options:\n\t-I: afficher les infos du ChordPeer\n\t-C: chatter avec un chordPeer\n\tcls:effacer l'ecran\n\t-Q: quitter");
            while (!(line = sc.nextLine()).equals("\n")) {
                if (!line.trim().isEmpty()) {
                    switch (line.toLowerCase()) {
                        case "-q":

                            //close all kind of listener or thread
                            System.out.println("sortir de l'application");
                            System.exit(0);
                        case "-i":

                            //show chordPeer infos
                            System.out.println("//Infos ChordPeer:\n//  Key: " + peerN.getMyId() + "\n//  Pred: " + peerN.getPred() + "\n//  Succ: " + peerN.getSucc());
                            break;

                        case "-c":

                            //start chatting ..
                            System.out.println("chatter :)");

                            while (!(line = sc.nextLine()).equals("\n")) {
                                if (line != "-c") {
                                    String data = peerN.getMyId() + " >> " + line;
                                    peerN.sendData(data.getBytes());
                                }
                                if (line.equals("cls")) {
                                    cleanScreen();
                                    System.out.println("Options:\n\tcls:effacer l'ecran\n\t-Q: sortir de cette conversation");
                                }
                                if (line.equals("-q")) {
                                    System.out.println("sortir du chat ..");
                                    break;
                                }
                            }
                            break;

                        case "cls":
                            cleanScreen();
                            System.out.println("Options:\n\t-I: afficher les infos du ChordPeer\n\t-C: chatter avec un chordPeer\n\tcls:effacer l'ecran\n\t-Q: quitter");
                            break;

                        default:
                            System.err.println("commande introuvable.");
                    }
                }
               }
            sc.close();

        } catch (NumberFormatException | NullPointerException e) {
            System.err.println("port incorrecte");
        }
    }

    private static void cleanScreen() {
        final String os = System.getProperty("os.name");
        try {
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ChatApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
