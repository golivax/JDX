package br.usp.ime.jdx.entity;

import java.util.Set;

public class JavaProject {

	private Set<Package> packages;
	private Set<CompUnit> compUnits;
	private Set<Type> types;
	private Set<Method> methods;
	
	public JavaProject(Set<Package> packages, Set<CompUnit> compUnits, 
			Set<Type> types, Set<Method> methods){
		
		this.packages = packages;
		this.compUnits = compUnits;
		this.types = types;
		this.methods = methods;
	}
	
	public Set<Package> getPackages(){
		return packages;
	}

	public Set<CompUnit> getCompUnits() {
		return compUnits;
	}

	public Set<Type> getTypes() {
		return types;
	}

	public Set<Method> getMethods() {
		return methods;
	}
	
	
}
