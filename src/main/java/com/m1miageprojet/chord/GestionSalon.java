package com.m1miageprojet.chord;

import java.util.ArrayList;
import java.util.Hashtable;

public class GestionSalon {
	private Hashtable<Integer,ArrayList<ChatRoom>> tableSalon;
	private ChordPeer noeud;
	public GestionSalon(){
		
	}
	public ArrayList<ChatRoom> getChatRoomList(Integer i){
		return tableSalon.get(i);
	}
	
	public  void joinChatRoom(long chatKey){
		
	}
	 public void sendToChatRoom(String s, long chatKey){
		 
	 }
	 public String readChatRoom(long chatKey){
		
		 return "";
	 }
	 
}
