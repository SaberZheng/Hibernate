package ecut.session.test;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ecut.session.entity.Customer;

public class TestLoadData {
	
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

	public @Test void testGet1() {
		Customer c = session.get( Customer.class ,  2 );
		if( c != null ){
			System.out.println( c.getEmail() );
		} else {
			System.out.println( "数据库中没有对应的数据" );
		}
	}
	
	public @Test void testGet2() {
		System.out.println( "准备查询数据库" );
		Customer c = session.get( Customer.class ,  1 ); // 立即查询并返回数据
		if( c != null ){
			System.out.println( "获取到的数据是" );
			System.out.println( c.getEmail() );
		}
	}

	
	public @Test void testLoad1() {
		Customer c = null ;
		try{
			c = session.load( Customer.class ,  2 );
			System.out.println( c.getEmail() );
		} catch ( ObjectNotFoundException e) {
			System.out.println( "数据库中没有对应的数据 , " + e.getMessage() );
		}
	}
	
	/**  延迟加载 ( 懒加载 )  
	 * 	   关闭对 Customer 的延迟加载: <class name="ecut.session.entity.Customer" table="t_customer"  lazy="false" >
	 * 	   启用对 Customer 的延迟加载: <class name="ecut.session.entity.Customer" table="t_customer"  lazy="true" >
	 * */
	public @Test void testLoad2() {
		System.out.println( "准备查询数据库" );
		Customer c = session.load( Customer.class , 1 ); // 默认并不立即查询,如果不存在会在最后面才抛出异常
		System.out.println( "获取到的数据是 Id" );
		System.out.println( c.getId() );  // 因为 id 是 load 方法中指定的，因此不需要查询数据库
		System.out.println( "获取到的数据是 Email" );
		System.out.println( c.getEmail() ); // 获取 id 之外的 其它 数据时，不得不查询数据库
	}
	
	public @Test void testFind1() {
		Customer c = session.find( Customer.class ,  2 );
		if( c != null ){
			System.out.println( c.getEmail() );
		} else {
			System.out.println( "数据库中没有对应的数据" );
		}
	}
	
	public @Test void testFind2() {
		System.out.println( "准备查询数据库" );
		Customer c = session.find( Customer.class ,  1); // 立即查询并返回数据
		if( c != null ){
			System.out.println( "获取到的数据是 " );
			System.out.println( c.getEmail() );
		}
	}
	
	public @After void destory(){
		session.close();
		factory.close();
	}

}
