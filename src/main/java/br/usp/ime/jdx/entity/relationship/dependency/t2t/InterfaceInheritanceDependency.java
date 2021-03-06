package br.usp.ime.jdx.entity.relationship.dependency.t2t;

import br.usp.ime.jdx.entity.relationship.dependency.DependencyType;
import br.usp.ime.jdx.entity.system.Interface;

public class InterfaceInheritanceDependency extends TypeToTypeDependency{

	private static final long serialVersionUID = -8571413829767869303L;

	public InterfaceInheritanceDependency(Interface client, Interface supplier) {
		super(client, supplier, "extends");
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.INTERFACE_INHERITANCE;
	}
	
}
