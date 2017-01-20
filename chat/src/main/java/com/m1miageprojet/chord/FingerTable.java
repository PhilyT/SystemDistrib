package com.m1miageprojet.chord;

public class FingerTable {

    Finger[] fingerList;

    public FingerTable(ChordPeer peer) {
        this.fingerList = new Finger[ChordPeer.KEY_LENGTH];
        for (int i = 0; i < fingerList.length; i++) {
            long startKey = peer.getMyId();
            fingerList[i] = new Finger(startKey, peer);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ChordPeer.KEY_LENGTH; i++) {
            Finger finger = this.getFingerList(i);
            sb.append(finger.getStart()).append("\t").append(finger.getPeer()).append("\n");
        }
        return sb.toString();
    }

    public Finger getFingerList(int i) {
        return fingerList[i];
    }
}
