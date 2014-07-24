package br.usp.ime.jdx.entity.dependency;

import br.usp.ime.jdx.entity.Clazz;
import br.usp.ime.jdx.entity.Interface;

public class InheritanceDependency extends TypeDependency{

	private static final long serialVersionUID = 205553691436632455L;
	
	public InheritanceDependency(Clazz client, Clazz supplier) {
		super(client, supplier, "extends");
	}
	
	public InheritanceDependency(Interface client, Interface supplier) {
		super(client, supplier, "extends");
	}
	
}
