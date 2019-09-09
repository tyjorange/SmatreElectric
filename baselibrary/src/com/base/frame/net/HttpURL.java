/**
 * All rights Reserved, Designed By solarstem 
 * @Description: 	TODO(用一句话描述该文件做什么) 
 * @author:	Liuchengran  
 * @date:	2016-4-27 上午11:21:37  
 */
package com.base.frame.net;

public class HttpURL {
	//public static String SERVER_URL_HEAD = "http://114.55.90.161";
	// public static String SERVER_URL_HEAD = "http://192.168.2.217";
	public static String SERVER_URL_HEAD = "http://116.62.38.203";
	//public static String SERVER_URL_HEAD = "http://192.168.2.114";

	public static String SERVER_PORT = "80";

	public static String getBaseUri() {
		return SERVER_URL_HEAD + ":" + SERVER_PORT + "/";
	}
	
}
