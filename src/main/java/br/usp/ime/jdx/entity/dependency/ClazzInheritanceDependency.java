package br.usp.ime.jdx.entity.dependency;

import br.usp.ime.jdx.entity.Clazz;

public class ClazzInheritanceDependency extends TypeDependency{

	private static final long serialVersionUID = 205553691436632455L;
	
	public ClazzInheritanceDependency(Clazz client, Clazz supplier) {
		super(client, supplier, "extends");
	}
	
}
