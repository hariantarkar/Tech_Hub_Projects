package Techhub.anno.code;

import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "Techhub.anno.code")

public class Config {

	
	@Bean(name="d")
	public Date getDate() {
		return new Date();
	}
}
