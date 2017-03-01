package com.pccw.util.db.stringOracleType;

import java.io.Serializable;

public class OraBLOB implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 796136092604015869L;
	/**
	 * 
	 */
	/**
	 * 
	 */
	private byte[] value; 
	
    public OraBLOB(byte[] pValue) {
        super();
        this.value = pValue;
    }

	public byte[] getValue() {
		return this.value;
	}

	public void setValue(byte[] pValue) {
		this.value = pValue;
	}
}
