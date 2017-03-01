package com.pccw.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChecksumTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
	public void testHash() throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA1");
		byte[] result = testChecksum(md);
		String hexStr = this.toHex(result);
		System.out.println(hexStr);
		md = MessageDigest.getInstance("SHA");
		result = testChecksum(md);
		hexStr = this.toHex(result);
		System.out.println(hexStr);
		md = MessageDigest.getInstance("MD5");
		result = testChecksum(md);
		hexStr = this.toHex(result);
		System.out.println(hexStr);
		

	}
	
	@Test
	public void testChecksum(){
		String hexStr = this.calc("0123456789");
		System.out.println(hexStr);
		
		hexStr = this.calc("0343542349");
		System.out.println(hexStr);
		
		hexStr = this.calc("03493542343");
		System.out.println(hexStr);
		
		hexStr = this.calc2("0123456789");
		System.out.println(hexStr);
		
		hexStr = this.calc2("0343542349");
		System.out.println(hexStr);
		
		hexStr = this.calc2("03493542343");
		System.out.println(hexStr);
		
		CRC32 crc32 = new CRC32();
		String msg = "0123456789";
		byte[] msgByte = msg.getBytes();
		int nread = 0;
		crc32.update(msgByte);
		long crcsum = crc32.getValue();
		System.out.println(crcsum);
	}
	
	public byte[] testChecksum(MessageDigest md){
		String msg = "0123456789";
		byte[] msgByte = msg.getBytes();
		int nread = 0;
		md.update(msgByte);
	    byte[] mdbytes = md.digest();
	    return mdbytes;
	}
	
	public String toHex(byte[] pbytes){
		//convert the byte to hex format
	    StringBuffer sb = new StringBuffer("");
	    for (int i = 0; i < pbytes.length; i++) {
	    	sb.append(Integer.toString((pbytes[i] & 0xff) + 0x100, 16).substring(1));
	    }
	    return sb.toString();
	}
//http://w2.syronex.com/jmr/programming/mod11chk
	public String calc(String digStr) {
	    int len = digStr.length();
	    int sum = 0, rem = 0;
	    int[] digArr = new int[len];
	    for (int k=1; k<=len; k++) // compute weighted sum
	      sum += (11 - k) * Character.getNumericValue(digStr.charAt(k - 1));
	    if ((rem = sum % 11) == 0) return "0";
	    else if (rem == 1) return "X";
	    return (new Integer(11 - rem)).toString();
	  }
	
	//Modified version based on http://w2.syronex.com/jmr/programming/mod11chk
	//changed the number of digit from 11 to the length of the input (len)
		public String calc2(String digStr) {
		    int len = digStr.length();
		    int sum = 0, rem = 0;
		    int[] digArr = new int[len];
		    for (int k=1; k<=len; k++){ // compute weighted sum
		      sum += (len - k) * Character.getNumericValue(digStr.charAt(k - 1));
		    }
		    if ((rem = sum % len) == 0){
		    	return "0";
		    }
		    else if (rem == 1) {
		    	return "X";
		    }
		    return (new Integer(len - rem)).toString();
		  }
}
