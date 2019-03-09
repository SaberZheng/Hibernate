package ecut.session.test;

import java.util.Calendar;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ecut.session.entity.Customer;

public class TestLifecycle {
	
	private SessionFactory factory ;
	private Session session ; 
	
	public @Before void init() {
		// 创建一个 Configuration 对象，用来读取配置文件 ( 默认位置、默认名称 )
		Configuration config = new Configuration();
		// 读取配置文件
		config.configure("ecut/session/hibernate.cfg.xml");

		//config.configure(); // 读取 默认位置 ( 当前 classpath ) 的 默认名称 ( hibernate.cfg.xml ) 的配置文件

		// 使用 Configuration 创建 SessionFactory
		factory = config.buildSessionFactory();
		
		// 创建 Session 对象 ( 这里的 Session 是 Java 程序 跟 数据库 之间的 会话 )
		session = factory.openSession();
	}

	public @Test void state() throws InterruptedException {
		
		Calendar calendar = Calendar.getInstance();
		
		Customer c = new Customer(); // Transient ( 瞬时状态 )
		
		c.setEmail( "sanf@wudang.com" );
		c.setPassword( "hello2017" );
		
		calendar.set( 1345 , 5 , 10 );
		Date birthdate = calendar.getTime() ;
		c.setBirthdate( birthdate );
		c.setGender( '男' );
		
		c.setNickname( "张三丰" );
		c.setMarried( false );
		
		System.out.println( session.contains( c ) ); // false
		
		Transaction tran = session.beginTransaction(); // session.getTransaction().begin();
		session.save( c ); // Persistent ( 持久化状态 )
		System.out.println( "~~~1~~~~~~~~~~~~~~~~~~" );
		//session.flush();  // 将当前尚未执行的 SQL 语句立即执行
		System.out.println( "~~~2~~~~~~~~~~~~~~~~~~" );
		Thread.sleep( 1 );
		System.out.println( "~~~3~~~~~~~~~~~~~~~~~~" );
		tran.commit(); // 提交事务 ( 如果还有没有执行的 SQL 语句就执行它们 )
		System.out.println( "~~~4~~~~~~~~~~~~~~~~~~" );

		System.out.println( session.contains( c ) ); // true
		
		session.evict( c ); // Detached ( 游离状态 )
		
		System.out.println( session.contains( c ) ); // false，是否被session所管理，session中已经没有这个数据了
		
		System.out.println( c.getEmail() );//虚拟机中依然有数据
		
	}
	
	public @Test void removed() {
		
		Customer c = session.get( Customer.class ,  4 ); // 持久化状态
		
		if( c != null ) {
			
			System.out.println( session.contains( c ) );
			System.out.println( c.getEmail() );
			
			Transaction tran = session.getTransaction();

			tran.begin();
			session.delete(c); // 删除对象
			System.out.println( "~~~1~~~~~~~~~~~~~~~~~~" );
			session.flush(); // 将当前尚未执行的 SQL 语句立即执行
			System.out.println( "~~~2~~~~~~~~~~~~~~~~~~" );
			tran.commit();
			
			System.out.println( session.contains( c ) );
			System.out.println( c.getEmail() );
		}
	}
	
	public @After void destory(){
		session.close();
		factory.close();
	}

	

}
