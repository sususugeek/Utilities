/*
 * Created on Oct 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.pccw.util.db.stringOracleType;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;


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

public class OraDate implements Serializable{

    static final long serialVersionUID = 6540754951552766938L;

	public static int UPD_BY_INPUT_VALUE = 0;

    public static int UPD_BY_SYS_DATE = 1;
    
    public static String SYSDATE = "SYSDATE";

    private String value;

    private String oracleDateFormat = "YYYYMMDD";

    private int updateUsing = 0;

    public OraDate() {

    }

    /**
     * @param pValue
     */
    public OraDate(String pValue) {
        super();
        this.setValue(pValue);
    }

    /**
     * @param pValue
     */
    protected OraDate(String pOracleDateFormat, String pValue) {
        super();
        this.setOracleDateFormat(pOracleDateFormat);
        this.setValue(pValue);
    }
    
    /**
     * @return Returns the oracleDateFormat.
     */
    public String getOracleDateFormat() {
        return this.oracleDateFormat;
    }

    /**
     * @param pOracleDateFormat
     *            The oracleDateFormat to set.
     */
    public void setOracleDateFormat(String pOracleDateFormat) {
        this.oracleDateFormat = pOracleDateFormat;
    }

    /**
     * @return Returns the updateUsing.
     */
    public int getUpdateUsing() {
        return this.updateUsing;
    }

    /**
     * @param pUpdateUsing
     *            The updateUsing to set.
     */
    public void setUpdateUsing(int pUpdateUsing) {
        this.updateUsing = pUpdateUsing;
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
    	int dateLength = this.oracleDateFormat.length();
    	if (this.oracleDateFormat.indexOf("HH24") != -1) {
    		dateLength = dateLength - 2;
    	}
    	if (StringUtils.isBlank(pValue)) {
    		this.value = null;
    		return;
    	} else if (SYSDATE.equals(pValue)) {
        	this.setUpdateUsing(UPD_BY_SYS_DATE);
        	this.value = null;
        	return;
    	} else if (pValue.length() == dateLength) {
    		this.value = pValue;
    		return;
    	} else if (pValue.length() < dateLength) {
    		pValue = StringUtils.rightPad(pValue, dateLength, "0");
    		if(this.oracleDateFormat.indexOf(".FF3") > 0){
    			pValue = pValue.substring(0, 14)+"."+pValue.substring(15);
    		}
    	} else if (pValue.length() > dateLength) {
    		pValue = StringUtils.left(pValue, dateLength);
    	}
    	    	
        this.value = pValue;
    }

    public void setValue(java.util.Date pDate) {
    	if (pDate == null) {
    		return;
    	}
    	this.value = this.getSimpleDateFormat().format(pDate);
    }

    public void setValue(Date pDate) {
    	if (pDate == null) {
    		return;
    	}
    	this.value = this.getSimpleDateFormat().format(pDate);
    }
    
    public String toString() {
        return this.value;
    }
    
    public Date toSqlDate() throws Exception {
    	if (this.value == null) {
    		return null;
    	}
    	if (SYSDATE.equals(this.value)) {
    		return new Date(this.getSimpleDateFormat().parse(this.getSimpleDateFormat().format(new java.util.Date())).getTime());
    	}
        return new Date(this.getSimpleDateFormat().parse(this.value).getTime());
    }
    
    public SimpleDateFormat getSimpleDateFormat() {
    	String dateFormat = this.oracleDateFormat.toUpperCase();
    	dateFormat = StringUtils.replace(dateFormat, "Y", "y");
    	dateFormat = StringUtils.replace(dateFormat, "D", "d");
    	dateFormat = StringUtils.replace(dateFormat, "S", "s");
    	dateFormat = StringUtils.replace(dateFormat, "HH24", "HH");
    	dateFormat = StringUtils.replace(dateFormat, "MI", "mm");
    	return new SimpleDateFormat(dateFormat);
    }
}