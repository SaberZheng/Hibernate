package ecut.onetomany.entity;

import java.io.Serializable;
import java.util.Set;

public class Clazz implements Serializable {
	
	private static final long serialVersionUID = 235617966763001653L;
	
	private Integer id;
	private String name;
	
	// 当前的班级都有那些学生
	private Set<Student> students; // 维护从 班级( one ) 到 学生( many ) 的 一对多 关联关系
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

}
