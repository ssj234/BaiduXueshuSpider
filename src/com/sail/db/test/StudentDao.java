package com.sail.db.test;

import java.util.List;

public interface StudentDao {

	/**
	 * ���ѧ����Ϣ
	 * 
	 * @param student
	 *            ѧ��ʵ��
	 * @return �����Ƿ���ӳɹ�
	 */
	public boolean addStudent(Student student);

	/**
	 * ���ѧ��idɾ��ѧ����Ϣ
	 *
	 * @param id
	 *            ѧ��id
	 * @return ɾ���Ƿ�ɹ�
	 */
	public boolean deleteStudentById(int id);

	/**
	 * ����ѧ����Ϣ
	 *
	 * @param student
	 *            ѧ��ʵ ��
	 * @return �����Ƿ�ɹ�
	 */
	public boolean updateStudent(Student student);

	/**
	 * ��ѯȫ��ѧ����Ϣ
	 *
	 * @return ����ѧ���б�
	 */
	public List<Student> selectAllStudent();

	/**
	 * ���ѧ������ģ���ѯѧ����Ϣ
	 *
	 * @param name
	 *            ѧ������
	 * @return ѧ����Ϣ�б�
	 */
	public List<Student> selectStudentByName(String name);

	/**
	 * ���ѧ��id��ѯѧ����Ϣ
	 *
	 * @param id
	 *            ѧ��id
	 * @return ѧ�����
	 */
	public Student selectStudentById(int id);
}
