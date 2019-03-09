package ecut.self.entity;

import java.io.Serializable;
import java.util.List;

public class Category implements Serializable {

	private static final long serialVersionUID = 4807135047518624567L;
	
	/** 对象标识符属性 ( 属性值就是 对象标识符 ( Object Identifier ) )*/
	private Integer id ;
	/** 分类的名称 */
	private String name ; 
	/** 将来在 界面 中显示时的位置*/
	private int position ;
	
	/** 先 把 当前的分类 对象 当作 一个 父分类来对待 ，那么它可能对应 多个 子分类 */
	private List<Category> categories ; // 维护 从 父分类( one ) 到 子分类 ( many ) 的 一对多 关联关系
	
	/** 再 把 当前分类对象 当作一个 子分类来对待，那么它可能有一个 父分类 */
	private Category parent ;  // 维护 从  子分类 ( many ) 到  父分类( one ) 的 多对一 关联关系
	
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

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}
	
}
