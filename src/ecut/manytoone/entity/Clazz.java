package ecut.manytoone.entity;

import java.io.Serializable;

public class Clazz implements Serializable {
	
	private static final long serialVersionUID = 235617966763001653L;
	
	private Integer id;
	private String name;

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
