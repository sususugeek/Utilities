package com.pccw.util.db.stringOracleType;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import com.pccw.util.db.DaoBaseImpl;
import com.pccw.util.db.DaoHelper;
import com.pccw.util.db.DaoHelperResolver;
import com.pccw.util.db.DaoHelperResolverImpl;
import com.pccw.util.db.OracleSelectHelper;

public class OraNumberInsertValueFromSelectSeq extends OraNumberInsertValueFromSelect {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6855112569416919702L;

	private static final String MY_SQL_SEQ_SP_OUT_PARAM_NAME = "ret";
	
	private String sequenceName;
	
	private String serverId;

	public OraNumberInsertValueFromSelectSeq(String pValue, String pSequenceName) {
		super(pValue);
		this.sequenceName = pSequenceName;
	}
	
	public OraNumberInsertValueFromSelectSeq(String pValue, String pSequenceName, String pServerId) {
		super(pValue);
		this.sequenceName = pSequenceName;
		this.serverId = pServerId;
	}
	
	@Override
	public String getInsertValue(DaoBaseImpl pDao) throws Exception {
		return generateId(pDao.getDataSource());
	}
	
	public String generateId(DataSource pDataSource) throws Exception {
		String dbType = DaoHelperResolverImpl.getInstance().resolveDBType(pDataSource);
		
		if (StringUtils.isBlank(this.sequenceName)) {
			throw new Exception("Sequence Name IS NULL");
		}

		String statementName = this.getClass().getName() + "." + this.sequenceName + ".nextval";
		if (DaoHelperResolver.ORACLE.equals(dbType)) {
			return OracleSelectHelper.getSqlFirstRowColumnString(pDataSource, 
					DaoHelper.insertPackageNameToSql(statementName, "SELECT TO_CHAR(" + this.sequenceName + ".nextval) FROM DUAL", DaoHelperResolver.ORACLE));
		} else if (DaoHelperResolver.MYSQL.equals(dbType)) {
			return getMySqlSeqNextvalue(pDataSource, this.sequenceName);
		} 
		
		throw new Exception("No implmentation found. Fail to generate ID: " + this.getClass().getName() + " - " + dbType);
	}
	
	private String getMySqlSeqNextvalue(DataSource pDataSource, String pSequenceName){
		StringBuilder sbSql = new StringBuilder("sp_get_sequence_nextval,in_server_id:");
		sbSql.append(this.serverId);
		sbSql.append(":"); 
		sbSql.append(Types.VARCHAR);
		sbSql.append(",in_seq_name:");
		sbSql.append(pSequenceName);
		sbSql.append(":"); 
		sbSql.append(Types.VARCHAR);
		sbSql.append(",out:");
		sbSql.append(MY_SQL_SEQ_SP_OUT_PARAM_NAME);
		sbSql.append(":");
		sbSql.append(Types.VARCHAR);
		
//		String[] sqlsSPCommands = StringUtils.split(sbSql.toString(), ',');
//		
//		JdbcTemplate jdbcTemplate = new JdbcTemplate(pDataSource);
//		
//		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(sqlsSPCommands[0]);
//		MapSqlParameterSource inMap = new MapSqlParameterSource();
//
//		List<String> outParamNames = new ArrayList<String>();
//		for (int i = 1; i < sqlsSPCommands.length; i++) {
//			String[] params = StringUtils.split(sqlsSPCommands[i], ':');
//			if ("out".equals(params[0])) {
//				jdbcCall.declareParameters(new SqlOutParameter(params[1], Integer.parseInt(params[2])));
//				outParamNames.add(params[1]);
//			} else {
//				jdbcCall.declareParameters(new SqlParameter(params[0], Integer.parseInt(params[2])));
//				inMap.addValue(params[0], params[1]);
//			}
//		}
//
//		SqlParameterSource in = inMap;
//		return (String) jdbcCall.execute(in).get(MY_SQL_SEQ_SP_OUT_PARAM_NAME);
		return callByCallableStatement(pDataSource, sbSql.toString());
	}
	
	private String callByCallableStatement(DataSource pDataSource, String pSqlCommandString){
		String ret = null;
		String[] sqlsSPCommands = StringUtils.split(pSqlCommandString.toString(), ',');
		String spSPCommandStr = "{call "+sqlsSPCommands[0]+"(";
		boolean first = true;
		List<String> outParamNames = new ArrayList<String>();
		for (int i = 1; i < sqlsSPCommands.length; i++) {
			String[] params = StringUtils.split(sqlsSPCommands[i], ':');
			if(first){
				spSPCommandStr = spSPCommandStr+"?";
				first=false;
			}else{
				spSPCommandStr = spSPCommandStr+",?";
			}
		}
		spSPCommandStr = spSPCommandStr+")}";
		Connection connection = null;
		try {
			connection = pDataSource.getConnection();
			CallableStatement callableSt = connection.prepareCall(spSPCommandStr);
			
			for (int i = 1; i < sqlsSPCommands.length; i++) {
				String[] params = StringUtils.split(sqlsSPCommands[i], ':');
				
				if ("out".equals(params[0])) {
					callableSt.registerOutParameter(params[1], Integer.parseInt(params[2]));
					outParamNames.add(params[1]);
				} else {
					callableSt.setString(params[0], params[1]);
				}
			}
			callableSt.execute();
			ret = callableSt.getString(outParamNames.get(0));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
}