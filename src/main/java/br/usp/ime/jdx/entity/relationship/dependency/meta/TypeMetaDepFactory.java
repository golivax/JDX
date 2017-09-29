package br.usp.ime.jdx.entity.relationship.dependency.meta;

import java.util.Collection;

import org.apache.commons.collections4.map.MultiKeyMap;

import br.usp.ime.jdx.entity.relationship.dependency.Dependency;
import br.usp.ime.jdx.entity.relationship.dependency.DependencyReport;
import br.usp.ime.jdx.entity.relationship.dependency.m2m.MethodToMethodDependency;
import br.usp.ime.jdx.entity.relationship.dependency.m2t.MethodToTypeDependency;
import br.usp.ime.jdx.entity.relationship.dependency.t2t.TypeToTypeDependency;
import br.usp.ime.jdx.entity.system.JavaElement;
import br.usp.ime.jdx.entity.system.Type;

public class TypeMetaDepFactory {
	
	public Collection<TypeMetaDependency> getTypeMetaDependencies(DependencyReport depReport){
	
		MultiKeyMap<Type,TypeMetaDependency> typeMetaDepsMap = new MultiKeyMap<>();
		
		for(MethodToMethodDependency m2mDep : 
				depReport.getMethodToMethodDependencies()){
			
			Type clientType = 
					m2mDep.getClient().getParentType();
						
			Type supplierType = 
					m2mDep.getSupplier().getParentType();
			
			addTypeMetaDep(typeMetaDepsMap, clientType, supplierType, m2mDep);
		}
		
		for(MethodToTypeDependency m2tDep : 
			depReport.getMethodToTypeDependencies()){
		
			Type clientType = 
					m2tDep.getClient().getParentType();
						
			Type supplierType = 
					m2tDep.getSupplier();
			
			if(supplierType == null){
				System.out.println("m2t - Null supplier");
				System.out.println(m2tDep);
				System.out.println(clientType.getParentCompUnit().getRelativePath());
			}
			
			addTypeMetaDep(typeMetaDepsMap, clientType, supplierType, m2tDep);
		}
					
		for(TypeToTypeDependency t2tDep : 
				depReport.getTypeToTypeDependencies()){
			
			Type clientType = t2tDep.getClient();
			Type supplierType = t2tDep.getSupplier();
			
			if(supplierType == null){
				System.out.println("t2t - Null supplier");
				System.out.println(t2tDep);
				System.out.println(clientType.getParentCompUnit().getRelativePath());
			}
			
			addTypeMetaDep(typeMetaDepsMap, clientType, supplierType, t2tDep);
		}
		
		//No import dependencies
		
		return typeMetaDepsMap.values();
	}
	
	private void addTypeMetaDep(MultiKeyMap<Type,TypeMetaDependency> typeMetaDepsMap, 
			Type clientType, Type supplierType, 
			Dependency<? extends JavaElement, ? extends JavaElement> dependency){

		
		TypeMetaDependency typeMetaDep = 
				typeMetaDepsMap.get(clientType,supplierType);
		
		if (typeMetaDep != null){	
			typeMetaDep.addDependency(dependency);
		}
		else{			
			typeMetaDep = new TypeMetaDependency(clientType, supplierType);
			typeMetaDep.addDependency(dependency);
			typeMetaDepsMap.put(clientType, supplierType, typeMetaDep);
		}
	}

}
