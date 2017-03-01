package com.pccw.util.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.sql.DataSource;
/**
 * E0002506 - Springboard SLV Enhancement (2015)
 * 
 * @author 01517028
 *
 */
public interface DaoHelperResolver extends DaoHelperResolverConstant {

	public DaoHelper resolveHelper(DataSource pDataSource);
	public DaoHelper resolveHelper(Connection pConn);
	public DaoHelper resolveHelper(DatabaseMetaData pDmd);
	public DaoHelper resolveDefaultDaoHelper(String pDBType);
	public String resolveDBType(DataSource pDataSource);
	public String resolveDBType(Connection pConn);
	public String resolveDBType(DatabaseMetaData pDmd);
	public void setDefaultDBType(String pDBType);
	public String getDefaultDBType();
	public String resolveDBType(ResultSetMetaData pResultSetMetaData);
	public String resolveDBType(ResultSet pResultSet);
	
	public boolean isOracle(String pDBType);
	public boolean isMySQL(String pDBType);

	public boolean isOracle(DataSource pDataSource);
	public boolean isMySQL(DataSource pDataSource);
	
	public boolean isOracle(Connection pConn);
	public boolean isMySQL(Connection pConn);
	
	public boolean isOracle(DatabaseMetaData pDmd);
	public boolean isMySQL(DatabaseMetaData pDmd);
}
