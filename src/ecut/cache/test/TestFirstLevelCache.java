package ecut.cache.test;

import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ecut.cache.entity.Clazz;
import ecut.cache.entity.Customer;
import ecut.cache.entity.Student;

public class TestFirstLevelCache {

	private SessionFactory factory ;
	private Session session ;

	public @Before void init() {
		
		Configuration config = new Configuration();
		config.configure("ecut/cache/hibernate.cfg.xml"); 
	
		factory = config.buildSessionFactory();
		
		session = factory.openSession();
	}
	
	public @Test void loadCustomer(){
		
		Customer c = session.get( Customer.class ,  1 );
		
		System.out.println( c.getEmail() );
		
		System.out.println( "~~~~~~~~~~~~~~~~~~~~~~" );
		
		// session.evict( c ); // 从 session 级别的缓存中驱逐 c 对象
		// session.clear();
		
		Customer x = session.get( Customer.class ,  1 );
		
		System.out.println( x.getEmail() );
		
		System.out.println(  c == x );//true，没有找数据库而是从session中直接取，这个就是一级缓存
		
	}
	
	public @Test void loadClazz(){
		
		Clazz c = session.get( Clazz.class ,  1 );
		
		System.out.println( c.getId() + " : " + c.getName() );
		
		Set<Student> students = c.getStudents();
		//遍历students会调用通过班级号去查询数据库,已经执行过一次查询，因此在session中有，再次查询时不会再查询数据库
		for( Student s : students ){
			System.out.println( "\t" + s.getId() + " : " + s.getName() );
		}
		
		System.out.println( "~~~~~~~~~~~~~~~~~~~~~~" );
		
		// session.evict( c ); // 从 session 级别的缓存中驱逐 c 对象，班级被驱逐里面的学生对象也被驱逐了
		// session.clear();
		
		Clazz x = session.get( Clazz.class ,  1 );
		
		System.out.println( x.getId() + " : " + x.getName() );
		
		Set<Student> set = x.getStudents();
		
		for( Student s : set ){
			System.out.println( "\t" + s.getId() + " : " + s.getName() );
		}
		
	}
	
	
	public @After void destory(){
		session.close();
		factory.close();
	}

}
