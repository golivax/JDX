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
		Type other = (Type) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString(){
		return getName();
	}

}
