/**
 * All rights Reserved, Designed By solarstem 
 * @Description: 	TODO(用一句话描述该文件做什么) 
 * @author:	Liuchengran  
 * @date:	2016-3-9 上午10:54:56  
 */
package com.base.frame.net.retrofit;


import com.base.frame.net.HttpURL;
import com.base.frame.net.retrofit.converter.gson.GsonConverterFactory;

import retrofit2.Retrofit;

public class RetrofitHelper {
	private static RetrofitHelper self = null;
	private static Retrofit retrofit = null;
	
	private String sessionId = null;
	
	
	public static RetrofitHelper instance() {
		if (self == null) {
			self = new RetrofitHelper();
		}
		return self;
	}
	
	public Retrofit getRetrofit() {
		if (retrofit == null) {
			retrofit = new Retrofit.Builder()
		    .baseUrl(HttpURL.getBaseUri())
		    .addConverterFactory(GsonConverterFactory.create())
		    .build();
		}
		
		return retrofit;
	}
	
	public void setSession(String session) {
		sessionId = session;
	}
	
	public String getSession() {
		return sessionId;
	}

	
}
