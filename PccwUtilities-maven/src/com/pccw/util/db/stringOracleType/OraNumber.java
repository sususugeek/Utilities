package com.pccw.util.db.stringOracleType;

import java.io.Serializable;

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
 * <b>Module Name: </b>
 * </p>
 * <p>
 * <b>Author: </b>
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
 * <td></td>
 * <td></td>
 * <td>Create</td>
 * </tr>
 * </table> </blockquote>
 */

public class OraNumber implements Serializable {
	
	static final long serialVersionUID = -5163233422458572279L;
	
	private String value;

	public OraNumber(String pValue) {
        super();
        this.value = pValue;
    }

	public OraNumber(int pValue) {
        super();
        this.value = String.valueOf(pValue);
    }


	public OraNumber(float pValue) {
        super();
        this.value = String.valueOf(pValue);
    }
	
    /**
     * @return Returns the value.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @param pValue
     *            The value to set.
     */
    public void setValue(String pValue) {
        this.value = pValue;
    }	
    
    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.value;
	}
}
