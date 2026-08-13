package Techhub.anno.code;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component("user")
public class User {

	@Value("1")
	private int id;
	@Value("vijay")
	private String name;
	@Value("12332")
	private int sal;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSal() {
		return sal;
	}
	public void setSal(int sal) {
		this.sal = sal;
	}
	public  void show() {
		System.out.println(id+"\t"+name+"\t"+sal);
		
	}
}
