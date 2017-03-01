package com.pccw.util.db.stringOracleType;

import org.apache.commons.lang.StringUtils;

public class OraDateCreateDate extends OraDate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5128434454661994183L;

	public OraDateCreateDate() {
        this.setOracleDateFormat("YYYYMMDDHH24MISS");
        this.setUpdateUsing(OraDate.UPD_BY_SYS_DATE);
    }
	
    /**
     * @param pValue
     */
    public OraDateCreateDate(String pValue) {
        super("YYYYMMDDHH24MISS", pValue);
        this.setUpdateUsing(StringUtils.isEmpty(pValue) ? OraDate.UPD_BY_SYS_DATE : OraDate.UPD_BY_INPUT_VALUE);
    }
    
    @Override
    public void setValue(String pValue) {
    	super.setValue(pValue);
        this.setUpdateUsing(StringUtils.isEmpty(pValue) ? OraDate.UPD_BY_SYS_DATE : OraDate.UPD_BY_INPUT_VALUE);
    }
}