/*
 * Created on 2005/11/19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.pccw.util.db.stringOracleType;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.nativejdbc.CommonsDbcpNativeJdbcExtractor;

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
 * <b>Module Name: bom.util.db.stringOracleType.OraArray </b>
 * </p>
 * <p>
 * <b>Author: Raymond Wong KH </b>
 * </p>
 * <p>
 * <b>Created Date: 2005�~11��19�� </b>
 * </p>
 * <h4>Change Log:</h4>
 * <blockquote><table width="100%" border="0" cellspacing="0" cellpadding="0">
 * <tr>
 * <td width="20%"><b>Date </b></td>
 * <td width="30%"><b>Modify By </b></td>
 * <td width="50%"><b>Description </b></td>
 * </tr>
 * <tr>
 * <td>2005�~11��19��</td>
 * <td>Raymond Wong KH</td>
 * <td>Create</td>
 * </tr>
 * </table> </blockquote>
 */
public abstract class OraArray implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8682015342771849132L;
	String oracleType;

	/**
	 * @param pValue
	 * @param pOracleType
	 */
	public OraArray(String pOracleType) {
		super();
		this.oracleType = pOracleType;
	}

	/**
	 * @return Returns the oracleType.
	 */
	public String getOracleType() {
		return this.oracleType;
	}

	/**
	 * @param pOracleType
	 *            The oracleType to set.
	 */
	public void setOracleType(String pOracleType) {
		this.oracleType = pOracleType;
	}

	/**
	 * @return Returns the value.
	 */
	public abstract Object[] getValue();

	public abstract Object[] getBindingValue();
	
	public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        Object[] values = this.getValue();
        for (int i = 0; values != null && i < values.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(values[i] == null ? "null" : values[i].toString());
        }
        sb.append("}");
        return sb.toString();
	}

	public ARRAY getOracleArray(Connection pConnection) throws SQLException {
		Connection conn = (new CommonsDbcpNativeJdbcExtractor()).getNativeConnection(pConnection);
		ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor(this.oracleType, conn);
		return new ARRAY(arrayDescriptor, conn, this.getBindingValue());
	}
	
	public ARRAY getOracleArray(DataSource pDataSource) throws SQLException {
		Connection conn = null;
		try {
			conn = DataSourceUtils.getConnection(pDataSource);
			return getOracleArray(conn);	
		} finally {
			DataSourceUtils.releaseConnection(conn, pDataSource);
		}
	}
}