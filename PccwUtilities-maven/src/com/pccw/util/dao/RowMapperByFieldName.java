package com.pccw.util.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

import org.springframework.jdbc.core.RowMapper;

import com.pccw.util.db.DaoBaseImpl;
import com.pccw.util.db.OracleSelectHelper;

public class RowMapperByFieldName implements RowMapper {

	private static TreeMap<String, ArrayList<String>> dtoPropertyMap = new TreeMap<String, ArrayList<String>>();
	
	@SuppressWarnings("rawtypes")
	private Class elementClass;
	//E0002506 - Springboard SLV Enhancement (2015)
	//dbType is the name of the database the system runs on.
	private String dbType;
	
	public RowMapperByFieldName(@SuppressWarnings("rawtypes") Class pElementClass, String pDBType) {
		this.elementClass = pElementClass;
		this.dbType = pDBType;
	}
	
	@Override
	public Object mapRow(ResultSet pRs, int pRowNum) throws SQLException {
		Object rowObj = null;
		try {
			
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
			
			rowObj = this.elementClass.newInstance();
            OracleSelectHelper.getResultSetRowObject(dtoPropertyMappingList, pRs, pRs.getMetaData(), rowObj);
		} catch (Exception e) {
			throw new SQLException(e);
		}
		
		return rowObj;
	}

}
