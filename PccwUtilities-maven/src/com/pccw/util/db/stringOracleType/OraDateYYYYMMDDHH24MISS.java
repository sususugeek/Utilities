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
public class OraDateYYYYMMDDHH24MISS extends OraDate {
	
	static final long serialVersionUID = 2005522882206012059L;

	public OraDateYYYYMMDDHH24MISS() {
        this.setOracleDateFormat("YYYYMMDDHH24MISS");
    }

    /**
     * @param pValue
     */
    public OraDateYYYYMMDDHH24MISS(String pValue) {
        super("YYYYMMDDHH24MISS", pValue);
    }
}