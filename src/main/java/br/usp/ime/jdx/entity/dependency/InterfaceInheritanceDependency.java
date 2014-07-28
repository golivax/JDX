package br.usp.ime.jdx.entity.dependency;

import br.usp.ime.jdx.entity.Interface;

public class InterfaceInheritanceDependency extends TypeDependency{

	private static final long serialVersionUID = -8571413829767869303L;

	public InterfaceInheritanceDependency(Interface client, Interface supplier) {
		super(client, supplier, "extends");
	}
	
}
