/**
 * 
 */
package com.m1miageprojet.tcpcommunication;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.m1miageprojet.chord.ChordPeer;

/**
 * @author Tom Phily
 *
 */
public class RequestTest {
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
	@Before
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
	@After
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

	@Test
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
		
		assertEquals(c1.getPred(), c4);
		System.out.println("test1 reussi");
		assertEquals(c1.getSucc(), c3);
		System.out.println("test2 reussi");
		/*assertEquals(c3.getPred(), c1);
		System.out.println("test3 reussi");
		assertEquals(c3.getSucc(), c4);
		System.out.println("test4 reussi");
		assertEquals(c4.getPred(), c3);
		System.out.println("test5 reussi");
		assertEquals(c4.getSucc(), c1);
		System.out.println("test6 reussi");*/
	}

}
