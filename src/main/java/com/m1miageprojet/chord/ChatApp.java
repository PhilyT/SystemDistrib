package com.m1miageprojet.chord;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.m1miageprojet.tcpcommunication.Request;

public class ChatApp {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String line = null;

		System.out.print("def un port source: ");
		String ports = sc.nextLine();
		System.out.print("def un port destination (ne pas definir si il sagit du premier client): ");
		String portd = sc.nextLine();
		System.out.print("def un key source: ");
		String keys = sc.nextLine();

		try {
			ChordPeer peerN = new ChordPeer(101, Integer.parseInt(ports));

			peerN.setMyId(Integer.parseInt(keys));
			Request req = new Request(peerN);
			peerN.runListener(req);

			if (!portd.isEmpty()) {
				System.out.print("def ip destination: ");
				String ipd = sc.nextLine();
				System.out.print("def un key destination: ");
				String keyd = sc.nextLine();
				ChordPeer dest = new ChordPeer(101, ipd, Integer.parseInt(portd));
				dest.setMyId(Integer.parseInt(keyd));
				req.sendRequest("JOIN", dest);
			}

			System.out.println(
					"Options:\n\t-I: afficher les infos du ChordPeer\n\t-C: chatter avec un chordPeer\n\tcls:effacer l'ecran\n\t-Q: quitter le chord\n\texit: Sortir de l'application");
			while (!(line = sc.nextLine()).equals("\n")) {
				if (!line.trim().isEmpty()) {
					switch (line.toLowerCase()) {
					case "exit":
						req.sendRequest("LEAVE", peerN.getSucc());
						System.out.println("sortir de l'application");
						System.exit(0);
					case "-q":
						// close all kind of listener or thread
						req.sendRequest("LEAVE", peerN.getSucc());
						break;

					case "-i":

						// show chordPeer infos
						System.out.println("//Infos ChordPeer:\n//  Key: " + peerN.getMyId() + "\n//  Pred: "
								+ peerN.getPred() + "\n//  Succ: " + peerN.getSucc());
						break;

					case "-c":

						// start chatting ..
						System.out.println("chatter :)");

						while (!(line = sc.nextLine()).equals("\n")) {
							if (line.equals("-q")) {
								System.out.println("sortir du chat ..");
								break;
							}
							if (!line.equals("-c")) {
								String data = peerN.getMyId() + " >> " + line;
								peerN.sendData(data.getBytes());
							}
							if (line.equals("cls")) {
								cleanScreen();
								System.out
										.println("Options:\n\tcls:effacer l'ecran\n\t-Q: sortir de cette conversation");
							}
						}
						break;

					case "cls":
						cleanScreen();
						System.out.println(
								"Options:\n\t-I: afficher les infos du ChordPeer\n\t-C: chatter avec un chordPeer\n\tcls:effacer l'ecran\n\t-Q: quitter");
						break;
					case "-ls":
						Hashtable<Long, ChatRoom> listeSalons = peerN.getGestionSalon().getChatRoomList();
						Set<Long> keyss = listeSalons.keySet();// liste des id
																// dans le
																// hastable
						for (Long id : keyss) {
							System.out.println("salon  de cle :" + id);
						}
						break;
					case "-creat":
						System.out.println("creation dun salon");
						String cleeS = sc.nextLine();
						peerN.getGestionSalon().creatChatRoom(peerN,Long.parseLong(cleeS) );
						
						req.sendRequest("CREAT_SALON", peerN.getSucc());
						break;
					case "-s":
						System.out.println("clee du salon a rejoindre");
						String cleeSalon = sc.nextLine();

						peerN.getGestionSalon().joinChatRoom(Long.parseLong(cleeSalon), peerN);
						req.sendRequest("JOIN_SALON", peerN.getSucc());
						break;
					case "-sms":
						System.out.println(" envoie de message dans un salon");

						Scanner scan = new Scanner(System.in);
						System.out.println(" saisissez la cle du salon dans le quel vous voulez envoyez un message");

						long cleSalon = scan.nextLong();

						Scanner s = new Scanner(System.in);
						System.out.println("entrez votre message dans le salon");
						String str = s.nextLine();
						peerN.getGestionSalon().sendToChatRoom(str, cleSalon, peerN);

						req.sendRequest("SMS_DANS_SALON", peerN.getSucc());
						break;
					case "redD":
						System.out.println("lecture du dernier message dans un salon");
						Scanner scann = new Scanner(System.in);
						System.out.println(" saisissez la cle du salon a lire ");
						long SalonKey = scann.nextLong();
						System.out.println(peerN.getGestionSalon().readlastMessage(SalonKey));
						break;
						
					case "redT":
						System.out.println("lecture de tout les messages dans un salon");
						
						System.out.println(" saisissez la cle du salon a lire ");
						long SalonKeyy = sc.nextLong();
						ArrayList<String> messages = new ArrayList<String> ();
						messages=peerN.getGestionSalon().readChatRoom(SalonKeyy);
						if(!messages.isEmpty()){
							for(String chaine:messages){
								System.out.println(chaine);
								
							}
						}
						else
							System.out.println("ce salon ne contient aucun message");
						
						break;
						
						
					default:
						System.err.println("commande introuvable.");
					}
				}
			}
			sc.close();

		} catch (NumberFormatException | NullPointerException e) {
			System.err.println("port incorrecte");
		} catch (NoSuchElementException e) {
			System.err.println("Arret de l'application");
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
