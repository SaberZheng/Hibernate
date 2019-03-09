package ecut.orm.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ecut.orm.entity.Customer;
public class TestGetCustomer {

	public static void main(String[] args) {
		
		// 创建一个 Configuration 对象，用来读取配置文件 ( 默认位置、默认名称 )
		Configuration config = new Configuration();
		// 读取配置文件
		config.configure("ecut/orm/hibernate.cfg.xml");
		//config.configure(); // 读取 默认位置 ( 当前 classpath ) 的 默认名称 ( hibernate.cfg.xml ) 的配置文件
		
		// 使用 Configuration 创建 SessionFactory
		SessionFactory factory = config.buildSessionFactory();
		
		// 创建 Session 对象 ( 这里的 Session 是 Java 程序 跟 数据库 之间的 会话 )
		Session session = factory.openSession();
		
		System.out.println( session );
		
		Customer c  = session.get( Customer.class ,  2 );
		
		System.out.println( c.getEmail() );
		
		session.close();
		
		factory.close();
		
	}

}
