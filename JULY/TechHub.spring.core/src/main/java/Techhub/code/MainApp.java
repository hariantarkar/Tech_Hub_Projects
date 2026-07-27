package Techhub.code;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import Techhub.anno.code.Config;
import Techhub.anno.code.Shop;
import Techhub.anno.code.User;

public class MainApp {

	public static void main(String[] args) {
		
		
		
		/*
		 * ApplicationContext context=new ClassPathXmlApplicationContext("test.xml");
		 * 
		 * Company com=(Company)context.getBean("c");
		 * 
		 * com.display();
		 */
		 
		/*
		 * ApplicationContext context = new ClassPathXmlApplicationContext("test1.xml");
		 * 
		 * Player p = (Player) context.getBean("p"); p.show();
		 * 
		 * System.out.println();
		 * 
		 * Player pm = (Player) context.getBean("p1"); pm.display();
		 */
		
		AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(Config.class);
		
		
		/*
		 * User u=(User)context.getBean("user");
		 * 
		 * 
		 * u.show();
		 * 
		 * Date d=(Date)context.getBean("d");
		 * 
		 * System.out.print(d);
		 */
		
		Shop v=(Shop)context.getBean("s");
		
		v.show();
		
		
		
		
		
	}

}
