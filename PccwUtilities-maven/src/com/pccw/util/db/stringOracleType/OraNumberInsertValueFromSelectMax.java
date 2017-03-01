package com.pccw.util.db.stringOracleType;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import com.pccw.util.db.DaoBaseImpl;
import com.pccw.util.db.DaoHelper;
import com.pccw.util.db.DaoHelper.DaoProperty;
import com.pccw.util.db.DaoHelper.OracleColumn;
import com.pccw.util.db.DaoHelperResolver;
import com.pccw.util.db.DaoHelperResolverImpl;
import com.pccw.util.db.DaoOracleHelper;
import com.pccw.util.db.OracleSelectHelper;

public class OraNumberInsertValueFromSelectMax extends OraNumberInsertValueFromSelect {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8432270629970362060L;

	private String selectMaxFieldName;

	private String selectMaxTableName;

	private String[] whereBindingField;
	// E0002506 - Springboard SLV Enhancement (2015)
	private DaoHelperResolver daoHelperResolver;
	
	public DaoHelperResolver getDaoHelperResolver() {
		if(daoHelperResolver == null){
			daoHelperResolver = DaoHelperResolverImpl.getInstance();
		}
		return daoHelperResolver;
	}

	public void setDaoHelperResolver(DaoHelperResolver daoHelperResolver) {
		this.daoHelperResolver = daoHelperResolver;
	}

	private DaoHelper daoHelper;
	
	/**
	 * Using resolver to get the DaoHelper instead injecting by Spring so that 
	 * no need to change the XML settings of each DAO for backward compatibility.
	 * 
	 * Of course an DaoHelper instance could be injected by Spring still from XML configuration.
	 * @return
	 * DaoHelper
	 */
	public DaoHelper getDaoHelper(DaoBaseImpl pDao) {
		if(this.daoHelper == null){
			if(this.daoHelper == null){
				this.daoHelper = getDaoHelperResolver().resolveHelper(pDao.getDataSource());
				if(this.daoHelper == null){//fallback to oracle implementation
					this.daoHelper = new DaoOracleHelper();
				}
			}
		}
		return daoHelper;
	}

	public void setDaoHelper(DaoHelper daoHelper) {
		this.daoHelper = daoHelper;
	}
	// Add an instance of daoHelper to enable injection of different implementation
	
	public OraNumberInsertValueFromSelectMax(String pSelectMaxFieldName,
			String pSelectMaxTableName, String[] pWhereBindingField) {
		super(null);
		this.selectMaxFieldName = pSelectMaxFieldName;
		this.selectMaxTableName = pSelectMaxTableName;
		this.whereBindingField = pWhereBindingField;
	}

	public String[] getWhereBindingField() {
		return this.whereBindingField;
	}

	@Override
	public String getInsertValue(DaoBaseImpl pDao) throws Exception {
		Map<String, DaoProperty> daoPropertyMap = pDao.getPropertyMap();
		DaoProperty selectDaoProperty = DaoHelper.getDaoProperty(daoPropertyMap, selectMaxFieldName);
		String maxDbColumnName = this.selectMaxFieldName;
		if (selectDaoProperty != null) {
			maxDbColumnName = getDaoHelper(pDao).getOracleColumn(pDao, selectDaoProperty).getOracleFieldName();// E0002506 - Springboard SLV Enhancement (2015)
		}
		StringBuilder sql = new StringBuilder("SELECT TO_CHAR(NVL(MAX(")
				.append(maxDbColumnName).append("), 0) + 1)").append(
						'"' + this.selectMaxFieldName + '"').append(" FROM ")
				.append(this.selectMaxTableName);
		ArrayList<String> bindingValueList = new ArrayList<String>();
		if (!ArrayUtils.isEmpty(this.whereBindingField)) {
			sql.append(" WHERE ");
			
			OracleColumn whereColumn = getDaoHelper(pDao).getOracleColumn(pDao, DaoHelper.getDaoProperty(daoPropertyMap, this.whereBindingField[0]));// E0002506 - Springboard SLV Enhancement (2015)
			sql.append(whereColumn.getOracleFieldName());
			sql.append(" = ");
			sql.append(whereColumn.getOracleInsUpdValueClause());
			if (whereColumn.getOracleInsUpdValueClause().indexOf("?") != -1) {
				bindingValueList.add(whereColumn.getColumnValue());
			}
			
			for (int i = 1; i < this.whereBindingField.length; i++) {
				sql.append(" AND ");
				whereColumn = getDaoHelper(pDao).getOracleColumn(pDao, DaoHelper.getDaoProperty(daoPropertyMap, this.whereBindingField[i]));// E0002506 - Springboard SLV Enhancement (2015)
				sql.append(whereColumn.getOracleFieldName());
				sql.append(" = ");
				sql.append(whereColumn.getOracleInsUpdValueClause());
				if (whereColumn.getOracleInsUpdValueClause().indexOf("?") != -1) {
					bindingValueList.add(whereColumn.getColumnValue());
				}
			}
		}
		
		String dbType = pDao.getDaoHelperResolver().resolveDBType(pDao.getDataSource());
		String execSql = sql.toString();
		if (!DaoHelperResolver.ORACLE.equals(dbType)) {
			execSql = DaoHelper.convertStatement(DaoHelperResolver.ORACLE, dbType, execSql);
		}
		
		return OracleSelectHelper.getSqlFirstRowColumnString(
				pDao.getDataSource(), 
				execSql, bindingValueList.toArray());
	}
}