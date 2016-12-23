package com.m1miageprojet.chord;

/**
 * 
 * @author Tom
 *
 */
public class ChordPeer 
{
	private int myId;
	private ChordPeer succ;
	private ChordPeer pred;
	
	/**
	 * @return the myId
	 */
	public int getMyId() 
	{
		return myId;
	}

	/**
	 * @param myId the myId to set
	 */
	public void setMyId(int myId) {
		this.myId = myId;
	}

	/**
	 * @return the succ
	 */
	public ChordPeer getSucc() {
		return succ;
	}

	/**
	 * @param succ the succ to set
	 */
	public void setSucc(ChordPeer succ) {
		this.succ = succ;
	}

	/**
	 * @return the pred
	 */
	public ChordPeer getPred() {
		return pred;
	}

	/**
	 * @param pred the pred to set
	 */
	public void setPred(ChordPeer pred) {
		this.pred = pred;
	}

	/**
	 * @return the className with value of myId
	 */
	@Override
	public String toString()
	{
		return "ChordPeer [myId=" + getMyId() + "]";
	}
	
	/**
	 * 
	 * @param key
	 * @return the node responsible for the key
	 */
	public ChordPeer findkey(int key)
	{
		if(getPred().getMyId() < this.getMyId() && key > getPred().getMyId() && key <= this.getMyId())
		{
			return this;
		}
		else if(getPred().getMyId() > this.getMyId() && key <= this.getMyId())
		{
			return this;
		}
		else
		{
			return getSucc().findkey(key);
		}
	}
}
