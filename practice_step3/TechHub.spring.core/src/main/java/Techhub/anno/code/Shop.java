package Techhub.anno.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("s")
public class Shop {
	
	@Autowired
	//@Qualifier("c")
	private Vehicle vehicle;
	
	public void setVehicle(Vehicle vehicle) {
	  this.vehicle=vehicle;
	}
	public void show() {
		vehicle.engine();
	}
	
}
