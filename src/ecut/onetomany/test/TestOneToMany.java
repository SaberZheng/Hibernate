package ecut.onetomany.test;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ecut.onetomany.entity.Clazz;
import ecut.onetomany.entity.Student;

public class TestOneToMany {
	private SessionFactory factory ;
	private Session session ; 
	
	public @Before void init() {
		// 创建一个 Configuration 对象，用来读取配置文件 ( 默认位置、默认名称 )
		Configuration config = new Configuration();
		// 读取配置文件
		config.configure("ecut/onetomany/hibernate.cfg.xml");
		//config.configure(); // 读取 默认位置 ( 当前 classpath ) 的 默认名称 ( hibernate.cfg.xml ) 的配置文件

		// 使用 Configuration 创建 SessionFactory
		factory = config.buildSessionFactory();
		
		// 创建 Session 对象 ( 这里的 Session 是 Java 程序 跟 数据库 之间的 会话 )
		session = factory.openSession();
	}
	
	public @Test void loadClazz(){
		
		Clazz c = session.find( Clazz.class ,  2 );
		
		if( c == null ){
			System.out.println( "没有找到对应的班级" );
		} else {
			System.out.println( c.getId() + " : " + c.getName() );
			
			Set<Student> students = c.getStudents(); // <set name="students" order-by="id DESC" >
			
			System.out.println( students.getClass() );
			
			if( students == null || students.isEmpty() ) {
				System.out.println( c.getName() + "还没有学生");
			} else {
				for( Student s : students ){
					System.out.println( "\t" + s.getId() + " : " + s.getName() );
				}
			}
			
		}
		
		
	}
	
	public @Test void saveClazz(){ 
		
		Clazz c = new Clazz();
		c.setName( "华山派" );
		
		Set<Student> students = new HashSet<>();
		
		Student s1 = new Student( "令狐冲" ) ;
		students.add( s1 );
		
		Student s2 = new Student( "岳灵珊" ) ;
		students.add( s2 );
		
		Student s3 = new Student( "陆大有" ) ;
		students.add( s3 );
		
		Student s4 = new Student( "林平之" ) ;
		students.add( s4 );

		c.setStudents( students ); // 确定 班级中都有哪些 学生
		
		session.getTransaction().begin();
		session.save( c ); // <set name="students" cascade="all" >
		session.getTransaction().commit();
		
	}
	
	public @After void destory(){
		session.close();
		factory.close();
	}

}
