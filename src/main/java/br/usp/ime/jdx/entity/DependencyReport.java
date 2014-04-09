package br.usp.ime.jdx.entity;

import java.util.Collection;

import org.apache.commons.collections.map.MultiKeyMap;

public class DependencyReport {

	private MultiKeyMap dependenciesMap = new MultiKeyMap();
	
	public DependencyReport(){
		
	}
	
	public void addDependency(String client, String supplier){
		if (!dependenciesMap.containsKey(client, supplier)){
			Dependency dependency = new Dependency(client, supplier);
			dependenciesMap.put(client, supplier, dependency);
		}
		else{
			Dependency dependency = 
					(Dependency) dependenciesMap.get(client, supplier);
			
			dependency.increaseStrength();
		}
	}
	
	public Collection<Dependency> getDependencies(){
		return dependenciesMap.values();
	}

	public String toString(){
		StringBuilder builder = new StringBuilder();
		for (Dependency dependency : 
			(Collection<Dependency>) dependenciesMap.values()){
			
			builder.append(dependency.toString());
			builder.append("\n");
		}
		
		return builder.toString();
	}
}
