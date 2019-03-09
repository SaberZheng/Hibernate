package ecut.orm.test;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ecut.orm.entity.Customer;

public class TestSaveCustomer {

	public static void main(String[] args) {
		
		// 对象 <======>  记录
		Customer c = new Customer();
		
		// c.setId( 100 ); // 不要指定 对象标识符 ，而是期望由 Hibernate 维护
		c.setEmail( "sanfeng@qq.com" );
		c.setPassword( "hello2017" );
		
		Date birthdate = null ;
		c.setBirthdate( birthdate );
		c.setGender( '男' );
		
		c.setNickname( "君宝" );
		c.setMarried( false );
		
		// 创建一个 Configuration 对象，用来读取配置文件 ( 默认位置、默认名称 )
		Configuration config = new Configuration();
		// 读取配置文件
		config.configure("ecut/orm/hibernate.cfg.xml");
		//config.configure(); // 读取 默认位置 ( 当前 classpath ) 的 默认名称 ( hibernate.cfg.xml ) 的配置文件
		
		// 使用 Configuration 创建 SessionFactory
		SessionFactory factory = config.buildSessionFactory();
		
		// 创建 Session 对象 ( 这里的 Session 是 Java 程序 跟 数据库 之间的 会话 )
		Session session = factory.openSession();
		
		System.out.println( session );
		
		// 如果执行的是 DML 语句，则需要 开启事务
		session.getTransaction().begin();
		
		// 保存指定的对象 到数据库中
		Serializable s = session.save( c ); // DML（数据操纵语言） : insert 、update 、delete 
		System.out.println( "返回 : " + s );
		
		// 如果执行的是 DML 语句，则需要 提交事务
		session.getTransaction().commit();
		
		session.close();
		
		factory.close();
		
	}

}
