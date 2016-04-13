package com.joup;

import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sail.db.step.one.Title;
import com.sail.db.step.one.TitleDao;

public class TaoThread extends Thread{
	private Document doc;

	public TaoThread() {
	}
	public TaoThread(Document doc) {
		this.doc = doc;
	}
	@Override
	public void run() {
		Elements ids = doc.getElementsByAttribute("data-link");
		for(Element id:ids){
			//System.out.println("ddd>"+URLDecoder.decode(id.attr("data-link")));
			if(id.attr("data-link")!=null){
//				Title title=new Title();
//				title.setLink(URLDecoder.decode(id.attr("data-link"));
//				title.setName();
//				title.setShownam();
//				title.setStatus(1);
//				boolean flag=new TitleDao().addTitle(title);
//				System.out.println(flag);
			}
		}
	}
}
