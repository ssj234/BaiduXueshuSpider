package com.sail.db.test;

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

public class StudentDaoImpl implements StudentDao {

	private static SqlMapClient sqlMapClient = null;

	// ��ȡ�����ļ�
	static {
		try {
			Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml");
			sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean addStudent(Student student) {
		Object object = null;
		boolean flag = false;
		try {
			object = sqlMapClient.insert("addStudent", student);
			System.out.println("���ѧ����Ϣ�ķ���ֵ��" + object);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (object != null) {
			flag = true;
		}
		return flag;
	}

	public boolean addStudent(Date date, String name, float socre) {
		Object object = null;
		boolean flag = false;
		try {
			Map map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("birth", date);
			map.put("score", socre);
			object = sqlMapClient.insert("addStudent2", map);
			System.out.println("���ѧ����Ϣ�ķ���ֵ��" + object);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (object != null) {
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean deleteStudentById(int id) {
		boolean flag = false;
		Object object = null;
		try {
			object = sqlMapClient.delete("deleteStudentById", id);
			System.out.println("ɾ��ѧ����Ϣ�ķ���ֵ��" + object + "�����ﷵ�ص���Ӱ�������");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (object != null) {
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean updateStudent(Student student) {
		boolean flag = false;
		Object object = false;
		try {
			object = sqlMapClient.update("updateStudent", student);
			System.out.println("����ѧ����Ϣ�ķ���ֵ��" + object + "������Ӱ�������");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (object != null) {
			flag = true;
		}
		return flag;
	}

	@Override
	public List<Student> selectAllStudent() {
		List<Student> students = null;
		try {
			students = sqlMapClient.queryForList("selectAllStudent");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return students;
	}

	@Override
	public List<Student> selectStudentByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Student selectStudentById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
