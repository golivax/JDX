package br.usp.ime.jdx.entity.relationship.dependency.meta;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.map.MultiKeyMap;

import br.usp.ime.jdx.entity.relationship.dependency.Dependency;
import br.usp.ime.jdx.entity.relationship.dependency.DependencyReport;
import br.usp.ime.jdx.entity.relationship.dependency.importdep.MethodImportDependency;
import br.usp.ime.jdx.entity.relationship.dependency.importdep.TypeImportDependency;
import br.usp.ime.jdx.entity.system.CompUnit;
import br.usp.ime.jdx.entity.system.JavaElement;

public class CompUnitMetaDepFactory {
	
	public Collection<CompUnitMetaDependency> getCompUnitMetaDependencies(
			DependencyReport depReport, boolean skipSelfDependencies){	
	
		MultiKeyMap<CompUnit,CompUnitMetaDependency> compUnitMetaDepsMap = new MultiKeyMap<>();		
		
		//Type Meta Dependencies
		for(TypeMetaDependency typeMetaDep : depReport.getTypeMetaDependencies()){
						
			CompUnit clientCU = typeMetaDep.getClient().getParentCompUnit();
			CompUnit supplierCU = typeMetaDep.getSupplier().getParentCompUnit();
			
			Set<Dependency<? extends JavaElement, ? extends JavaElement>> deps = 
					typeMetaDep.getDependencies();
			
			if(!clientCU.equals(supplierCU) || !skipSelfDependencies) {
				this.addCompUnitMetaDep(compUnitMetaDepsMap, clientCU, supplierCU, deps);	
			}			
		}
		
		//CompUnit to Type import dependencies
		for(TypeImportDependency typeImportDep : 
			depReport.getTypeImportDependencies()){
			
			CompUnit clientCU = typeImportDep.getClient();
			CompUnit supplierCU = typeImportDep.getSupplier().getParentCompUnit();
			
			Set<Dependency<? extends JavaElement, ? extends JavaElement>> deps = 
					new HashSet<>();
			
			deps.add(typeImportDep);
			
			if(!clientCU.equals(supplierCU) || !skipSelfDependencies) {
				addCompUnitMetaDep(compUnitMetaDepsMap, clientCU, supplierCU, deps);
			}
		}
		
		//CompUnit to Method import dependencies
		for(MethodImportDependency methodImportDep : 
				depReport.getMethodImportDependencies()){
			
			CompUnit clientCU = methodImportDep.getClient();
			CompUnit supplierCU = methodImportDep.getSupplier().getParentCompUnit();
			
			Set<Dependency<? extends JavaElement, ? extends JavaElement>> deps = 
					new HashSet<>();
			
			deps.add(methodImportDep);
			
			if(!clientCU.equals(supplierCU) || !skipSelfDependencies) {
				addCompUnitMetaDep(compUnitMetaDepsMap, clientCU, supplierCU, deps);
			}
		}
		
		return compUnitMetaDepsMap.values();
	}
	
	private void addCompUnitMetaDep(
			MultiKeyMap<CompUnit,CompUnitMetaDependency> compUnitMetaDepsMap,
			CompUnit clientCU, CompUnit supplierCU, 
			Set<Dependency<? extends JavaElement, ? extends JavaElement>> deps){


		CompUnitMetaDependency compUnitMetaDep = 
				compUnitMetaDepsMap.get(clientCU,supplierCU);
		
		if (compUnitMetaDep != null){			

			for(Dependency<? extends JavaElement, ? extends JavaElement> dep : deps){
				compUnitMetaDep.addDependency(dep);
			}
			
		}
		else{
			compUnitMetaDep = 
					new CompUnitMetaDependency(clientCU, supplierCU);

			for(Dependency<? extends JavaElement, ? extends JavaElement> dep : deps){
				compUnitMetaDep.addDependency(dep);
			}

			compUnitMetaDepsMap.put(clientCU, supplierCU, compUnitMetaDep);
		}
	}
}