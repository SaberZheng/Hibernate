package ecut.query.test.hql;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ecut.query.entity.Student;

public class TestHibernateQuery3 {
	private SessionFactory factory ;
	private Session session ; 
	
	public @Before void init() {
		// 创建一个 Configuration 对象，用来读取配置文件 ( 默认位置、默认名称 )
		Configuration config = new Configuration();
		// 读取配置文件
		config.configure("ecut/query/hibernate.cfg.xml");

		//config.configure(); // 读取 默认位置 ( 当前 classpath ) 的 默认名称 ( hibernate.cfg.xml ) 的配置文件

		// 使用 Configuration 创建 SessionFactory
		factory = config.buildSessionFactory();
		
		// 创建 Session 对象 ( 这里的 Session 是 Java 程序 跟 数据库 之间的 会话 )
		session = factory.openSession();
	}
	
	/** 使用 ? 做参数占位符 */
	public @Test void query16(){
		
		String HQL = "FROM Student AS s  WHERE s.id BETWEEN ? AND ? " ;
		
		Query<Student> queryer = session.createQuery( HQL , Student.class );
		
		queryer.setParameter( 0 , 5 ); // JDBC是从1开始，HQL的参数占位符的索引从 0 开始
		queryer.setParameter( 1 , 10 );
		
		List<Student> list = queryer.list();
		
		System.out.println( list.size() );
		
		for( Student c : list ){
			System.out.println( c.getId() + " : " + c.getName()  );
		}
		
	}
	
	/** 使用 命名 参数占位符 */
	public @Test void query17(){
		
		String HQL = "FROM Student AS s  WHERE s.id BETWEEN :start AND :end " ;
		
		Query<Student> queryer = session.createQuery( HQL , Student.class );
		
		queryer.setParameter( "start" , 5 ); // 参数占位符的索引从 0 开始
		queryer.setParameter( "end" , 10 );
		
		List<Student> list = queryer.list();
		
		System.out.println( list.size() );
		
		for( Student c : list ){
			System.out.println( c.getId() + " : " + c.getName()  );
		}
		
	}
	
	public @After void destory(){
		session.close();
		factory.close();
	}

}
