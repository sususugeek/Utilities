package com.pccw.util.db;

import static org.junit.Assert.*;

import org.apache.commons.lang.CharUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
//		final SimpleObject ob1 = new SimpleObject();
//		ob1.setStr1("SimpleObject");
//		ob1.setInt1(1);
//		System.out.println(ob1);
//		new SimpleService().changeObject(ob1);
//		System.out.println(ob1);
//		
//		String str = "DEF";
//		System.out.println(str);
//		new SimpleService().changeString(str);
//		System.out.println(str);

	}
	
	@Test
	public void testCharUtils(){
		boolean result = CharUtils.isAsciiPrintable(' ');
		System.out.println(" is "+result);
		result =  CharUtils.isAsciiPrintable('	');
		System.out.println("	is "+result);
		result =  CharUtils.isAsciiPrintable('\n');
		System.out.println("\n is "+result);
		result =  CharUtils.isAsciiPrintable('\r');
		System.out.println("\r is "+result);
		result =  CharUtils.isAsciiPrintable('?');
		System.out.println("? is "+result);
	}

}
