package com.pccw.util.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class DBTypeResolver {
	private static final Logger logger = LoggerFactory.getLogger(DBTypeResolver.class);
	public static String resolveDBType(DatabaseMetaData pDmd) {
		String ret = null;
		if(pDmd != null){
			String driverName = "";
			try {
				driverName = pDmd.getDriverName();
			} catch (SQLException e) {
				if(logger.isDebugEnabled()){
					logger.debug("Error getting DatabaseMetaData:{}, Error:{}", pDmd, e);
				}
				e.printStackTrace();
			}
			if(StringUtils.isNotBlank(driverName)){
				for(int i=0; i<DaoHelperResolver.DATABASE_NAMES.length;i++){
					if(StringUtils.containsIgnoreCase(driverName, DaoHelperResolver.DATABASE_NAMES[i])){
						ret = DaoHelperResolver.DATABASE_NAMES[i];
						break;
					}
				}

			}
		}
		return ret;
	}
	
	public static String resolveDBType(Connection pConn) {
		String ret = null;
		DatabaseMetaData dmd = null;
		if(pConn != null){
			 try {
				dmd = pConn.getMetaData();
			} catch (SQLException e) {
				if(logger.isDebugEnabled()){
					logger.debug("Error getting DatabaseMetaData:{}, Error:{}", pConn, e);
				}
				e.printStackTrace();
			}
		}
		ret = resolveDBType(dmd);
		return ret;
	}
	
	public static String resolveDBType(DataSource pDataSource) {
		String ret = DaoHelperResolver.ORACLE;
		Connection conn = null;
		if( pDataSource != null ){
			try {
				conn = DataSourceUtils.getConnection(pDataSource);
				ret = resolveDBType(conn);
			} finally {
				DataSourceUtils.releaseConnection(conn, pDataSource);
			}
		}
		
		return ret;
	}
	
	public static String resolveDBType(ResultSetMetaData pResultSetMetaData) {
		String ret = null;
		if(pResultSetMetaData != null){
			String driverName = "";
			driverName = pResultSetMetaData.getClass().getName();
		
			if(StringUtils.isNotBlank(driverName)){
				for(int i=0; i<DaoHelperResolver.DATABASE_NAMES.length;i++){
					if(StringUtils.containsIgnoreCase(driverName, DaoHelperResolver.DATABASE_NAMES[i])){
						ret = DaoHelperResolver.DATABASE_NAMES[i];
						break;
					}
				}

			}
		}
		return ret;
	}
}
