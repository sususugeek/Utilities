package com.pccw.util.cache; 

import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.pccw.util.CommonUtil;


public abstract class CacheManager { 
	private static Logger logger = Logger.getLogger(CacheManager.class);		
	private TreeMap <String,Object> cacheMap = new TreeMap<String,Object>();
        
	public abstract void buildCacheMap();
	
    public Object get(String pKey) {
    	Object retObj = null;
    	if (pKey == null) {
    		return null;
    	}
    	if (cacheMap.size() < 1) {
    		this.buildCacheMap();
    	}
    	Object obj = cacheMap.get(pKey);
    	try {
    		retObj = obj;
    		if (obj instanceof Cloneable) {
				retObj = obj.getClass().getMethod("clone", (Class[]) null)
						.invoke(obj, (Object[]) null);
			}
    	} catch (NoSuchMethodException e) {
    		try {
    			retObj = CommonUtil.cloneNestedSerializableObject(obj);
    		} catch (Exception ex) {
        		logger.error(e);
        		retObj = obj;
    		}
    	} catch (Exception e) {
    		logger.error(e);
    		retObj = obj;
    	}
		return retObj;
    }
    
    public void put(String pKey, Object pValue) {
    	synchronized (cacheMap) {
    		cacheMap.put(pKey,pValue);
		}
    }

    public boolean containsKey(String pKey) {
    	return cacheMap.containsKey(pKey);
    }
    
    public void clear() {
        synchronized (this.cacheMap) {
        	this.cacheMap.clear();
        }
    }
}