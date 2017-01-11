package com.m1miageprojet.chord;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChordPeer noeud1 = new  ChordPeer(10,300);
		GestionSalon g = new GestionSalon();
		ChatRoom  chat = new ChatRoom(noeud1,1);
		System.out.println(chat.getNoeuds().get(0));
		System.out.println("noeud1 de port"+chat.getNoeuds().get(0).getPort());
		System.out.println("identifiant de la salle du noeud1 est"+' '+chat.getId());
		g.joinChatRoom(1,noeud1 );
		
		
		ChordPeer noeud2 = new  ChordPeer(11,301);
		
		g.joinChatRoom(2,noeud2 );
		System.out.println("le nombre de salle disponible "+g.getChatRoomList().size());
		System.out.println("le nombre de noeud dans la salle dID"+(g.getChatRoom().get(0))+"est :"+g.getChatRoom().get(0).getNoeuds().size());
		//System.out.println(""+g.getChatRoom().size());

	}

}
