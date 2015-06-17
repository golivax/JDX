package br.usp.ime.jdx.entity.relationship.dependency.t2t;

import br.usp.ime.jdx.entity.relationship.dependency.DependencyType;
import br.usp.ime.jdx.entity.system.Clazz;

public class ClazzInheritanceDependency extends TypeToTypeDependency{

	private static final long serialVersionUID = 205553691436632455L;
	
	public ClazzInheritanceDependency(Clazz client, Clazz supplier) {
		super(client, supplier, "extends");
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.CLASS_INHERITANCE;
	}
	
}
