package com.sail.db.test;

import java.sql.Date;


public class IBatisTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		StudentDaoImpl dao=new StudentDaoImpl();
		Student addStudent = new Student();
		addStudent.setName("test1");
		addStudent.setBirth(Date.valueOf("2011-09-02"));
		addStudent.setScore(88);
//		dao.addStudent(addStudent);
		dao.addStudent(Date.valueOf("2014-09-02"),"test00",199.2f);
//		dao.deleteStudentById(53);
		
//		addStudent.setId(54);
//		dao.updateStudent(addStudent);
		
	}

}
