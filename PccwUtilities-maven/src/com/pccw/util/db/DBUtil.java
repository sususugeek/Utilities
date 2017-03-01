package com.pccw.util.db;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;



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
 * <b>Module Name: bom.util.db.DBUtil </b>
 * </p>
 * <p>
 * <b>Author: Chris Leung </b>
 * </p>
 * <p>
 * <b>Created Date: 20051018</b>
 * </p>
 * <h4>Change Log:</h4>
 * <blockquote><table width="100%" border="0" cellspacing="0" cellpadding="0">
 * <tr>
 * <td width="20%"><b>Date </b></td>
 * <td width="30%"><b>Modify By </b></td>
 * <td width="50%"><b>Description </b></td>
 * </tr>
 * <tr>
 * <td>14-Apr-2005</td>
 * <td>Chris Leung</td>
 * <td>Create</td>
 * </tr>
 * <tr>
 * <td>18-OCT-2005</td>
 * <td>Raymond Wong KH</td>
 * <td>ADD DFM Connection</td>
 * </tr>
 * </table> </blockquote>
 */
public class DBUtil {
    private DBUtil() {
    }

   
    private final static Logger logger = Logger.getLogger(DBUtil.class);


    /*
     * Close a resultset
     *
     * @param conn Connection that need to release.
     */
    public static void close(ResultSet pResultSet) {
        if (pResultSet == null) {
            return;
        }

        try {
            pResultSet.close();
        } catch (SQLException e) {
            logger.warn("Error in closing a resultset! " + pResultSet, e);
        }
    }

    /*
     * Close a statement
     *
     * @param conn Connection that need to release.
     */
    public static void close(Statement pStatement) {
        if (pStatement == null) {
            return;
        }

        try {
            pStatement.close();
        } catch (SQLException e) {
            logger.warn("Error in closing a statment! " + pStatement, e);
        }
    }

    /*
     * Set parameter array for preparedstatement
     */
    public static void setStatementParam(PreparedStatement pStatement,
            Object[] pParameters) throws SQLException {

        if (pStatement == null || pParameters == null || pParameters.length < 1) {
            return;
        }

        for (int i = 0; i < pParameters.length; i++) {
            if (pParameters[i] == null) {
                pStatement.setNull(i + 1, Types.NULL);
            }
            if (pParameters[i] instanceof String) {
                pStatement.setString(i + 1, (String) pParameters[i]);
            } else if (pParameters[i] instanceof Boolean) {
                pStatement.setBoolean(i + 1, ((Boolean) pParameters[i])
                        .booleanValue());
            } else if (pParameters[i] instanceof Integer) {
                pStatement.setInt(i + 1, ((Integer) pParameters[i]).intValue());
            } else if (pParameters[i] instanceof Date) {
                pStatement.setDate(i + 1, (Date) pParameters[i]);
            } else if (pParameters[i] instanceof Time) {
                pStatement.setTime(i + 1, (Time) pParameters[i]);
            } else if (pParameters[i] instanceof Timestamp) {
                pStatement.setTimestamp(i + 1, (Timestamp) pParameters[i]);
            } else if (pParameters[i] instanceof Double) {
                pStatement.setDouble(i + 1, ((Double) pParameters[i])
                        .doubleValue());
            } else if (pParameters[i] instanceof Float) {
                pStatement.setFloat(i + 1, ((Float) pParameters[i])
                        .floatValue());
            } else if (pParameters[i] instanceof Byte) {
                pStatement.setByte(i + 1, ((Byte) pParameters[i]).byteValue());
            } else if (pParameters[i] instanceof byte[]) {
                pStatement.setBytes(i + 1, (byte[]) pParameters[i]);
            } else if (pParameters[i] instanceof Long) {
                pStatement.setLong(i + 1, ((Long) pParameters[i]).longValue());
            } else if (pParameters[i] instanceof Short) {
                pStatement.setShort(i + 1, ((Short) pParameters[i])
                        .shortValue());
            } else if (pParameters[i] instanceof BigDecimal) {
                pStatement.setBigDecimal(i + 1, (BigDecimal) pParameters[i]);
            } else if (pParameters[i] instanceof Ref) {
                pStatement.setRef(i + 1, (Ref) pParameters[i]);
            } else if (pParameters[i] instanceof Clob) {
                pStatement.setClob(i + 1, (Clob) pParameters[i]);
            } else if (pParameters[i] instanceof Array) {
                pStatement.setArray(i + 1, (Array) pParameters[i]);
            } else {
                pStatement.setObject(i + 1, pParameters[i]);
            }
        }
    }

    /*
     * Set autocommit flag for connection
     *
     * @param conn Connection that need to set autocommit flag.
     * @param autoCommit Autocommit flag
     */
    public static void setAutoCommit(Connection pConnection, boolean pIsAutoCommit) throws Exception {
        try {
            if (pConnection == null) {
                logger.warn("Null Connection is asked to setAutoCommit to " + pIsAutoCommit);
                return;
            }
            pConnection.setAutoCommit(pIsAutoCommit);

        } catch ( SQLException ex) {
            logger.error("SET CONNECTION AUTOCOMMIT - EXCEPTON:\n" + ExceptionUtils.getFullStackTrace(ex));
            throw ex;
        }
    }

    /*
     * Commit the DB connection
     *
     * @param conn Connection that need to commit.
     */
    public static void commit(Connection pConnection) throws Exception{
        try {
            if (pConnection == null) {
                logger.warn("Null Connection is asked to commit");
                return;
            }
            pConnection.commit();
        } catch ( SQLException ex ) {
            logger.error("CONNECTION COMMIT - EXCEPTON:\n" + ExceptionUtils.getFullStackTrace(ex));
            throw ex;
        }
    }

    /*
     * Rollback the DB connection
     *
     * @param conn Connection that need to rollback.
     */
    public static void rollback(Connection conn) throws Exception {
        try {
            if (conn == null) {
                return;
            }
            conn.rollback();
        } catch ( SQLException ex ) {
            logger.error("CONNECTION ROLLBACK - EXCEPTON:\n" + ExceptionUtils.getFullStackTrace(ex));
            throw ex;
        }
    }
}