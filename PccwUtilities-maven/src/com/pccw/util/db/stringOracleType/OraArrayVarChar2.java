package com.pccw.util.db.stringOracleType;

public class OraArrayVarChar2 extends OraArray {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8078700416608277499L;
	private String[] values;
	
	public OraArrayVarChar2(String pOracleType, String[] pValues) {
		super(pOracleType);
		this.values = pValues;
	}
	
	@Override
	public Object[] getValue() {
		return this.values;
	}

	public void setValues(String[] pValues) {
		this.values = pValues;
	}
	
	@Override
	public Object[] getBindingValue() {
		return this.values;
	}
}