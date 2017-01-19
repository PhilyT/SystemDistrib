/**
 * 
 */
package com.m1miageprojet.tcpcommunication;

import com.m1miageprojet.chord.ChordPeer;

import junit.framework.TestCase;

/**
 * @author Tom Phily
 *
 */
public class RequestTest extends TestCase{
	ChordPeer c1;
	ChordPeer c2;
	ChordPeer c3;
	ChordPeer c4;
	Request req1;
	Request req2;
	Request req3;
	Request req4;

	/**
	 * @throws java.lang.Exception
	 */
	public void setUp() throws Exception {
		c1 = new ChordPeer(101,3000);
		c2 = new ChordPeer(101,3001);
		c3 = new ChordPeer(101,3002);
		c4 = new ChordPeer(101,3003);
		c1.setMyId(42);
		c2.setMyId(58);
		c3.setMyId(78);
		c4.setMyId(10);
		req1 = new Request(c1);
		req2 = new Request(c2);
		req3 = new Request(c3);
		req4 = new Request(c4);
	}

	/**
	 * @throws java.lang.Exception
	 */
	public void tearDown() throws Exception {
		c1 = null;
		c2 = null;
		c3 = null;
		c4 = null;
		req1= null;
		req2 = null;
		req3 = null;
		req4 = null;
	}

	public void testJointureASortieDuChord() throws InterruptedException {
		c1.runListener(req1);
		c2.runListener(req2);
		c3.runListener(req3);
		c4.runListener(req4);
		req2.sendRequest("JOIN", c1);
		Thread.sleep(1000);
		req3.sendRequest("JOIN", c1);
		Thread.sleep(1000);
		req4.sendRequest("JOIN", c1);
		Thread.sleep(1000);
		req2.sendRequest("LEAVE", c2.getSucc());
		Thread.sleep(1000);
		
		assertEquals(c3, c4.getPred());
		assertEquals(c1, c4.getSucc());
		assertEquals(c4, c3.getSucc());
		assertEquals(c1, c3.getPred());
		assertEquals(c4, c1.getPred());
		assertEquals(c3, c1.getSucc());
		
		
	}

}
