package com.pccw.util.db;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pccw.util.db.mysql.DaoMySQLHelper;

public class DaoHelperTest {

	DaoHelper daoMySQLHelper = new DaoMySQLHelper();
	DaoHelper daoOracleHelper = new DaoOracleHelper();
	static final Logger logger = LoggerFactory.getLogger(DaoHelperTest.class);
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		String[][] dateFormat = {
				{"YYYYMMDD","%Y%m%d"},//OraDate
				{"YYYYMMDDHH24MISS","%Y%m%d%H%i%s"},//OraDateCreateDate
				{"YYYYMMDDHH24MISS","%Y%m%d%H%i%s"},//OraDateLastUpdDate
				{"DD/MM/YYYY","%d/%m/%Y"},//OraDateTerms
				{"YYYYMMDDHH24MISS.FF3","%Y%m%d%H%i%s.%f"},//OraDateTimestamp
				{"yyyyMMddHHmmss.SSS","%Y%m%d%h%m%s.%sS"},//OraDateTimestamp ???
				{"YYYYMMDDHH24MI","%Y%m%d%H%i"},//OraDateYYYYMMDDHH24MI
				{"YYYYMMDDHH24MISS","%Y%m%d%H%i%s"},//OraDateYYYYMMDDHH24MISS
				};
		ArrayList<Throwable> errors = new ArrayList<Throwable>();
		logger.info("Orginal, result");
		for(int i=0; i<dateFormat.length; i++){
			String convertedDateFormat = daoMySQLHelper.convertDateFormat(dateFormat[i][0], DaoHelperResolver.ORACLE);
			try{
				logger.info(dateFormat[i][0]+", "+ convertedDateFormat);
				assertEquals(dateFormat[i][1], convertedDateFormat);
				logger.info("Passed");
			}catch(Exception e){
				logger.info("Failed");
				errors.add(e);
			}
		}
		assertEquals(0,errors.size());
	}
}
