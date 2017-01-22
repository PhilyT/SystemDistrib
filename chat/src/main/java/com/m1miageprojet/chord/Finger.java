package com.m1miageprojet.chord;

class Finger {

    ChordPeer peer;
    long startKey;

    public Finger(long start, ChordPeer peer) {
        this.peer = peer;
        this.startKey = start;
    }

    public long getStart() {
        return startKey;
    }

    public void setStart(long startKey) {
        this.startKey = startKey;
    }

    public ChordPeer getPeer() {
        return peer;
    }

    public void setPeer(ChordPeer peer) {
        this.peer = peer;
    }
}
