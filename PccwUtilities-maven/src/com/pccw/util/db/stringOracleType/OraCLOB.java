package com.pccw.util.db.stringOracleType;

import java.io.Serializable;

public class OraCLOB implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3646451130585654964L;
	private String value; 
	
    public OraCLOB(String pValue) {
        super();
        this.value = pValue;
    }

	public String getValue() {
		return this.value;
	}

	public void setValue(String pValue) {
		this.value = pValue;
	}
	
	@Override
	public String toString() {
		return this.value;
	}
}
