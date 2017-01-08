package com.m1miageprojet.interfaces;

import com.m1miageprojet.chord.ChordPeer;

/**
 * 
 * @author Tom
 *
 */
public interface IChordPeer {
	public ChordPeer findkey(int key);
	public ChordPeer getSucc();
	public ChordPeer getPred();
}
