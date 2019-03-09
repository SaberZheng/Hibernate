package ecut.cache.test;

import org.hibernate.Cache;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ecut.cache.entity.Clazz;
import ecut.cache.entity.Customer;
import ecut.cache.entity.Student;

public class TestSecondLevelCache {

	private SessionFactory factory ;
	
	public @Before void init() {
		Configuration config = new Configuration();
		config.configure("ecut/cache/hibernate.cfg.xml"); 
		factory = config.buildSessionFactory();
	}
	
	//load 方法当从数据库中获取到数据后，会添加到 缓存 一级缓存 和 二级缓存 中
	public @Test void loadCustomer1(){
		
		Session session = factory.openSession(); // 第一次开启 session
		Customer c = session.load( Customer.class ,  2 ); // 第一次在缓存中未命中 id = 1 的对象，因此需要查询数据库
		// 当从数据库中获取到数据后，会添加到 缓存中 ( 一级缓存 和 二级缓存 )
		System.out.println( c.getEmail() );
		session.close();  // 第一次 关闭 session ( 关闭后，一级缓存失效 )
		
		System.out.println( "~~~~~~~~~~~~~~~~~~~~~~" );
		
		session = factory.openSession(); // 第二次开启 Session，开启了一个新的session
		Customer x = session.load( Customer.class ,  2 ); // 当启用 二级缓存时，这里不会再查询数据库
		System.out.println( x.getEmail() );
		session.close(); // 第二次关闭 Session
		
		System.out.println(  c == x );
		
	}
	//一级和二级缓存都开启的情况下，session没有关闭，去找一级缓存，如果一级缓存中没有找到不会再查二级缓存，而是再次发起数据库查询
	public @Test void loadCustomer2(){
		
		Session session = factory.openSession(); // 开启回话
		
		Customer c = session.load( Customer.class ,  2 ); // 第一次获取对象
		System.out.println( c.getEmail() );
		
		session.evict( c ); // 从一级缓存中驱逐 指定的对象
		
		System.out.println( "~~~~~~~~~~~~~~~~~~~~~~" );
		
		Customer x = session.load( Customer.class ,  2 );// 第二次获取 ( 并没有使用 二级缓存 )
		System.out.println( x.getEmail() );
		
		System.out.println(  c == x );
		
		session.close(); // 关闭
		
	}
	//二级缓存的管理evict方法
	public @Test void load3(){
		
		Cache  cache = factory.getCache();
		
		Session session = factory.openSession(); // 第一次开启 session
		Customer customer1 = session.find( Customer.class ,  2 );
		System.out.println( customer1 );
		Clazz clazz1 = session.find( Clazz.class ,  5 ); 
		System.out.println( clazz1.getName() );
		//遍历students会调用通过班级号去查询数据库，与Hibernate.initialize( clazz1.getStudents() );等价
		for(Student s: clazz1.getStudents()){
			System.out.println(s.getId()+":"+s.getName());
		}
		session.close();  // 第一次 关闭 session ( 关闭后，一级缓存失效 )
		
		cache.evict( Customer.class ); // 从二级缓存中驱逐指定类型的所有对象
		cache.evict( Student.class,6 ); // 从二级缓存中驱逐指定类型的id为6的对象

		System.out.println( "~~~~~~~~~~~~~~~~~~~~~~" );
		
		session = factory.openSession(); // 第二次开启 Session
		Customer customer2 = session.find( Customer.class ,  2 );
		System.out.println( customer2 );//二级缓存中不存在客户的对象，因此会在掉用数据库查询
		Clazz clazz2 = session.find( Clazz.class ,  5 ); //二级缓存中存在班级的对象，因此不会在掉用数据库查询
		System.out.println(clazz2.getName() );
		for(Student s: clazz2.getStudents()){
			System.out.println(s.getId()+":"+s.getName());
		}
		session.close(); // 第二次关闭 Session
		
	}
	//二级缓存的管理evictAll方法
	public @Test void load4(){
		
		Cache  cache = factory.getCache();
		
		Session session = factory.openSession(); // 第一次开启 session
		Customer customer1 = session.find( Customer.class ,  2 );
		System.out.println( customer1 );
		Clazz clazz1 = session.find( Clazz.class ,  5 ); 
		System.out.println( clazz1.getName() );
		//遍历students会调用通过班级号去查询数据库，与Hibernate.initialize( clazz1.getStudents() );等价
		for(Student s: clazz1.getStudents()){
			System.out.println(s.getId()+":"+s.getName());
		}
		session.close();  // 第一次 关闭 session ( 关闭后，一级缓存失效 )
		
		cache.evictAll();

		System.out.println( "~~~~~~~~~~~~~~~~~~~~~~" );
		
		session = factory.openSession(); // 第二次开启 Session
		Customer customer2 = session.find( Customer.class ,  2 );
		System.out.println( customer2 );
		Clazz clazz2 = session.find( Clazz.class ,  5 ); 
		System.out.println(clazz2.getName() );
		for(Student s: clazz2.getStudents()){
			System.out.println(s.getId()+":"+s.getName());
		}		
		session.close(); // 第二次关闭 Session
		
	}
	//使用initialize方法将student集合缓存起来
	public @Test void loadClazz5(){
		
		Session session = factory.openSession(); // 第一次开启 session
		Clazz clazz1 = session.find( Clazz.class ,  6 ); 
		System.out.println( clazz1.getName() );
		//强制初始化集合，执行查询并把数据封装到Student对象中
		Hibernate.initialize( clazz1.getStudents() );
		session.close();  // 第一次 关闭 session ( 关闭后，一级缓存失效 )
		
		System.out.println( "~~~~~~~~~~~~~~~~~~~~~~" );
		
		session = factory.openSession(); // 第二次开启 Session
		Clazz clazz2 = session.find( Clazz.class ,  6 ); 
		System.out.println( clazz2.getName() );
		Hibernate.initialize( clazz2.getStudents() );
		//不仅需要在班级的映射配置文件中指定集合的缓存策略还需要指定学生对象的缓存策略，不然只缓存了学生对象的id，获取名称的时候还会执行查询语句
		for(Student s: clazz2.getStudents()){
			System.out.println(s.getId()+":"+s.getName());
		}
		session.close(); // 第二次关闭 Session
		
	}
	//使用initialize方法将student集合缓存起来,在用evictCollectionRegion将students清理掉
	public @Test void loadClazz6(){
		
		Cache cache = factory.getCache();
		
		Session session = factory.openSession(); // 第一次开启 session
		Clazz clazz1 = session.find( Clazz.class ,  6 ); 
		System.out.println( clazz1.getName() );
		Hibernate.initialize( clazz1.getStudents() );
		session.close();  // 第一次 关闭 session ( 关闭后，一级缓存失效 )
		//只能清理集合
		cache.evictCollectionRegion( "ecut.cache.entity.Clazz.students" );
		//cache.evictEntityRegion( "ecut.cache.entity.Student" );//清除 指定类型对应的区域 参照ehcache.xml文件

		System.out.println( "~~~~~~~~~~~~~~~~~~~~~~" );
		
		session = factory.openSession(); // 第二次开启 Session
		Clazz clazz2 = session.find( Clazz.class ,  6 ); 
		System.out.println( clazz2.getName() );
		for(Student s: clazz2.getStudents()){
			System.out.println(s.getId()+":"+s.getName());
		}
		session.close(); // 第二次关闭 Session				
	}
	
	public @After void destory(){
		factory.close();
	}

}
