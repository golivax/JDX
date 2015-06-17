package br.usp.ime.jdx.entity.relationship.dependency;

import br.usp.ime.jdx.entity.Interface;

public class InterfaceInheritanceDependency extends TypeDependency{

	private static final long serialVersionUID = -8571413829767869303L;

	public InterfaceInheritanceDependency(Interface client, Interface supplier) {
		super(client, supplier, "extends");
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.INTERFACE_INHERITANCE;
	}
	
}
