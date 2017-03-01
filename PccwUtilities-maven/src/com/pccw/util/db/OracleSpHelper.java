package com.pccw.util.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

//import oracle.jdbc.driver.OracleTypes;
import oracle.jdbc.OracleTypes;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

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
public class OracleSpHelper extends OracleHelperBase {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(OracleSpHelper.class);

    protected int spReturnCode;

    protected int spErrorCode;

    protected String spErrMsg;

    protected ArrayList<String> vcOutParamNameList;

    protected ArrayList<String> cursorOutParamNameList;

    @SuppressWarnings("rawtypes")
	protected ArrayList<Class> cursorOutParamClassList;

    protected ArrayList<Object> inputParamList;

    protected Map<String, String> vcOutParamMap;

    protected Map<String, Object[]> cursorOutParamMap;

    protected String spName;

    protected int numOfVarcharOut;

    protected int numOfCursorOut;

    protected boolean oracle817Required = false;

    protected boolean previousOracleVersion = false;

    protected String _sOracleVersion = "";

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
    
    public OracleSpHelper() {
        super();
        clear();
    }

    public void clear() {
        inputParamList = null;
        vcOutParamMap = null;
        this.cursorOutParamMap = null;
        spName = "";
        spReturnCode = 0;
        spErrorCode = 0;
        spErrMsg = "";
        numOfVarcharOut = 0;
        numOfCursorOut = 0;
        oracle817Required = false;
        previousOracleVersion = false;
        _sOracleVersion = "";

    }

    /**
     * @return Returns the cursorOutParamSize.
     */
    public int getCursorOutParamSize() {
        return this.cursorOutParamMap.size();
    }

    /**
     * @return Returns the vcOutParamSize.
     */
    public int getVcOutParamSize() {
        return this.vcOutParamMap.size();
    }

    public void setSpName(String psSpName) {
        spName = psSpName.trim();
    }

    public String getSpName() {
        return spName;
    }

    public int getSpReturnCode() {
        return spReturnCode;
    }

    public int getSpErrorCode() {
        return spErrorCode;
    }

    public String getSpErrMsg() {
        return spErrMsg;
    }

    public String getVCOutParamValue(String pKey) {
        return (String) this.vcOutParamMap.get(pKey);
    }

    protected void appendVCOutParamMap(String pKey, String psValue) {
        if (vcOutParamMap == null) {
            this.vcOutParamMap = new TreeMap<String, String>();
        }

        if (pKey == null) {
            pKey = String.valueOf(this.getVcOutParamSize());
        }

        vcOutParamMap.put(pKey, psValue);

        if (logger.isDebugEnabled()) {
            logger.debug("appendVCOutParamList() - " + "vcOutParamSize: " + this.getVcOutParamSize());
        }
    }

    public Object[] getCursorOutParamObjects(String pKey) {
        return cursorOutParamMap.get(pKey);
    }

    public Map<String, String> getVcOutParamMap() {
        return this.vcOutParamMap;
    }

    public Map<String, Object[]> getCursorOutParamMap() {
        return this.cursorOutParamMap;
    }

    protected void appendOutParamCursorMap(String pKey, Object[] pObjects) {
        if (this.cursorOutParamMap == null) {
            this.cursorOutParamMap = new TreeMap<String, Object[]>();
        }
        if (pKey == null) {
            pKey = String.valueOf(this.getCursorOutParamSize());
        }
        this.cursorOutParamMap.put(pKey, pObjects);
    }

    public void setInputParamList(ArrayList<Object> pInputParamMap) {
        this.inputParamList = pInputParamMap;
    }

    public ArrayList<Object> getInputParamList() {
        return this.inputParamList;
    }

    public void appendInputParam(String pParamValue) {
        if (inputParamList == null) {
            inputParamList = new ArrayList<Object>();
        }
        inputParamList.add(pParamValue);
    }

    public int getInputParamSize() {
        if (inputParamList == null) {
            return 0;
        }

        return inputParamList.size();
    }

    public void setNumOfVarcharOut(int piNumOfVarcharOut) {
        this.numOfVarcharOut = piNumOfVarcharOut;
    }

    public void setNumOfCursorOut(int piNumOfCursorOut) {
        this.numOfCursorOut = piNumOfCursorOut;
    }

    public int getNumOfVarcharOut() {
        return numOfVarcharOut;
    }

    public int getNumOfCursorOut() {
        return numOfCursorOut;
    }

    public CallableStatement generateCallableStatement(int pNumOfOutput) {
        String sSPCmd = null;

        logger.debug("execSp() iNumOfOutput:\n" + String.valueOf(numOfVarcharOut + numOfCursorOut));
        logger.debug("execSp() _listInputParam:\n" + String.valueOf(inputParamList.size()));

        String dbType = this.getDaoHelperResolver().resolveDBType(this.getDataSource());
		if(DaoHelperResolverImpl.MYSQL.equals(dbType)){
			sSPCmd = "{call " + this.spName.replace('.', '_') + "(";
		} else{
			sSPCmd = "{call " + this.spName + "(";

	        if (oracle817Required && previousOracleVersion && numOfCursorOut > 0) {
	            sSPCmd = "{call " + this.spName + "_gensql(";
	        }
		}
			
//        sSPCmd = "{call " + this.spName + "(";
//
//        if (oracle817Required && previousOracleVersion && numOfCursorOut > 0) {
//            sSPCmd = "{call " + this.spName + "_gensql(";
//        }

        //input param
        for (int i = 0; i < inputParamList.size(); i++) {
            sSPCmd += "?, ";
        }

        //output param
        for (int i = 0; i < pNumOfOutput; i++) {
            sSPCmd += "?, ";
        }

        sSPCmd += "?, ?, ?)}";

        if (logger.isDebugEnabled()) {
            logger.debug("execSp(int, int) sSPCmd:\n" + sSPCmd);
        }

        if (this.conn == null) {
        	this.conn = this.getConnection();
        }
        
        try {
            return this.conn.prepareCall(sSPCmd);
        } catch (SQLException e) {
            logger.error("generateCallableStatement(int) - Exception: ", e);

            this.errMsg = "generateCallableStatement(int) - Exception: " + e.toString() + "\n"
                    + ExceptionUtils.getFullStackTrace(e);
            return null;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean execSp() {

        if (this.spName == null || this.spName.equals("")) {
            this.errMsg = "SP Name CANNOT Be Blank";
            return false;
        }

        StringBuffer sOutParam = new StringBuffer("");
        int i, iNumOfOutput;

        boolean bRtnVal;

        if (this.getConnection() == null) {
            this.errMsg = "NO DB CONNECTION";
            return false;
        }

        StringBuffer sbInParmsDebug = new StringBuffer();
        CallableStatement sproc = null;
        ResultSet rs = null;
        try {

            if (numOfVarcharOut + numOfCursorOut < 0)
                iNumOfOutput = 0;
            else
                iNumOfOutput = numOfVarcharOut + numOfCursorOut;

            sproc = this.generateCallableStatement(iNumOfOutput);

            if (sproc == null) {
                return false;
            }

            if (!this.registerInputParameters(sproc, sbInParmsDebug)) {
                return false;
            }

            for (i = 0; i < numOfVarcharOut; i++) {
                sproc.registerOutParameter(inputParamList.size() + i + 1, OracleTypes.VARCHAR);
            }

            for (i = 0; i < numOfCursorOut; i++) {
                sproc.registerOutParameter(inputParamList.size() + numOfVarcharOut + i + 1, OracleTypes.CURSOR);
            }

            sproc.registerOutParameter(inputParamList.size() + iNumOfOutput + 1, Types.INTEGER);
            sproc.registerOutParameter(inputParamList.size() + iNumOfOutput + 2, Types.INTEGER);
            sproc.registerOutParameter(inputParamList.size() + iNumOfOutput + 3, Types.VARCHAR);

            sproc.execute();

            spReturnCode = sproc.getInt(inputParamList.size() + iNumOfOutput + 1);
            spErrorCode = sproc.getInt(inputParamList.size() + iNumOfOutput + 2);
            spErrMsg = sproc.getString(inputParamList.size() + iNumOfOutput + 3);
            
            if (spReturnCode != 0) {
            	StringBuffer sbErrMsg = new StringBuffer(sbInParmsDebug.toString());
            	sbErrMsg.append("spReturnCode: " + this.spReturnCode + "\n");
            	sbErrMsg.append("spErrorCode: " + this.spErrorCode + "\n");
            	sbErrMsg.append("spErrorMsg: " + this.spErrMsg + "\n");
            	logger.error(sbErrMsg.toString());
            }

            String key = null;
            for (i = 0; i < numOfVarcharOut; i++) {
                key = String.valueOf(i);

                if (this.vcOutParamNameList != null) {
                    key = (String) this.vcOutParamNameList.get(i);
                }

                this.appendVCOutParamMap(key, sproc.getString(inputParamList.size() + i + 1));
                sOutParam.append(":" + sproc.getString(inputParamList.size() + i + 1) + ":" + "\n");
            }

            int cursorOutParamIndex = 0;
            Object[] rsObjects = null;
            for (i = numOfVarcharOut; i < iNumOfOutput; i++) {
                key = String.valueOf(cursorOutParamIndex);

                if (this.cursorOutParamNameList != null) {
                    key = (String) this.cursorOutParamNameList.get(cursorOutParamIndex);
                }

                try {
                	rs = (ResultSet) sproc.getObject(inputParamList.size() + i + 1);
                } catch (Exception e) {
                	logger.error(ExceptionUtils.getFullStackTrace(e));
                	if (spReturnCode == 0) {
                		throw e;
                	} else {
                		this.appendOutParamCursorMap(key, null);
                		continue;
                	}
                }

                if (rs == null) {
                    this.appendOutParamCursorMap(key, null);
                    continue;
                }

                try {
                    Class rowObjectClass = DynaBean.class;

                    if (this.cursorOutParamClassList != null) {
                        rowObjectClass = (Class) this.cursorOutParamClassList.get(cursorOutParamIndex);
                    }

                    rsObjects = OracleSelectHelper.getResultSetObjects(rs, rs.getMetaData(), rowObjectClass);//E0002506 - Springboard SLV Enhancement (2015)

                    this.appendOutParamCursorMap(key, rsObjects);

                } catch (Exception e) {
                    throw e;
                } finally {
                    rs.close();
                }

                cursorOutParamIndex++;
            }

            bRtnVal = (spReturnCode == 0);
        } catch (SQLException ex) {
            logger.error("SQLException: ", ex);

            this.errMsg = "SQLException: " + ex.toString() + "\n" + ExceptionUtils.getFullStackTrace(ex);

            bRtnVal = false;
        } catch (Exception ex) {
            logger.error("Exception: ", ex);

            this.errMsg = "Exception: " + ex.toString() + "\n" + ExceptionUtils.getFullStackTrace(ex);
            bRtnVal = false;
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

        return bRtnVal;
    }

    public boolean registerInputParameters(CallableStatement pCallableStatement, StringBuffer pSbDebugMsg) {
        try {
        	StringBuffer sInParam = null;
        	if (pSbDebugMsg != null){
        		sInParam = pSbDebugMsg;
        	}
        	sInParam.append(spName + "\n");
        	OracleSelectHelper.setBindingValues(this.getConnection(), pCallableStatement, 
        			inputParamList.toArray(), sInParam);
        	
            logger.debug("execSp(Connection, String, List, int) sInParam:\n" + sInParam.toString());

            return true;
        } catch (SQLException e) {
            logger.error("registerInputParameters(CallableStatement) - SQLException: ", e);
            this.errMsg = "registerInputParameters(CallableStatement) - SQLException: " + e.toString() + "\n"
                    + ExceptionUtils.getFullStackTrace(e);
            return false;
        }
    }

    /**
     * @return Returns the cursorOutParamClassList.
     */
    @SuppressWarnings("rawtypes")
	public ArrayList<Class> getCursorOutParamClassList() {
        return this.cursorOutParamClassList;
    }

    /**
     * @param pCursorOutParamClassList
     *            The cursorOutParamClassList to set.
     */
    @SuppressWarnings("rawtypes")
	public void setCursorOutParamClassList(ArrayList<Class> pCursorOutParamClassList) {
        this.cursorOutParamClassList = pCursorOutParamClassList;
    }

    /**
     * @return Returns the cursorOutParamNameList.
     */
    public ArrayList<String> getCursorOutParamNameList() {
        return this.cursorOutParamNameList;
    }

    /**
     * @param pCursorOutParamNameList
     *            The cursorOutParamNameList to set.
     */
    public void setCursorOutParamNameList(ArrayList<String> pCursorOutParamNameList) {
        this.cursorOutParamNameList = pCursorOutParamNameList;
    }

    /**
     * @return Returns the vcOutParamNameList.
     */
    public ArrayList<String> getVcOutParamNameList() {
        return this.vcOutParamNameList;
    }

    /**
     * @param pVcOutParamNameList
     *            The vcOutParamNameList to set.
     */
    public void setVcOutParamNameList(ArrayList<String> pVcOutParamNameList) {
        this.vcOutParamNameList = pVcOutParamNameList;
    }
}