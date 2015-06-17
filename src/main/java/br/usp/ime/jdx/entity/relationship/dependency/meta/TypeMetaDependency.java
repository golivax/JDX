package br.usp.ime.jdx.entity.relationship.dependency.meta;

import java.util.HashSet;
import java.util.Set;

import br.usp.ime.jdx.entity.relationship.dependency.Dependency;
import br.usp.ime.jdx.entity.relationship.dependency.DependencyType;
import br.usp.ime.jdx.entity.system.JavaElement;
import br.usp.ime.jdx.entity.system.Type;

public class TypeMetaDependency extends Dependency<Type,Type>{
	
	private static final long serialVersionUID = 737621518094256388L;
	
	private Set<Dependency<? extends JavaElement, ? extends JavaElement>> dependencies = 
			new HashSet<>();
	
	public TypeMetaDependency(Type client, Type supplier) {
		super(client, supplier, "meta", 1);
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.TYPE_META_DEPENDENCY;
	}
	
	public void addDependency(Dependency<? extends JavaElement, ? extends JavaElement> dependency){
		this.dependencies.add(dependency);
	}
	
	public Set<Dependency<? extends JavaElement, ? extends JavaElement>> getDependencies(){
		return dependencies;
	}
	
}