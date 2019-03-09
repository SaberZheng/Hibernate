package ecut.query.entity;

import java.io.Serializable;

public class Student  implements Serializable {

	private static final long serialVersionUID = 399214402506858645L;
	
	private Integer id;
	private String name;
	
	// 反映当前的学生属于哪个班级
	private Clazz clazz ; /** 维护 从 学生( many ) 到 班级 ( one ) 的 多对一 关联 */

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

	public Clazz getClazz() {
		return clazz;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}

}
