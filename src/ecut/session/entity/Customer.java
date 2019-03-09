package ecut.session.entity;

import java.util.Date;

/**
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
 *   new Customer(); 		<==========>    一条记录 (关系)
 *   
 */
public class Customer {

	// 对象标识符 ( Object Identifier ) 属性 ( 对应数据库主键 ) 使用包装类型，才会有null值，才可以执行saveOrUpdate
	private Integer id; // 属性的值 被称作 对象标识符
	
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
