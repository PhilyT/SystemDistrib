package com.m1miageprojet.chord;
import com.m1miageprojet.tcpcommunication.ConnexionListener;
import com.m1miageprojet.tcpcommunication.DataSender;
import java.util.Random;

/**
 * 
 * @author Tom
 *
 */
public class ChordPeer 
{
	private int myId;
        private int port;
        private String ip;
	private ChordPeer succ;
	private ChordPeer pred;
	
	public ChordPeer()
	{
		Random rand = new Random();
		setMyId(rand.nextInt(101));
		setSucc(this);
		setPred(this);
	}
	
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
        * port getter
        * @return 
        */
        public int getPort() {
                return port;
        }

        /**
        * ip getter 
        * @return 
        */
        public String getIp() {
                return ip;
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
		if(getPred() == this)
		{
			return this;
		}
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
	
	/**
	 * join the node in chord 
	 * @param ChordPeerhandle
	 */
	public void JoinChord(ChordPeer ChordPeerhandle)
	{
		ChordPeer S = ChordPeerhandle.findkey(getMyId());
		ChordPeer pred = S.getPred();
		S.setPred(this);
		this.setPred(pred);
		pred.setSucc(this);
		this.setSucc(S);
	}
	
	/**
	 * the node leave the chord
	 */
	public void LeaveChord()
	{
		ChordPeer succ = getSucc();
		ChordPeer pred = getPred();
		succ.setPred(pred);
		pred.setSucc(succ);
	}
        
        /**
        * send data (ex: text msg) to given chord peer
        *
        * @param key to target the wanted peer
        * @param data is some data to communicate
        */
        public void sendData(int key, byte[] data) {

                int port = this.getPort();
                String ip = this.getIp();
                DataSender sender = new DataSender(data, ip, port);
                sender.start();

        }

        /**
        * establish connexion by starting a listener on given chord peer using his
        * key on the chord network
        *
        * @param key
        */
        public void establishConnection(int key) {

                ChordPeer distinationPeer = findkey(key);
                ConnexionListener listener = new ConnexionListener(distinationPeer.getPort());
                listener.start();

        }
}
