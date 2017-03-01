package com.pccw.util.db;

import java.util.ArrayList;

public class DaoBaseReadonlyImpl extends DaoBaseImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5619408676131990918L;

	@Override
	public boolean doDelete() throws Exception {
		throw new Exception("DAO is readonly");
	}
	
	@Override
	public boolean doDelete(ArrayList<String> pConditionFieldList)
			throws Exception {
		throw new Exception("DAO is readonly");
	}
	
	@Override
	public boolean doInsert() throws Exception {
		throw new Exception("DAO is readonly");
	}
	
	@Override
	public boolean doUpdate() throws Exception {
		throw new Exception("DAO is readonly");
	}
	
	@Override
	public boolean doUpdate(ArrayList<String> pConditionFieldList)
			throws Exception {
		throw new Exception("DAO is readonly");
	}
}