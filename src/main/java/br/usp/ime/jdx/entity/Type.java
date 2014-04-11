package br.usp.ime.jdx.entity;

public class Type {

	private String name;
	private CompUnit compUnit;
	
	public Type(String name, CompUnit compUnit){
		this.name = name;
		this.compUnit = compUnit;
		compUnit.addType(this);
	}
	
	public String getName(){
		return name;
	}
	
	public CompUnit getCompUnit(){
		return compUnit;
	}
	
	public String toString(){
		return getName();
	}
	
	@Override
	public boolean equals(Object o) {
		Type otherType = (Type)o;
		return this.getName().equals(otherType.getName());
	}
	
}
