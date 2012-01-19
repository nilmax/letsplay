package com.letsplay.util;

import java.util.Date;

import junit.framework.TestCase;

public class TestUtils extends TestCase {

	public void testTime(){
		Date d = Utils.getTime(1, 25);
		System.out.println(Utils.getTimeString(d));
	}
	
}
