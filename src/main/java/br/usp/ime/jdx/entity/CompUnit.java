package br.usp.ime.jdx.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import br.usp.ime.jdx.util.compress.StringCompressor;

public class CompUnit implements Serializable{

	private static final long serialVersionUID = 5569390666734073800L;

	private Package pkg;
	private String path;
	private byte[] sourceCode;
	private Set<Type> types;
	
	public CompUnit(Package pkg, String path, String sourceCode){
		
		this.pkg = pkg;
		
		//Always replace back slashes with forward slashes
		this.path = path.replaceAll("\\\\", "/");
		
		if(sourceCode != null){
			this.sourceCode = StringCompressor.compress(sourceCode);
		}
		
		types = new HashSet<Type>();
	}
	
	public Package getPackage(){
		return pkg;
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
	
	public String getPath(){
		return path;
	}
	
	public String getSourceCode(){
		return StringCompressor.decompress(sourceCode);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
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
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return path;
	}
	
	public Set<Method> getMethods(){

		Set<Method> methodsFromTypes = new HashSet<>();
		for(Type type : types){
			methodsFromTypes.addAll(type.getMethods());
		}
		return methodsFromTypes;
	}
}
