package br.usp.ime.jdx.entity.dependency;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.collections4.map.MultiKeyMap;

import br.usp.ime.jdx.entity.CompUnit;
import br.usp.ime.jdx.entity.Method;
import br.usp.ime.jdx.entity.Type;

public class DependencyReport implements Serializable{

	private static final long serialVersionUID = 2294342917700842739L;

	private MultiKeyMap<Method,MethodCallDependency> dependenciesMap = 
			new MultiKeyMap<>();
	
	public DependencyReport(){
		
	}
	
	public void addDependency(Method client, Method supplier){
		if (!dependenciesMap.containsKey(client, supplier)){
			MethodCallDependency dependency = new MethodCallDependency(client, supplier);
			dependenciesMap.put(client, supplier, dependency);
		}
		else{
			MethodCallDependency dependency = dependenciesMap.get(client, supplier);
			dependency.increaseStrength();
		}
	}
	
	public Collection<MethodCallDependency> getMethodCallDependencies(){
		return dependenciesMap.values();
	}
	
	public Collection<Dependency<Type>> getTypeDependencies(){
		
		MultiKeyMap<Type,Dependency<Type>> typeDependenciesMap = 
				new MultiKeyMap<>();
		
		Collection<MethodCallDependency> methodCallDependencies = 
				getMethodCallDependencies();
		
		for(MethodCallDependency methodCallDependency : methodCallDependencies){
			
			Type clientType = 
					methodCallDependency.getClient().getContainingType();
			
			Type supplierType = 
					methodCallDependency.getSupplier().getContainingType();
			
			//Somente deps entre types diferentes
			if(!clientType.equals(supplierType)){
			
				if (typeDependenciesMap.containsKey(clientType,supplierType)){
					Dependency<Type> dep = 
							typeDependenciesMap.get(clientType,supplierType);
					
					dep.increaseStrength(methodCallDependency.getStrength());
				}
				else{
					Dependency<Type> typeDep = new Dependency<Type>(clientType, 
							supplierType, methodCallDependency.getStrength());
					
					typeDependenciesMap.put(clientType, supplierType, typeDep);
				}
			}
		}
		
		return typeDependenciesMap.values();
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
		
			Collection<Dependency<Type>> typeDependencies = getTypeDependencies();
			for(Dependency<Type> typeDependency : typeDependencies){
				
				if(typeDependency.getClient().getFQN().equals(clientName) && 
					typeDependency.getSupplier().getFQN().equals(supplierName)){
				
					return typeDependency;
				}
			}
			
			return null;
	}
	

	public String toString(){
		StringBuilder builder = new StringBuilder();
		for (Dependency<Method> dependency : dependenciesMap.values()){
			
			builder.append(dependency.toString());
			builder.append("\n");
		}
		
		return builder.toString();
	}
}