package br.usp.ime.jdx.entity.dependency;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.collections4.map.MultiKeyMap;

import br.usp.ime.jdx.entity.CompUnit;
import br.usp.ime.jdx.entity.Method;
import br.usp.ime.jdx.entity.Type;

public class DependencyReport implements Serializable{

	private static final long serialVersionUID = 2294342917700842739L;

	private MultiKeyMap<Type,TypeDependency> typeDependenciesMap = 
			new MultiKeyMap<>();
	
	private MultiKeyMap<Method,MethodCallDependency> callDependenciesMap = 
			new MultiKeyMap<>();
	
	public DependencyReport(){
		
	}
	
	public void addDependency(Type client, Type supplier){
		if (!typeDependenciesMap.containsKey(client, supplier)){
			TypeDependency dependency = new TypeDependency(client, supplier);
			typeDependenciesMap.put(client, supplier, dependency);
		}
		else{
			System.out.println("Warning: Trying to define two different "
					+ "relationships between types");
		}
	}
	
	public void addDependency(Method client, Method supplier){
		if (!callDependenciesMap.containsKey(client, supplier)){
			MethodCallDependency dependency = new MethodCallDependency(client, supplier);
			callDependenciesMap.put(client, supplier, dependency);
		}
		else{
			MethodCallDependency dependency = callDependenciesMap.get(client, supplier);
			dependency.increaseStrength();
		}
	}
	
	public Collection<MethodCallDependency> getMethodCallDependencies(){
		return callDependenciesMap.values();
	}
	
	public Collection<TypeDependency> getTypeDependencies(
			boolean inferFromMethodCalls){
		
		if(inferFromMethodCalls){
			Collection<TypeDependency> typeDependencies = 
					inferDependenciesFromMethodCalls();
			
			typeDependencies.addAll(this.typeDependenciesMap.values());
			return typeDependencies;
		}
		else{
			return typeDependenciesMap.values();
		}
	}
	
	private Collection<TypeDependency> inferDependenciesFromMethodCalls() {
		MultiKeyMap<Type,TypeDependency> typeDependenciesMap = 
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
					TypeDependency dep = 
							typeDependenciesMap.get(clientType,supplierType);
					
					dep.increaseStrength(methodCallDependency.getStrength());
				}
				else{
					TypeDependency typeDep = new TypeDependency(clientType, 
							supplierType, methodCallDependency.getStrength());
					
					typeDependenciesMap.put(clientType, supplierType, typeDep);
				}
			}
		}
		
		return typeDependenciesMap.values();
	}

	public Collection<Dependency<CompUnit>> getCompUnitDependencies(
			boolean inferFromMethodCalls){
		
		MultiKeyMap<CompUnit,Dependency<CompUnit>> cuDependenciesMap = 
				new MultiKeyMap<CompUnit,Dependency<CompUnit>>();
		
		Collection<TypeDependency> typeDependencies = getTypeDependencies(
				inferFromMethodCalls);
		
		for(TypeDependency typeDependency : typeDependencies){
			
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
	
	
	public Dependency<Type> getTypeDependency(
			String clientName, String supplierName){
		
			Collection<TypeDependency> typeDependencies = 
					getTypeDependencies(true);
			
			for(TypeDependency typeDependency : typeDependencies){
				
				if(typeDependency.getClient().getFQN().equals(clientName) && 
					typeDependency.getSupplier().getFQN().equals(supplierName)){
				
					return typeDependency;
				}
			}
			
			return null;
	}
	

	public String toString(){
		StringBuilder builder = new StringBuilder();
		for (Dependency<Method> dependency : callDependenciesMap.values()){
			
			builder.append(dependency.toString());
			builder.append("\n");
		}
		
		return builder.toString();
	}
}