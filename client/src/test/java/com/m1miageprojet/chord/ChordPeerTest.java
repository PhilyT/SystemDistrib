package com.m1miageprojet.chord;

import java.util.ArrayList;
import java.util.Random;

import junit.framework.TestCase;

/**
 * 
 * @author Tom
 *
 */
public class ChordPeerTest extends TestCase 
{
	/**
	 * test la deuxieme condition de findKey
	 */
	public void testDeuxiemeConditionFindKey()
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
	 * test la troixieme condition de findKey
	 */
	public void testTroisiemeConditionDeFindKey()
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
	
	/**
	 * test la quatrieme condition de findKey
	 */
	public void testQuetriemeConditionDeFindKey()
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
	 * test si les neuds se rajoute bien dans le reseau
	 */
	public void testJoinChord()
	{
		ChordPeer c1 = new ChordPeer();
		ChordPeer c2 = new ChordPeer();
		Random rand = new Random();
		while(c1.getMyId() == c2.getMyId())
		{
			c2.setMyId(rand.nextInt(101));
		}
		c2.JoinChord(c1);

		assertEquals(c1, c2.getPred());
		assertEquals(c1, c2.getSucc());

		assertEquals(c2, c1.getPred());
		assertEquals(c2, c1.getSucc());
	}
	
	/**
	 * test si les neuds quitte bien le reseau
	 */
	public void testLeaveChord()
	{
		ChordPeer c1 = new ChordPeer();
		ChordPeer c2 = new ChordPeer();
		Random rand = new Random();
		while(c1.getMyId() == c2.getMyId())
		{
			c2.setMyId(rand.nextInt(101));
		}
		c2.JoinChord(c1);
		c2.LeaveChord();
		assertEquals(c1, c1.getPred());
		assertEquals(c1, c1.getSucc());
	}
}