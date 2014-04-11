package br.usp.ime.jdx.entity;

import java.util.HashSet;
import java.util.Set;

public class CompUnit {

	private String name;
	private Set<Type> types;
	
	public CompUnit(String name){
		this.name = name;
		types = new HashSet<Type>();
	}
	
	public void addType(Type type){
		this.types.add(type);
	}
	
	public Set<Type> getTypes(){
		return types;
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	public boolean equals(Object o) {
		CompUnit otherCU = (CompUnit)o;
		return this.getName().equals(otherCU.getName());
	}
}
