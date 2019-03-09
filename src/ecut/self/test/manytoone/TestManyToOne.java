package ecut.self.test.manytoone;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ecut.self.entity.Category;

public class TestManyToOne {
	
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
		
		Category s = session.find( Category.class  , 1002 );
		
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
		}
		
	}
	public @Test void saveCategory(){
		Category p =new Category();
		p.setId(4001);
		p.setName("银行");
		Category s =new Category();
		s.setId(4002);
		s.setName("中国工商银行");
		s.setParent(p);
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
