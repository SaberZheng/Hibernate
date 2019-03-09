package ecut.cache.test;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ecut.cache.entity.Customer;

public class TestQueryCache {

	private SessionFactory factory;

	public @Before void init() {
		Configuration config = new Configuration();
		config.configure("ecut/cache/hibernate.cfg.xml");
		factory = config.buildSessionFactory();
	}

	// session 没有关闭，list（）方法不会一级缓存中去查询，查询语句执行了两次
	public @Test void query1() {

		Session session1 = factory.openSession();

		final String HQL = "FROM Customer";

		Query<Customer> queryer1 = session1.createQuery(HQL, Customer.class);

		List<Customer> customers1 = queryer1.list();// 第一次执行查询
		for (Customer c : customers1) {
			System.out.println(c.getId() + ":" + c.getNickname());
		}

		System.out.println("~~~~~~~~~~~~~~~~~~");

		Query<Customer> queryer2 = session1.createQuery(HQL, Customer.class);
		List<Customer> customers2 = queryer2.list();// 第二次执行查询
		for (Customer c : customers2) {
			System.out.println(c.getId() + ":" + c.getNickname());
		}

	}

	// session关闭，list（）方法不会二级缓存中去查询，查询语句执行了两次
	public @Test void query2() {

		Session session1 = factory.openSession();

		final String HQL = "FROM Customer";

		Query<Customer> queryer1 = session1.createQuery(HQL, Customer.class);

		List<Customer> customers1 = queryer1.list();// 第一次执行查询
		for (Customer c : customers1) {
			System.out.println(c.getId() + ":" + c.getNickname());
		}

		System.out.println("~~~~~~~~~~~~~~~~~~");
		session1.close();
		Session session2 = factory.openSession();

		Query<Customer> queryer2 = session2.createQuery(HQL, Customer.class);
		List<Customer> customers2 = queryer2.list();// 第二次执行查询
		for (Customer c : customers2) {
			System.out.println(c.getId() + ":" + c.getNickname());
		}

	}

	// session不 关闭，iterate（）方法会去一级缓存中去查询，查询id的语句执行了两次，查询根据id获取客户对象的执行了一次
	@SuppressWarnings("deprecation")
	public @Test void query3() {

		Session session1 = factory.openSession();

		final String HQL = "FROM Customer";

		Query<Customer> queryer1 = session1.createQuery(HQL, Customer.class);

		Iterator<Customer> it1 = queryer1.iterate(); // 执行查询操作 ( 但是 只获取 每个对象对应的
														// 对象标识符 )

		// 每循环一次 就查询一条记录 ( N )
		while (it1.hasNext()) {
			Customer c = it1.next();
			System.out.println(c.getId() + " : " + c.getNickname());
			//session1.evict(c);//从一级缓存中驱逐掉，去找一级缓存，如果一级缓存中没有找到不会再查二级缓存，而是再次发起数据库查询
		}
		System.out.println("~~~~~~~~~~~~~~~~~~");

		Query<Customer> queryer2 = session1.createQuery(HQL, Customer.class);

		Iterator<Customer> it2 = queryer2.iterate(); // 执行查询操作 ( 但是 只获取 每个对象对应的
														// 对象标识符 )

		// 每循环一次 就查询一条记录 ( N )
		while (it2.hasNext()) {
			Customer c = it2.next();
			System.out.println(c.getId() + " : " + c.getNickname());
		}

	}

	// session 关闭，iterate（）方法会去二级缓存中去查询，查询id的语句执行了两次，查询根据id获取客户对象的执行了一次
	@SuppressWarnings("deprecation")
	public @Test void query4() {

		Session session1 = factory.openSession();

		final String HQL = "FROM Customer";

		Query<Customer> queryer1 = session1.createQuery(HQL, Customer.class);

		Iterator<Customer> it1 = queryer1.iterate(); // 执行查询操作 ( 但是 只获取 每个对象对应的
														// 对象标识符 )

		// 每循环一次 就查询一条记录 ( N )
		while (it1.hasNext()) {
			Customer c = it1.next();
			System.out.println(c.getId() + " : " + c.getNickname());
		}

		System.out.println("~~~~~~~~~~~~~~~~~~");
		session1.close();
		Session session2 = factory.openSession();

		Query<Customer> queryer2 = session2.createQuery(HQL, Customer.class);

		Iterator<Customer> it2 = queryer2.iterate(); // 执行查询操作 ( 但是 只获取 每个对象对应的
														// 对象标识符 )

		// 每循环一次 就查询一条记录 ( N )
		while (it2.hasNext()) {
			Customer c = it2.next();
			System.out.println(c.getId() + " : " + c.getNickname());
		}

	}


	public @Test void query5(){
		
		Session session = factory.openSession();
		
		final String HQL = "FROM Customer" ;
		
		Query<Customer> queryer = session.createQuery( HQL , Customer.class );
		
		queryer.setCacheable( true );
		
		queryer.list();
		List<Customer> customers1 = queryer.list();// 第一次执行查询
		for (Customer c : customers1) {
			System.out.println(c.getId() + ":" + c.getNickname());
		}
		System.out.println( "~~~~~~~~~~~~~~~~~~" );
		
		List<Customer> customers2 = queryer.list();// 第二次执行查询
		for (Customer c : customers2) {
			System.out.println(c.getId() + ":" + c.getNickname());
		}

		
	}
	
	public @Test void query6(){
		
		Session session = factory.openSession();
		
		final String HQL = "FROM Customer" ;
		
		Query<Customer> queryer1 = session.createQuery( HQL , Customer.class );
		
		queryer1.setCacheable( true );
		
		List<Customer> customers1 = queryer1.list();// 第一次执行查询
		for (Customer c : customers1) {
			System.out.println(c.getId() + ":" + c.getNickname());
		}  
		
		System.out.println( "~~~~~~~~~~~~~~~~~~" );
		
		Query<Customer> queryer2 = session.createQuery( HQL , Customer.class );
		
		queryer2.setCacheable( true );
		
		List<Customer> customers2 = queryer2.list();// 第二次执行查询
		for (Customer c : customers2) {
			System.out.println(c.getId() + ":" + c.getNickname());
		} 
		
		session.close();
		
	}
	
	public @Test void query7(){
		
		Session session = factory.openSession();
		
		final String HQL = "FROM Customer" ;
		
		Query<Customer> queryer1 = session.createQuery( HQL , Customer.class );
		
		queryer1.setCacheable( true );
		
		List<Customer> customers1 = queryer1.list();// 第一次执行查询
		for (Customer c : customers1) {
			System.out.println(c.getId() + ":" + c.getNickname());
		}  		
		session.close();
		
		System.out.println( "~~~~~~~~~~~~~~~~~~" );
		
		session = factory.openSession();
		
		Query<Customer> queryer2 = session.createQuery( HQL , Customer.class );
		
		queryer2.setCacheable( true );
		
		List<Customer> customers2 = queryer2.list();// 第二次执行查询
		for (Customer c : customers2) {
			System.out.println(c.getId() + ":" + c.getNickname());
		} 		
		session.close();
		
	}
	
	
	public @After void destory(){
		factory.close();
	}

}