package com.joup;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sail.db.step.one.Title;
import com.sail.db.step.one.TitleDao;

public class Taodake {
	public static void main(String[] args){
		
		Taodake tdk=new Taodake();
		tdk.parse("STRUCTURE OF ION EXCHANGE MEMBRANES");
	}
	/**
	 * 根据标题获取链接，更新数据库
	 * @param key
	 */
	public void parse(String name){
		String host = "182.18.19.218";  
        String port = "3128";  
        //setProxy(host, port);
		String url="http://xueshu.baidu.com/s?wd="+URLEncoder.encode(name);
		Document doc = null;;
			try {
				doc = Jsoup.connect(url).get();
				System.out.println(doc);
			} catch (IOException e) {
				System.out.println("发生异常"+e.getMessage());
				e.printStackTrace();
			}
			String link=null;
			Elements ids = doc.getElementsByAttribute("data-link");
			for(Element id:ids){
				if(id.attr("data-link")!=null){
					link=id.attr("data-link");
					if(link!=null&&link.trim().length()>0)
						link=URLDecoder.decode(link);
					break;
				}
			}
			
	
		
	}
	
	public static void setProxy(String host, String port) {
		System.setProperty("proxySet", "true");
		System.setProperty("proxyHost", host);
		System.setProperty("proxyPort", port);
	}
}
