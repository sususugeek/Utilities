package com.pccw.util.db.stringOracleType;

import com.pccw.util.db.DaoBaseImpl;

public abstract class OraNumberInsertValueFromSelect extends OraNumber {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7862889564458966277L;

	public OraNumberInsertValueFromSelect(String pValue) {
		super(pValue);
	}
	
	public abstract String getInsertValue(DaoBaseImpl pDao) throws Exception; 
}
