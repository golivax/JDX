package br.usp.ime.jdx.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CompUnit extends NamedEntity implements Serializable{

	private static final long serialVersionUID = 5569390666734073800L;

	private String name;
	private String sourceCode;
	private Set<Type> types;
	
	public CompUnit(String name, String sourceCode){
		//Always replace back slashes with forward slashes
		this.name = name.replaceAll("\\\\", "/");
		this.sourceCode = sourceCode;
		types = new HashSet<Type>();
	}
	
	public void addType(Type type){
		this.types.add(type);
		type.setCompUnit(this);
	}
	
	public Set<Type> getTypes(){
		return types;
	}
	
	public boolean containsType(String typeName){
		for(Type type : types){
			if(typeName.equals(type.getFQN())){
				return true;
			}
		}
		return false;
	}
	
	public String getName(){
		return name;
	}
	
	public String getSourceCode(){
		return sourceCode;
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
