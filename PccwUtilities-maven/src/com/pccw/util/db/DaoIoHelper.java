package com.pccw.util.db;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.pccw.util.db.DaoHelper.DaoProperty;

public class DaoIoHelper {
	private static final Logger logger = Logger.getLogger(DaoIoHelper.class);

	private TreeMap<String, DaoPair> daoMap = new TreeMap<String, DaoPair>();

	public DaoIoHelper(DaoBaseImpl[] pDaos) {
		if (pDaos == null) {
			return;
		}

		String key = null;
		for (DaoBaseImpl dao : pDaos) {
			key = getPrimaryKeyStringNoIo(dao);
			DaoPair daoPair = (DaoPair) this.daoMap.get(key);
			if (daoPair == null) {
				daoPair = new DaoPair();
				this.daoMap.put(key, daoPair);
			}
			daoPair.addDao(dao);
		}
	}

	@SuppressWarnings("rawtypes")
	public Object[] getPrimaryDaos(Class pClass) {
		ArrayList<Object> rtnList = new ArrayList<Object>();
		for (DaoPair daoPair : this.daoMap.values()) {
			rtnList.add(daoPair.getPrimaryDao());
		}
		return rtnList.toArray((Object[]) Array.newInstance(pClass, rtnList.size()));
	}
	
	public DaoBase getProfileDao(DaoBaseImpl pDao) {
		if (pDao == null) {
			return null;
		}
		String key = getPrimaryKeyStringNoIo(pDao);
		DaoPair daoPair = (DaoPair) this.daoMap.get(key);
		if (daoPair == null) {
			return null;
		}
		return daoPair.getProfileDao();
	}
	
	public static String getDaoIoIndString(DaoBaseImpl pDao) {
		if (pDao.getPrimaryKeyFields() == null || pDao.getPrimaryKeyFields().length <= 0) {
			return null;
		}
        Map<String, DaoProperty> daoPropertyMap = pDao.getPropertyMap();
        for (int i = 0; i < pDao.getPrimaryKeyFields().length; i++) {
            String propertyName = pDao.getPrimaryKeyFields()[i];
        	if (!StringUtils.equalsIgnoreCase("IOIND", propertyName)) {
        		continue;
        	}
            DaoProperty daoProperty = (DaoProperty) daoPropertyMap.get(propertyName);

            if (daoProperty == null) {
                logger.warn(propertyName + "NOT FOUND in " + pDao.getClass().getName());
                continue;
            }
            return daoProperty.getPropertyValue(pDao);
        }
        return null;
	}
	
	public static String getPrimaryKeyStringNoIo(DaoBaseImpl pDao) {
		StringBuffer sb = new StringBuffer();
		if (pDao.getPrimaryKeyFields() != null && pDao.getPrimaryKeyFields().length > 0) {
            Map<String, DaoProperty> daoPropertyMap = pDao.getPropertyMap();
            String propertyName = null;
            DaoProperty daoProperty = null;
            for (int i = 0; i < pDao.getPrimaryKeyFields().length; i++) {
                propertyName = pDao.getPrimaryKeyFields()[i];
            	if (StringUtils.equalsIgnoreCase("IOIND", propertyName)) {
            		continue;
            	}
            		
                daoProperty = (DaoProperty) daoPropertyMap.get(propertyName);

                if (daoProperty == null) {
                    logger.warn(propertyName + "NOT FOUND in " + pDao.getClass().getName());
                    continue;
                }
                if (i > 0) {
                	sb.append("^");
                }
                sb.append(daoProperty.getPropertyValue(pDao));
            }
		}
        return sb.toString();
	}
	
	public static class DaoPair {
		DaoBaseImpl primaryDao;
		DaoBase profileDao;

		public void addDao(DaoBaseImpl pDao) {
			String ioIndValue = getDaoIoIndString(pDao);
			if (StringUtils.equalsIgnoreCase("I", ioIndValue) || StringUtils.equalsIgnoreCase(" ", ioIndValue)) {
				if (this.primaryDao != null
						&& StringUtils.equalsIgnoreCase("O",
								getDaoIoIndString(primaryDao))) {
					this.profileDao = this.primaryDao;
				}
				this.primaryDao = pDao;
			} else if (this.primaryDao == null) {
				this.primaryDao = pDao;
			} else {
				this.profileDao = pDao;
			}
		}
		
		/**
		 * @return the primaryDao
		 */
		public DaoBase getPrimaryDao() {
			return this.primaryDao;
		}
		
		/**
		 * @param pPrimaryDao the primaryDao to set
		 */
		public void setPrimaryDao(DaoBaseImpl pPrimaryDao) {
			this.primaryDao = pPrimaryDao;
		}
		
		/**
		 * @return the profileDao
		 */
		public DaoBase getProfileDao() {
			return this.profileDao;
		}
		
		/**
		 * @param pProfileDao the profileDao to set
		 */
		public void setProfileDao(DaoBase pProfileDao) {
			this.profileDao = pProfileDao;
		}
		
		
	}
}
