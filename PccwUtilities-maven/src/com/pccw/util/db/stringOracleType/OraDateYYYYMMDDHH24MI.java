package com.pccw.util.db.stringOracleType;

public class OraDateYYYYMMDDHH24MI extends OraDate {
	static final long serialVersionUID = -1921232334469992106L;

	public OraDateYYYYMMDDHH24MI() {
        this.setOracleDateFormat("YYYYMMDDHH24MI");
    }

    /**
     * @param pValue
     */
    public OraDateYYYYMMDDHH24MI(String pValue) {
        super("YYYYMMDDHH24MI", pValue);
    }
}