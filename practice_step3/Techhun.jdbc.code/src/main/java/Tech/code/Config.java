package Tech.code;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan(basePackages = {"Tech.code"})
public class Config {

	@Bean(name="dataSource")
	public DriverManagerDataSource getDataSource() {
		
	DriverManagerDataSource dataSource =new DriverManagerDataSource();
	dataSource.setUsername("root");
	dataSource.setPassword("root");
	dataSource.setUrl("jdbc:mysql://localhost:3307/revcode");
	dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
	
	
	return dataSource;
	}
	 
	@Bean(name="template")
	public JdbcTemplate getTemplate() {
		JdbcTemplate template=new JdbcTemplate();
		template.setDataSource(getDataSource());	
		return template;
	}
}
