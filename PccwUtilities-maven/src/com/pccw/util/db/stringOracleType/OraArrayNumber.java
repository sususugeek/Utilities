package com.pccw.util.db.stringOracleType;

import java.util.ArrayList;

import org.apache.commons.lang.ArrayUtils;

public class OraArrayNumber extends OraArray {

	/**
	 * 
	 */
	private static final long serialVersionUID = 792961763050628215L;
	private OraNumber[] values;
	
	public OraArrayNumber(String pOracleType, String[] pValues) {
		super(pOracleType);
		this.setValues(pValues);
	}

	public OraArrayNumber(String pOracleType, int[] pValues) {
		super(pOracleType);
		this.setValues(pValues);
	}

	public OraArrayNumber(String pOracleType, Integer[] pValues) {
		super(pOracleType);
		this.setValues(pValues);
	}

	public OraArrayNumber(String pOracleType, float[] pValues) {
		super(pOracleType);
		this.setValues(pValues);
	}

	public OraArrayNumber(String pOracleType, Float[] pValues) {
		super(pOracleType);
		this.setValues(pValues);
	}

	public OraArrayNumber(String pOracleType, OraNumber[] pValues) {
		super(pOracleType);
		this.setValues(pValues);
	}
	
	@Override
	public Object[] getValue() {
		return (Object[]) this.values;
	}

	public void setValues(String[] pValues) {
		if (pValues == null) {
			this.values = null;
			return;
		}
		ArrayList<OraNumber> valueList = new ArrayList<OraNumber>();
		for (String value : pValues) {
			valueList.add(new OraNumber(value));
		}
		this.values = valueList.toArray(new OraNumber[0]);
	}
	
	public void setValues(OraNumber[] pValues) {
		if (pValues == null) {
			this.values = null;
			return;
		}
		this.values = pValues;
	}
	
	public void setValues(int[] pValues) {
		if (pValues == null) {
			this.values = null;
			return;
		}
		ArrayList<OraNumber> valueList = new ArrayList<OraNumber>();
		for (int value : pValues) {
			valueList.add(new OraNumber(value));
		}
		this.values = valueList.toArray(new OraNumber[0]);
	}

	public void setValues(Integer[] pValues) {
		if (pValues == null) {
			this.values = null;
			return;
		}
		ArrayList<OraNumber> valueList = new ArrayList<OraNumber>();
		for (int value : pValues) {
			valueList.add(new OraNumber(value));
		}
		this.values = valueList.toArray(new OraNumber[0]);
	}

	public void setValues(float[] pValues) {
		if (pValues == null) {
			this.values = null;
			return;
		}
		ArrayList<OraNumber> valueList = new ArrayList<OraNumber>();
		for (float value : pValues) {
			valueList.add(new OraNumber(value));
		}
		this.values = valueList.toArray(new OraNumber[0]);
	}

	public void setValues(Float[] pValues) {
		if (pValues == null) {
			this.values = null;
			return;
		}
		ArrayList<OraNumber> valueList = new ArrayList<OraNumber>();
		for (float value : pValues) {
			valueList.add(new OraNumber(value));
		}
		this.values = valueList.toArray(new OraNumber[0]);
	}
	
	@Override
	public Object[] getBindingValue() {
		if (ArrayUtils.isEmpty(this.values)) {
			return null;
		}
		ArrayList<String> valueList = new ArrayList<String>();
		for (OraNumber oraNumber : this.values) {
			valueList.add(oraNumber.getValue());
		}
		return valueList.toArray(new String[0]);
	}
}