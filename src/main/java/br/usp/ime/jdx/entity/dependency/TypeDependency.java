package br.usp.ime.jdx.entity.dependency;

import br.usp.ime.jdx.entity.Type;

public class TypeDependency extends Dependency<Type>{

	private static final long serialVersionUID = -3038818691650424132L;

	public TypeDependency(Type client, Type supplier) {
		super(client, supplier);
	}

	public TypeDependency(Type client, Type supplier, Integer strength) {
		super(client, supplier, strength);
	}

}
