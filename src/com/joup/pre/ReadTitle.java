package com.joup.pre;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.joup.client.ProcessM;
import com.sail.db.step.one.Title;
import com.sail.db.step.one.TitleDao;

public class ReadTitle {
	
	//Log log=LogFactory.getLog(ReadTitle.class);
	Logger log=Logger.getLogger(ReadTitle.class);
	boolean quaot=true;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ReadTitle rt=new ReadTitle();
		rt.read("d:\\papers.txt",1,null);
	}
	
	public void read(String pathname,int scope,ProcessM process) {
		log.info("开始解析文件:"+pathname);
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(new File(pathname));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("未找到文件", e);
		}
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(fs,"utf8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error("InputStreamReader", e1);
		}
		BufferedReader read=new BufferedReader(isr);
		LineNumberReader reader=new LineNumberReader(read);
		String line = null;
		try {
			line = reader.readLine();
			//System.out.println(222+line);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("文件读取失败", e);
		}
		while(line!=null){
			if(line.trim().length()==0){
				try {
					line=reader.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.error("文件读取失败", e);
				}
				continue;
			}
			
			log.info("文件解析，行:"+line);
			
			if(quaot){
				int begin=line.indexOf("\"");
				int end=line.indexOf("\"",begin+1);
				if(begin>-1&&end>-1)
				line=line.substring(begin+1,end);
			}
			if(process!=null)process.show(line);
			//插入数据库
			Title title=new Title();
			title.setName(line.trim());
			title.setScope(scope);
			new TitleDao().addTitle(title);
			//下一行
			try {
				line=reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("文件读取失败", e);
			}
		}
		log.info("文件解析结束："+pathname);
		try {
			reader.close();
			read.close();
			isr.close();
		} catch (IOException e) {
			e.printStackTrace();
			log.error("关闭读写流失败", e);
		}
		if(process!=null)process.show("导入完成!!!");
	}

}
