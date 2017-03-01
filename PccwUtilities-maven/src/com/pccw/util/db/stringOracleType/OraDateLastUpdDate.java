/*
 * Created on Oct 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.pccw.util.db.stringOracleType;

/**
 * @author 00941773
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OraDateLastUpdDate extends OraDate {
	
	static final long serialVersionUID = -5227930434285127144L;

	public OraDateLastUpdDate() {
        this.setOracleDateFormat("YYYYMMDDHH24MISS");
        this.setUpdateUsing(OraDate.UPD_BY_SYS_DATE);
    }

    /**
     * @param pValue
     */
    public OraDateLastUpdDate(String pValue) {
        super("YYYYMMDDHH24MISS", pValue);
        this.setUpdateUsing(OraDate.UPD_BY_SYS_DATE);
    }
}