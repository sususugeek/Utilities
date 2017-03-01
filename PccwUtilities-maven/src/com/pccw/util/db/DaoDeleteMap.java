package com.pccw.util.db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

public class DaoDeleteMap extends TreeMap<String, DaoBaseImpl> {
	
    static final long serialVersionUID = -9059341468538338386L;
  
    protected final Log logger = LogFactory.getLog(getClass());
	
	public static DaoDeleteMap createDaoDeleteMap(DaoBase pDao,
			ArrayList<String> pConditionFieldList, String pAdditionWhere)
			throws Exception {

		return createDaoDeleteMap(pDao, pConditionFieldList, true,
				pAdditionWhere);
	}
	
	public static DaoDeleteMap createDaoDeleteMap(DaoBase pDao,
			ArrayList<String> pConditionFieldList, boolean pPrimaryKeyOnly,
			String pAdditionWhere) throws Exception {

		DaoDeleteMap deleteMap = new DaoDeleteMap();
		DaoBaseImpl[] daos = (DaoBaseImpl[]) pDao.doSelect(pConditionFieldList, pPrimaryKeyOnly, pAdditionWhere);
		if (daos == null) {
			return deleteMap;
		}
	    for (int i = 0; i < daos.length; i++) {
	    	deleteMap.put(daos[i]);
	    }
	    return deleteMap;
	}
	
	public static DaoDeleteMap createDaoDeleteMap(DaoBase pDao,
			ArrayList<String> pConditionFieldList,
			ArrayList<String> pSelectFieldList, String pAdditionWhere)
			throws Exception {

		DaoDeleteMap deleteMap = new DaoDeleteMap();
		DaoBaseImpl[] daos = (DaoBaseImpl[]) pDao.doSelect(pConditionFieldList,
				pSelectFieldList, pAdditionWhere);
		if (daos == null) {
			return deleteMap;
		}
	    for (int i = 0; i < daos.length; i++) {
	    	deleteMap.put(daos[i]);
	    }
	    return deleteMap;
	}
	
	public DaoBaseImpl get(Object pKey) {
		if (pKey instanceof DaoBaseImpl) {
			return super.get(((DaoBaseImpl) pKey).getPrimaryKeyString());
		}
		return super.get(pKey);
	}
	
	public Object put(DaoBaseImpl pDao) {
		return super.put(pDao.getPrimaryKeyString(), pDao);
	}
	
	public DaoBaseImpl remove(Object pKey) {
		if (pKey instanceof DaoBaseImpl) {
			return super.remove(((DaoBaseImpl) pKey).getPrimaryKeyString());
		}
		return super.remove(pKey);
	}
	
	public boolean containsKey(Object pSearchKey) {
		if (pSearchKey instanceof DaoBaseImpl) {
			return super.containsKey(((DaoBaseImpl) pSearchKey).getPrimaryKeyString());
		}
		return super.containsKey(pSearchKey);
	}
	
	public boolean doDelete(Connection pConnection) throws Exception {
        try {
        	for (DaoBaseImpl dao : this.values()) {
                if( ! dao.doDelete()) {
                    throw new Exception( dao.getClass().getName() + " - "
							+ dao.getPrimaryKeyString() + " - "
							+ dao.getErrCode() + " - "
							+ dao.getErrMsg());
                }
            }
            return true;
        } catch (Exception ex) {
            logger.error("doDelete() - Error: " + ExceptionUtils.getFullStackTrace(ex));
            throw new Exception("DaoDeleteMap.doDelete() - "+ ex.toString());
        }
	}
	
	public void printKeysLoggerInfo(Logger pLogger, String pAdditionalInfo) {
		StringBuilder sb = new StringBuilder("DaoDeleteMap - ");
		sb.append(this.hashCode());
		sb.append(" - size()=");
		sb.append(this.size());
		if (this.size() > 0) {
			sb.append(" - contains()");
			sb.append(this.get(this.firstKey()).getClass().getName());
			sb.append(" - Keys:");
        	for (String key : this.keySet()) {
        		sb.append(key);
        		sb.append(";");
        	}
		}
		if (StringUtils.isNotBlank(pAdditionalInfo)) {
			sb.append(pAdditionalInfo);
		}
		if (pLogger != null) {
			pLogger.info(sb.toString());
		} else {
			logger.info(sb.toString());
		}
	}
}