package com.pccw.util.db;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.SpringVersion;

import com.pccw.util.datatransfer.dataAccess.BomwebAcctDAOImpl;
import com.pccw.util.datatransfer.dataAccess.WItemAttbDAO;
import com.pccw.util.spring.ApplicationContextProvider;
import com.pccw.util.spring.SpringApplicationContext;

public class DaoBaseImplTest {
	private static final Logger logger = Logger.getLogger(DaoBaseImplTest.class);
	@Before
	public void setUp() throws Exception {
		ClassPathXmlApplicationContext jdbcCtx = new ClassPathXmlApplicationContext("ApplicationContextMySQLUnitTest.xml");
		ApplicationContextProvider applicationContextProvider = new ApplicationContextProvider();
		applicationContextProvider.setApplicationContext(jdbcCtx);
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
	public void testSpringVersion() {
		String springVersionStr = SpringVersion.getVersion();
		logger.debug(springVersionStr);
	}

//	@Test
	public void testInsertBomwebAcct(){
		BomwebAcctDAOImpl bomwebAcctDAOImplMySQL = SpringApplicationContext.getBean("bomwebAcctDAOImplMySQL");
//		BomwebAcctDAOImpl bomwebAcctDAOImplOracle = SpringApplicationContext.getBean("bomwebAcctDAOImplOracle");
	
		bomwebAcctDAOImplMySQL.setAcctName("WHELAN TEST");
		bomwebAcctDAOImplMySQL.setCustNo("");
//		bomwebAcctDAOImplOracle.setCustNo("");
		
		try {
			bomwebAcctDAOImplMySQL.doInsert();
//			bomwebAcctDAOImplOracle.doInsert();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
//	@Test
	public void testDeleteBomwebAcctMy(){
		BomwebAcctDAOImpl bomwebAcctDAOImplMySQL = SpringApplicationContext.getBean("bomwebAcctDAOImplMySQL");
		try {
			bomwebAcctDAOImplMySQL.setSearchKey("acctName", "WHELAN TEST");
			DaoBase[] results = bomwebAcctDAOImplMySQL.doSelect(null,null);
			
			for(DaoBase daoBase: results){
//				daoBase.setSearchKey("acctName", "WHELAN TEST");
				daoBase.doDelete();
			}
			System.out.println(results);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
//	@Test
	public void testDeleteBomwebAcctOra(){
		BomwebAcctDAOImpl bomwebAcctDAOImplOracle = SpringApplicationContext.getBean("bomwebAcctDAOImplOracle");

		try {
			bomwebAcctDAOImplOracle.setSearchKey("acctName", "WHELAN TEST");
			bomwebAcctDAOImplOracle.setSearchKey("acctName", "");
			DaoBase[] results = bomwebAcctDAOImplOracle.doSelect(null,null);
			
			for(DaoBase daoBase: results){
				daoBase.doDelete();
			}
//			bomwebAcctDAOImplOracle.doInsert();
			System.out.println(results);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
//	@Test
	public void testDoDeleteMethod() throws Exception
	{
		try{
		//select * from w_item_attb where item_id = 502004789 and attb_cd = 'BASKET_CAT_EXCLUDE';
		//502004789	BASKET_CAT_EXCLUDE			ELOADER	2016-11-14 18:40:08.167524	ELOADER	2016-11-14 18:40:08.167535	15660
		//502004789	BASKET_CAT_EXCLUDE			ELOADER	2016-11-14 18:40:08.0	ELOADER	2016-11-18 11:52:16.587736	16215
//			String itemId = "502004789";
//			String attbCd = "BASKET_CAT_EXCLUDE";
			String itemId = "1";
			String attbCd = "TESTING";
			
			WItemAttbDAO criteria = SpringApplicationContext.getBean("WItemAttbDAO");

			criteria.setSearchKey("itemId", itemId);
			criteria.setSearchKey("attbCd", attbCd);
			
			WItemAttbDAO[] attbList =(WItemAttbDAO[])criteria.doSelect(null, null);
				
			if(attbList != null){
				logger.info("Original data size: " + attbList.length);
				for(WItemAttbDAO attb : attbList){
					logger.info("Oracle ROW_ID : " + attb.getOracleRowID());
					logger.info("create_date : " + attb.getCreateDate());
					attb.doDelete();
				}
				
			}	
			
			WItemAttbDAO[] newList =(WItemAttbDAO[])criteria.doSelect(null, null);
			logger.info("After deleted, data size: " + newList.length);
			
			//502004789	BASKET_CAT_EXCLUDE			ELOADER	2016-11-14 18:40:08.167524	ELOADER	2016-11-14 18:40:08.167535	15660
			
			/*
			WItemAttbDAO newWItemAttbDAO = SpringApplicationContext.getBean(WItemAttbDAO.class);
			newWItemAttbDAO.setItemId(itemId);
			newWItemAttbDAO.setAttbCd(attbCd);
			newWItemAttbDAO.setLastUpdBy("ELOADER");				
			newWItemAttbDAO.setCreateBy("ELOADER");
			newWItemAttbDAO.doInsert();
			*/
			
		} catch (Exception e) {
			//throw new RuntimeException(e);
			e.printStackTrace();
		}
	}
//	@Test
	public void testWItemAttbInsert() throws Exception
	{
		WItemAttbDAO witemAttbDAO = SpringApplicationContext.getBean("WItemAttbDAO");
		witemAttbDAO.setItemId("9999990");
		witemAttbDAO.setAttbCd("WHELAN TEST");
		witemAttbDAO.setAttbDesc("");
		witemAttbDAO.doInsert();
	}
	@Test
	public void testWItemAttbDelete() throws Exception
	{
		WItemAttbDAO witemAttbDAO = SpringApplicationContext.getBean("WItemAttbDAO");
		witemAttbDAO.setSearchKey("itemId", "9999990");
		witemAttbDAO.setSearchKey("attbCd", "WHELAN TEST");
		
		DaoBase[] results = witemAttbDAO.doSelect(null, null);
		for(DaoBase result:results){
			result.doDelete();
		}
	}
}
