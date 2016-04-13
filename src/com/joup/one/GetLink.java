package com.joup.one;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.joup.client.ProcessM;
import com.joup.pre.ReadTitle;
import com.sail.db.step.one.Title;
import com.sail.db.step.one.TitleDao;

public class GetLink {
	
	Logger log=Logger.getLogger(GetLink.class);
	private int per=300;//每次读取300条，查找link
	int status=0;
	public static void main(String[] args){
		
		GetLink tdk=new GetLink();
		tdk.beginParse(1,null);
	}
	
	public void beginParse(int scope,ProcessM process){
		//1.查找需要更新的条目，由于比较多，按照多次处理
		//2.按照分页情况获取300条，
		//3.每条，执行parse，
		TitleDao dao=new TitleDao();
		long count=dao.countTitle(scope, status);
		System.out.println(count);
		int begin=0;
		int tmp=0;
		while(tmp<count){
			List<Title> titles=dao.getTitle(scope, status, begin, per);
			for(Title title:titles){
				//System.out.println(title.getName());
				log.info("开始处理:"+title.getId()+">"+title.getName());
				if(process!=null)process.show("开始处理:"+title.getId()+">"+title.getName());
				parse(title);
			}
			tmp=tmp+per;//
		}
		if(process!=null)process.show("获取Link结束!!!");
	}
	/**
	 * 根据标题获取链接，更新数据库
	 * @param key
	 */
	public void parse(Title title){
		
      //SetProxy.setProxy();
		String url="http://xueshu.baidu.com/s?wd="+URLEncoder.encode(title.getName());
		Document doc = null;;
			try {
				doc = Jsoup.connect(url).get();
				//System.out.println(doc);
			} catch (IOException e) {
				log.error(title.getId()+"获取link时发生异常：",e);
			}
			
			String link=null;
			if(doc!=null){
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
			if(link!=null){
				title.setLink(link);
				title.setStatus(2);//获取url成功
				new TitleDao().update(title);
				log.info(title.getId()+">"+link);
			}else{
				title.setLink("");
				title.setStatus(1);//获取url失败
				new TitleDao().update(title);
				log.info(title.getId()+">未找到link");
			}
	
		
	}
	
}
