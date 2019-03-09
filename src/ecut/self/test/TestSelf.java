package ecut.self.test;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ecut.self.entity.Category;

public class TestSelf {
	
	private SessionFactory factory ;
	private Session session ; 
	
	public @Before void init() {
		// 创建一个 Configuration 对象，用来读取配置文件 ( 默认位置、默认名称 )
		Configuration config = new Configuration();
		// 读取配置文件
		config.configure("ecut/self/hibernate.cfg.xml");
		//config.configure(); // 读取 默认位置 ( 当前 classpath ) 的 默认名称 ( hibernate.cfg.xml ) 的配置文件

		// 使用 Configuration 创建 SessionFactory
		factory = config.buildSessionFactory();
		
		// 创建 Session 对象 ( 这里的 Session 是 Java 程序 跟 数据库 之间的 会话 )
		session = factory.openSession();
	}
	
	public @Test void loadCategory(){
		
		Category s = session.find( Category.class  , 2002 );
		
		if( s == null ){
			System.out.println( "未找到分类" );
		} else {
			System.out.println( s.getName() );
			
			Category p = s.getParent();
			if( p == null ){
				System.out.println( s.getName() + "是一个顶级分类" );
			} else {
				System.out.println( s.getName() + " 分类的 父分类是 " + p.getName() );
			}
			
			List<Category> categories = s.getCategories();
			 
			 if( categories == null || categories.isEmpty() ) {
				 System.out.println( s.getName() + " 没有子分类" );
			 } else {
				 for( int i = 0 , n =categories.size() ; i < n ; i++ ){
					 Category c = categories.get( i ); 
					 System.out.println( "\t" + c.getName() + " , " + c.getPosition() );
				 }
			 }
		}
		
	}
	public @Test void saveCategory(){
		Category p =new Category();
		p.setId(5001);
		p.setName("支付");
		Category s =new Category();
		s.setId(5002);
		s.setName("网银");
		s.setParent(p);
		List<Category> categories =  new ArrayList<Category>() ;	
		Category c1 =new Category();
		c1.setId(5003);
		c1.setName("B2B");
		c1.setParent(s);
		categories.add(c1);
		Category c2 =new Category();
		c2.setId(5004);
		c2.setName("B2C");
		c2.setParent(s);
		categories.add(c2);
		s.setCategories(categories);
		Transaction t=session.getTransaction();
		t.begin();
		session.save(s);
		t.commit();
	}
	public @After void destory(){
		session.close();
		factory.close();
	}

}
