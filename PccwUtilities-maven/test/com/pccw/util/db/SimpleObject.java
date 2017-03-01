package com.pccw.util.db;

public class SimpleObject {

	@Override
	public String toString() {
		return "SimpleObject [str1=" + str1 + ", int1=" + int1 + "]";
	}
	private String str1;
	private int int1;
	public String getStr1() {
		return str1;
	}
	public void setStr1(String str1) {
		this.str1 = str1;
	}
	public int getInt1() {
		return int1;
	}
	public void setInt1(int int1) {
		this.int1 = int1;
	}
}
