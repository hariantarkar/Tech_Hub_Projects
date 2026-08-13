package Techhub.anno.code;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("b")
@Primary
public class Bus implements Vehicle{
	public void engine() {
	 System.out.println("Bus vehicle engine...");
	 
	 
	}
}



