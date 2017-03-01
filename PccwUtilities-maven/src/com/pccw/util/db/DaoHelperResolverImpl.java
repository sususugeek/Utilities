package com.pccw.util.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.pccw.util.spring.SpringApplicationContext;
/**
 * E0002506 - Springboard SLV Enhancement (2015)
 * This class resolve the underlying database name by checking the driver name of DatabaseMetaData.
 * 
 * @author 01517028
 *
 */
public final class DaoHelperResolverImpl implements DaoHelperResolver {
	
	String defaultDBType;
	
	private static final Logger logger = LoggerFactory.getLogger(DaoHelperResolverImpl.class);

	private DaoHelperResolverImpl(){}
	
	private static class holder {
		private static final DaoHelperResolver instance = new DaoHelperResolverImpl();
		private static DaoHelperResolver customDaoHelperResolver = null;
		private static boolean isResolvedFromBean = false; 
	}

	public static DaoHelperResolver getInstance(){
		if(holder.isResolvedFromBean){
			return holder.customDaoHelperResolver == null? holder.instance:holder.customDaoHelperResolver;
		}else{
			try{
				holder.customDaoHelperResolver = SpringApplicationContext.getBean(CUSTOM_DAO_HELPER_RESOLVER);
			}catch(NoSuchBeanDefinitionException e){
				if(logger.isDebugEnabled()){
					logger.debug("No customDaoHelperResolver is found in context, using default resolver.");
				}
			}
			holder.isResolvedFromBean = true;
			return getInstance();
		}
	}
	
	/**
	 * Try to resolve the helper from driver name. E.g. if using db driver of oracle, this method will 
	 * return an instance of DaoOracleHelper, if driver is of MySQL, this method will return an instance
	 * of DaoMySQLHelper.
	 */
	public DaoHelper resolveHelper(DataSource pDataSource){
		DaoHelper ret = resolveDefaultDaoHelper(this.defaultDBType);
		Connection conn = null;
		if( pDataSource != null ){
			try {
//				conn = pDataSource.getConnection();
				conn = DataSourceUtils.getConnection(pDataSource);//use DataSourceUtils to prevent connection leak
				ret = resolveHelper(conn);
			}  finally {
				DataSourceUtils.releaseConnection(conn, pDataSource);
			}
		}
		
		return ret;
	}

	public DaoHelper resolveDefaultDaoHelper(String pDBType) {
		DaoHelper ret = null;
		try {
			if (DaoHelperResolverImpl.MYSQL.equals(pDBType)) {
				ret = SpringApplicationContext.getBean(DaoHelperResolverImpl.MYSQL + "DaoHelper");
			} else if (DaoHelperResolverImpl.ORACLE.equals(pDBType)) {
				ret = SpringApplicationContext.getBean(DaoHelperResolverImpl.ORACLE + "DaoHelper");
			} else if (DaoHelperResolverImpl.POSTGRES.equals(pDBType)) {
				ret = SpringApplicationContext.getBean(DaoHelperResolverImpl.POSTGRES + "DaoHelper");
			} else if (DaoHelperResolverImpl.SQLSERVER.equals(pDBType)) {
				ret = SpringApplicationContext.getBean(DaoHelperResolverImpl.SQLSERVER + "DaoHelper");
			}
		} catch (Exception e) {
			if(logger.isDebugEnabled()){
				logger.debug("Error getting DaoHelper Bean for type {}",pDBType);
			}
//			e.printStackTrace();
		}
		if (ret == null) {
			ret = new DaoOracleHelper();
		}
		return ret;
	}

	public String getDefaultDBType() {
		return defaultDBType;
	}

	public void setDefaultDBType(String defaultDBType) {
		this.defaultDBType = defaultDBType;
	}

	public DaoHelper resolveHelper(Connection pConn) {
		DaoHelper ret = null;
		DatabaseMetaData dmd = null;
		if(pConn != null){
			 try {
				dmd = pConn.getMetaData();
			} catch (SQLException e) {
				if(logger.isDebugEnabled()){
					logger.debug("Error getting DatabaseMetaData:{}, Error:{}", pConn, e);
				}
//				e.printStackTrace();
			}
		}
		ret = resolveHelper(dmd);
		return ret;
	}
	@Override
	public DaoHelper resolveHelper(DatabaseMetaData pDmd) {
		DaoHelper ret = null;
		if(pDmd != null){
			String driverName = "";
			try {
				driverName = pDmd.getDriverName();
			} catch (SQLException e) {
				if(logger.isDebugEnabled()){
					logger.debug("Error getting DaoHelper with DatabaseMetaData:{}, Error:{}", pDmd, e);
				}
//				e.printStackTrace();
			}
			if(StringUtils.isNotBlank(driverName)){
				try{
					for(int i=0; i<DATABASE_NAMES.length;i++){
						if(StringUtils.containsIgnoreCase(driverName, DATABASE_NAMES[i])){
							ret = SpringApplicationContext.getBean(DATABASE_NAMES[i]+"DaoHelper");
							break;
						}
					}
				}catch(Exception e){
					if(logger.isDebugEnabled()){
						logger.debug("Error getting DaoHelper bean with DatabaseMetaData:{}, DriverName:{}, Exception:{}.", new Object[]{pDmd, driverName, e});
					}
//					e.printStackTrace();
				}
			}
		}
		if(ret == null){
			ret = resolveDefaultDaoHelper(this.defaultDBType);
		}
		return ret;
	}

	@Override
	public String resolveDBType(DataSource pDataSource) {
		String ret = null;
		Connection conn = null;
		if( pDataSource != null ){
			try{
				conn = DataSourceUtils.getConnection(pDataSource);
				ret = resolveDBType(conn);
			}finally{
				DataSourceUtils.releaseConnection(conn, pDataSource);
			}
		}
		
		return ret;
	}

	@Override
	public String resolveDBType(Connection pConn) {
		String ret = null;
		DatabaseMetaData dmd = null;
		if(pConn != null){
			 try {
				dmd = pConn.getMetaData();
			} catch (SQLException e) {
				if(logger.isDebugEnabled()){
					logger.debug("Error getting DatabaseMetaData:{}, Error:{}", pConn, e);
				}
//				e.printStackTrace();
			}
		}
		ret = resolveDBType(dmd);
		return ret;
	}

	@Override
	public String resolveDBType(DatabaseMetaData pDmd) {
		String ret = null;
		if(pDmd != null){
			ret = DBTypeResolver.resolveDBType(pDmd);
		}
		if(ret == null){
			ret = this.getDefaultDBType();
			if(StringUtils.isBlank(ret)){
				ret = ORACLE;
			}
		}
		return ret;
	}
	
	@Override
	public String resolveDBType(ResultSet pResultSet) {
		String ret = null;
		ResultSetMetaData rsmd = null;
		if(pResultSet != null){
			try {
				rsmd = pResultSet.getMetaData();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
		if(rsmd != null){
			ret = resolveDBType(rsmd);
		}
		if(ret == null){
			ret = this.getDefaultDBType();
			if(StringUtils.isBlank(ret)){
				ret = ORACLE;
			}
		}
		return ret;
	}
	
	@Override
	public String resolveDBType(ResultSetMetaData pResultSetMetaData) {
		String ret = null;
		if(pResultSetMetaData != null){
			ret = DBTypeResolver.resolveDBType(pResultSetMetaData);
		}
		if(ret == null){
			ret = this.getDefaultDBType();
			if(StringUtils.isBlank(ret)){
				ret = ORACLE;
			}
		}
		return ret;
	}

	@Override
	public boolean isOracle(String pDBType) {
		return StringUtils.equalsIgnoreCase(DaoHelperResolver.ORACLE, pDBType);
	}

	@Override
	public boolean isMySQL(String pDBType) {
		return StringUtils.equalsIgnoreCase(DaoHelperResolver.MYSQL, pDBType);
	}

	@Override
	public boolean isOracle(DataSource pDataSource) {
		String dbType = this.resolveDBType(pDataSource);
		return this.isOracle(dbType);
	}

	@Override
	public boolean isMySQL(DataSource pDataSource) {
		String dbType = this.resolveDBType(pDataSource);
		return this.isMySQL(dbType);
	}

	@Override
	public boolean isOracle(Connection pConn) {
		String dbType = this.resolveDBType(pConn);
		return this.isOracle(dbType);
	}

	@Override
	public boolean isMySQL(Connection pConn) {
		String dbType = this.resolveDBType(pConn);
		return this.isMySQL(dbType);
	}

	@Override
	public boolean isOracle(DatabaseMetaData pDmd) {
		String dbType = this.resolveDBType(pDmd);
		return this.isOracle(dbType);
	}

	@Override
	public boolean isMySQL(DatabaseMetaData pDmd) {
		String dbType = this.resolveDBType(pDmd);
		return this.isMySQL(dbType);
	}
}
