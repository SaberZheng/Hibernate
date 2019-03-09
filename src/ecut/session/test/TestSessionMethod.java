package ecut.session.test;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ecut.session.entity.Customer;

public class TestSessionMethod {
	
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

	public @Test void testSave() {
		
		Customer c = new Customer();
		
		c.setEmail( "zhangcuishan@wudang.com" );
		c.setPassword( "hello2017" );
		
		Transaction tran = session.getTransaction();
		
		try{
			tran.begin();
			// dynamic-insert="false" : 
			// insert   into  t_customer  (email, password, nickname, gender, birthdate, married, id)   values  (?, ?, ?, ?, ?, ?, ?)
			// dynamic-insert="true"
			// insert   into  t_customer  (email, password, gender , married ,id)   values  (?, ?, ?,?, ?)
			System.out.println( "customer.id : " + c.getId() );
			Serializable id = session.save( c );
			System.out.println( "id : " + id );
			System.out.println( "customer.id : " + c.getId() );
			tran.commit();
		} catch ( HibernateException e) {
			tran.rollback();
		}
		
	}
	
	public @Test void testPersist() {
		
		Customer c = new Customer();
		
		c.setEmail( "moshenggu@wudang.com" );
		c.setPassword( "hello2017" );
		
		Transaction tran = session.getTransaction();
		
		try{
			tran.begin();
			System.out.println( "customer.id : " + c.getId() );
			session.persist( c );
			System.out.println( "customer.id : " + c.getId() );
			tran.commit();
		} catch ( HibernateException e) {
			tran.rollback();
		}
		
	}
	
	public @Test void testDelete1() {
		// 先尝试从数据库中获取某条记录对应的一个 Java 对象
		Customer c = session.get( Customer.class ,  3 );
		// 如果指定 id 对应的 对象存在 ( 数据库中有相应的记录 )
		if( c != null ) {
			
			Transaction tran = session.getTransaction();
			
			try{
				tran.begin();
				session.delete( c ); // 删除对象
				tran.commit();
				System.out.println(c.getId());

			} catch ( HibernateException e) {
				tran.rollback();
			}
			
		}
	}
	
	public @Test void testDelete2() {
		
		// 自己创建一个对象并指定 id 
		Customer c = new Customer();
		c.setId(2); // 如果 id 是 2 的记录在数据库中存在
		
		Transaction tran = session.getTransaction();

		try {
			tran.begin();
			session.delete( c ); // 删除对象
			tran.commit();
		} catch (HibernateException e) {
			tran.rollback();
		}

	}
	
	public @Test void query() {
		
		// Hibernate 3.x : org.hibernate.Query
		
		// HQL : Hibernate Query Language FROM之后跟的是实体类的类名 SELECT c FROM Customer AS c
		String HQL = "FROM Customer AS c ORDER BY c.id DESC" ; // SQL : SELECT * FROM t_customer 
		
		// 创建 查询器 
		//Query<?> query = session.createQuery( HQL);
		Query<Customer> query = session.createQuery( HQL , Customer.class );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// 使用 查询器 进行查询
		List<Customer> list = query.list(); 
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		for( Customer c : list ){
			System.out.println( c.getId() + " , " + c.getEmail() );
		}

	}
	
	public @After void destory(){
		session.close();
		factory.close();
	}

}
