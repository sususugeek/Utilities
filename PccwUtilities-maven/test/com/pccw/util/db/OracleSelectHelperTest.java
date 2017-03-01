package com.pccw.util.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pccw.util.db.stringOracleType.OraArrayDate;
import com.pccw.util.db.stringOracleType.OraArrayVarChar2;
import com.pccw.util.db.stringOracleType.OraDate;
import com.pccw.util.db.stringOracleType.OraNumber;
import com.pccw.util.spring.ApplicationContextProvider;
import com.pccw.util.spring.SpringApplicationContext;

import org.junit.Assert;

public class OracleSelectHelperTest {
	private static final Logger logger = LoggerFactory.getLogger(OracleSelectHelperTest.class);
	OracleSelectHelper oracleSelectHelper = null;
	
	@Before
	public void setUp() throws Exception {
		ClassPathXmlApplicationContext jdbcCtx = new ClassPathXmlApplicationContext("ApplicationContextMySQLUnitTest.xml");
		ApplicationContextProvider applicationContextProvider = new ApplicationContextProvider();
		applicationContextProvider.setApplicationContext(jdbcCtx);
		
		this.oracleSelectHelper = SpringApplicationContext.getBean("OracleSelectHelper-BomWebPortalMySQLDS");
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
	public void testMemberOf() throws Exception {
//		OracleSelectHelper oracleSelectHelper = new OracleSelectHelper();
		
		String pSql = " gWER MEMBER   OF ? and Mem = 'AA' and AWD MembeR   Of  ? ";
		List<Object> pObj = new ArrayList<Object>();

		String[] values = {"A","BBB","ACSW","RW","HTRER","MEMBER OF ?"};
		OraArrayVarChar2 oraArray = new OraArrayVarChar2("OPS$BOM.STRING_ARRAY", values);
		pObj.add(oraArray);

		OraDate[] values2 = {new OraDate("20160703"),new OraDate("20160803"),new OraDate("20160903")};
		OraArrayDate oraDateAry = new OraArrayDate("OPS$BOM.STRING_ARRAY", values2);
		pObj.add(oraDateAry);
		
//		String result = oracleSelectHelper.convertMemberOfToIn(pSql, pObj);
		String result = oracleSelectHelper.convertSql(pSql, pObj);
		logger.debug(result);
		System.out.println("Result:\""+result+"\"");
	}
	
	@Test
	public void testFullSQL() throws Exception {
//		OracleSelectHelper oracleSelectHelper = new OracleSelectHelper();
		
		String pSql = "SELECT 'MEMBER OF ?', TEST, MEMBER from dual a where a.y=? and a.z=? and a.X MEMBER OF ? and a.b='MEMBER OF' and a.c = "
				+ " (select distinct name from test_t1 where name member of ? ) ";
		Object[] bindingValues = new Object[4];
		List<Object> pObj = new ArrayList<Object>();
		OraDate oraDate = new OraDate("20160822");
		OraNumber oraNumber = new OraNumber("2016");
		String[] values = {"A","BBB","ACSW","RW","HTRER","MEMBER OF ?"};
		OraArrayVarChar2 oraArray = new OraArrayVarChar2("OPS$BOM.STRING_ARRAY", values);
		OraDate[] values2 = {new OraDate("20160703"), new OraDate("20160803"), new OraDate("20160903")};
		OraDate[] values3 = {};
		
		OraArrayDate oraDateAry = new OraArrayDate("OPS$BOM.STRING_ARRAY", values2);
		
		bindingValues[0] = oraDate;
		bindingValues[1] = oraNumber;
		bindingValues[2] = oraArray;
		bindingValues[3] = oraDateAry;
		
		pObj.addAll(Arrays.asList(bindingValues));
		
//		String result = oracleSelectHelper.convertMemberOfToIn(pSql, pObj);
		String result = oracleSelectHelper.convertSql(pSql, pObj);
		logger.debug(result);
		System.out.println(result);
		System.out.println(pObj);
		
//		List<Object> bindingValueList = new ArrayList<Object>();
//		bindingValueList.add(this.bindingValues);
//		this.sqlStatement = convertMemberOfToIn(this.sqlStatement, bindingValueList);
//		this.bindingValues = bindingValueList.toArray();
	}
//	@Test
	public void testWorkingSql() throws Exception{
//		OracleSelectHelper oracleSelectHelper = new OracleSelectHelper();
		String sqlStr = "select distinct order_id from SLV_ORDER_ITEM_ENQUIRY_V where profile_id member of ?";
		
		Object[] bindingValues = new Object[1];
		List<Object> pObj = new ArrayList<Object>();

		String[] values = {"A","BBB","ACSW","RW","HTRER","MEMBER OF ?"};
		OraArrayVarChar2 oraArray = new OraArrayVarChar2("OPS$BOM.STRING_ARRAY", values);

		bindingValues[0] = oraArray;
				
		pObj.addAll(Arrays.asList(bindingValues));
		
//		String result = oracleSelectHelper.convertMemberOfToIn(sqlStr, pObj);
		String result = oracleSelectHelper.convertSql(sqlStr, pObj);
		logger.debug(result);
		System.out.println(result);
		System.out.println(pObj);
		
		
	}
	
//	@Test
	public void testMemberOfSql() throws Exception{
		ClassPathXmlApplicationContext jdbcCtx = new ClassPathXmlApplicationContext("ApplicationContextMySQLUnitTest.xml");
		ApplicationContextProvider applicationContextProvider = new ApplicationContextProvider();
		applicationContextProvider.setApplicationContext(jdbcCtx);
		
		OracleSelectHelper oracleSelectHelper = SpringApplicationContext.getBean("OracleSelectHelper-BomWebPortalMySQLDS");
		
//		OracleSelectHelper oracleSelectHelper = new OracleSelectHelper();
		String sqlStr = SQLForTest.getSqlForTest()[23][0];
		Object[] bindingValues = new Object[1];
		List<Object> pObj = new ArrayList<Object>();

		String[] values = {"A","BBB","ACSW","RW","HTRER","MEMBER OF ?"};
		OraArrayVarChar2 oraArray = new OraArrayVarChar2("OPS$BOM.STRING_ARRAY", values);

		bindingValues[0] = oraArray;
				
//		pObj.addAll(Arrays.asList(bindingValues));
		pObj.add("A");
		pObj.add("B");
		pObj.add("C");
//		String result = oracleSelectHelper.convertMemberOfToIn(sqlStr, pObj);
		String result = oracleSelectHelper.convertSql(sqlStr, pObj);
		logger.debug(result);
		System.out.println(result);
		System.out.println(pObj);
		Assert.assertEquals(SQLForTest.getSqlForTest()[23][1], result);
		
	}
}
