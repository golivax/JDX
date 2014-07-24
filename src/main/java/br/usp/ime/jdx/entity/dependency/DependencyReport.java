package br.usp.ime.jdx.entity.dependency;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.collections4.map.MultiKeyMap;

import br.usp.ime.jdx.entity.Clazz;
import br.usp.ime.jdx.entity.CompUnit;
import br.usp.ime.jdx.entity.Interface;
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
	
	public void addInheritanceDependency(Clazz client, Clazz supplier){
		if (!typeDependenciesMap.containsKey(client, supplier)){
			
			InheritanceDependency dependency = 
					new InheritanceDependency(client, supplier);
			
			typeDependenciesMap.put(client, supplier, dependency);
		}
		else{
			System.out.println("Warning: Trying to define two different "
					+ "relationships between the same types");
		}
	}
	
	public void addInheritanceDependency(Interface client, Interface supplier){
		if (!typeDependenciesMap.containsKey(client, supplier)){
			
			InheritanceDependency dependency = 
					new InheritanceDependency(client, supplier);
			
			typeDependenciesMap.put(client, supplier, dependency);
		}
		else{
			System.out.println("Warning: Trying to define two different "
					+ "relationships between the same types");
		}
	}
	
	public void addImplementsDependency(Clazz client, Interface supplier){
		if (!typeDependenciesMap.containsKey(client, supplier)){
			
			ImplementsDependency dependency = 
					new ImplementsDependency(client, supplier);
			
			typeDependenciesMap.put(client, supplier, dependency);
		}
		else{
			System.out.println("Warning: Trying to define two different "
					+ "relationships between the same types");
		}
	}

	public void addMethodCallDependency(Method client, Method supplier){
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
	
	public Collection<TypeDependency> getTypeDependencies(){
		return typeDependenciesMap.values();
	}
	
	public Collection<TypeMetaDependency> getTypeMetaDependencies(){
		
		MultiKeyMap<Type,TypeMetaDependency> typeMetaDepsMap = 
				new MultiKeyMap<>();
		
		for(TypeDependency typeDependency : getTypeDependencies()){
			
			Type client = typeDependency.getClient();
			Type supplier = typeDependency.getSupplier();
			int strength = typeDependency.getStrength();
			
			TypeMetaDependency typeMetaDep = 
					new TypeMetaDependency(client,supplier,strength);
			
			typeMetaDepsMap.put(client,supplier,typeMetaDep);
		}
		
		inferDependenciesFromMethodCalls(typeMetaDepsMap);
		
		return typeMetaDepsMap.values();
	}
	
	private void inferDependenciesFromMethodCalls(
			MultiKeyMap<Type, TypeMetaDependency> typeMetaDepsMap) {
		
		Collection<MethodCallDependency> methodCallDependencies = 
				getMethodCallDependencies();
		
		for(MethodCallDependency methodCallDependency : methodCallDependencies){
			
			Type clientType = 
					methodCallDependency.getClient().getContainingType();
			
			Type supplierType = 
					methodCallDependency.getSupplier().getContainingType();
			
			int strength = methodCallDependency.getStrength();
			
			//Somente deps entre types diferentes
			if(!clientType.equals(supplierType)){
			
				if (typeMetaDepsMap.containsKey(clientType,supplierType)){
					
					TypeMetaDependency typeMetaDep = 
							typeMetaDepsMap.get(clientType,supplierType);
					
					typeMetaDep.increaseStrength(strength);
				}
				else{
					TypeMetaDependency typeMetaDep = new TypeMetaDependency(
							clientType, supplierType, strength);
					
					typeMetaDepsMap.put(clientType, supplierType, typeMetaDep);
				}
			}
		}
	}

	public Collection<CompUnitMetaDependency> getCompUnitMetaDependencies(){
		
		MultiKeyMap<CompUnit,CompUnitMetaDependency> cuDependenciesMap = 
				new MultiKeyMap<CompUnit,CompUnitMetaDependency>();
		
		Collection<TypeMetaDependency> typeMetaDependencies = 
				getTypeMetaDependencies();
				
		for(TypeMetaDependency typeDependency : typeMetaDependencies){
			
			CompUnit clientCU = typeDependency.getClient().getCompUnit();
			CompUnit supplierCU = typeDependency.getSupplier().getCompUnit();
			int strength = typeDependency.getStrength();
			
			//Somente deps entre compUnits diferentes
			if(!clientCU.equals(supplierCU)){
			
				if (cuDependenciesMap.containsKey(clientCU,supplierCU)){
					
					CompUnitMetaDependency dep = 
							cuDependenciesMap.get(clientCU,supplierCU);
					
					dep.increaseStrength(strength);
				}
				else{
					CompUnitMetaDependency dep = new CompUnitMetaDependency(
							clientCU, supplierCU, strength);
					
					cuDependenciesMap.put(clientCU, supplierCU, dep);
				}
			}
		}
		
		return cuDependenciesMap.values();
	}

	public TypeMetaDependency getTypeMetaDependency(
			String clientName, String supplierName){
		
		Collection<TypeMetaDependency> metaTypeDeps = 
				getTypeMetaDependencies();
		
		for(TypeMetaDependency typeMetaDep : metaTypeDeps){
			
			if(typeMetaDep.getClient().getFQN().equals(clientName) && 
				typeMetaDep.getSupplier().getFQN().equals(supplierName)){
			
				return typeMetaDep;
			}
		}
		
		return null;
	}
	
	public TypeDependency getTypeDependency(
			String clientName, String supplierName){
		
			Collection<TypeDependency> typeDependencies = getTypeDependencies();
			
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