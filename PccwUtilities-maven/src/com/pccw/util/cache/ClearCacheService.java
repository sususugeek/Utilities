/*
 * Created on Mar 2, 2012
 *
 * @author Alfredo P. Ricafort
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.pccw.util.cache;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import com.pccw.util.spring.SpringApplicationContext;

public class ClearCacheService {

	private static Logger logger = Logger.getLogger(ClearCacheService.class);
	
	private List <CacheManager> cacheManagerList;
		
	public int clearCache() throws Exception {
		try {
        	for (CacheManager cacheManager : cacheManagerList) {        		
				cacheManager.clear();
			}
        	return cacheManagerList.size();
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}		
	}

	public int clearCache(String [] pBeanIds) throws Exception {
		int clearCnt = 0;
		try {
			for (int i = 0; pBeanIds != null && i < pBeanIds.length; i++) {
				try {
					Object cacheManager = SpringApplicationContext
							.getBean(pBeanIds[i]);
					
					if (cacheManager instanceof CacheManager) {
						((CacheManager) cacheManager).clear();
						clearCnt++;
					}
				} catch (NoSuchBeanDefinitionException e) {
					logger.error(e);
				}
			}
			return clearCnt;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}		
	}

	public List<CacheManager> getCacheManagerList() {
		return cacheManagerList;
	}

	public void setCacheManagerList(List<CacheManager> cacheManagerList) {
		this.cacheManagerList = cacheManagerList;
	}
	
}
