package com.sail.db.step.one;

import java.io.IOException;
import java.io.Reader;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class TitleDao {

	private static SqlMapClient sqlMapClient = null;

	static {
		try {
			Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml");
			sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 新增title
	 * @param title
	 * @return
	 */
	public boolean addTitle(Title title) {
		Object object = null;
		boolean flag = false;
		try {
			object = sqlMapClient.insert("addTitle", title);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (object != null) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 查询所有title
	 * @return
	 */
	public List<Title> selectAll() {
		List<Title> titles = null;
		try {
			titles = sqlMapClient.queryForList("selectAll");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return titles;
	}
	
	/**
	 * 更新
	 * @param id
	 */
	public void update(Title title){
		try {
			sqlMapClient.update("updateTitle", title);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public long countTitle(int scope,int status){
		Map map=new HashMap();
		map.put("scope", scope);
		map.put("status", status);
		try {
			String count=(String) sqlMapClient.queryForObject("countTitle",map);
			return Long.parseLong(count);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	
	}
	
	public List<Title> getTitle(int scope,int status,int begin,int pageSize){
		Map map=new HashMap();
		map.put("scope",scope);
		map.put("status", status);
		map.put("begin", begin);
		map.put("pageSize",pageSize );
		List<Title> ret=null;
		try {
			ret=sqlMapClient.queryForList("getTitle", map);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public boolean addAbs(Abstract abs){
		Object object = null;
		boolean flag = false;
		try {
			object = sqlMapClient.insert("addAbs", abs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (object != null) {
			flag = true;
		}
		return flag;
	}
	
	public List<Abstract> getAbstract(Title title){
		Map map=new HashMap();
		map.put("titleid",title.getId());
		List<Abstract> ret=null;
		try {
			ret=sqlMapClient.queryForList("getAbstract", map);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Title title=new Title();
		title.setId(13);
		List id=new TitleDao().getAbstract(title);
		System.out.println(((Abstract)id.get(0)).getName());
//		Title title=new Title();
//		title.setLink("link11");
//		title.setName("name11");
//		title.setShowname("showname11");
//		title.setStatus(11);
//		title.setId(1);
//		new TitleDao().update(title);
		//System.out.println();
	}
}
