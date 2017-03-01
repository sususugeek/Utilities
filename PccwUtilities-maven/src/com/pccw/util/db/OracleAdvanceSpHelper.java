package com.pccw.util.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

//import oracle.jdbc.driver.OracleTypes;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.pccw.util.db.stringOracleType.OraArray;
import com.pccw.util.db.stringOracleType.OraDate;
import com.pccw.util.db.stringOracleType.OraNumber;

import oracle.jdbc.OracleTypes;

/**
 * <h4>Purpose:</h4>
 * <blockquote>
 * <p>
 * 
 * </p>
 * </blockquote>
 * <p>
 * <b>Version: </b> 1.0
 * </p>
 * <p>
 * <b>System Name: </b> BOM
 * </p>
 * <p>
 * <b>Module Name: bom.util.db.OracleSpHelper </b>
 * </p>
 * <p>
 * <b>Author: Raymond Wong KH </b>
 * </p>
 * <p>
 * <b>Created Date: 2005�~10��13�� </b>
 * </p>
 * <h4>Change Log:</h4>
 * <blockquote><table width="100%" border="0" cellspacing="0" cellpadding="0">
 * <tr>
 * <td width="20%"><b>Date </b></td>
 * <td width="30%"><b>Modify By </b></td>
 * <td width="50%"><b>Description </b></td>
 * </tr>
 * <tr>
 * <td>2005�~10��13��</td>
 * <td>Raymond Wong KH</td>
 * <td>Create</td>
 * </tr>
 * </table> </blockquote>
 */
public class OracleAdvanceSpHelper extends OracleHelperBase {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(OracleAdvanceSpHelper.class);

    protected ArrayList<StoreProcParameter> paramList;

    protected TreeMap<String, StoreProcParameter> paramMap = new TreeMap<String, StoreProcParameter>();
    
    protected String spName;

    protected String spDebugInfo;
    
    private Connection conn = null;
    
    //E0002506 - Springboard SLV Enhancement (2015)
	private DaoHelperResolver daoHelperResolver;
	
	public DaoHelperResolver getDaoHelperResolver() {
		if(daoHelperResolver == null){
			daoHelperResolver = DaoHelperResolverImpl.getInstance();
		}
		return daoHelperResolver;
	}

	public void setDaoHelperResolver(DaoHelperResolver daoHelperResolver) {
		this.daoHelperResolver = daoHelperResolver;
	}
    //E0002506 - Springboard SLV Enhancement (2015)
    public OracleAdvanceSpHelper() {
        super();
        clear();
    }

    public void clear() {
        this.paramList = null;
        spName = "";
    }

    public void setSpName(String psSpName) {
        spName = psSpName.trim();
    }

    public String getSpName() {
        return spName;
    }

    public Object getParamValue(String pKey) {
    	StoreProcParameter spParam = this.paramMap.get(pKey);
    	if (spParam == null) {
    		return null;
    	}
        return spParam.getValue();
    }

    protected void indexParamList() {
    	StoreProcParameter param = null;
    	for (int i = 0; i < this.paramList.size(); i++) {
    		param = this.paramList.get(i);
    		if (param == null) {
    			this.paramMap.put(String.valueOf(i + 1), param);
    			continue;
    		}
			this.paramMap.put((StringUtils.isBlank(param.getName()) ? String.valueOf(i + 1) : param.getName()), param);
		}
    }

    public ArrayList<StoreProcParameter> getParamList() {
		return this.paramList;
	}

	public void setParamList(ArrayList<StoreProcParameter> pParamList) {
		this.paramList = pParamList;
		this.indexParamList();
	}

	public String getSpDebugInfo() {
		return this.spDebugInfo;
	}

	public void appendStoreProcParameter(StoreProcParameter pStoreProcParameter) {
        if (this.paramList == null) {
        	this.paramList = new ArrayList<StoreProcParameter>();
        }
        this.paramList.add(pStoreProcParameter);
    }

    private CallableStatement generateCallableStatement(StringBuffer pSpDebugInfo) throws SQLException {
        StringBuilder sbSpCmd = new StringBuilder();

        sbSpCmd.append("{call " + this.spName + "(");

        if (this.paramList == null) {
        	this.paramList = new ArrayList<StoreProcParameter>();
        }
        
        this.indexParamList();
        
        StoreProcParameter param = null;
        String sql = null;
        for (int i = 0; i < this.paramList.size(); i++) {
        	if (i > 0) {
        		sbSpCmd.append(", ");
        	}
        	param = this.paramList.get(i);
        	if (param == null) {
        		sbSpCmd.append("null");
        		continue;
        	}
        	
        	param.setBindSqlValueRequired(param.getParamType() == StoreProcParameter.PARAM_TYPE_IN
        			|| param.getParamType() == StoreProcParameter.PARAM_TYPE_IN_OUT);
        	
        	if (param.getOracleType() == OracleTypes.CURSOR) {
        		param.setBindSqlValueRequired(false);
        	}
        	
        	if (param.value instanceof OraNumber) {
        		sbSpCmd.append("TO_NUMBER(?)");
        	} else if (param.value instanceof OraDate) {
        		// E0002506 - Springboard SLV Enhancement (2015)
//        		sql = DaoProperty.getOracleInsertUpdateClause((OraDate) param.value);
        		sql = this.daoHelperResolver.resolveHelper(this.conn).generateSQLInsertUpdateClause((OraDate) param.value);
        		sbSpCmd.append(sql);
        		param.setBindSqlValueRequired(param.isBindSqlValueRequired() && sql.contains("?"));
        	} else {
        		sbSpCmd.append("?");
        	}
        }

        sbSpCmd.append(")}");
        
        if (this.conn == null) {
        	this.conn = this.getConnection();
        }
        
        if (pSpDebugInfo != null) {
        	pSpDebugInfo.append(sbSpCmd.toString());
        	pSpDebugInfo.append("\n");
        }
        
        try {
            return this.conn.prepareCall(sbSpCmd.toString());
        } catch (SQLException e) {
            logger.error(ExceptionUtils.getFullStackTrace(e));
            throw e;
        }
    }

	@SuppressWarnings("unchecked")
	public void execSp() throws SQLException, Exception {

        if (StringUtils.isBlank(this.spName)) {
            this.errMsg = "SP Name CANNOT Be Blank";
            throw new Exception(this.errMsg);
        }

        if (this.getConnection() == null) {
            this.errMsg = "NO DB CONNECTION";
            throw new Exception(this.errMsg);
        }

        StringBuffer sbDebugInfo = new StringBuffer();
        CallableStatement sproc = null;
        ResultSet rs = null;
        try {
            sproc = this.generateCallableStatement(sbDebugInfo);

            this.registerInOutParameters(sproc, sbDebugInfo);

            this.spDebugInfo = sbDebugInfo.toString();
            
            sproc.execute();

            StoreProcParameter param = null;
            Object[] rsObjects = null;
            int pos = 0;
            for (int i = 0; pos < this.paramList.size(); i++) {
            	param = this.paramList.get(i);
            	if (param.getParamType() == StoreProcParameter.PARAM_TYPE_IN) {
            		continue;
            	}
            	pos++;
            	if (param.getOracleType() == OracleTypes.VARCHAR) {
            		param.setValue(sproc.getString(pos));
            	} else if (param.getOracleType() == OracleTypes.DATE) {
            		param.setValue(sproc.getDate(pos));
            	} else if (param.getOracleType() == OracleTypes.FLOAT) {
            		param.setValue(sproc.getFloat(pos));
            	} else if (param.getOracleType() == OracleTypes.ARRAY) {
            		param.setValue(sproc.getArray(pos).getArray());
            	} else if (param.getOracleType() == OracleTypes.CURSOR) {
            		rs = (ResultSet) sproc.getObject(pos);
            		if (rs == null) {
            			continue;
            		}
                    try {
                    	//E0002506 - Springboard SLV Enhancement (2015)
                    	rsObjects = OracleSelectHelper.getResultSetObjects(rs, rs.getMetaData(), param.getCursorObjectClass());
                    	param.setValue(rsObjects);
                    } catch (Exception e) {
                    	logger.error(ExceptionUtils.getFullStackTrace(e));
                        throw e;
                    } finally {
                        rs.close();
                    }

            	} else if (param.getOracleType() == OracleTypes.BLOB) {
            		InputStream is = sproc.getBlob(pos + 1).getBinaryStream();
            		if (is != null) {
                		try {
                			param.setValue(IOUtils.toByteArray(is));
                		} finally {
                			is.close();
                		}
            		}
            	} else if (param.getOracleType() == OracleTypes.CLOB) {
            	    StringBuilder sb = new StringBuilder();
        	        BufferedReader br = new BufferedReader(sproc.getCharacterStream(pos + 1));
            	    try {
            	        String line;
            	        line = br.readLine();
            	        while(line != null) {
            	            sb.append(line);
            	            line = br.readLine();
            	        }
            	    } finally {
            	        br.close();
            	    }
            	    param.setValue(sb.toString());
            	}
            }

        } catch (Exception e) {
        	logger.error(ExceptionUtils.getFullStackTrace(e));
        	throw e;
        } finally {
            if (sproc != null) {
                try {
                    sproc.close();
                } catch (SQLException e) {
                    logger.error("Close CallableStatement: " + e);
                }
            }
            
            if (this.conn != null) {
            	this.releaseConnection(this.conn);
            	this.conn = null;
            }
        }
    }

    private void registerInOutParameters(CallableStatement pCallableStatement, StringBuffer pDebugInfo) throws SQLException {
        try {
        	if (pDebugInfo == null) {
        		pDebugInfo = new StringBuffer();
        	}

            StoreProcParameter param = null;
            int pos = 0;
            for (int i = 0; i < this.paramList.size(); i++) {
            	param = this.paramList.get(i);
            	if (param == null) {
            		continue;
            	}
            	pos++;
            	
            	if (param.getParamType() == StoreProcParameter.PARAM_TYPE_IN && !param.isBindSqlValueRequired()) {
            		pos--;	
            	}
            	
            	if (param.getParamType() == StoreProcParameter.PARAM_TYPE_IN) {
            		if (param.isBindSqlValueRequired()) {
            			OracleSelectHelper.setBindingValue(this.getConnection(), pCallableStatement, pos, param.getValue(), pDebugInfo);	
            		}
            	} else if (param.getParamType() == StoreProcParameter.PARAM_TYPE_OUT) {
            		if (StringUtils.isNotBlank(param.getOracleArrayType())) {
                		pCallableStatement.registerOutParameter(pos, param.getOracleType(), param.getOracleArrayType());
            		} else {
                		pCallableStatement.registerOutParameter(pos, param.getOracleType());
            		}
            	} else if (param.getParamType() == StoreProcParameter.PARAM_TYPE_IN_OUT) {
            		if (StringUtils.isNotBlank(param.getOracleArrayType())) {
                		pCallableStatement.registerOutParameter(pos, param.getOracleType(), param.getOracleArrayType());
            		} else {
                		pCallableStatement.registerOutParameter(pos, param.getOracleType());
            		}
            		if (param.getOracleType() != OracleTypes.CURSOR
            				&& param.isBindSqlValueRequired()) {
            			OracleSelectHelper.setBindingValue(this.getConnection(), pCallableStatement, pos, param.getValue(), pDebugInfo);	
            		}
            	}
            }
        	
            logger.debug("execSp(Connection, String, List, int) sInParam:\n" + pDebugInfo.toString());
        } catch (SQLException e) {
            logger.error(ExceptionUtils.getFullStackTrace(e));
            logger.error(pDebugInfo.toString());
            throw e;
        }
    }

    
    public static class StoreProcParameter implements Serializable {
    	/**
		 * 
		 */
		private static final long serialVersionUID = -44100044313029354L;

		public static final int ORA_TYPE_ARRAY = OracleTypes.ARRAY;
		
		public static final int ORA_TYPE_BLOB = OracleTypes.BLOB;

		public static final int ORA_TYPE_CLOB = OracleTypes.CLOB;
		
		public static final int ORA_TYPE_CURSOR = OracleTypes.CURSOR;

		public static final int ORA_TYPE_DATE = OracleTypes.DATE;

		public static final int ORA_TYPE_FLOAT = OracleTypes.FLOAT;

		public static final int ORA_TYPE_VARCHAR = OracleTypes.VARCHAR;

		public static final int PARAM_TYPE_IN = 0;

		public static final int PARAM_TYPE_OUT = 1;
		
		public static final int PARAM_TYPE_IN_OUT = 2;
		
		private String name;
 
		private int paramType;
		
    	private int oracleType;
    	
    	private String oracleArrayType;
 
    	private boolean bindSqlValueRequired;
    	
    	@SuppressWarnings("rawtypes")
		private Class cursorObjectClass;
    	
    	private Object value;

    	/**
    	 * Create a Stored Procedure Parameter with name
    	 * 
    	 * @param pName
    	 */
    	public StoreProcParameter(String pName) {
    		super();
    		this.name = pName;
    	}
    	
    	
    	/**
    	 * Create an Output Parameter
    	 * 
    	 * @param pName
    	 * @param pOracleType
    	 */
    	public StoreProcParameter(String pName, int pOracleType) {
    		this(pName);
    		this.paramType = PARAM_TYPE_OUT;
    		this.oracleType = pOracleType;
    	}
    	
    	/**
    	 * Create an Output Parameter
    	 * 
    	 * @param pName
    	 * @param pOracleType
    	 */
    	public StoreProcParameter(String pName, int pOracleType, String pOracleArrayType) {
    		this(pName);
    		this.paramType = PARAM_TYPE_OUT;
    		this.oracleType = pOracleType;
    		this.oracleArrayType = pOracleArrayType;
    	}
    	
    	/**
    	 * Create an Input Parameter - Array
    	 * 
    	 * @param pName
    	 * @param pData
    	 */
    	public StoreProcParameter(String pName, String pValue) {
    		this(pName);
    		this.value = pValue;
    		this.paramType = PARAM_TYPE_IN;
    		this.oracleType = ORA_TYPE_VARCHAR;
    	}
    	
    	/**
    	 * Create an Input Parameter - Array (Can specify as PARAM_TYPE_IN_OUT)
    	 * 
    	 * @param pName
    	 * @param pData
    	 */
    	public StoreProcParameter(String pName, String pValue, int pParamType) {
    		this(pName, pValue);
    		this.paramType = pParamType;
    	}

       	/**
    	 * Create an Input Parameter - Array
    	 * 
    	 * @param pName
    	 * @param pData
    	 */
    	public StoreProcParameter(String pName, OraArray pValue) {
    		this(pName);
    		this.value = pValue;
    		this.paramType = PARAM_TYPE_IN;
    		this.oracleType = ORA_TYPE_ARRAY;
    	}
    	
    	/**
    	 * Create an Input Parameter - Array (Can specify as PARAM_TYPE_IN_OUT)
    	 * 
    	 * @param pName
    	 * @param pData
    	 */
    	public StoreProcParameter(String pName, OraArray pValue, int pParamType) {
    		this(pName, pValue);
    		this.paramType = pParamType;
    	}
    	
    	/**
    	 * Create an Input Parameter - NUMBER
    	 * 
    	 * @param pName
    	 * @param pData
    	 */
    	public StoreProcParameter(String pName, OraNumber pValue) {
    		this(pName);
    		this.value = pValue;
    		this.paramType = PARAM_TYPE_IN;
    	}

    	
    	/**
    	 * Create an Input Parameter - NUMBER (Can specify as PARAM_TYPE_IN_OUT)
    	 * 
    	 * @param pName
    	 * @param pData
    	 */
    	public StoreProcParameter(String pName, OraNumber pValue, int pParamType) {
    		this(pName, pValue);
    		this.paramType = pParamType;
    	}

    	/**
    	 * Create an Input Parameter - DATE
    	 * 
    	 * @param pName
    	 * @param pData
    	 */
    	public StoreProcParameter(String pName, OraDate pValue) {
    		this(pName);
    		this.value = pValue;
    		this.paramType = PARAM_TYPE_IN;
    	}

    	
    	/**
    	 * Create an Input Parameter - NUMBER (Can specify as PARAM_TYPE_IN_OUT)
    	 * 
    	 * @param pName
    	 * @param pData
    	 */
    	public StoreProcParameter(String pName, OraDate pValue, int pParamType) throws Exception {
    		this(pName, pValue);
    		this.paramType = pParamType;
    		if (pParamType == PARAM_TYPE_IN_OUT) {
    			this.value = pValue.toSqlDate();
    		}
    	}

    	
    	/**
    	 * Create an Ouptput Parameter with Cursor Type
    	 * 
    	 * @param pName
    	 * @param pCursorObjectClass
    	 */
		@SuppressWarnings("rawtypes")
		public StoreProcParameter(String pName, Class pCursorObjectClass) {
			super();
			this.name = pName;
			this.oracleType = ORA_TYPE_CURSOR;
			this.paramType = PARAM_TYPE_OUT;
			this.cursorObjectClass = pCursorObjectClass;
		}

		public String getName() {
			return this.name;
		}

		public void setName(String pName) {
			this.name = pName;
		}

		public int getParamType() {
			return this.paramType;
		}

		public void setParamType(int pParamType) {
			this.paramType = pParamType;
		}

		public int getOracleType() {
			return this.oracleType;
		}

		public void setOracleType(int pOracleType) {
			this.oracleType = pOracleType;
		}
		
		
		public String getOracleArrayType() {
			return this.oracleArrayType;
		}


		public void setOracleArrayType(String pOracleArrayType) {
			this.oracleArrayType = pOracleArrayType;
		}


		@SuppressWarnings("rawtypes")
		public Class getCursorObjectClass() {
			return this.cursorObjectClass;
		}

		public void setCursorObjectClass(@SuppressWarnings("rawtypes") Class pCursorObjectClass) {
			this.cursorObjectClass = pCursorObjectClass;
		}

		public Object getValue() {
			return this.value;
		}

		public void setValue(Object pValue) {
			this.value = pValue;
		}

		public boolean isBindSqlValueRequired() {
			return this.bindSqlValueRequired;
		}

		public void setBindSqlValueRequired(boolean pBindSqlValueRequired) {
			this.bindSqlValueRequired = pBindSqlValueRequired;
		}
    }
}