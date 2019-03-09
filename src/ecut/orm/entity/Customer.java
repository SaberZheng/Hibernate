package ecut.orm.entity;

import java.util.Date;

/**
 * source命令加上 .sql的完整路径导入来创建表
 * 实体类 Customer <================> 数据库表: t_customer
 * 		   属性			<================>		列
 *        id				 <================>       id
 *        emial			 <================> 		email
 *        password	 <================> 		password
 *        nickname	 	<================> 		nickname
 *        gender	 	<================> 		gender
 *        birthdate	 <================> 		birthdate
 *        married	 	<================> 		married
 *        
 *        从对象到关系型数据库之间的映射（对应关系） object relation mapping
 *   new Customer(); 		<==========>    一条记录 (关系)
 *   
 *   hibernate作用 ：把对象存入数据库变成记录或者从数据库查询一条记录包装成一对象个（orm框架）
 *   
 */
public  class Customer {

	// 对象标识符 ( Object Identifier，id的值是对象标识符 ) 属性 ( 对应数据库主键 )
	private Integer id;
	
	private String email;
	private String password;
	private String nickname;
	private char gender;
	private Date birthdate;
	private boolean married;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public boolean isMarried() {
		return married;
	}

	public void setMarried(boolean married) {
		this.married = married;
	}

}
