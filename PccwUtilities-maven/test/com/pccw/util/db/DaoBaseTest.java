package com.pccw.util.db;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pccw.util.datatransfer.dataAccess.SbActvDAOImpl;
import com.pccw.util.spring.ApplicationContextProvider;
import com.pccw.util.spring.SpringApplicationContext;

public class DaoBaseTest {

	@Before
	public void setUp() throws Exception {
		ClassPathXmlApplicationContext jdbcCtx = new ClassPathXmlApplicationContext("ApplicationContextMySQLUnitTest.xml");
		ApplicationContextProvider applicationContextProvider = new ApplicationContextProvider();
		applicationContextProvider.setApplicationContext(jdbcCtx);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetTableName() throws Exception {
		SbActvDAOImpl sbActvDAOImpl = SpringApplicationContext.getBean("sbActvDAOImpl");
		sbActvDAOImpl.doSelect();
		
	}

	@Test
	public void testDoSelect() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoSelectArrayListOfStringBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoSelectArrayListOfStringArrayListOfString() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoSelectArrayListOfStringArrayListOfStringStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoSelectBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoSelectArrayListOfString() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoSelectArrayListOfStringArrayListOfStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoSelectArrayListOfStringBooleanString() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoInsert() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoUpdateArrayListOfString() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoDeleteArrayListOfString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetErrCode() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetErrMsg() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetOracleRowID() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPrimaryKeyFields() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetSelectMarkDel() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsSelectMarkDel() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetOracleHints() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetOracleHints() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddExcludeColumnString() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddExcludeColumnStringArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveExcludeColumn() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsExcludeColumn() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddIncludeColumnString() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddIncludeColumnStringArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveIncludeColumn() {
		fail("Not yet implemented");
	}

	@Test
	public void testClearIncludeColumn() {
		fail("Not yet implemented");
	}

	@Test
	public void testClearExcludeColumn() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsIncludeColumn() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSearchKeyList() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSearchKeyInList() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetSearchKey() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetSearchKeyIn() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetExtraBind() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBindingValue() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAdditionWhere() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetAdditionWhere() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsDistinctResult() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetDistinctResult() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPrimaryRowId() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetPrimaryRowId() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsClobFieldExists() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetClobFieldName() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsBlobFieldExists() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBlobFieldName() {
		fail("Not yet implemented");
	}

}
