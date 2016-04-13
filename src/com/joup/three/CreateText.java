package com.joup.three;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.joup.client.ProcessM;
import com.joup.one.GetLink;
import com.joup.pre.ReadTitle;
import com.sail.db.step.one.Abstract;
import com.sail.db.step.one.Title;
import com.sail.db.step.one.TitleDao;

public class CreateText {
	Logger log=Logger.getLogger(CreateText.class);
	TitleDao dao=new TitleDao();
	BufferedWriter bw;
	int count=0;//abstract的页数
	int status=6;
	String line="\r\n";
	String expath="c:\\rs.txt";
	String namePrev=null;//记录上一页第一个名字
	private int per=300;//每次读取300条，查找link
	public static void main(String[] args){
		CreateText tdk=new CreateText();
		tdk.beginParse(1,null);
	}
	
	public void beginParse(int scope,ProcessM process){
		File file=new File(expath);
		FileOutputStream fos = null;
		try {
			fos=new FileOutputStream(file);
			bw=new BufferedWriter(new OutputStreamWriter(fos, "utf8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		TitleDao dao=new TitleDao();
		long count=dao.countTitle(scope, status);
		int begin=0;
		int tmp=0;
		while(tmp<count){
			List<Title> titles=dao.getTitle(scope, status, begin, per);
			for(Title title:titles){
				log.info("开始处理:"+title.getId()+">"+title.getName());
				if(process!=null)process.show("开始处理:"+title.getId()+">"+title.getName());
				count=0;
				getAbstract(title);
//				break;
			} 
//			break;
			tmp=tmp+titles.size();
		}
		if(process!=null)process.show("导出参考文献结束!!!");
		
		try {
			bw.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getAbstract(Title title){
		StringBuffer sb=new StringBuffer("PT J");
		sb.append(line);
		sb.append("TI ");
		sb.append(title.getName());
		sb.append(line);
		List rs=new TitleDao().getAbstract(title);
		for(int i=0;i<rs.size();i++){
			if(i==0)
				sb.append("CR ");
			else
				sb.append("   ");
			Abstract abs=(Abstract)rs.get(i);
			sb.append(abs.getName());
			sb.append(line);
		}
		sb.append("ER");
		sb.append(line);
		try {
			bw.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
