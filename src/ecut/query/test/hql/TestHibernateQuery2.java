package ecut.query.test.hql;

import java.lang.reflect.Array;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ecut.query.entity.Clazz;
import ecut.query.entity.Student;

public class TestHibernateQuery2 {
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
	
	/** 使用 表连接进行查询 ( 内连接 ) */
	public @Test void query10(){
		
		// select * from t_class c , t_student s where c.id = s.class_id ;
		// select * from t_class c inner join t_student s on c.id = s.class_id ;
		// select * from t_class c cross join t_student s where c.id = s.class_id ;
		String HQL = "FROM Clazz AS c , Student AS s WHERE c.id = s.clazz.id" ;
		
		Query<?> queryer = session.createQuery( HQL );
		
		List<?> list = queryer.list();
		
		System.out.println( list.size() );
		
		for( Object o : list ){
			Class<?> c = o.getClass();
			if( c.isArray() ){
				final int length = Array.getLength( o ) ;
				for( int i = 0 ; i < length ; i ++ ){
					Object e = Array.get( o ,  i ) ; // Object e = o[ i ] ;
					System.out.print( e + "\t" );
				}
				System.out.println();
			}
		}
		
	}
	
	/** 连接查询 ( Clazz 对应表 跟  Clazz 中 students 对应的表 进行连接 ) */
	public @Test void query11(){
		
		// select * from t_class c inner join t_student s on c.id = s.class_id ;
		String HQL = "FROM Clazz AS c JOIN c.students" ;
		
		Query<?> queryer = session.createQuery( HQL );
		
		List<?> list = queryer.list();
		
		System.out.println( list.size() );

		for( Object o : list ){
			Class<?> c = o.getClass();
			if( c.isArray() ){
				final int length = Array.getLength( o ) ;
				for( int i = 0 ; i < length ; i ++ ){
					Object e = Array.get( o ,  i ) ; // Object e = o[ i ] ;
					System.out.print( e + "\t" );
				}
				System.out.println();
			}
		}
		
	}
	
	/** 连接查询 ( 使用 fetch 关键字 ) */
	public @Test void query12(){
		
		// 使用 fetch 关键字后，下面的 HQL 语句执行后返回 由 Clazz 对象组成的 List 集合
		// Student 对象被自动添加到 相应 的班级对象的 students 集合中
		String HQL = "FROM Clazz AS c JOIN fetch c.students" ;
		
		Query<Clazz> queryer = session.createQuery( HQL , Clazz.class );
		
		List<Clazz> list = queryer.list();
		
		System.out.println( list.size() );
		
		for( Clazz c : list ){
			System.out.println( c.getName() + " : " + c.getStudents().size() );
		}
		
	}
	
	/** 使用 DISTINCT 剔除重复数据 */
	public @Test void query13(){
		
		String HQL = "SELECT DISTINCT c FROM Clazz AS c JOIN fetch c.students" ;
		
		Query<Clazz> queryer = session.createQuery( HQL , Clazz.class );
		
		List<Clazz> list = queryer.list();
		
		System.out.println( list.size() );
		
		for( Clazz c : list ){
			System.out.println( c.getName() + " : " + c.getStudents().size() );
		}
		
	}
	
	/** 使用左外连接: 查询所有班级 及 各班级的学生 ( 暂时没有学生的班级也列出来 ) */
	public @Test void query14(){
		//SELECT * FROM t_class c LEFT JOIN  t_student AS s ON  s.class_id = c.id
		String HQL = "SELECT DISTINCT c FROM Clazz AS c LEFT JOIN fetch c.students" ;
		
		Query<Clazz> queryer = session.createQuery( HQL , Clazz.class );
		
		List<Clazz> list = queryer.list();
		
		System.out.println( list.size() );
		
		for( Clazz c : list ){
			System.out.println( c.getName() + " : " + c.getStudents().size() );
		}
		
	}
	
	/** 使用左外连接: 查询每个学生及其班级信息，如果某个学生没有明确班级，也列出来 */
	public @Test void query15(){
		
		String HQL = "FROM Student AS s  LEFT  OUTER  JOIN fetch s.clazz" ;
		
		Query<Student> queryer = session.createQuery( HQL , Student.class );
		
		List<Student> list = queryer.list();
		
		System.out.println( list.size() );
		
		for( Student c : list ){
			System.out.println( c.getName() + " : " + c.getClazz() );
		}
		
	}
	
	public @After void destory(){
		session.close();
		factory.close();
	}

}
