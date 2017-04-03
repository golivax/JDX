package br.usp.ime.jdx.entity.system;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaProject implements Serializable {

	private static final long serialVersionUID = 5055713865573892829L;

	private List<String> sourceDirs;
	private Set<Package> packages;
	private Map<String,CompUnit> compUnitMap;
	private Set<Type> types;
	private Set<Method> methods;
	
	public JavaProject(List<String> sourceDirs, Set<Package> packages, Set<CompUnit> compUnits, 
			Set<Type> types, Set<Method> methods){
		
		this.sourceDirs = sourceDirs;
		this.packages = packages;
		this.types = types;
		this.methods = methods;
		
		this.compUnitMap = new HashMap<>();
		for(CompUnit compUnit : compUnits){
			this.compUnitMap.put(compUnit.getPath(), compUnit);
		}
	}
	
	public List<String> getSourceDirs() {
		return sourceDirs;
	}
	
	public Set<Package> getPackages(){
		return packages;
	}

	public Set<CompUnit> getCompUnits() {
		return new HashSet<>(compUnitMap.values());
	}

	public Set<Type> getTypes() {
		return types;
	}

	public Set<Method> getMethods() {
		return methods;
	}

	public CompUnit getCompUnit(String compUnitPath) {
		return compUnitMap.get(compUnitPath);
	}
	
}