package com.pccw.util.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.pccw.util.db.DaoBaseImpl;
import com.pccw.util.db.DaoHelper;
import com.pccw.util.db.OracleSelectHelper;

public class ParameterizedRowMapperByFieldName<T> implements ParameterizedRowMapper<T> {

	private static TreeMap<String, ArrayList<String>> dtoPropertyMap = new TreeMap<String, ArrayList<String>>();

	private static final Logger logger = Logger.getLogger(ParameterizedRowMapperByFieldName.class);

	private Class<T> elementClass;
	// E0002506 - Springboard SLV Enhancement (2015)
	// dbType is the name of the database the system runs on.
	// private String dbType;

	public ParameterizedRowMapperByFieldName(Class<T> pClass) {
		this.elementClass = pClass;
		// this.dbType = DaoHelperResolver.ORACLE;
	}

	// public ParameterizedRowMapperByFieldName (Class<T> pClass, String
	// pDBType) {
	// this.elementClass = pClass;
	// this.dbType = pDBType;
	// }

	@Override
	public T mapRow(ResultSet pRs, int pRowNum) throws SQLException {
		T rowObj = null;

		boolean isDao = false;

		try {
			isDao = (this.elementClass.newInstance() instanceof DaoBaseImpl);
		} catch (Exception ignore) {
		}

		ArrayList<String> dtoPropertyMappingList = null;

		if (isDao) {
			dtoPropertyMappingList = dtoPropertyMap.get(this.elementClass.toString());
		}

		if (dtoPropertyMappingList == null) {
			dtoPropertyMappingList = OracleSelectHelper.getColumnPropertyMappingList(pRs.getMetaData(), this.elementClass);
			if (isDao) {
				dtoPropertyMap.put(this.elementClass.toString(), dtoPropertyMappingList);
			}
		}

		try {
			rowObj = DaoHelper.newDaoInstance(this.elementClass);
			OracleSelectHelper.getResultSetRowObject(dtoPropertyMappingList, pRs, pRs.getMetaData(), rowObj);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			if (e instanceof SQLException) {
				throw (SQLException) e;
			}
			throw new SQLException(e);
		}

		return rowObj;
	}
}
