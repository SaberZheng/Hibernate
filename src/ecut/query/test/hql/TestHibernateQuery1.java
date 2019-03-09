package ecut.query.test.hql;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ecut.query.entity.Clazz;

public class TestHibernateQuery1 {
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
	
	/** 查询  Clazz 类 对应的表中的所有数据并 封装成  Clazz 对象 组成的 集合 */
	public @Test void query1(){
		
		String HQL = "FROM  Clazz" ; // 建议 所有的 SQL 关键字 一律大写
		
		// 通过 session 对象 来创建一个 查询器
		// session.createQuery( HQL ); // 如果类型不明确，可以采用该方法
		Query<Clazz>  queryer = session.createQuery( HQL , Clazz.class ) ; // 如果类型能够确定，建议指定类型
		System.out.println( "~~~~~~~~~~~~~~~~~~~" );
		List<Clazz> list = queryer.list() ; // 执行查询操作 ( 与 queryer.getResultList() 功能 相似 )
		System.out.println( list.size() );
		
		for( Clazz c : list ){
			System.out.println( c.getName() );
		}
		
	}
	
	/** 查询  Clazz 类 对应的表中的所有数据并 封装成  Clazz 对象 组成的 集合 */
	public @Test void query2(){
		
		String HQL = "SELECT c FROM  Clazz AS c" ; // 建议 所有的 SQL 关键字 一律大写
		
		Query<Clazz>  queryer = session.createQuery( HQL , Clazz.class ) ; 
		System.out.println( "~~~~~~~~~~~~~~~~~~~" );
		List<Clazz> list = queryer.list() ; // 执行查询操作
		
		for( Clazz c : list ){
			System.out.println( c.getName() );
		}
		
	}
	
	/** N + 1 问题  */
	@SuppressWarnings("deprecation")
	public @Test void query3(){
		
		String HQL = "SELECT c FROM  Clazz AS c" ; // 建议 所有的 SQL 关键字 一律大写
		
		Query<Clazz>  queryer = session.createQuery( HQL , Clazz.class ) ;
		
		System.out.println( "~~~~~~~~~~~~~~~~~~~" );
		
		// 第一次查询 ( 查询所有的对象标识符 )
		Iterator<Clazz> it = queryer.iterate(); // 执行查询操作 ( 但是 只获取 每个对象对应的 对象标识符 )
		
		// 每循环一次 就查询一条记录 ( N )
		while( it.hasNext() ){
			Clazz c = it.next();
			System.out.println( c.getId() + " : " + c.getName() );
		}
		
	}
	
	/** 使用 条件过滤 和 排序 */
	public @Test void query4(){
		
		String HQL = "FROM  Clazz AS c WHERE c.id > 4 ORDER BY c.id DESC" ;
		
		Query<Clazz>  queryer = session.createQuery( HQL , Clazz.class ) ; 

		List<Clazz> list = queryer.list() ; // 执行查询操作
		
		for( Clazz c : list ){
			System.out.println( c.getId() + " : " + c.getName() );
		}
		
	}
	
	/** 标量查询: 查询单个属性 */
	public @Test void query5(){
		
		String HQL = "SELECT c.name FROM  Clazz AS c" ;
		
		Query<String>  queryer = session.createQuery( HQL , String.class) ; 

		List<String> list = queryer.list() ; // 执行查询操作
		
		for( String o : list ){
			System.out.println( o );
		}
		
	}
	
	/** 标量查询: 查询多个属性 */
	public @Test void query6(){
		
		String HQL = "SELECT c.id , c.name FROM  Clazz AS c" ;
		
		Query<?>  queryer = session.createQuery( HQL ) ; 

		List<?> list = queryer.list() ; // 执行查询操作
		
		for( Object o : list ){
			Class<?> c = o.getClass(); // 获得当前元素的类型
			if( c.isArray() ) {
				int length = Array.getLength( o ); // 用  java.lang.reflect.Array 中的方法获取数组长度
				// 用反射的方式遍历数组
				for( int i = 0 ; i < length ; i ++ ){
					Object e = Array.get( o ,  i ) ; // Object e = o[ i ] ;
					System.out.print( e );
					System.out.print( "\t");
				}
				System.out.println();
			}
		}
		
	}
	
	/** 标量查询: 查询多个属性 */
	public @Test void query7(){
		
		String HQL = "SELECT c.id , c.name FROM  Clazz AS c" ;
		
		Query<Object[]>  queryer = session.createQuery( HQL , Object[].class ) ; 

		List<Object[]> list = queryer.list() ; // 执行查询操作
		
		for( Object[] array : list ){
			if( array == null || array.length == 0 ){
				System.out.println( "没有数据" );
			} else {
				for( int i = 0 , n = array.length ; i < n ; i++ ){
					Object e = array[ i ] ; 
					System.out.print( e + "\t" );
				}
				System.out.println();
			}
		}
		
	}
	
	/** 查询多个属性返回实体类型的对象 */
	public @Test void query8(){
		
		// String HQL = "SELECT new Clazz( c.id , c.name ) FROM  Clazz AS c" ;
		
		String HQL = "SELECT new Clazz( c.name , c.id ) FROM  Clazz AS c" ;
		
		Query<Clazz>  queryer = session.createQuery( HQL ,Clazz.class ) ; 

		List<Clazz> list = queryer.list() ; // 执行查询操作
		
		for( Clazz c : list ){
			if( c != null ) {
				System.out.println( c.getId() + " : " + c.getName() );
			}
		}
		
	}
	
	/** 查询单个或多个属性，并返回 map 组成的 List 集合 */
	@SuppressWarnings("rawtypes")
	public @Test void query9(){
		
		// 如果没有通过 AS 关键字指定 属性的 别名，
		// 那么将来的 Map 集合中 key 是按照 属性出现的顺序对应的索引 ( 不是数字类型而是 String 类型 )
		// String HQL = "SELECT new map( c.id , c.name ) FROM  Clazz AS c WHERE c.id = 1 " ;
		
		// 使用 AS 关键字指定  属性的 别名 ，将的 Map 集合中 key 的名称就是这里的 别名
		String HQL = "SELECT new map( c.id AS id , c.name AS name ) FROM  Clazz AS c" ;
		
		Query<Map>  queryer = session.createQuery( HQL ,Map.class ) ; 

		List<Map> list = queryer.list() ; // 执行查询操作
		
		for( Map map : list ){
			System.out.println( map.get( "id" ) + " : " + map.get( "name" ) );
		}
		
		/*
		for( Map map : list ){
			Set<Entry> entrySet =  map.entrySet();
			for ( Entry e : entrySet ){
				//System.out.println( e.getKey() + " : " + e.getKey().getClass().getName() );
				// System.out.println( e.getValue() + " : " + e.getValue().getClass().getName() );
				 System.out.println( e.getKey() + " : " + e.getValue() );
				
			}
		}
		*/
		
	}
	
	public @After void destory(){
		session.close();
		factory.close();
	}

}
