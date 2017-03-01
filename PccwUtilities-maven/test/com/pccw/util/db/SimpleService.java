package com.pccw.util.db;

public class SimpleService {
	public void changeObject(final SimpleObject simpleObject){
		simpleObject.setStr1("SimpleService");
		simpleObject.setInt1(5);
	}
	
	public void changeString(String pString) {
		pString = "ABC";
		System.out.println(pString);
	}
}
