package br.usp.ime.jdx.entity.dependency;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.map.MultiKeyMap;

import br.usp.ime.jdx.entity.Clazz;
import br.usp.ime.jdx.entity.CompUnit;
import br.usp.ime.jdx.entity.Interface;
import br.usp.ime.jdx.entity.Method;
import br.usp.ime.jdx.entity.Type;

public class DependencyReport implements Serializable{

	private static final long serialVersionUID = 2294342917700842739L;

	private MultiKeyMap<Type,ClazzInheritanceDependency> 
		clazzInheritanceMap = new MultiKeyMap<>();
			
	private MultiKeyMap<Type,InterfaceInheritanceDependency> 
		interfaceInheritanceMap = new MultiKeyMap<>();
		
	private MultiKeyMap<Type,ImplementsDependency> implementsMap = 
			new MultiKeyMap<>();
	
	private MultiKeyMap<Method,MethodCallDependency> callMap = 
			new MultiKeyMap<>();
	
	public DependencyReport(){
		
	}
	
	public void addMethodCallDependency(Method client, Method supplier){
		if (!callMap.containsKey(client, supplier)){
			MethodCallDependency dependency = new MethodCallDependency(client, supplier);
			callMap.put(client, supplier, dependency);
		}
		else{
			MethodCallDependency dependency = callMap.get(client, supplier);
			dependency.increaseStrength();
		}
	}
	
	public void addClazzInheritanceDependency(Clazz client, Clazz supplier){
		
		//TODO: Check whether two clazz inheritances have the same client and 
		//throw an exception
		
		ClazzInheritanceDependency dependency = 
				new ClazzInheritanceDependency(client, supplier);

		clazzInheritanceMap.put(client, supplier, dependency);
	}
	
	public void addInterfaceInheritanceDependency(
			Interface client, Interface supplier){
			
		InterfaceInheritanceDependency dependency = 
				new InterfaceInheritanceDependency(client, supplier);
			
		interfaceInheritanceMap.put(client, supplier, dependency);
	}
	
	public void addImplementsDependency(Clazz client, Interface supplier){
		ImplementsDependency dependency = 
				new ImplementsDependency(client, supplier);

		implementsMap.put(client, supplier, dependency);	
	}
	
	public Collection<MethodCallDependency> getMethodCallDependencies(){
		return callMap.values();
	}
	
	public Collection<ClazzInheritanceDependency> 
		getClazzInheritanceDependencies(){
		
		return clazzInheritanceMap.values();
	}
	
	public Collection<InterfaceInheritanceDependency> 
		getInterfaceInheritanceDependencies(){
		
		return interfaceInheritanceMap.values();
	}
	
	public Collection<ImplementsDependency> getImplementsDependencies(){
		
		return implementsMap.values();
	}
	
	public Collection<TypeDependency> getTypeDependencies(){
		
		Set<TypeDependency> typeDependencies = new HashSet<>();
		typeDependencies.addAll(getClazzInheritanceDependencies());
		typeDependencies.addAll(getInterfaceInheritanceDependencies());
		typeDependencies.addAll(getImplementsDependencies());
		return typeDependencies;
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
		for (Dependency<Method> dependency : callMap.values()){
			
			builder.append(dependency.toString());
			builder.append("\n");
		}
		
		return builder.toString();
	}
}