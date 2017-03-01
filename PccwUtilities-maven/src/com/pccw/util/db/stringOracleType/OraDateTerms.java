/*
 * Created on Oct 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.pccw.util.db.stringOracleType;


/**
 * <h4>Purpose:</h4>
 * <blockquote>
 * <p>
 * 
 * </p>
 * </blockquote>
 * <p>
 * <b>Version: </b> 1.0
 * </p>
 * <p>
 * <b>System Name: </b> BOM
 * </p>
 * <p>
 * <b>Module Name: .StringOracleDate </b>
 * </p>
 * <p>
 * <b>Author: Raymond Wong KH </b>
 * </p>
 * <p>
 * <b>Created Date: </b>
 * </p>
 * <h4>Change Log:</h4>
 * <blockquote><table width="100%" border="0" cellspacing="0" cellpadding="0">
 * <tr>
 * <td width="20%"><b>Date </b></td>
 * <td width="30%"><b>Modify By </b></td>
 * <td width="50%"><b>Description </b></td>
 * </tr>
 * <tr>
 * <td>Oct 21, 2005</td>
 * <td>Raymond Wong KH</td>
 * <td>Create</td>
 * </tr>
 * </table> </blockquote>
 */

public class OraDateTerms extends OraDate {
	
	static final long serialVersionUID = 4986937942055341181L;

	public OraDateTerms() {
        this.setOracleDateFormat("DD/MM/YYYY");
    }

    /**
     * @param pValue
     */
    public OraDateTerms(String pValue) {
        super("DD/MM/YYYY", pValue);
    }
}