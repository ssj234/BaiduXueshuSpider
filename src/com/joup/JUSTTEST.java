package com.joup;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JUSTTEST {
	private static String url="http://xueshu.baidu.com/usercenter/data/schinfo?url=http%3A%2F%2Fwww.cqvip.com%2FMain%2FDetail.aspx%3Fid%3D2477433&callback=hello&sign=391048a4c63a41e965a77144e909a75c";
	private static int start_page = 0;
	private static int last_page = 10;
	public static void main(String[] args) throws Exception{
//		Document doc = null;;
//		for(int i=start_page;i<1;i++){
//			try {
//				doc = Jsoup.connect(url).post();
//			} catch (IOException e) {
//				System.out.println("HEHEDA");
//				e.printStackTrace();
//			}
//			System.out.println(doc);
//		}
		URL url=new URL("http://xueshu.baidu.com/usercenter/data/schinfo?url=http%3A%2F%2Fwww.cqvip.com%2FMain%2FDetail.aspx%3Fid%3D2477433&callback=hello&sign=42408390ebceb37dc804edec00351aa8");
//		URLConnection conn=url.openConnection();
//		conn.connect();
//		System.out.println();
		InputStream io=url.openStream();
		BufferedInputStream is=new BufferedInputStream(io);
		byte bytes[]=new byte[1024*8];
		int size=is.read(bytes);
		System.out.println(size);
		String str=new String(bytes);
		str=str.trim();
		str=str.substring(6,str.length()-1);
		System.out.println(str);
		JSONObject obj=(JSONObject) JSON.parse(str);
		obj=(JSONObject) obj.get("meta_di_info");
		JSONArray array=(JSONArray) obj.get("sc_longsign");
		System.out.println(array.size());
	//	System.out.println(JSEscape.unescape("\u751f\u6001\u7cfb\u7edf\u7a33\u5b9a\u6027\u7814\u7a76"));
//		try
//		  {
//		   URL url=new URL("http://xueshu.baidu.com/usercenter/data/schinfo?url=http%3A%2F%2Fwww.cqvip.com%2FMain%2FDetail.aspx%3Fid%3D2477433&callback=hello&sign=391048a4c63a41e965a77144e909a75c");
//		   InputStreamReader isr=new InputStreamReader(url.openStream());
//		   BufferedReader br=new BufferedReader(isr);
//		  
//		   String str;
//		   while((str=br.readLine())!=null)
//		   {
//		    System.out.println(str);
//		   }
//		  
//		   br.close();
//		   isr.close();
//		   }
//		   catch(Exception e)
//		   {
//		    System.out.println(e);
//		   }
	}
}
