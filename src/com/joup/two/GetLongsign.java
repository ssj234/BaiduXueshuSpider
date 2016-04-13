package com.joup.two;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.joup.SetProxy;
import com.joup.client.ProcessM;
import com.joup.one.GetLink;
import com.joup.pre.ReadTitle;
import com.sail.db.step.one.Title;
import com.sail.db.step.one.TitleDao;

public class GetLongsign {
	Logger log=Logger.getLogger(GetLongsign.class);
	TitleDao dao=new TitleDao();
	int status=2;//2=获取link成功
	private int per=300;//每次读取300条，查找link
	public static void main(String[] args){
		GetLongsign tdk=new GetLongsign();
		tdk.beginParse(1,null);
	}
	
	public void beginParse(int scope,ProcessM process){
		TitleDao dao=new TitleDao();
		long count=dao.countTitle(scope, status);
		int begin=0;
		int tmp=0;
		while(tmp<count){
			List<Title> titles=dao.getTitle(scope, status, begin, per);
			for(Title title:titles){
				log.info("开始处理:"+title.getId()+">"+title.getName());
				if(process!=null)process.show("开始处理:"+title.getId()+">"+title.getName());
				getLongsign(title);
			}
			tmp=tmp+per;
		}
		if(process!=null)process.show("获取Sign结束!!!");
	}
	
	public String getLongsign(Title title){
		URL url = null;
		int size = 0;
		byte bytes[]=new byte[1024*5];
		//SetProxy.setProxy();
		try {
			url = new URL("http://xueshu.baidu.com/usercenter/data/schinfo?url="+URLEncoder.encode(title.getLink())+"&callback=hello&sign=391048a4c63a41e965a77144e909a75c");
		} catch (MalformedURLException e) {
			log.error(title.getId()+"获取URL失败", e);
			update(title, null);
			return null;
		}
		InputStream io;
		try {
			io = url.openStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(title.getId()+"openStream失败", e);
			update(title, null);
			return null;
		}
		BufferedInputStream is=new BufferedInputStream(io);
		StringBuffer sb=new StringBuffer();
		try {
			while(size!=-1){
				size = is.read(bytes);
				if(size!=-1)
					sb.append(new String(bytes,0,size));
			}
		} catch (IOException e) {
			log.error(title.getId()+"读取失败", e);
			update(title, null);
			return null;
		}
		String str=sb.toString();
		str=str.trim();
		str=str.substring(6,str.length()-1);
		log.info(title.getId()+"成功获取返回:"+str);
		JSONObject obj=null;
		try{
			obj=(JSONObject) JSON.parse(str);
		}catch(JSONException e){
			log.info(title.getId()+"解析json失败，obj为null");
			update(title, null);
			return null;
		}
		if(obj==null){
			log.info(title.getId()+"解析json失败，obj为null");
			update(title, null);
			return null;
		}
		obj=(JSONObject) obj.get("meta_di_info");
		if(obj==null){
			log.info(title.getId()+"获取meta_di_info失败");
			update(title, null);
			return null;
		}
		JSONArray array=(JSONArray) obj.get("sc_longsign");
		if(array==null||array.size()==0){
			log.info(title.getId()+"获取sc_longsign失败");
			update(title, null);
			return null;
		}
		String sign=array.getString(0);
		log.info(title.getId()+"成功获取Longsign:"+sign);
		
		//更新数据库
		update(title, sign);
		return sign;
	}
	
	public void update(Title title,String sign){
		if(sign!=null){
			title.setShowname(sign);
			title.setStatus(4);
		}else{
			title.setStatus(3);
		}
		dao.update(title);
	}
}
