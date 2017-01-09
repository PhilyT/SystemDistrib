package com.m1miageprojet.chord;

import java.util.ArrayList;

public class GestionSalon {
private ArrayList<ChatRoom> salles;
private  ArrayList<Integer> idSalles;


public GestionSalon(){
	this.salles = new ArrayList<ChatRoom>();
	this.idSalles= new  ArrayList<Integer>();
	
}

 public ArrayList<Integer> getChatRoomList(){
		return idSalles;
	}
	
	public  void joinChatRoom(int chatKey,ChordPeer newNoeud){
		if(!salles.isEmpty() && !idSalles.isEmpty()){
			
				for(int i=0; i<idSalles.size(); i++){
					if(idSalles.get(i).equals((Integer)chatKey) && salles.get(i).getId()==chatKey ){
						
								salles.get(i).getNoeuds().add(newNoeud);
							}
					else{
						salles.add(new ChatRoom(newNoeud, chatKey));
						idSalles.add((Integer)chatKey);
					}
				}
						
			}
		else {
			System.out.println("salon vide");
		}
			
}
			
		
		
	
	 public void sendToChatRoom(String s, int chatKey, ChordPeer noeud){
		 if(!idSalles.isEmpty()){
			 for(int i=0; i<idSalles.size(); i++){
					if(idSalles.get(i)==(Integer)chatKey && salles.get(i).getId()==chatKey ){
						salles.get(i).getMessages().add(s);
						
					}
					else{
						ChatRoom newSalle = new ChatRoom(noeud,chatKey);
						newSalle.getMessages().add(s);
						salles.add(newSalle);
						idSalles.add((Integer)chatKey);
					}
			 }
		 
		 }
	 }
	 public  String readChatRoom(int chatKey){
		 String s ="";
		 if(!idSalles.isEmpty()){
			 for(int i=0; i<idSalles.size(); i++){
					if(idSalles.get(i)==(Integer)chatKey && salles.get(i).getId()==chatKey ){
						if(!salles.get(i).getMessages().isEmpty()){
							for(int j=0; j<salles.get(i).getMessages().size();j++){
								s+=salles.get(i).getMessages().get(j);
							}
							
						}
					}
					else
						System.out.println("salle inexistant");
				
			 }
			 
		 }
		 
		
		 return s;
	 }
	 
}
