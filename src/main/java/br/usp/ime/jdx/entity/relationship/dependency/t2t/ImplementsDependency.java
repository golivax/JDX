package br.usp.ime.jdx.entity.relationship.dependency.t2t;

import br.usp.ime.jdx.entity.relationship.dependency.DependencyType;
import br.usp.ime.jdx.entity.system.Clazz;
import br.usp.ime.jdx.entity.system.Interface;

public class ImplementsDependency extends TypeToTypeDependency{

	private static final long serialVersionUID = 205553691436632455L;
	
	public ImplementsDependency(Clazz client, Interface supplier) {
		super(client, supplier, "implements");
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.INTERFACE_IMPLEMENTATION;
	}	

}
