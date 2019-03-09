package ecut.session.test;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ecut.session.entity.Customer;

public class TestUpdate {
	
	private SessionFactory factory ;
	
	public @Before void init() {
		// 创建一个 Configuration 对象，用来读取配置文件 ( 默认位置、默认名称 )
		Configuration config = new Configuration();
		// 读取配置文件
		config.configure("ecut/session/hibernate.cfg.xml");

		//config.configure(); // 读取 默认位置 ( 当前 classpath ) 的 默认名称 ( hibernate.cfg.xml ) 的配置文件

		// 使用 Configuration 创建 SessionFactory
		factory = config.buildSessionFactory();
	}

	/** 更新一个持久化状态的对象中的数据 后 同步到数据库中 ，但是不用 update 方法 */
	public @Test void testUpdate1() throws InterruptedException {
		
		Session session = factory.openSession();
		
		// 执行查询操作获取数据
		Customer c = session.find( Customer.class ,  1 ); // 持久化状态
		
		if(c != null){
			System.out.println(  c.getNickname() +  " : " + c.getEmail() );
			
			Transaction tran = session.beginTransaction(); // 开启一个事务并返回该事务的引用
			
			//c.setEmail( "sanfeng@wudang.com" );
			//可以在映射配置文件中设置动态插入和动态更新，只更新所更改的数值
			c.setNickname( "三丰".equals( c.getNickname() ) ? "君宝" : "三丰" );//如果数据值更改了（只有数据更改了才会执行update语句），提交事务的时候更改的值会同步到数据库
			
			Thread.sleep( 1 );
			
			tran.commit(); // 提交事务
		}
		
		session.close();
		
	}
	
	/** 使用 update 方法将 游离状态 的对象 还原到 持久化状态*/
	public @Test void testUpdate2() {
		
		Session firstSession = factory.openSession();
		
		Customer c = firstSession.find( Customer.class ,  1 ); // 持久化状态
		
		firstSession.close(); // 游离状态
		
		String nickname = c.getNickname() ;
		//可以在映射配置文件中设置动态插入和动态更新，只更新所更改的数值
		c.setNickname( "三丰".equals( nickname ) ? "君宝" : "三丰" );
		
		Session secondSession = factory.openSession() ;
		
		secondSession.getTransaction().begin();
		secondSession.update( c ); // 将一个 游离状态 的对象重新转换到 持久化状态，无法动态更新，动态更新是针对持久化对象的，只有是持久化对象才知道数据是什么样的。
		secondSession.getTransaction().commit(); // 提交事务后，事务即结束
		
		System.out.println( "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" );
		
		secondSession.getTransaction().begin();
		String email = c.getEmail() ;
		//已经是持久化对象因此可以动态对象
		c.setEmail( "sanfeng@wudang.com".equals( email ) ? "junbao@qq.com" :"zsf@qq.com" );
		secondSession.getTransaction().commit();
	}
	
	/** 使用 merge 方法 将一个 游离状态的 对象的 数据 合并到 与其id相等的持久化状态的对象中去*/
	public @Test void testUpdate3() {
		
		Session session = factory.openSession();
		
		Customer c1 = session.find( Customer.class ,  1 ); // c1 处于持久化状态
		
		session.evict( c1 ); // c1 开始进入到 游离状态
		
		String nickname = c1.getNickname() ;
		c1.setNickname( "三丰".equals( nickname ) ? "君宝" : "三丰" );
		
		Customer c2 = session.find( Customer.class ,  1 ) ; // 持久化状态
		
		
		// 根据 c1.id 来在 session 管理的对象中寻找 与它id 相同的对象
		// 如果找到 id 相同的对象，就将 c1 中的数据 合并到 相应的持久化状态的对象中去
		// 最后 merge 方法返回 那个持久化状态的对象
		Object o = session.merge( c1 ); // c1.id == c2.id id是一样的将两个对象的数据进行合并
		session.getTransaction().begin();
		session.getTransaction().commit();//只要事务开启之后提交就好了，代码位置没有要求
		
		System.out.println( o == c1 ); // false
		System.out.println( o == c2 ); // true c1合并到c2中了，输出日志中动态更新起了作用，因此是持久化对象C2
		
		System.out.println( session.contains( c1 ) ); // false
		System.out.println( session.contains( c2 ) ); // true
		
	}
	
	/** 使用 saveOrUpdate 方法执行 更新操作，saveOrUpdate的操作条件 取决于映射文件中的unsaved-value属性的属性值*/
	public @Test void testUpdate4() {
		
		Session session = factory.openSession();
		Customer c = session.find( Customer.class ,  1 ); //  持久化状态
		session.close(); // 游离状态
		
		System.out.println( "id : " + c.getId() );
		
		String nickname = c.getNickname() ;
		c.setNickname( "三丰".equals( nickname ) ? "君宝" : "三丰" );
		
		session = factory.openSession();//重新开启一个session
		
		session.getTransaction().begin();
		
		// <id name="id" type="integer" column="id" unsaved-value="null" >
		// 取决于 c.id 是否是 null ，如果 id 是 null 就执行 insert ，否则执行 update
		session.saveOrUpdate( c ); // 执行 update 操作，将 游离状态的对象  转换到 持久化状态
		
		session.getTransaction().commit();
		
	}
	
	/** 使用 saveOrUpdate 方法执行 保存操作 */
	public @Test void testSave() {
		
		/*
		Session session = factory.openSession();
		Customer c = session.find( Customer.class ,  1 ); //  持久化状态
		session.close(); // 游离状态
		
		c.setId( null ); //等价于new一个新的对象
		c.setEmail( "zhangwuji@wudang.com" );
		c.setNickname( "曾阿牛" );
		*/
		
		Customer c = new Customer();
		
		c.setEmail( "yinsusu@tianying.com" );
		c.setPassword( "hello2017" );
		
		Date birthdate = null ;
		c.setBirthdate( birthdate );
		c.setGender( '女' );
		
		c.setNickname( "素素" );
		c.setMarried( false );
		
		Session session = factory.openSession();
		
		session.getTransaction().begin();
		
		// <id name="id" type="integer" column="id" unsaved-value="null" >
		// 取决于 c.id 是否是 null ，如果 id 是 null 就执行 insert ，否则执行 update
		session.saveOrUpdate( c ); // 执行 insert 操作，对象 进入 持久化状态
		
		session.getTransaction().commit();
		
	}
	
	public @After void destory(){
		factory.close();
	}

}
