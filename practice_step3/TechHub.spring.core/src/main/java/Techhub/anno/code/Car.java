package Techhub.anno.code;

import org.springframework.stereotype.Component;

@Component("c")
public class Car implements Vehicle {
    
	public void engine() {
	  System.out.print("car vehicle engine");
    } 
}

