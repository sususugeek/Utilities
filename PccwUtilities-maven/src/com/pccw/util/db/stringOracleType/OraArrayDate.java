package com.pccw.util.db.stringOracleType;

import java.sql.Date;
import java.util.ArrayList;

import org.apache.commons.lang.ArrayUtils;

public class OraArrayDate extends OraArray {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6543943547308984678L;
	private OraDate[] values;
	
	public OraArrayDate(String pOracleType, OraDate[] pValues) throws Exception {
		super(pOracleType);
		this.setValues(pValues);
	}
	
	@Override
	public Object[] getValue() {
		return this.values;
	}

	public void setValues(OraDate[] pValues) throws Exception {
		this.values = pValues;
	}
	
	@Override
	public Object[] getBindingValue() {
		if (ArrayUtils.isEmpty(this.values)) {
			return null;
		}

		try {
			ArrayList<Date> valueList = new ArrayList<Date>();
			for (OraDate value : this.values) {
				valueList.add(value.toSqlDate());
			}
			return valueList.toArray(new Date[0]);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}