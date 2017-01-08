package com.m1miageprojet.chord;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.Scanner;

@SuppressWarnings("deprecation")
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
        
        try {
            /*
        	// Enregistrement dans le registre de noms RMI de la classe distante ChordPeer
        	if(System.getSecurityManager() == null)
        	{
        		System.setSecurityManager(new RMISecurityManager());
        	}
             */
            ChordPeer peerN = new ChordPeer(101, Integer.parseInt(ports));
            /*String url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + "/TestRMI";
            System.out.println("Enregistrement de l'objet avec l'url : " + url);
            Naming.rebind(url, peerN);

            System.out.println("Serveur lancé");
            // Fin enregistrement
             */
            peerN.setMyId(Integer.parseInt(keys));
            peerN.establishConnection(Integer.parseInt(portd/* keyd */));
            System.out.println("chatter");
            while (!(line = sc.nextLine()).equals("\n")) {
                if (!line.isEmpty()) {
                    String data = peerN.getMyId() + " >> " + line;
                    peerN.sendData(data.getBytes());
                }
            }
            sc.close();

        } catch (NumberFormatException e) {
            System.err.println("port incorrecte");
        } catch (NullPointerException e) {
            System.err.println("port incorrecte");
        }
        /*catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
    }

}
