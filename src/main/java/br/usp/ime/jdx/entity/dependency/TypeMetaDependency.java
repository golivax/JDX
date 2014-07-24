package br.usp.ime.jdx.entity.dependency;

import br.usp.ime.jdx.entity.Type;

public class TypeMetaDependency extends Dependency<Type>{
	
	private static final long serialVersionUID = 737621518094256388L;
	
	public TypeMetaDependency(Type client, Type supplier, int strength) {
		super(client, supplier, "meta", strength);
	}
	
	//addDependency(Method etc)
}
