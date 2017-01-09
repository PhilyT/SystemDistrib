package com.m1miageprojet.chord;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

import junit.framework.TestCase;

/**
 * 
 * @author Tom
 *
 */
public class ChordPeerTest extends TestCase {

	private ArrayList<ChordPeer> chord;
	private ArrayList<ChordPeer> anotherChord;
	private ChordPeer c1;
	private ChordPeer c2;

	public void setUp() throws RemoteException {
		chord = new ArrayList<ChordPeer>();
		for (int i = 0; i < 4; i++) {
			chord.add(new ChordPeer(101, 1111));
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

		anotherChord = new ArrayList<ChordPeer>();
		for (int i = 0; i < 2; i++) {
			anotherChord.add(new ChordPeer(64, 2222));
		}

		// anotherChord is set on 64 keys
		anotherChord.get(0).setMyId(5);
		anotherChord.get(1).setMyId(55);

		// set pred and succ
		anotherChord.get(0).setSucc(anotherChord.get(1));
		anotherChord.get(1).setSucc(anotherChord.get(0));
		anotherChord.get(0).setPred(anotherChord.get(1));
		anotherChord.get(1).setPred(anotherChord.get(0));

		c1 = new ChordPeer(101, 2000);
		c2 = new ChordPeer(101, 4000);
		Random rand = new Random();
		while (c1.getMyId() == c2.getMyId()) {
			c2.setMyId(rand.nextInt(101));
		}
	}

	public void tearDown() {
		chord = null;
		c1 = null;
		c2 = null;
	}

	/**
	 * test la deuxieme condition de findKey
	 */
	public void testDeuxiemeConditionFindKey() {
		assertEquals(chord.get(0), chord.get(0).findkey(5));
	}

	/**
	 * test la troisieme condition de findKey
	 */
	public void testTroisiemeConditionDeFindKey() {
		assertEquals(chord.get(0), chord.get(3).findkey(2));
	}

	/**
	 * test la quatrieme condition de findKey
	 */
	public void testQuatriemeConditionDeFindKey() {
		assertEquals(chord.get(2), chord.get(0).findkey(18));
	}

	public void testValidValuesAfterMaxId() {
		// 57 is a valid value because 57 < 63, 63 being the max valid value out of the 64 keys
		assertEquals(anotherChord.get(0), anotherChord.get(0).findkey(57));
	}

	public void testInvalidValuesAfterMaxId() {
		// but 64 is not
		assertEquals(null, anotherChord.get(0).findkey(64));
	}

	/**
	 * test si les neuds se rajoute bien dans le reseau
	 */
	public void testJoinChord() {
		c2.joinChord(c1.getIp(), c1.getPort());
		assertEquals(c1, c2.getPred());
		assertEquals(c1, c2.getSucc());

		assertEquals(c2, c1.getPred());
		assertEquals(c2, c1.getSucc());
	}

	/**
	 * test si les neuds quitte bien le reseau
	 */
	public void testLeaveChord() {
		c2.joinChord(c1.getIp(), c1.getPort());
		c2.leaveChord();
		assertEquals(c1, c1.getPred());
		assertEquals(c1, c1.getSucc());
	}
}