package com.m1miageprojet.chord;

import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * 
 * @author Tom
 *
 */
public class ChordPeerTest extends TestCase 
{
	/**
	 * test si findkey renvoi le bon noeud avec une cle inferieur a l'id du noeud apellant
	 */
	public void testChordAvecCleInferieurAID()
	{
		ArrayList<ChordPeer> chord = new ArrayList<ChordPeer>();
		for(int i =0; i<4; i++)
		{
			chord.add(new ChordPeer());
		}
		chord.get(0).setMyId(8);
		chord.get(1).setMyId(13);
		chord.get(2).setMyId(25);
		chord.get(3).setMyId(96);
		
		chord.get(0).setSucc(chord.get(1));
		chord.get(1).setSucc(chord.get(2));
		chord.get(2).setSucc(chord.get(3));
		chord.get(3).setSucc(chord.get(0));
		
		chord.get(0).setPred(chord.get(3));
		chord.get(1).setPred(chord.get(0));
		chord.get(2).setPred(chord.get(1));
		chord.get(3).setPred(chord.get(2));
		
		assertEquals(chord.get(0), chord.get(0).findkey(5));
	}
	
	/**
	 * test si findkey renvoi le bon noeud avec une cle suppérieur a l'id du noeud apellant
	 */
	public void testChordAvecCleSuperieurAID()
	{
		ArrayList<ChordPeer> chord = new ArrayList<ChordPeer>();
		for(int i =0; i<4; i++)
		{
			chord.add(new ChordPeer());
		}
		chord.get(0).setMyId(8);
		chord.get(1).setMyId(13);
		chord.get(2).setMyId(25);
		chord.get(3).setMyId(96);
		
		chord.get(0).setSucc(chord.get(1));
		chord.get(1).setSucc(chord.get(2));
		chord.get(2).setSucc(chord.get(3));
		chord.get(3).setSucc(chord.get(0));
		
		chord.get(0).setPred(chord.get(3));
		chord.get(1).setPred(chord.get(0));
		chord.get(2).setPred(chord.get(1));
		chord.get(3).setPred(chord.get(2));
		
		assertEquals(chord.get(2), chord.get(0).findkey(18));
	}
	
	/**
	 * test si findkey renvoi le bon noeud avec une cle inferieur a l'id du noeud apellant
	 * avec le dernier noeud
	 */
	public void testChordAvecDernierNoeud()
	{
		ArrayList<ChordPeer> chord = new ArrayList<ChordPeer>();
		for(int i =0; i<4; i++)
		{
			chord.add(new ChordPeer());
		}
		chord.get(0).setMyId(8);
		chord.get(1).setMyId(13);
		chord.get(2).setMyId(25);
		chord.get(3).setMyId(96);
		
		chord.get(0).setSucc(chord.get(1));
		chord.get(1).setSucc(chord.get(2));
		chord.get(2).setSucc(chord.get(3));
		chord.get(3).setSucc(chord.get(0));
		
		chord.get(0).setPred(chord.get(3));
		chord.get(1).setPred(chord.get(0));
		chord.get(2).setPred(chord.get(1));
		chord.get(3).setPred(chord.get(2));
		
		assertEquals(chord.get(0), chord.get(3).findkey(2));
	}
}
