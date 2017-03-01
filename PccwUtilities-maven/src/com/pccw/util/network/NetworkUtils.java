package com.pccw.util.network;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.TreeSet;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

public class NetworkUtils {
	
	private static Logger logger = Logger.getLogger(NetworkUtils.class); 
	
	public static String[] getLocalIp4Address() throws Exception {
		TreeSet<String> rtnSet = new TreeSet<String>();
	    String ip;
	    try {
	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	        NetworkInterface iface = null;
	        Enumeration<InetAddress> addresses = null;
	        InetAddress addr = null;
	        while (interfaces.hasMoreElements()) {
	            iface = interfaces.nextElement();
	            // filters out 127.0.0.1 and inactive interfaces
	            if (iface.isLoopback() || !iface.isUp()) {
	                continue;
	            }

	            addresses = iface.getInetAddresses();
	            while(addresses.hasMoreElements()) {
	                addr = addresses.nextElement();
	                if (!(addr instanceof Inet4Address)) {
	                	continue;
	                }
	                ip = addr.getHostAddress();
	                rtnSet.add(ip);
	                System.out.println(iface.getDisplayName() + " " + ip);
	            }
	        }
	    } catch (SocketException e) {
	    	logger.error(ExceptionUtils.getFullStackTrace(e));
	        throw new RuntimeException(e);
	    }
	    return rtnSet.toArray(new String[0]);
	}
}