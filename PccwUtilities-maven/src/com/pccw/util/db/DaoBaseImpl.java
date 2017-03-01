package com.pccw.util.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import com.pccw.util.db.DaoHelper.DaoProperty;
import com.pccw.util.db.stringOracleType.OraNumber;

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
 * <b>Module Name: bom.util.db.DAOBase </b>
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
 * <td>Oct 24, 2005</td>
 * <td>Raymond Wong KH</td>
 * <td>Create</td>
 * </tr>
 * </table> </blockquote>
 */

public class DaoBaseImpl extends SimpleJdbcDaoSupport implements Serializable, DaoBase {
	static final long serialVersionUID = 6127468382694389356L;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DaoBaseImpl.class);

	private Map<String, DaoProperty> propertyMap = null;

	private String tableName = null;

	protected String[] primaryKeyFields = null;

	private String oracleHints;

	private String oracleRowID;

	private String errCode;

	private String errMsg;
	
	private boolean selectMarkDel = false;

	private TreeSet<String> excludeColumnSet = new TreeSet<String>();

	private TreeSet<String> includeColumnSet = new TreeSet<String>();

	private ArrayList<String> searchKeyList = new ArrayList<String>();

	private ArrayList<String> searchKeyInList = new ArrayList<String>();
	
	private TreeMap<String, String> bindingValuesMap = new TreeMap<String, String>();	
	
	private String additionWhere;
	
	private String primaryRowId;
	
	private boolean distinctResult = false;

	private String clobFieldName;
	
	private String blobFieldName;
	// E0002506 - Springboard SLV Enhancement (2015)
	
	private int topRows;

	private int tableNameCase;
	
	private boolean tableNameCaseProcessed = false;
	
	public int getTableNameCase() {
		return tableNameCase;
	}

	public void setTableNameCase(int tableNameCase) {
		this.tableNameCase = tableNameCase;
	}

	public int getTopRows() {
		return topRows;
	}

	public void setTopRows(int topRows) {
		this.topRows = topRows;
	}

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
	public DaoHelper getDaoHelper() {
		if (this.daoHelper == null) {
			this.daoHelper = getDaoHelperResolver().resolveHelper(this.getDataSource());
		}
		if (this.daoHelper == null) {
			return new DaoOracleHelper();
		}

		return this.daoHelper;
	}

	public void setDaoHelper(DaoHelper daoHelper) {
		this.daoHelper = daoHelper;
	}
	// Add an instance of daoHelper to enable injection of different implementation

	/**
	 * @return Returns the propertyMap.
	 */
	public Map<String, DaoProperty> getPropertyMap() {
		if (this.propertyMap == null) {
			this.propertyMap = DaoHelper.getPropertyMap(this.daoHelper.getDBType(), this.getClass());
			this.clobFieldName = this.getDaoHelper().getClobFieldName(this.propertyMap);
			this.blobFieldName = this.getDaoHelper().getBlobFieldName(this.propertyMap);
		}
		return this.propertyMap;
	}

	/* (non-Javadoc)
	 * @see com.pccw.util.db.DaoBase#getTableName()
	 */
	@Override
	public String getTableName() {
		if (this.tableName == null) {// if null, use classname to generate tablename
			String tmpTableName = this.getClass().getName();
			tmpTableName = tmpTableName.substring(tmpTableName.lastIndexOf(".") + 1);
			if (tmpTableName.indexOf("DAO") != -1) {
				tmpTableName = StringUtils.left(tmpTableName, tmpTableName.indexOf("DAO"));
			}
			this.tableName = DaoHelper.hungarianToOracleName(tmpTableName);
		}
		/*if(!this.tableNameCaseProcessed){
			if(this.tableNameCase == TABLE_NAME_LOWER_CASE){
				this.tableName = this.tableName.toLowerCase();
			}else if(this.tableNameCase == TABLE_NAME_UPPER_CASE){
				this.tableName = this.tableName.toUpperCase();
			}
			this.tableNameCaseProcessed = true;
		}*/
		return this.tableName;
	}

	public String getPrimaryKeyString() {
		StringBuffer sb = new StringBuffer();
		if (this.getPrimaryKeyFields() != null && this.getPrimaryKeyFields().length > 0) {
            Map<String, DaoProperty> daoPropertyMap = this.getPropertyMap();
            for (int i = 0; i < this.getPrimaryKeyFields().length; i++) {
                String propertyName = this.getPrimaryKeyFields()[i];
                DaoProperty daoProperty = (DaoProperty) daoPropertyMap.get(propertyName);

                if (daoProperty == null) {
                    logger.warn(propertyName + "NOT FOUND in " + this.getClass().getName());
                    continue;
                }
                if (i > 0) {
                	sb.append("^");
                }
                sb.append(daoProperty.getPropertyValue(this));
            }
		}
        return sb.toString();
	}
	
	public ArrayList<String> getPrimaryKeyList() {
		ArrayList<String> selectFieldList = new ArrayList<String>(this.primaryKeyFields.length);
		if (!ArrayUtils.isEmpty(this.primaryKeyFields)) {
			selectFieldList.addAll(Arrays.asList(this.primaryKeyFields));
		}
		return selectFieldList;
	}

	/* (non-Javadoc)
	 * @see com.pccw.util.db.DaoBase#doSelect()
	 */
	@Override
	public boolean doSelect() throws Exception {
		return doSelect(false);
	}


	/* (non-Javadoc)
	 * @see com.pccw.util.db.DaoBase#doSelect(java.util.ArrayList, boolean)
	 */
	@Override
	public DaoBase[] doSelect(ArrayList<String> pConditionFieldList, boolean pPrimayKeyOnly) throws Exception  {
		return doSelect(pConditionFieldList, (pPrimayKeyOnly && this.primaryKeyFields != null) ? getPrimaryKeyList(): null);
	}

	/* (non-Javadoc)
	 * @see com.pccw.util.db.DaoBase#doSelect(java.util.ArrayList, java.util.ArrayList)
	 */
	@Override
	public DaoBase[] doSelect(ArrayList<String> pConditionFieldList, ArrayList<String> pSelectFieldList) throws Exception {
		return doSelect(pConditionFieldList, pSelectFieldList, null);
	}

	/* (non-Javadoc)
	 * @see com.pccw.util.db.DaoBase#doSelect(java.util.ArrayList, java.util.ArrayList, java.lang.String, java.lang.String)
	 */
	@Override
	public DaoBase[] doSelect(ArrayList<String> pConditionFieldList,
			ArrayList<String> pSelectFieldList, String pAdditionWhere,
			String pOrderBy) throws Exception {
		return this.getDaoHelper().daoDoSelect(this, pSelectFieldList, pConditionFieldList, pAdditionWhere, pOrderBy);
	}
	
	/* (non-Javadoc)
	 * @see com.pccw.util.db.DaoBase#doSelect(boolean)
	 */
	@Override
	public boolean doSelect(boolean pPrimayKeyOnly) throws Exception  {
		try {
			return this.getDaoHelper().daoDoSelect(this, (pPrimayKeyOnly && this.primaryKeyFields != null) ? getPrimaryKeyList(): null);
		} catch (EmptyResultDataAccessException ignore) {
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see com.pccw.util.db.DaoBase#doSelect(java.util.ArrayList)
	 */
	@Override
	public boolean doSelect(ArrayList<String> pSelectFieldList) throws Exception  {
		return this.getDaoHelper().daoDoSelect(this, pSelectFieldList);
	}

	/* (non-Javadoc)
	 * @see com.pccw.util.db.DaoBase#doSelect(java.util.ArrayList, java.util.ArrayList, java.lang.String)
	 */
	@Override
	public DaoBase[] doSelect(ArrayList<String> pConditionFieldList,
			ArrayList<String> pSelectFieldList, String pAdditionWhere)
			throws Exception {
		return this.getDaoHelper().daoDoSelect(this, pSelectFieldList, pConditionFieldList, pAdditionWhere);
	}

	/* (non-Javadoc)
	 * @see com.pccw.util.db.DaoBase#doSelect(java.util.ArrayList, boolean, java.lang.String)
	 */
	@Override
	public DaoBase[] doSelect(ArrayList<String> pConditionFieldList, boolean pPrimayKeyOnly,
			String pAdditionWhere) throws Exception {
		return this.getDaoHelper().daoDoSelect(this, (pPrimayKeyOnly && this.primaryKeyFields != null) ? getPrimaryKeyList(): null, 
				pConditionFieldList, pAdditionWhere);
	}
	
	/* (non-Javadoc)
	 * @see com.pccw.util.db.DaoBase#doInsert()
	 */
	@Override
	public boolean doInsert() throws Exception {
		return this.getDaoHelper().daoDoInsert(this);

	}
	
	public boolean doInsert(boolean pIgnoringNull) throws Exception {
		return this.getDaoHelper().daoDoInsert(this, pIgnoringNull);

	}

	/* (non-Javadoc)
	 * @see com.pccw.util.db.DaoBase#doUpdate()
	 */
	@Override
	public boolean doUpdate() throws Exception  {
		return doUpdate(null);
	}

	/* (non-Javadoc)
	 * @see com.pccw.util.db.DaoBase#doUpdate(java.util.ArrayList)
	 */
	@Override
	public boolean doUpdate(ArrayList<String> pConditionFieldList) throws Exception {
		return doUpdate(pConditionFieldList, false);
	}
	public boolean doUpdate(ArrayList<String> pConditionFieldList, boolean pIgnoringNull) throws Exception {
		return this.getDaoHelper().daoDoUpdate(this, pConditionFieldList, pIgnoringNull);
	}

	/* (non-Javadoc)
	 * @see com.pccw.util.db.DaoBase#doDelete()
	 */
	@Override
	public boolean doDelete() throws Exception  {
		return doDelete(null);
	}

	/* (non-Javadoc)
	 * @see com.pccw.util.db.DaoBase#doDelete(java.util.ArrayList)
	 */
	@Override
	public boolean doDelete(ArrayList<String> pConditionFieldList) throws Exception {
		return this.getDaoHelper().daoDoDelete(this, pConditionFieldList);
	}
	
	public boolean doDeleteAll(){
		return this.getDaoHelper().daoDoDeleteAll(this);
	}
	
	/* (non-Javadoc)
	 * @see com.pccw.util.db.DaoBase#getErrCode()
	 */
	@Override
	public String getErrCode() {
		return this.errCode;
	}

	/**
	 * @param pErrCode
	 *            The errCode to set.
	 */
	public void setErrCode(String pErrCode) {
		this.errCode = pErrCode;
	}

	/* (non-Javadoc)
	 * @see com.pccw.util.db.DaoBase#getErrMsg()
	 */
	@Override
	public String getErrMsg() {
		return this.errMsg;
	}

	/**
	 * @param pErrMsg
	 *            The errMsg to set.
	 */
	public void setErrMsg(String pErrMsg) {
		this.errMsg = pErrMsg;
	}

	/* (non-Javadoc)
	 * @see com.pccw.util.db.DaoBase#getOracleRowID()
	 */
	@Override
	public String getOracleRowID() {
		return this.oracleRowID;
	}

	/**
	 * @param pOracleRowID
	 *            The oracleRowID to set.
	 */
	public void setOracleRowID(String pOracleRowID) {
		this.oracleRowID = pOracleRowID;
	}

	/**
	 * @param pTableName
	 *            The tableName to set.
	 */
	public void setTableName(String pTableName) {
		this.tableName = pTableName;
	}

	/* (non-Javadoc)
	 * @see com.pccw.util.db.DaoBase#getPrimaryKeyFields()
	 */
	@Override
	public String[] getPrimaryKeyFields() {
		return this.primaryKeyFields;
	}

	/**
	 * @param pPrimaryKeyFields
	 *            The primaryKeyFields to set.
	 */
	public void setPrimaryKeyFields(String[] pPrimaryKeyFields) {
		this.primaryKeyFields = pPrimaryKeyFields;
	}
	
	/**
	 * @return Returns the selectMarkDel.
	 */
	@Override
	public boolean isSelectMarkDel() {
		return this.selectMarkDel;
	}
	/**
	 * @param pSelectMarkDel The selectMarkDel to set.
	 */
	@Override
	public void setSelectMarkDel(boolean pSelectMarkDel) {
		this.selectMarkDel = pSelectMarkDel;
    }

    protected static OraNumber string2OraNumber(String pValue) {
        return new OraNumber(pValue);
    }

    protected static String oraNumber2String(OraNumber pOraNumber) {
        return pOraNumber == null ? null : pOraNumber.toString();
    }
    
	@Override
	public String getOracleHints() {
		return oracleHints;
	}

	@Override
	public void setOracleHints(String pOracleHints) {
		this.oracleHints = pOracleHints;
	}
	
	@Override
	public void clearIncludeColumn() {
		this.includeColumnSet.clear();		
	}

	public void clearExcludeColumn() {
		this.excludeColumnSet.clear();
	}
	
	@Override
	public void addExcludeColumn(String pColumn) {
		this.excludeColumnSet.add(pColumn);
	}
	
	@Override
	public void addExcludeColumn(String [] pColumn) {
		for (String sColumn: pColumn) {
		    this.excludeColumnSet.add(sColumn);
		}
	}	
	
	@Override
	public void removeExcludeColumn(String pColumn) {
		this.excludeColumnSet.remove(pColumn);
	}

	@Override
	public boolean isExcludeColumn(String pColumn) {
		return this.excludeColumnSet.contains(pColumn);
	}
	
	@Override
	public void addIncludeColumn(String pColumn) {
		this.includeColumnSet.add(pColumn);
	}
	
	@Override
	public void addIncludeColumn(String [] pColumn) {
		for (String sColumn: pColumn) {
		    this.includeColumnSet.add(sColumn);
		}
	}		
	
	@Override
	public void removeIncludeColumn(String pColumn) {
		this.includeColumnSet.remove(pColumn);
	}

	@Override
	public boolean isIncludeColumn(String pColumn) {
		if (this.includeColumnSet.size() == 0) {
			return true;
		}
		return this.includeColumnSet.contains(pColumn);
	}
	
	@Override
	public ArrayList<String> getSearchKeyList() {
		return searchKeyList;
	}
	
	@Override
	public void setSearchKey(String pFieldName, String pValue) throws Exception {
		this.searchKeyList.add(pFieldName);
		BeanUtils.setProperty(this, pFieldName, pValue);
		this.bindingValuesMap.put(pFieldName, pValue);
	}

	@Override
	public ArrayList<String> getSearchKeyInList() {
		return searchKeyInList;
	}
	
	@Override
	public void setSearchKeyIn(String pFieldName, String [] pValues) throws Exception {
		StringBuilder keyInWhereSb = new StringBuilder();
		keyInWhereSb.append(DaoHelper.hungarianToOracleName(pFieldName));
		keyInWhereSb.append(" IN (");		
		for (int i = 0; i < pValues.length;i++) {
	       if (i>0) keyInWhereSb.append(",");
	       keyInWhereSb.append(":");
	       keyInWhereSb.append(pFieldName);
	       keyInWhereSb.append(i);
	       keyInWhereSb.append(" ");
		   this.setExtraBind(pFieldName+i,pValues[i]);
        }   		
		keyInWhereSb.append(")");
		searchKeyInList.add(keyInWhereSb.toString());
	}
	
	@Override
	public void setExtraBind(String pBindingName, String pBindingValue) {
		this.bindingValuesMap.put(pBindingName, pBindingValue);
	}	
	
	@Override
	public String getBindingValue(String pBindingName) throws Exception {
		String rtnValue = (String) this.bindingValuesMap.get(pBindingName);
		if (rtnValue != null) {
			return rtnValue;
		}
		if (this.bindingValuesMap.containsKey(pBindingName)) {
			return null;
		}
		throw new Exception("Binding Value NOT FOUND");
	}
	
	@Override
	public String getAdditionWhere() {
		return additionWhere;
	}

	@Override
	public void setAdditionWhere(String pAdditionWhere) {
		this.additionWhere = pAdditionWhere;
	}

	@Override
	public boolean isDistinctResult() {
		return distinctResult;
	}

	@Override
	public void setDistinctResult(boolean pDistinctResult) {
		this.distinctResult = pDistinctResult;
	}

	@Override
	public String getPrimaryRowId() {
		return primaryRowId;
	}

	@Override
	public void setPrimaryRowId(String pPrimaryRowId) {
		this.primaryRowId = pPrimaryRowId;
	}

	@Override
	public boolean isClobFieldExists() {
		if (this.propertyMap == null) {
			this.getPropertyMap();
		}
		return StringUtils.isNotBlank(this.clobFieldName);
	}

	@Override
	public String getClobFieldName() {
		return this.clobFieldName;
	}
	
	@Override
	public boolean isBlobFieldExists() {
		if (this.propertyMap == null) {
			this.getPropertyMap();
		}
		return StringUtils.isNotBlank(this.blobFieldName);
	}

	@Override
	public String getBlobFieldName() {
		return this.blobFieldName;
	}
	
	public JdbcTemplate getStreamingResultSetEnabledJdbcTemplate() {
		final JdbcTemplate jdbcTemplate = new StreamingResultSetEnabledJdbcTemplate(getDataSource());
		jdbcTemplate.setFetchSize(Integer.MIN_VALUE);
		return jdbcTemplate;
	}

}