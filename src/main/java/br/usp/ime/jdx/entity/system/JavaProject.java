package br.usp.ime.jdx.entity.system;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class JavaProject implements Serializable {

	private static final long serialVersionUID = 5055713865573892829L;

	private List<String> sourceDirs;
	private Set<Package> packages;
	private Set<CompUnit> compUnits;
	private Set<Type> types;
	private Set<Method> methods;
	
	public JavaProject(List<String> sourceDirs, Set<Package> packages, Set<CompUnit> compUnits, 
			Set<Type> types, Set<Method> methods){
		
		this.sourceDirs = sourceDirs;
		this.packages = packages;
		this.compUnits = compUnits;
		this.types = types;
		this.methods = methods;
	}
	
	public List<String> getSourceDirs() {
		return sourceDirs;
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

	public CompUnit getCompUnit(String compUnitPath) {
		CompUnit searchedCompUnit = null;
		for(CompUnit compUnit : compUnits){
			if(compUnit.getPath().equals(compUnitPath)) {
				searchedCompUnit = compUnit;
				break;
			}
		}
		return searchedCompUnit;
	}
	
}