package com.m1miageprojet.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.m1miageprojet.chord.ChordPeer;

/**
 * 
 * @author Tom
 *
 */
public interface IChordPeer extends Remote {
	public ChordPeer findkey(int key)throws RemoteException;
	public ChordPeer getSucc()throws RemoteException;
	public ChordPeer getPred()throws RemoteException;
}
