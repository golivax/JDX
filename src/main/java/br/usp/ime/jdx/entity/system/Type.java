package br.usp.ime.jdx.entity.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public abstract class Type implements Serializable, JavaElement{

	private static final long serialVersionUID = -7126906063529157990L;

	private String fqn;
	private String sourceCode;
	private int[] sourceCodeLocation;
	private CompUnit compUnit;
	private Set<Method> methods;
	
	public Type(String fqn, int[] sourceCodeLocation) {
		this.fqn = fqn;
		this.sourceCodeLocation = sourceCodeLocation;
		this.methods = new HashSet<Method>();
	}
	
	public Type(String fqn, String sourceCode){
		this.fqn = fqn;
		this.sourceCode = sourceCode;
		this.methods = new HashSet<Method>();
	}
	
	public String getName(){
		return StringUtils.substringAfterLast(fqn, ".");
	}
	
	public String getFQN(){
		return fqn;
	}
	
	public String getSourceCode(){
		if(sourceCode != null) return sourceCode;
		
		String compUnitSourceCode = this.getCompUnit().getSourceCode();
		String sourceCode = compUnitSourceCode.substring(sourceCodeLocation[0], sourceCodeLocation[1]);
		return sourceCode;
	}
	
	public CompUnit getCompUnit(){
		return compUnit;
	}
	
	public void setCompUnit(CompUnit compUnit){
		this.compUnit = compUnit;
	}
	
	public void addMethod(Method method){
		this.methods.add(method);
		method.setContainingType(this);
	}

	@Override
	public String toString(){
		return getFQN();
	}
	
	public Set<Method> getConstructors(){
		Set<Method> constructors = new HashSet<>();
		for(Method method : methods){
			if(method.isConstructor()) {
				constructors.add(method);
			}
		}
		return constructors;
	}
	
	public Set<Method> getMethods(){
		return methods;
	}
	
	public List<Method> getMethods(String methodName) {
		List<Method> methodsFound = new ArrayList<>();
		for (Method method : methods){
			
			if(method.getName().equals(methodName)){
				methodsFound.add(method);
			}
		}
		return methodsFound;
	}

	public Method getMethod(String methodName, List<String> parameters) {
		for (Method method : getMethods(methodName)){			
			if(method.getParameters().equals(parameters)){				
				return method;
			}
		}
		return null;		
	}

	//TODO: Criar atributo para guardar attrib method
	public Method getAttribMethod() {
		return getMethod("attrib<>", new ArrayList<String>());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((compUnit == null) ? 0 : compUnit.hashCode());
		result = prime * result + ((fqn == null) ? 0 : fqn.hashCode());
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
		if (compUnit == null) {
			if (other.compUnit != null)
				return false;
		} else if (!compUnit.equals(other.compUnit))
			return false;
		if (fqn == null) {
			if (other.fqn != null)
				return false;
		} else if (!fqn.equals(other.fqn))
			return false;
		return true;
	}
}