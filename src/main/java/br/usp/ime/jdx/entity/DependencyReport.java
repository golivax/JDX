package br.usp.ime.jdx.entity;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.MultiKeyMap;

public class DependencyReport {

	private MultiKeyMap<Type,Dependency<Type>> dependenciesMap = 
			new MultiKeyMap<Type,Dependency<Type>>();
	
	public DependencyReport(){
		
	}
	
	public void addDependency(Type client, Type supplier){
		if (!dependenciesMap.containsKey(client, supplier)){
			Dependency<Type> dependency = new Dependency<Type>(client, supplier);
			dependenciesMap.put(client, supplier, dependency);
		}
		else{
			Dependency<Type> dependency = dependenciesMap.get(client, supplier);
			dependency.increaseStrength();
		}
	}
	
	public Collection<Dependency<Type>> getTypeDependencies(){
		return dependenciesMap.values();
	}
	
	public Collection<Dependency<CompUnit>> getCompUnitDependencies(){
		MultiKeyMap<CompUnit,Dependency<CompUnit>> cuDependenciesMap = 
				new MultiKeyMap<CompUnit,Dependency<CompUnit>>();
		
		Collection<Dependency<Type>> typeDependencies = getTypeDependencies();
		for(Dependency<Type> typeDependency : typeDependencies){
			
			CompUnit clientCU = typeDependency.getClient().getCompUnit();
			CompUnit supplierCU = typeDependency.getSupplier().getCompUnit();
			
			//Somente deps entre compUnits diferentes
			if(!clientCU.equals(supplierCU)){
			
				if (cuDependenciesMap.containsKey(clientCU,supplierCU)){
					Dependency<CompUnit> dep = cuDependenciesMap.get(clientCU,supplierCU);
					dep.increaseStrength(typeDependency.getStrength());
				}
				else{
					Dependency<CompUnit> dep = new Dependency<CompUnit>(
							clientCU, supplierCU, typeDependency.getStrength());
					cuDependenciesMap.put(clientCU, supplierCU, dep);
				}
			}
		}
		
		return cuDependenciesMap.values();
	}
	
	public Dependency<Type> getTypeDependency(String clientName, String supplierName){
		
			Dependency<Type> typeDependency = 
					CollectionUtils.find(dependenciesMap.values(),
					new DependencyPredicate<Type>(clientName,supplierName));
			
			return typeDependency;
	}

	public String toString(){
		StringBuilder builder = new StringBuilder();
		for (Dependency<Type> dependency : dependenciesMap.values()){
			
			builder.append(dependency.toString());
			builder.append("\n");
		}
		
		return builder.toString();
	}
}