package com.pccw.util.db;

import java.util.ArrayList;

public interface DaoBase {

	/**
	 * @return Returns the tableName.
	 */
	public String getTableName();

	public boolean doSelect() throws Exception;

	public DaoBase[] doSelect(ArrayList<String> pConditionFieldList,
			boolean pPrimayKeyOnly) throws Exception;

	public DaoBase[] doSelect(ArrayList<String> pConditionFieldList,
			ArrayList<String> pSelectFieldList) throws Exception;

	public DaoBase[] doSelect(ArrayList<String> pConditionFieldList,
			ArrayList<String> pSelectFieldList, String pAdditionWhere,
			String pOrderBy) throws Exception;

	public boolean doSelect(boolean pPrimayKeyOnly) throws Exception;

	public boolean doSelect(ArrayList<String> pSelectFieldList)
			throws Exception;

	public DaoBase[] doSelect(ArrayList<String> pConditionFieldList,
			ArrayList<String> pSelectFieldList, String pAdditionWhere)
			throws Exception;

	public DaoBase[] doSelect(ArrayList<String> pConditionFieldList,
			boolean pPrimayKeyOnly, String pAdditionWhere) throws Exception;

	public boolean doInsert() throws Exception;

	public boolean doUpdate() throws Exception;

	public boolean doUpdate(ArrayList<String> pConditionFieldList)
			throws Exception;

	public boolean doDelete() throws Exception;

	public boolean doDelete(ArrayList<String> pConditionFieldList)
			throws Exception;

	/**
	 * @return Returns the errCode.
	 */
	public String getErrCode();

	/**
	 * @return Returns the errMsg.
	 */
	public String getErrMsg();

	/**
	 * @return Returns the oracleRowID.
	 */
	public String getOracleRowID();

	/**
	 * @return Returns the primaryKeyFields.
	 */
	public String[] getPrimaryKeyFields();

	/**
	 * 
	 * @param pSelectMarkDel
	 */
	public void setSelectMarkDel(boolean pSelectMarkDel);
	
	/**
	 * 
	 * @return
	 */
	public boolean isSelectMarkDel();
	
	public String getOracleHints();
	
	public void setOracleHints(String oracleHints);
	
	public void addExcludeColumn(String pColumn);

	public void addExcludeColumn(String [] pColumn);
		
	public void removeExcludeColumn(String pColumn);
	
	public boolean isExcludeColumn(String pColumn);
	
	public void addIncludeColumn(String pColumn);
	
	public void addIncludeColumn(String [] pColumn);	
	
	public void removeIncludeColumn(String pColumn);
	
	public void clearIncludeColumn();

	public void clearExcludeColumn();
		
	public boolean isIncludeColumn(String pColumn);
	
	public ArrayList<String> getSearchKeyList();
	
	public ArrayList<String> getSearchKeyInList();

	public void setSearchKey(String pFieldName, String pValue) throws Exception;
	
	public void setSearchKeyIn(String pFieldName, String [] pValues) throws Exception;
		
	public void setExtraBind(String pBindingName, String pBindingValue);
	
	public String getBindingValue(String pBindingName) throws Exception;
	
	public String getAdditionWhere();
	
	public void setAdditionWhere(String pAdditionWhere);
	
	public boolean isDistinctResult();
	
	public void setDistinctResult(boolean pDistinctResult);
	
	public String getPrimaryRowId();
	
	public void setPrimaryRowId(String pPrimaryRowId);
	
	public boolean isClobFieldExists();

	public String getClobFieldName();
	
	public boolean isBlobFieldExists();

	public String getBlobFieldName();
	
	public static int TABLE_NAME_LOWER_CASE = 0;
	public static int TABLE_NAME_UPPER_CASE = 1;
	public static int TABLE_NAME_CAMEL_CASE = 2;
	
}