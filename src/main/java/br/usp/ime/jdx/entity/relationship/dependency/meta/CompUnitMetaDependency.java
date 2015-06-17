package br.usp.ime.jdx.entity.relationship.dependency.meta;

import java.util.HashSet;
import java.util.Set;

import br.usp.ime.jdx.entity.relationship.dependency.Dependency;
import br.usp.ime.jdx.entity.relationship.dependency.DependencyType;
import br.usp.ime.jdx.entity.system.CompUnit;
import br.usp.ime.jdx.entity.system.JavaElement;

public class CompUnitMetaDependency extends Dependency<CompUnit,CompUnit>{

	private static final long serialVersionUID = -3038818691650424132L;

	private Set<Dependency<? extends JavaElement, ? extends JavaElement>> dependencies = 
			new HashSet<>();
		
	public CompUnitMetaDependency(
			CompUnit client, CompUnit supplier) {
		
		super(client, supplier, "meta", 1);
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.COMP_UNIT_META_DEPENDENCY;
	}

	public void addDependency(Dependency<? extends JavaElement, ? extends JavaElement> dependency){
		this.dependencies.add(dependency);
	}
	
	public Set<Dependency<? extends JavaElement, ? extends JavaElement>> getDependencies(){
		return dependencies;
	}
	
}
