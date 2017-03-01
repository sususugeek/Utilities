package com.pccw.util.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.pccw.util.db.stringOracleType.OraDate;
import com.pccw.util.db.stringOracleType.OraDateTimestamp;
import com.pccw.util.db.stringOracleType.OraNumber;

/**
 * 
 * For project E0002506 - Springboard SLV Enhancement (2015)
 * 
 * Oracle implementation of DaoHelper.
 * Actually just use all method of parent class as original implementation is for Oracle.
 * 
 * @author 01517028
 *
 */
public class DaoOracleHelper extends DaoHelper {
	private final static Logger logger = Logger.getLogger(DaoOracleHelper.class);
	
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
		this.addExtraBinds(bindOracleColumnList, pDao, pDao.getTableName(), sbSql);
		if (oraWhereColumnList != null && oraWhereColumnList.size() > 0) {
			bindOracleColumnList.addAll(oraWhereColumnList);
		}
		this.addExtraBinds(bindOracleColumnList, pDao, additionWhere, sbSql);

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
				primaryRowId = "ROWID";
			}
			sbRtnSql.append(" ROWIDTOCHAR(" + primaryRowId + ") \"oracleRowID\"");
		} else {
			sbRtnSql.append(" NULL \"oracleRowID\"");
		}

		if (this.isFieldExistInDao(pDao, DaoHelper.DAO_PROPERTY_ORA_ROWSCN)) {
			sbRtnSql.append(", ");
			sbRtnSql.append(this.getOraRowscnColumn(pDao));
			sbRtnSql.append(" \"oraRowscn\"");
		}
		
		String columnsToRetrieve = (String) getColumnsToSelect(pDao, pColumnsToRetrieve).get(ORACLE_SELECT_CLAUSE);
		if (StringUtils.isNotBlank(columnsToRetrieve)) {
			sbRtnSql.append(", ");
			sbRtnSql.append(columnsToRetrieve);
		}

		sbRtnSql.append(" FROM ");
		sbRtnSql.append(getTableName(pDao));

		String statementName = pDao.getClass().getName() + ".SELECT";
		return insertPackageNameToSql(statementName, sbRtnSql.toString(), DaoHelperResolverConstant.ORACLE);
	}
	
	/**
	 * @param pOraDate
	 *            The OraDate instance to get the format to set.
	 * @return Returns the oracleSelectClause.
	 */
	public String getSQLInsertUpdateClause(OraDate pOraDate) {
		if (pOraDate.getUpdateUsing() == OraDate.UPD_BY_SYS_DATE) {
			if (pOraDate instanceof OraDateTimestamp) {
				return "SYSTIMESTAMP";
			} else {
				return "SYSDATE";
			}
		}
		StringBuffer sb = new StringBuffer();
		if (pOraDate instanceof OraDateTimestamp) {
			sb.append("TO_TIMESTAMP(?, '");
		} else {
			sb.append("TO_DATE(?, '");
		}
		sb.append(pOraDate.getOracleDateFormat());
		sb.append("')");
		return sb.toString();
	}
	
	@Override
	public String generateSQLSelectClause(DaoProperty pDaoProperty) {
		if (DaoHelper.DAO_PROPERTY_ORACLE_ROW_ID.equals(pDaoProperty.getPropertyName())) {
			return "ROWIDTOCHAR(ROWID) \"oracleRowID\"";
		} else if (pDaoProperty.getPropertyClass().getName().indexOf("OraNumber") != -1) {
			return "TO_CHAR(" + pDaoProperty.getOracleFieldName() + ") \"" + pDaoProperty.getPropertyName() + "\"";
		}
		return pDaoProperty.getOracleFieldName() + " \"" + pDaoProperty.getPropertyName() + "\"";
	}
	
	public String generateSQLInsertUpdateClause(DaoProperty pDaoProperty) {
		if (DaoHelper.DAO_PROPERTY_ORACLE_ROW_ID.equals(pDaoProperty.getPropertyName())) {
			return "CHARTOROWID(?)";
		} else if (pDaoProperty.getPropertyClass().getName().indexOf("OraNumber") != -1) {
			return generateSQLInsertUpdateClause((OraNumber) null);
		}
		return "?";
	}

	@Override
	public String generateSQLInsertUpdateClause(OraNumber pOraNumber) {
		return "TO_NUMBER(?)";
	}

	
	public String getSQLSelectClause(OraDate pOraDate, DaoProperty pDaoProperty) {
		StringBuffer sb = new StringBuffer();
		sb.append("TO_CHAR(");
		sb.append(pDaoProperty.getOracleFieldName());
		sb.append(", '");
		sb.append(pOraDate.getOracleDateFormat());
		sb.append("')");
		sb.append(" \"");
		sb.append(pDaoProperty.getPropertyName());
		sb.append("\"");
		return sb.toString();
	}
	
	public String getWhereClause(DaoBaseImpl pDao, ArrayList<OracleColumn> pOraWhereColumnList, String pAdditionWhere) {
		String sqlStr = super.getWhereClause(pDao, pOraWhereColumnList, pAdditionWhere);
		StringBuilder sb = new StringBuilder();
		sb.append(sqlStr);
		if(pDao.getTopRows() > 0){
			if(sqlStr.length()==0 ){
				sb.append(" WHERE ROWNUM <= ").append(pDao.getTopRows());;
			}else{
				sb.append(" AND ROWNUM <= ").append(pDao.getTopRows());
			}
		}
		return sb.toString();
	}

	@Override
	public String convertDateFormat(String pDateFormatStr, String pOriginalDBStr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String topRowsClause(int pNumOfTop) {
		StringBuilder ret = new StringBuilder("");
		if(pNumOfTop > 0){
			ret.append("ROWNUM <=").append(pNumOfTop);
		}
		return ret.toString();
	}

	@Override
	public String getDBType() {
		return DaoHelperResolver.ORACLE;
	}

	@Override
	public String generateSQLInsertUpdateClause(OraDate pOraDate) {
		if (pOraDate.getUpdateUsing() == OraDate.UPD_BY_SYS_DATE) {
			if (pOraDate instanceof OraDateTimestamp) {
			  return "SYSTIMESTAMP";	
			} else {
			  return "SYSDATE";
			}
		}
		StringBuffer sb = new StringBuffer();
		if (pOraDate instanceof OraDateTimestamp) { 
		  sb.append("TO_TIMESTAMP(?, '");
		} else {
		  sb.append("TO_DATE(?, '");
		}
		sb.append(pOraDate.getOracleDateFormat());
		sb.append("')");
		return sb.toString();
	}

	@Override
	public DaoProperty getDaoPropertyRowID() {
		return new DaoProperty(DaoBaseImpl.class, DaoHelper.DAO_PROPERTY_ORACLE_ROW_ID, "ROWID", String.class);
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
		} else if (!StringUtils.isEmpty(pDao.getOracleRowID())) {
			OracleColumn oraColumn = new OracleColumn();
			oraColumn.setDaoFieldName(DaoHelper.DAO_PROPERTY_ORACLE_ROW_ID);
			oraColumn.setOracleFieldName("ROWID");
			oraColumn.setColumnValue(pDao.getOracleRowID());
			oraColumn.setOracleInsUpdValueClause("CHARTOROWID(?)");
			oraColumn.setOracleSelectClause("ROWIDTOCHAR(ROWID) \"oracleRowID\"");
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
	public boolean resolveFieldExistency(DaoBase pDaoBase, String pFieldName) {
		return false;
	}
	@Override
	public ArrayList<OracleColumn> getOracleColumnList(DaoBaseImpl pDao) {
		ArrayList<OracleColumn> oraColumnList = new ArrayList<OracleColumn>();
		OracleColumn oracleColumn;

		try {
			for (DaoProperty daoProperty : pDao.getPropertyMap().values()) {
				oracleColumn = getOracleColumn(pDao, daoProperty);
				if (!pDao.isIncludeColumn(oracleColumn.getOracleFieldName())
						|| pDao.isExcludeColumn(oracleColumn.getOracleFieldName())) {
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
				if (!pDao.isIncludeColumn(propertyName) || pDao.isExcludeColumn(propertyName)) {
					continue;
				}
				rtnList.add(propertyName);
			}
		}
		return rtnList;
	}

	@Override
	public String getOraRowscnColumn(DaoBaseImpl pDao) throws Exception {
		return "TO_CHAR(ORA_ROWSCN)";
	}
}
