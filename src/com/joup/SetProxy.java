package com.joup;

public class SetProxy {
	public static boolean useProxy=true;
	public static void setProxy() {
		if(useProxy){
			String host = "182.18.19.218";  
	        String port = "3128";
			System.setProperty("proxySet", "true");
			System.setProperty("proxyHost", host);
			System.setProperty("proxyPort", port);
		}
	}
}
