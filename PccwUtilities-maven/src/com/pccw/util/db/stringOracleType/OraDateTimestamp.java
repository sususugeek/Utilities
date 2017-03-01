package com.pccw.util.db.stringOracleType;

import java.text.SimpleDateFormat;

public class OraDateTimestamp extends OraDate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3437963244766669514L;
	
	private static final String TS_ORACLE_FORMAT = "YYYYMMDDHH24MISS.FF3";
	private static final String dateFormatterTS  = "yyyyMMddHHmmss.SSS";

	public OraDateTimestamp() {
        this.setOracleDateFormat(TS_ORACLE_FORMAT);
    }

    /**
     * @param pValue
     */
    public OraDateTimestamp(String pValue) {
        super(TS_ORACLE_FORMAT, pValue);
    }
    
    public SimpleDateFormat getSimpleDateFormat() {
    	return new SimpleDateFormat(dateFormatterTS);
    }    
}
