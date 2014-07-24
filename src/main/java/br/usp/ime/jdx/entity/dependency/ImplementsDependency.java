package br.usp.ime.jdx.entity.dependency;

import br.usp.ime.jdx.entity.Clazz;
import br.usp.ime.jdx.entity.Interface;

public class ImplementsDependency extends TypeDependency{

	private static final long serialVersionUID = 205553691436632455L;
	
	public ImplementsDependency(Clazz client, Interface supplier) {
		super(client, supplier, "implements");
	}	

}
