package Tech.code;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class MainApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		/*
		 * DriverManagerDataSource dataSource=new DriverManagerDataSource();
		 * 
		 * dataSource.setUsername("root"); dataSource.setPassword("root");
		 * dataSource.setUrl("jdbc:mysql://localhost:3607/mysqls");
		 * dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		 */
		//ApplicationContext context = new ClassPathXmlApplicationContext("test.xml");
		//DriverManagerDataSource dataSource = (DriverManagerDataSource) context.getBean("dataSource");

		ApplicationContext context=new AnnotationConfigApplicationContext(Config.class);
		JdbcTemplate template=(JdbcTemplate)context.getBean("template");
		

		try {
			
		//Connection contection=dataSource.getConnection();
			Connection contection=template.getDataSource().getConnection();
			System.out.println("data base is a connected..");
			
			//template.execute("insert into employee value('1','sham','12211')");
			//System.out.println("record save in db");
			
			Scanner sc=new Scanner(System.in); 
			
			/*
			 * System.out.print("enter a employee id of employee which you to delete ..");
			 * int id=sc.nextInt(); sc.nextLine();
			 */

			/*
			 * System.out.print("enter a employee name..");
			 * 
			 * String name=sc.nextLine(); System.out.print("enter a employee sal..");
			 * 
			 * int sal=sc.nextInt();
			 * 
			 * int value=template.update("insert into employee values(?,?,?)",new
			 * PreparedStatementSetter() {
			 * 
			 * @Override public void setValues(PreparedStatement ps) throws SQLException {
			 * 
			 * ps.setInt(1, id); ps.setString(2, name); ps.setInt(3, sal);
			 * 
			 * }
			 * 
			 * });
			 */
			/*
			 * int value=template.update("delete from employee where id=?",new
			 * PreparedStatementSetter() {
			 * 
			 * @Override public void setValues(PreparedStatement ps) throws SQLException {
			 * // TODO Auto-generated method stub ps.setInt(1, id); }
			 * 
			 * });
			 */
			System.out.print("enter a employee name..");
			 
			  String name=sc.nextLine(); System.out.print("enter a employee sal..");
			  
			  int sal=sc.nextInt();
			System.out.print("enter a employee id of employee which you want update ..");
			int id=sc.nextInt();
			sc.nextLine();
			int value=template.update("update employee set name=?,sal=? where id=?",new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					// TODO Auto-generated method stub
					ps.setString(1, name);
					ps.setInt(2, sal);
					ps.setInt(3, id);

				}
				
			});
			
			if(value>0) {
				//System.out.println("employee data save....");
				//System.out.println("employee data deleted ....");
				System.out.println("employee data update successfully ....");

			}
			else {
				System.out.print("employee data not save...");
				
			}
			
			
			contection.close();
		}
		catch(Exception ex) {
			System.out.print("data base is not connected.."+ex.getMessage());
			//ex.printStackTrace();
		}
		

	}

}
