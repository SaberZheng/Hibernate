package ecut.cache.entity;

import java.io.Serializable;

public class Student  implements Serializable {

	private static final long serialVersionUID = 399214402506858645L;
	
	private Integer id;
	private String name;
	
	public Student() {
		super();
	}
	
	public Student(String name) {
		super();
		this.name = name;
	}

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

}
