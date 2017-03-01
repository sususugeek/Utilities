/*
 * Created on Nov 22, 2009
 *
 * @author Alfredo P. Ricafort
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.pccw.util.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class FIFOCacheMap <K,V> extends LinkedHashMap <K,V> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9215489566579534755L;
	
	private final int maxEntries;

	public  FIFOCacheMap(int pMaxEntries) {
		super(pMaxEntries + 1, 1.1f, true);
		this.maxEntries = pMaxEntries;
	}

	protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
		return size() > maxEntries;
	}
}
