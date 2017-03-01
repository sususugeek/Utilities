package com.pccw.util;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;

public class CommonUtil {

	private static Logger logger = Logger.getLogger(CommonUtil.class);
	
	public static Object cloneNestedSerializableObject(Object pObject) throws Exception {
		try {
	        FastByteArrayOutputStream fbos = new FastByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(fbos);
			out.writeObject(pObject);
			out.flush();
			out.close();

			ObjectInputStream in = new ObjectInputStream(fbos.getInputStream());
			return in.readObject();
		} catch (Exception e) {
    		logger.error(e);
    		throw e;
		}
	}

	public static URI rewriteURI(URI pSourceUri, String pHost) throws URISyntaxException {
		return rewriteURI(pSourceUri, pHost, pSourceUri.getPort());
	}
	
	public static URI rewriteURI(URI pSourceUri, String pHost, int pPort) throws URISyntaxException {
	    try {
			URI newUri = new URI(pSourceUri.getScheme(),
					pSourceUri.getUserInfo(), pHost, pPort,
					pSourceUri.getPath(), pSourceUri.getQuery(), pSourceUri.getFragment());
	        return newUri;
	    } catch (URISyntaxException e) {
	    	logger.error(e);
	        throw e;
	    }
	}

}
