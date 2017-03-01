package com.pccw.util.db.mysql;

import java.util.ArrayList;

import com.pccw.util.db.DaoBase;

public class DaoBaseMySQLImpl implements DaoBase {

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean doSelect() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DaoBase[] doSelect(ArrayList<String> pConditionFieldList, boolean pPrimayKeyOnly) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DaoBase[] doSelect(ArrayList<String> pConditionFieldList, ArrayList<String> pSelectFieldList)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DaoBase[] doSelect(ArrayList<String> pConditionFieldList, ArrayList<String> pSelectFieldList,
			String pAdditionWhere, String pOrderBy) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean doSelect(boolean pPrimayKeyOnly) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doSelect(ArrayList<String> pSelectFieldList) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DaoBase[] doSelect(ArrayList<String> pConditionFieldList, ArrayList<String> pSelectFieldList,
			String pAdditionWhere) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DaoBase[] doSelect(ArrayList<String> pConditionFieldList, boolean pPrimayKeyOnly, String pAdditionWhere)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean doInsert() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doUpdate() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doUpdate(ArrayList<String> pConditionFieldList) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doDelete() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doDelete(ArrayList<String> pConditionFieldList) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getErrCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrMsg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOracleRowID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getPrimaryKeyFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSelectMarkDel(boolean pSelectMarkDel) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSelectMarkDel() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getOracleHints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOracleHints(String oracleHints) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addExcludeColumn(String pColumn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addExcludeColumn(String[] pColumn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeExcludeColumn(String pColumn) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isExcludeColumn(String pColumn) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addIncludeColumn(String pColumn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addIncludeColumn(String[] pColumn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeIncludeColumn(String pColumn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearIncludeColumn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearExcludeColumn() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isIncludeColumn(String pColumn) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<String> getSearchKeyList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getSearchKeyInList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSearchKey(String pFieldName, String pValue) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSearchKeyIn(String pFieldName, String[] pValues) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void setExtraBind(String pBindingName, String pBindingValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBindingValue(String pBindingName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAdditionWhere() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAdditionWhere(String pAdditionWhere) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDistinctResult() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDistinctResult(boolean pDistinctResult) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getPrimaryRowId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPrimaryRowId(String pPrimaryRowId) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isClobFieldExists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getClobFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isBlobFieldExists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getBlobFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

}
