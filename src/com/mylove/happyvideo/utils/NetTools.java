package com.mylove.happyvideo.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2015/12/4 0004.
 */
public class NetTools {
    public static boolean isConnect(Context context) {
        ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cwjManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }
    
    
    public static String get(String url){  
        StringBuffer sb = new StringBuffer();  
        String line = null;  
        BufferedReader buffer = null;  
        try {							
        	InputStream is = getInputStreamFromURL(url);
        	if(is != null){
	            buffer = new BufferedReader(new InputStreamReader(is, "UTF-8"), 100000);  
	            while( (line = buffer.readLine()) != null){  
	                sb.append(line);  
	            }  
        	}
        }   
        catch (Exception e) { 
        	
        }
        return sb.toString();  
    }  
    
    public static InputStream getInputStreamFromURL(String urlStr) {  
    	HttpURLConnection urlConn = null;  
    	InputStream inputStream = null;  
    	try {  
    		URL url = new URL(urlStr);  
    		
    		urlConn = (HttpURLConnection)url.openConnection();
    		urlConn.setConnectTimeout(5000);
    		urlConn.setReadTimeout(5000);
    		if(urlConn.getResponseCode()==200){
        		inputStream = urlConn.getInputStream();  
    		}
    	} catch (Exception e) {
    		
    	}  
    	 
    	return inputStream;  
    }
}
