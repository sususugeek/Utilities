package com.pccw.util.db.mysql;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.pccw.util.db.DaoBase;
import com.pccw.util.db.DaoBaseImpl;
import com.pccw.util.db.DaoHelper;
import com.pccw.util.db.DaoHelperResolver;
import com.pccw.util.db.DaoHelperResolverConstant;
import com.pccw.util.db.OracleSelectHelper;
import com.pccw.util.db.DaoHelper.DaoProperty;
import com.pccw.util.db.DaoHelper.OracleColumn;
import com.pccw.util.db.stringOracleType.OraBLOB;
import com.pccw.util.db.stringOracleType.OraCLOB;
import com.pccw.util.db.stringOracleType.OraDate;
import com.pccw.util.db.stringOracleType.OraDateTimestamp;
import com.pccw.util.db.stringOracleType.OraNumber;
import com.pccw.util.db.stringOracleType.OraNumberInsertValueFromSelect;
/**
 * 
 * For project E0002506 - Springboard SLV Enhancement (2015)
 * 
 * MySQL implementation of DaoHelper
 * @author 01517028
 *
 */
public class DaoMySQLHelper extends DaoHelper {
	private final static Logger logger = Logger.getLogger(DaoMySQLHelper.class);
		
	private final static String RESOLVE_COLUMN_EXISTENCE_SQL = 
			" select CAST(count(*) AS CHAR(5)) " +
			"   from information_schema.columns " +
			"  where table_schema = ? and table_name = ? and column_name = ? ";
	
	private String defaultSchema = "spbuat";
	static final String LAST_UPDATE_DATE = "lastUpdateDate";
	static final String CREATE_DATE = "CREATEDATE";
	static final String LAST_UPD_DATE = "lastUpdDate";
	static final String[][] ORACLE_TO_CHAR_DATEFORMAT_MAPPING = { 
			{ "YYYY", "%Y" }, 
			{ "YY", "%y" },
			{ "RRRR", "%Y" },
			{ "RR", "%y" },
			{ "MON", "%b" },
			{ "MONTH", "%M" },
			{ "MM", "%m" },
			{ "DY", "%a" },
			{ "DD", "%d" },
			{ "HH24", "%H" },
			{ "HH12", "%h" },
			{ "HH", "%h" },
			{ "MI", "%i" },
			{ "SS", "%s" },
			{ "FF3", "%f" }
			};
	
	public DaoSql buildSelectStatement(DaoBaseImpl pDao, ArrayList<String> pColumnsToRetrieve,
			ArrayList<String> pWhereColumns, String pAdditionWhere, String pOrderBy) throws Exception {

		DaoSql daoSql = new DaoSql();
		StringBuilder sbSql = new StringBuilder();
		StringBuilder additionWhereSb = new StringBuilder(pAdditionWhere == null
				? (pDao.getAdditionWhere() == null ? "" : pDao.getAdditionWhere()) : pAdditionWhere);

		ArrayList<String> searchKeyInList = pDao.getSearchKeyInList();
		for (String searchKeyIn : searchKeyInList) {
			if (additionWhereSb.length() > 0) {
				additionWhereSb.append(" AND ");
			}
			additionWhereSb.append(searchKeyIn);
		}

		String additionWhere = additionWhereSb.toString();

		sbSql.append(getSelectClause(pDao, pColumnsToRetrieve));

		ArrayList<OracleColumn> oraWhereColumnList = new ArrayList<OracleColumn>();
		oraWhereColumnList = getSelUpdWhereFieldList(pDao,
				pWhereColumns == null ? pDao.getSearchKeyList() : pWhereColumns, daoSql);

		sbSql.append(getWhereClause(pDao, oraWhereColumnList, additionWhere));

		if (StringUtils.isNotBlank(pOrderBy)) {
			sbSql.append(" ORDER BY ");
			sbSql.append(pOrderBy);
		}

		ArrayList<OracleColumn> bindOracleColumnList = new ArrayList<OracleColumn>();
		super.addExtraBinds(bindOracleColumnList, pDao, pDao.getTableName(), sbSql);
		if (oraWhereColumnList != null && oraWhereColumnList.size() > 0) {
			bindOracleColumnList.addAll(oraWhereColumnList);
		}
		super.addExtraBinds(bindOracleColumnList, pDao, additionWhere, sbSql);

		ArrayList<String> bindingValueList = new ArrayList<String>();

		/*
		 * for (OracleColumn oraColumn : oraWhereColumnList) { if
		 * (oraColumn.getOracleInsUpdValueClause().indexOf("?") != -1) {
		 * bindingValueList.add(oraColumn.getColumnValue()); } }
		 */

		for (OracleColumn oraColumn : bindOracleColumnList) {
			if (oraColumn.getOracleInsUpdValueClause().indexOf("?") != -1) {
				bindingValueList.add(oraColumn.getColumnValue());
			}
		}

		// Add LIMIT clause to restrict number of rows to be returned
		if(pDao.getTopRows() > 0){
			sbSql.append(" LIMIT ").append(pDao.getTopRows());
		}
		
		daoSql.setSql(sbSql.toString());
		daoSql.setBindingValues(bindingValueList);
		return daoSql;
	}
	
	public String getSelectClause(DaoBaseImpl pDao, ArrayList<String> pColumnsToRetrieve) throws Exception {

		String primaryRowId = null;
		StringBuffer sbRtnSql = new StringBuffer();

		sbRtnSql.append("SELECT ");
		if (StringUtils.isNotBlank(pDao.getOracleHints())) {
			if (pDao.getOracleHints().indexOf("/*+") != 0) {
				sbRtnSql.append("/*+");
			}
			sbRtnSql.append("pDao.getOracleHints()");
			if (pDao.getOracleHints().indexOf("*/") == -1) {
				sbRtnSql.append("*/");
			}
		}

		if (pDao.isDistinctResult()) {
			sbRtnSql.append("DISTINCT ");
		}

		if (pDao.isIncludeColumn(DaoHelper.DAO_PROPERTY_ORACLE_ROW_ID) && !pDao.isExcludeColumn(DaoHelper.DAO_PROPERTY_ORACLE_ROW_ID)) {
			primaryRowId = pDao.getPrimaryRowId();
			if (primaryRowId == null) {
				if (this.resolveFieldExistency(pDao, "UNIQUE_ROW_ID")){
					primaryRowId = " UNIQUE_ROW_ID ";
				} else {
					primaryRowId = getPrimaryKeyColumn(pDao);
				}
			}
			sbRtnSql.append(" " + primaryRowId + " `oracleRowID`");
		} else {
			sbRtnSql.append(" NULL `oracleRowID`");
		}
		
		Map<String, String> sqlMap = getColumnsToSelect(pDao, pColumnsToRetrieve);
		
		String oraRowscnClause = sqlMap.get(MYSQL_ORA_ROWSCN_CLAUSE);
		if (StringUtils.isNotBlank(oraRowscnClause)) {
			sbRtnSql.append(", ");
			sbRtnSql.append(this.getOraRowscnColumn(oraRowscnClause));
			sbRtnSql.append(" `oraRowscn`");
		}
		
		String columnsToRetrieve = sqlMap.get(ORACLE_SELECT_CLAUSE);
				
		if (StringUtils.isNotBlank(columnsToRetrieve)) {
			sbRtnSql.append(", ");
			sbRtnSql.append(columnsToRetrieve);
		}

		sbRtnSql.append(" FROM ");
		sbRtnSql.append(getTableName(pDao));

		String statementName = pDao.getClass().getName() + ".SELECT";
		return insertPackageNameToSql(statementName, sbRtnSql.toString(), DaoHelperResolverConstant.MYSQL);
	}

	public String getSQLInsertUpdateClause(OraDate pOraDate) {
		if(!this.isSkipUpdBySysDate()){
			if (pOraDate.getUpdateUsing() == OraDate.UPD_BY_SYS_DATE) {
				if (pOraDate instanceof OraDateTimestamp) {
					return "NOW(6)";
				} else {
					return "SYSDATE(6)";
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		if (pOraDate instanceof OraDateTimestamp) {
			sb.append("STR_TO_DATE(?, '");//for timestamp
		} else {
			sb.append("STR_TO_DATE(?, '");//for datetime
		}
		sb.append(this.convertDateFormat(pOraDate.getOracleDateFormat(),DaoHelperResolver.ORACLE));
		sb.append("')");
		return sb.toString();
	}
	
	public String generateSQLSelectClause(DaoProperty pDaoProperty) {
		if (pDaoProperty.getPropertyName().equals(DaoHelper.DAO_PROPERTY_ORACLE_ROW_ID)) {
//			return "ROWIDTOCHAR(ROWID) \"oracleRowID\"";
			return "CAST (UNIQUE_ROW_ID as char) `oracleRowID`";
		} else if (pDaoProperty.getPropertyClass().getName().indexOf("OraNumber") != -1) {
			return "CAST(" + pDaoProperty.getOracleFieldName() + " as CHAR) + 0 `" + pDaoProperty.getPropertyName() + "`";
		}
		return pDaoProperty.getOracleFieldName() + " `" + pDaoProperty.getPropertyName() + "`";
	}

	@Override
	public String generateSQLInsertUpdateClause(DaoProperty pDaoProperty) {
		if (pDaoProperty.getPropertyName().equals(DaoHelper.DAO_PROPERTY_ORACLE_ROW_ID)) {
//			return "CHARTOROWID(?)";
			return generateSQLInsertUpdateClause((OraNumber) null);
		} else if (pDaoProperty.getPropertyClass().getName().indexOf("OraNumber") != -1) {
			return generateSQLInsertUpdateClause((OraNumber) null);
		}
		return "?";
	}
	
	@Override
	public String generateSQLInsertUpdateClause(OraNumber pOraNumber) {
		return "CONVERT(?, decimal(65,20))";
	}
	
	public String getSQLSelectClause(OraDate pOraDate, DaoProperty pDaoProperty) {
		StringBuffer sb = new StringBuffer();
		String propertyName = pDaoProperty.getPropertyName();
		String dataFormatStr = convertDateFormat(pOraDate.getOracleDateFormat(), DaoHelperResolver.ORACLE);
		if(StringUtils.equalsIgnoreCase(LAST_UPDATE_DATE, propertyName) || 
			StringUtils.equalsIgnoreCase(CREATE_DATE, propertyName) ||
			StringUtils.equalsIgnoreCase(LAST_UPD_DATE, propertyName) ){
			dataFormatStr = dataFormatStr+".%f";
		}
		
		sb.append("DATE_FORMAT(");
		sb.append(pDaoProperty.getOracleFieldName());
		sb.append(", '");
//		sb.append(convertDateFormat(pOraDate.getOracleDateFormat(), DaoHelperResolver.ORACLE));
		sb.append(dataFormatStr);
		sb.append("')");
		sb.append(" `");
		sb.append(pDaoProperty.getPropertyName());
		sb.append("`");
		return sb.toString();
	}

	/**
	 * Convert date format string to MySQL format.
	 * @param pDateFormatStr. The date format string to be converted.
	 * @param pOriginalDBStr. The name of original database.
	 * @return The date format string of MySQL format.
	 */
	public String convertDateFormat(String pDateFormatStr, String pOriginalDBStr){
		String ret = null;
		if(DaoHelperResolver.ORACLE.equals(pOriginalDBStr)){//oracle format
			ret = super.convertDateFormat(ORACLE_TO_CHAR_DATEFORMAT_MAPPING, pDateFormatStr, false);
		}
		return ret;
	}

	@Override
	public String topRowsClause(int pNumOfTop) {
		StringBuilder ret = new StringBuilder("");
		if(pNumOfTop > 0){
			ret.append("LIMIT ").append(pNumOfTop);
		}
		return ret.toString();
	}

	@Override
	public String getDBType() {
		return DaoHelperResolver.MYSQL;
	}

	@Override
	public String generateSQLInsertUpdateClause(OraDate pOraDate) {
		if(!this.isSkipUpdBySysDate()){
			if (pOraDate.getUpdateUsing() == OraDate.UPD_BY_SYS_DATE) {
				if (pOraDate instanceof OraDateTimestamp) {
				  return "NOW(6)";	
				} else {
				  return "SYSDATE(6)";
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		
		sb.append("STR_TO_DATE(?, '");
		sb.append(convertDateFormat(pOraDate.getOracleDateFormat(), DaoHelperResolver.ORACLE));
		sb.append("')");
		return sb.toString();
	}
	
	@Override
	public DaoProperty getDaoPropertyRowID() {
		return new DaoProperty(DaoBaseImpl.class, DaoHelper.DAO_PROPERTY_ORACLE_ROW_ID, "UNIQUE_ROW_ID", String.class);
	}
	
	@Override
	public ArrayList<OracleColumn> getSelUpdWhereFieldList(DaoBaseImpl pDao, ArrayList<String> pWhereColumns,
			DaoSql pDaoSql) throws Exception {
		ArrayList<OracleColumn> whereFieldList = new ArrayList<OracleColumn>();
		Map<String, DaoProperty> daoPropertyMap = pDao.getPropertyMap();
		DaoProperty daoProperty = null;
		if (pWhereColumns != null && pWhereColumns.size() > 0) {
			for (String propertyName : pWhereColumns) {
				daoProperty = getDaoProperty(daoPropertyMap, propertyName);
				if (daoProperty == null) {
					logger.warn(propertyName + " NOT FOUND in " + pDao.getClass().getName());
					continue;
				}
				whereFieldList.add(getOracleColumn(pDao, daoProperty));
			}
		} else if (pDao.getAdditionWhere() != null || pDao.getSearchKeyInList().size() > 0) {
			// additionWhere or searchKeyIn is first priority over rowid and
			// primary key fields

		} else if (!StringUtils.isEmpty(pDao.getOracleRowID()) && this.resolveFieldExistency(pDao, "UNIQUE_ROW_ID")) {
			OracleColumn oraColumn = new OracleColumn();
			oraColumn.setDaoFieldName(DaoHelper.DAO_PROPERTY_ORACLE_ROW_ID);
			oraColumn.setOracleFieldName("UNIQUE_ROW_ID");
			oraColumn.setColumnValue(pDao.getOracleRowID());
			oraColumn.setOracleInsUpdValueClause("CONVERT(?, decimal(22,6))");
			oraColumn.setOracleSelectClause("CAST(UNIQUE_ROW_ID as char) \"oracleRowID\"");
			whereFieldList.add(oraColumn);
			if (pDaoSql != null) {
				pDaoSql.setUpdateByRowId(true);
			}
		} else if (!ArrayUtils.isEmpty(pDao.getPrimaryKeyFields())) {
			daoPropertyMap = pDao.getPropertyMap();
			for (String propertyName : pDao.getPrimaryKeyFields()) {
				daoProperty = getDaoProperty(daoPropertyMap, propertyName);
				if (daoProperty == null) {
					logger.warn(propertyName + "NOT FOUND in " + pDao.getClass().getName());
					continue;
				}

				OracleColumn oraColumn = getOracleColumn(pDao, daoProperty);
				whereFieldList.add(oraColumn);
			}
			if (pDaoSql != null) {
				pDaoSql.setUpdateByPrimaryKey(true);
			}
		}
		return whereFieldList;
	}

	@Override
	public boolean resolveFieldExistency(DaoBase pDaoBase, String pFieldName) throws Exception {
//		RESOLVE_COLUMN_EXISTENCE_SQL
		boolean ret = false;
		if(pDaoBase instanceof DaoBaseImpl){
			DaoBaseImpl daoBaseImpl = (DaoBaseImpl)pDaoBase;
			String schema = this.getDefaultSchema();
			Connection conn = null;
			DataSource ds = daoBaseImpl.getDataSource();
			try {
//				schema = daoBaseImpl.getDataSource().getConnection().getCatalog();
				conn = DataSourceUtils.getConnection(ds);//use DataSourceUtils to prevent connection leak
				schema = conn.getCatalog();
			} catch (SQLException ignore) {
				ignore.printStackTrace();
			} finally {
				DataSourceUtils.releaseConnection(conn, ds);
			}
			String tablename = pDaoBase.getTableName();
			Object [] args = {schema, tablename.toLowerCase(), pFieldName};
			
			String count = OracleSelectHelper.getSqlFirstRowColumnString(daoBaseImpl.getDataSource(), RESOLVE_COLUMN_EXISTENCE_SQL, args);
			if (Integer.parseInt(count) == 1) {
				ret = true;
			}
		}
		return ret;
	}
	@Override
	public ArrayList<OracleColumn> getOracleColumnList(DaoBaseImpl pDao) {
		ArrayList<OracleColumn> oraColumnList = new ArrayList<OracleColumn>();
		OracleColumn oracleColumn;

		try {
			for (DaoProperty daoProperty : pDao.getPropertyMap().values()) {
				oracleColumn = getOracleColumn(pDao, daoProperty);
				if (!pDao.isIncludeColumn(oracleColumn.getOracleFieldName())
						|| pDao.isExcludeColumn(oracleColumn.getOracleFieldName()) || "ORA_ROWSCN".equals(oracleColumn.getOracleFieldName())) {
					continue;
				} else {
					oraColumnList.add(oracleColumn);
				}

			}
		} catch (Exception e) {
			logger.error("getOracleColumnList() - Exception" + ExceptionUtils.getFullStackTrace(e));
		}
		return oraColumnList;
	}
	


	
	@Override
	public ArrayList<String> getAllFieldList(DaoBaseImpl pDao, Map<String, DaoProperty> pDaoPropertyMap)
			throws Exception {

		ArrayList<String> rtnList = new ArrayList<String>();
		if (pDaoPropertyMap.size() > 0) {
			String propertyName = null;
			for (Iterator<String> it = pDaoPropertyMap.keySet().iterator(); it.hasNext();) {
				propertyName = it.next();
				if (!pDao.isIncludeColumn(propertyName) || pDao.isExcludeColumn(propertyName) || "ORA_ROWSCN".equals(propertyName)) {
					continue;
				}
				rtnList.add(propertyName);
			}
		}
		return rtnList;
	}
	
	@Override
	public OracleColumn getOracleColumn(DaoBase pDao, DaoProperty pDaoProperty) throws Exception {
		try {
			OracleColumn oraColumn = new OracleColumn();
			oraColumn.setDaoFieldName(pDaoProperty.getPropertyName());
			oraColumn.setOracleFieldName(pDaoProperty.getOracleFieldName());
			oraColumn.setPropertyClass(pDaoProperty.getPropertyClass());
			if (pDaoProperty.getPropertyGetter() != null) {
				Object tmp = pDaoProperty.getPropertyGetter().invoke(pDao, (Object[]) null);
				
				if (tmp instanceof String) {
					String value = (String) tmp;
					if(pDaoProperty.getPropertyClass().equals(OraDateTimestamp.class)){
						if(!StringUtils.contains(value, ".")){
							StringBuilder sb = new StringBuilder().append(
							StringUtils.substring(value, 0, 14)).append(".").append(
							StringUtils.substring(value, 14, StringUtils.length(value)));
							value = sb.toString();
						}
					}
					oraColumn.setColumnValue(value);
					
				} else if (tmp instanceof byte[]) {
					oraColumn.setColumnBlobValue((byte[]) tmp);
				}
			}
			if (pDaoProperty.getPropertyClass().equals(String.class)
					|| pDaoProperty.getPropertyClass().equals(OraNumber.class)
					|| pDaoProperty.getPropertyClass().equals(OraNumberInsertValueFromSelect.class)
					|| pDaoProperty.getPropertyClass().equals(OraBLOB.class)
					|| pDaoProperty.getPropertyClass().equals(OraCLOB.class)) {
				// E0002506 - Springboard SLV Enhancement (2015)
				oraColumn.setOracleSelectClause(this.getSQLSelectClause(pDaoProperty));
				oraColumn.setOracleInsUpdValueClause(this.generateSQLInsertUpdateClause(pDaoProperty));
//				oraColumn.setOracleSelectClause(pDaoProperty.getOracleSelectClause());
//				oraColumn.setOracleInsUpdValueClause( pDaoProperty.getOracleInsertUpdateClause());
			} else if (pDaoProperty.getPropertyOracleGetter() != null) {
				Object tmp = pDaoProperty.getPropertyOracleGetter().invoke(pDao, (Object[]) null);
				if (tmp instanceof OraDate) {
					OraDate oraDate = (OraDate) tmp;
					oraColumn.setOracleSelectClause(this.getSQLSelectClause(oraDate, pDaoProperty));
					oraColumn.setOracleInsUpdValueClause(this.generateSQLInsertUpdateClause(oraDate));
//					oraColumn.setOracleSelectClause(pDaoProperty.getOracleSelectClause(oraDate));
//					oraColumn.setOracleInsUpdValueClause(pDaoProperty.getOracleInsertUpdateClause(oraDate));
				}
			}
			return oraColumn;
		} catch (IllegalArgumentException e) {
			logger.error("getOracleColumnList() - " + pDao.getClass().getName() + " - " + pDaoProperty == null ? "NULL"
					: pDaoProperty.getPropertyName() + " - IllegalArgumentException: ", e);
			logger.error("getOracleColumnList() - " + pDao.getClass().getName() + " - " + pDaoProperty == null ? "NULL"
					: pDaoProperty.getPropertyName() + " - IllegalArgumentException: "
							+ ExceptionUtils.getFullStackTrace(e));
			throw e;
		} catch (IllegalAccessException e) {
			logger.error("getOracleColumnList() - " + pDao.getClass().getName() + " - " + pDaoProperty == null ? "NULL"
					: pDaoProperty.getPropertyName() + " - IllegalAccessException: ", e);
			logger.error("getOracleColumnList() - " + pDao.getClass().getName() + " - " + pDaoProperty == null ? "NULL"
					: pDaoProperty.getPropertyName() + " - IllegalAccessException: "
							+ ExceptionUtils.getFullStackTrace(e));
			throw e;
		} catch (InvocationTargetException e) {
			logger.error("getOracleColumnList() - " + pDao.getClass().getName() + " - " + pDaoProperty == null ? "NULL"
					: pDaoProperty.getPropertyName() + " - InvocationTargetException: ", e);
			logger.error("getOracleColumnList() - " + pDao.getClass().getName() + " - " + pDaoProperty == null ? "NULL"
					: pDaoProperty.getPropertyName() + " - InvocationTargetException: "
							+ ExceptionUtils.getFullStackTrace(e));
			throw e;
		}

	}

	public String getPrimaryKeyColumn(DaoBaseImpl pDao) throws Exception {
		if (ArrayUtils.isEmpty(pDao.getPrimaryKeyFields())) {
			return null;
		}
		
		StringBuffer sbMySqlOraRowscn = new StringBuffer("JSON_OBJECT(");
		DaoProperty daoProperty = null;
		OracleColumn oraColumn = null;
		Map<String, DaoProperty> daoPropertyMap = pDao.getPropertyMap();

		for (String propertyName : pDao.getPrimaryKeyFields()) {
			
			daoProperty = getDaoProperty(daoPropertyMap, propertyName);

			if (daoProperty == null) {
				logger.warn(propertyName + " NOT FOUND in " + pDao.getClass().getName());
				continue;
			}

			oraColumn = getOracleColumn(pDao, daoProperty);

			sbMySqlOraRowscn.append("'");
			sbMySqlOraRowscn.append(oraColumn.getOracleFieldName());
			sbMySqlOraRowscn.append("', ");
			sbMySqlOraRowscn.append(oraColumn.getOracleFieldName());
			sbMySqlOraRowscn.append(", ");
		}

		sbMySqlOraRowscn.setLength(sbMySqlOraRowscn.length() - 2);
		sbMySqlOraRowscn.append(")");
		
		return getOraRowscnColumn(sbMySqlOraRowscn.toString());
	}
	
	@Override
	public String getOraRowscnColumn(DaoBaseImpl pDao) throws Exception {
		return getOraRowscnColumn(this.getColumnsToSelect(pDao, null).get(MYSQL_ORA_ROWSCN_CLAUSE));
	}
	
	protected String getOraRowscnColumn(String pOraColumns) throws Exception {
		StringBuilder sb = new StringBuilder("MD5(");
		sb.append(pOraColumns);
		sb.append(")");
		return sb.toString() ;
	}
	
	public String getDefaultSchema() {
		return defaultSchema;
	}
	public void setDefaultSchema(String defaultSchema) {
		this.defaultSchema = defaultSchema;
	};
}
