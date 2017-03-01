/*
 * Created on 2005/10/13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.pccw.util.db;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author 688457
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OracleHelperBase extends JdbcDaoSupport {
	protected String errMsg;

	public OracleHelperBase() {

	}

	/**
	 * @return Returns the errMsg.
	 */
	public String getErrMsg() {
		return this.errMsg;
	}

	/**
	 * @param pErrMsg
	 *            The errMsg to set.
	 */
	public void setErrMsg(String pErrMsg) {
		this.errMsg = pErrMsg;
	}
}