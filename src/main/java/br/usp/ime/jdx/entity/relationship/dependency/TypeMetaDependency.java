package br.usp.ime.jdx.entity.relationship.dependency;

import br.usp.ime.jdx.entity.Type;

public class TypeMetaDependency extends Dependency<Type,Type>{
	
	private static final long serialVersionUID = 737621518094256388L;
	
	public TypeMetaDependency(Type client, Type supplier, int strength) {
		super(client, supplier, "meta", strength);
	}

	@Override
	public DependencyType getDependencyType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//addDependency(Method etc)
}
