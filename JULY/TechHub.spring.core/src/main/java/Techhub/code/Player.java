package Techhub.code;

import java.util.*;

public class Player {

	private List <String>names;
	
	public void setName(List<String>  names) {
		this.names=names;
	}
	public void show() {
		
		for(String name:names) {
			System.out.println(name);
		
		}
	}
	
	Map<Integer,String> palyers;
	public void setPlayers(Map<Integer,String>palyers) {
		this.palyers=palyers;
		
	}
	void display() {
		Set<Map.Entry<Integer,String>> set=palyers.entrySet();
		
	for(Map.Entry<Integer,String> m:set) {
		
		System.out.println(m.getKey()+"\t"+m.getValue());
		
		
	}
	}
	
	
}
