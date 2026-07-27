package Techhub.code;

public class Company {

	private Employee emp;
	
	public void setEmployee(Employee emp) {
		this.emp=emp;
		
	}
	public void display() {
		System.out.println(emp.getId()+"\t"+emp.getName()+"\t"+emp.getAddress());
		
	}
}
