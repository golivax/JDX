package br.usp.ime.jdx.entity.dependency;

import br.usp.ime.jdx.entity.Type;

public abstract class TypeDependency extends Dependency<Type>{

	private static final long serialVersionUID = -3038818691650424132L;

	public TypeDependency(Type client, Type supplier, String label) {
		super(client, supplier, label);
	}

}
