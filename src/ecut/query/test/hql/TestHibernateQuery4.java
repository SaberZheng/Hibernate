package ecut.query.test.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ecut.query.entity.Student;

public class TestHibernateQuery4 {
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
	
	/** 使用 HQL 方式支持 update */
	public @Test void update(){
		
		//String HQL = "UPDATE Student AS s SET s.name = ? WHERE id = ? " ;
		String HQL = "UPDATE Student AS s SET s.name = :name WHERE s.id = :id " ;
		
		Query<?> queryer = session.createQuery( HQL );
		
		queryer.setParameter( "name" , "张无忌" );
		queryer.setParameter( "id" , 2 );
		
		session.getTransaction().begin();
		
		int count = queryer.executeUpdate();
		
		session.getTransaction().commit();
		
		System.out.println( "受影响的记录数: " +  count );
		
	}

	/** 使用 HQL 方式支持 delete */
	public @Test void delete(){
		
		String HQL = "DELETE  FROM Student AS s WHERE s.id = :id " ;
		
		Query<?> queryer = session.createQuery( HQL );
		
		queryer.setParameter( "id" , 8 );
		
		session.getTransaction().begin();
		
		int count = queryer.executeUpdate();
		
		session.getTransaction().commit();
		
		System.out.println( "受影响的记录数: " +  count );
		
	}
	
	/** 使用 HQL 方式不支持 insert into */
	public @Test void insert(){
		
		// 错误: String HQL = "INSERT INTO Student ( id , name ) VALUES ( ? , ? )" ;
		
		Student s = new Student();
		s.setName( "林平之" );
		
		session.getTransaction().begin();
		session.save( s );
		session.getTransaction().commit();
		
	}
	
	public @After void destory(){
		session.close();
		factory.close();
	}

}
