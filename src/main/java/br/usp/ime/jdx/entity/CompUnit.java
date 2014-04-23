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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompUnit other = (CompUnit) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
