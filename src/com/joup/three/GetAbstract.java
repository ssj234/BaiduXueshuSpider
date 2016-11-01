package com.joup.three;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public class GetAbstract {
	Logger log=Logger.getLogger(GetAbstract.class);
	TitleDao dao=new TitleDao();
	int count=0;//abstract的页数
	int status=4;
	String namePrev=null;//记录上一页第一个名字
	private int per=6000;//每次读取300条，查找link
	public static void main(String[] args){
		GetAbstract tdk=new GetAbstract();
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
				count=0;
				getAbstract(title);
//				break;
			} 
//			break;
			tmp=tmp+titles.size();
			begin=begin+titles.size(); 
		}
		if(process!=null)process.show("获取参考文献结束!!!");
	}
	
	public void getAbstract(Title title){
		int page=1;
		getAbstract(title,page++);//page=1 set count
		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>"+count);
		while(page<=count){
//			System.out.println("page>>"+page+" "+namePrev);
			Boolean flag=getAbstract(title,page++);
			if(flag==null)
				break;
		}
		title.setStatus(6);
		dao.update(title);
	}
	
	public Boolean getAbstract(Title title,int page){
		URL url = null;
		int size = 0;
		byte bytes[]=new byte[1024*5];
		String showname=title.getShowname();
		if(showname==null||showname.trim().length()==0)
			return false;
		try {
			url = new URL("http://xueshu.baidu.com/usercenter/data/schpaper?callback=hello&wd=citepaperuri%3A("+URLEncoder.encode(showname)+")&type=reference&rn=10&page_no="+page);
		} catch (MalformedURLException e) {
			log.error(title.getId()+"获取URL失败", e);
			return false;
		}
		InputStream io;
		try {
			io = url.openStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(title.getId()+"openStream失败", e);
			return false;
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
			return false;
		}
		String str=sb.toString();
		str=str.trim();
		str=str.substring(6,str.length()-1);
		//log.info(title.getId()+"成功获取返回:"+str);
		JSONObject obj=null;
		try{
			obj=(JSONObject) JSON.parse(str);
		}catch(JSONException e){
			log.info(title.getId()+"解析json失败，obj为null");
			return false;
		}
		if(obj==null){
			log.info(title.getId()+"解析json失败，obj为null");
			return false;
		}
		obj=(JSONObject) obj.get("data");
		if(obj==null){
			log.info(title.getId()+"获取data失败");
			return false;
		}
		if(page==1){
			String cnt=obj.getString("totalPageNum");
			count=Integer.valueOf(cnt);
			log.info(title.getId()+"摘要页数:"+count);
		}
		//
		JSONArray array=(JSONArray) obj.get("resultList");
		if(array==null||array.size()==0){
			log.info(title.getId()+"获取resultList失败");
			return false;
		}
		
		for(int i=0;i<array.size();i++){
			JSONObject absObj=(JSONObject) array.get(i);
			JSONObject info=getJSONObject(absObj, "meta_di_info");
			if(info!=null){
				Abstract abs=new Abstract();
				abs.setTitleId(title.getId());
				String name=getJSONString(info, "sc_title");
				if(name!=null){
					if(i==0&&name.equals(namePrev))//若第1个题目与上个相同，设置prev
						return null;
					abs.setName(name);
				}
				
				String pdf=getJSONString(info, "sc_pdf_read");
				if(pdf!=null)
					abs.setPdf(pdf);
				
				String cited=getJSONString(info, "sc_cited");
				if(cited!=null)
					abs.setCited(cited);
				
				JSONArray years=(JSONArray) info.get("sc_year");
				if(years!=null&&years.size()>0){
					//log.info(title.getId()+"获取sc_year失败");
					//update(title, null);
					//return null;
					abs.setYear(years.getString(0));
				}
				
				JSONArray authors=(JSONArray) info.get("sc_author");
				if(authors!=null&&authors.size()>0){
					//log.info(title.getId()+"获取sc_year失败");
					//update(title, null);
					//return null;
					JSONObject names=authors.getJSONObject(0);
					JSONArray aname=getJSONArray(names,"sc_name");
					if(aname==null){
						
					}else{
						try{
							if(aname.getString(1)!=null)
								abs.setAuthor(aname.getString(1));
							else
								abs.setAuthor(aname.getString(0));
						}catch(Exception e){
							abs.setAuthor(aname.getString(0));
						}
					}
					
				}
				
				JSONArray publishs=(JSONArray) info.get("sc_publish");
				if(publishs!=null&&publishs.size()>0){
					
					JSONObject publish=publishs.getJSONObject(0);
					JSONArray aname=getJSONArray(publish,"sc_journal");
					if(aname==null){
						abs.setPublish("");
					}else{
						try{
							 if(aname.getString(1)!=null)
									abs.setPublish(aname.getString(1));
								else
									abs.setPublish(aname.getString(0));
						}catch(Exception e){
							abs.setPublish(aname.getString(0));
						}
					}
				}else{
					abs.setPublish("");
				}
				
				log.info(abs);
				addAbs(abs);//插入数据库
				if(i==0)
					namePrev=name;
			}
			
		}
		
		//更新数据库
		//addAbs(title, sign);
		return true;
	}
	
	public String getJSONString(JSONObject obj,String key){
		String rs=null;
		try{
			rs=obj.getString(key);
		}catch(Exception e){
			log.error("获取"+key+"失败",e);
		}
		return rs;		
	}
	public JSONObject getJSONObject(JSONObject obj,String key){
		JSONObject rs = null;
		try{
			rs=(JSONObject) obj.get(key);
		}catch(Exception e){
			log.error("获取"+key+"失败",e);
		}
		return rs;
	}
	public JSONArray getJSONArray(JSONObject obj,String key){
		JSONArray rs = null;
		try{
			rs= obj.getJSONArray(key);
		}catch(Exception e){
			log.error("获取"+key+"失败",e);
		}
		return rs;
	}
	public void addAbs(Abstract abs){
		dao.addAbs(abs);
	}
}
