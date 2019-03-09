package ecut.manytoone.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ecut.manytoone.entity.Clazz;
import ecut.manytoone.entity.Student;

public class TestManyToOne {
	
	private SessionFactory factory ;
	private Session session ; 
	
	public @Before void init() {
		// 创建一个 Configuration 对象，用来读取配置文件 ( 默认位置、默认名称 )
		Configuration config = new Configuration();
		// 读取配置文件
		config.configure("ecut/manytoone/hibernate.cfg.xml");
		//config.configure(); // 读取 默认位置 ( 当前 classpath ) 的 默认名称 ( hibernate.cfg.xml ) 的配置文件

		// 使用 Configuration 创建 SessionFactory
		factory = config.buildSessionFactory();
		
		// 创建 Session 对象 ( 这里的 Session 是 Java 程序 跟 数据库 之间的 会话 )
		session = factory.openSession();
	}
	
	public @Test void loadStudent(){
		
		Student s = session.find( Student.class  , 2 );
		
		if( s == null ) {
			System.out.println( "查无此人" );
		} else {
			System.out.println( s.getId() + " : " + s.getName() );
			
			Clazz c = s.getClazz();
			if( c == null ){
				System.out.println( s.getName() + " 同学还没有指定班级" );
			} else {
				System.out.println( c.getId() );//c对象只有id，默认只有获取name的时候才会去查询
				System.out.println( "~~~~~~~~~~~~" );
				System.out.println( c.getName() );
			}
			
		}
		
	}
	
	public @Test void saveStudent(){
		
		Clazz clazz = session.find( Clazz.class ,  3 );
		
		Student s = new Student();
		s.setName( "殷离" );
		
		s.setClazz( clazz ); // 告诉 hibernate 新创建的学生 对应的 班级
		
		session.getTransaction().begin();
		session.save( s );
		session.getTransaction().commit();
		
	}
	
	public @Test void saveStudentAndClazz(){
		
		Clazz clazz = new Clazz();
		clazz.setName( "挖煤工程1班" );
		
		Student s = new Student();
		s.setName( "丽丽" );
		
		s.setClazz( clazz );
		
		session.getTransaction().begin();
		session.save( s ); //  cascade="all" 
		session.getTransaction().commit();
		
	}
	
	public @After void destory(){
		session.close();
		factory.close();
	}

}
