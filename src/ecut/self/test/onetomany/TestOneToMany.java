package ecut.self.test.onetomany;

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

public class TestOneToMany {
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
	
	/** 获取一个分类，并获取其子分类 */
	public @Test void loadCategory(){
		
		Category c = session.find( Category.class ,  1001 );
		
		if( c == null ){
			System.out.println( "未找到分类" );
		} else {
			System.out.println( "分类: " + c.getName());
			
			 List<Category> categories = c.getCategories();
			 
			 if( categories == null || categories.isEmpty() ) {
				 System.out.println( c.getName() + " 没有子分类" );
			 } else {
				 for( int i = 0 , n =categories.size() ; i < n ; i++ ){
					 Category s = categories.get( i ); 
					 System.out.println( "\t" + s.getName() + " , " + s.getPosition() );
				 }
			 }
			 
		}
		
	}
	
	/** 保存一个分类，并指定其子分类 ( 子分类被一起保存 )*/
	public @Test void saveCategory(){
		Category c =new Category();
		c.setId(3001);
		c.setName("动漫");
		List<Category> categories =  new ArrayList<Category>() ;	
		Category c1 =new Category();
		c1.setId(3002);
		c1.setName("火影忍者");
		c1.setParent(c);
		categories.add(c1);
		Category c2 =new Category();
		c2.setId(3003);
		c2.setName("海贼王");
		c2.setParent(c);
		categories.add(c2);
		Category c3 =new Category();
		c3.setId(3004);
		c3.setName("名侦探柯南");
		c3.setParent(c);
		categories.add(c3);
		c.setCategories(categories);
		Transaction t=session.getTransaction();
		t.begin();
		session.save(c);
		t.commit();
	}
	
	public @After void destory(){
		session.close();
		factory.close();
	}

}
