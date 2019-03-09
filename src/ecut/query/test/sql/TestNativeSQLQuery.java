package ecut.query.test.sql;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ecut.query.entity.Clazz;

public class TestNativeSQLQuery {

	private SessionFactory factory ;
	private Session session ;

	public @Before void init() {
		
		Configuration config = new Configuration();
		config.configure("ecut/query/hibernate.cfg.xml");

		//config.configure(); 
	
		factory = config.buildSessionFactory();
		
		session = factory.openSession();
	}
	//标量查询
	public @Test void query1(){
		
		String SQL = "SELECT id , name FROM t_class" ;
		
		// SQLQuery queryer = session.createSQLQuery( SQL ); // 过时
		// NativeQuery<?> queryer = session.createNativeQuery( SQL );
		NativeQuery<Object[]> queryer = session.createNativeQuery( SQL , Object[].class );
		
		List<Object[]> list = queryer.list();
		
		for( Object[] o : list ){
			System.out.println( o[ 0 ] + " : " + o[ 1 ]);
		}
		
	}
	
	public @Test void query2(){
		
		String SQL = "SELECT name FROM t_class" ;
		
		NativeQuery<?> queryer = session.createNativeQuery( SQL );
		
		List<?> list = queryer.list();
		
		for( Object o : list ){
			System.out.println( o + " : " + o.getClass().getName() );
		}
		
	}
	
	/** 执行 SQL 语句并将结果集中的数据 封装到 指定的实体类型的对象中 */
	public @Test void query3(){
		
		String SQL = "SELECT id , name FROM t_class" ;
		
		NativeQuery<Clazz> queryer = session.createNativeQuery( SQL , Clazz.class );
		
		List<Clazz> list = queryer.list();
		
		for( Clazz c : list ){
			System.out.println( c.getId() + " : " + c.getName() ) ;
		}
		
	}
	/** 使用 ? 做参数占位符 */
	public @Test void query4(){
		
		String SQL = "SELECT id , name FROM t_class WHERE id > ? ORDER BY id DESC" ;
		
		NativeQuery<Clazz> queryer = session.createNativeQuery( SQL , Clazz.class );
		
		queryer.setParameter( 1 , 5 ); // 执行 SQL 语句跟 执行 HQL 语句不同，这里的索引跟JDBC一致，从 1 开始
		
		List<Clazz> list = queryer.list();
		
		for( Clazz c : list ){
			System.out.println( c.getId() + " : " + c.getName() ) ;
		}
		
	}

	/** 使用 命名 参数占位符 */
	public @Test void query5(){
		
		String SQL = "SELECT id , name FROM t_class WHERE id > :id ORDER BY id DESC" ;
		
		NativeQuery<Clazz> queryer = session.createNativeQuery( SQL , Clazz.class );
		
		queryer.setParameter( "id" , 5 ); 
		
		List<Clazz> list = queryer.list();
		
		for( Clazz c : list ){
			System.out.println( c.getId() + " : " + c.getName() ) ;
		}
		
	}
	
	public @Test void insert(){
		
		String SQL = "INSERT INTO t_class ( id , name ) VALUES ( :id , :name )" ;
		
		NativeQuery<?> queryer = session.createNativeQuery( SQL );
		
		queryer.setParameter( "id" , 8 ); 
		queryer.setParameter( "name" ,  "计算机科学技术" );
		
		session.getTransaction().begin();
		int count = queryer.executeUpdate();
		System.out.println( count );
		session.getTransaction().commit();
		
	}
	
	public @Test void update(){
		
		String SQL = "UPDATE t_class SET name = :name WHERE id = :id" ;
		
		NativeQuery<?> queryer = session.createNativeQuery( SQL );
		
		queryer.setParameter( "id" , 8 ); 
		queryer.setParameter( "name" ,  "大数据与云计算" );
		
		session.getTransaction().begin();
		int count = queryer.executeUpdate();
		System.out.println( count );
		session.getTransaction().commit();
		
	}
	
	public @Test void delete(){
		
		String SQL = "DELETE FROM t_class WHERE id = :id" ;
		
		NativeQuery<?> queryer = session.createNativeQuery( SQL );
		
		queryer.setParameter( "id" , 8 ); 
		
		session.getTransaction().begin();
		int count = queryer.executeUpdate();
		System.out.println( count );
		session.getTransaction().commit();
		
	}
	
	public @After void destory(){
		session.close();
		factory.close();
	}

}
