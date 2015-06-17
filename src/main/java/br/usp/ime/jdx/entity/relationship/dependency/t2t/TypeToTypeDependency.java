package br.usp.ime.jdx.entity.relationship.dependency.t2t;

import br.usp.ime.jdx.entity.relationship.dependency.Dependency;
import br.usp.ime.jdx.entity.system.Type;

public abstract class TypeToTypeDependency extends Dependency<Type,Type>{

	private static final long serialVersionUID = -3038818691650424132L;

	public TypeToTypeDependency(Type client, Type supplier, String label) {
		super(client, supplier, label);
	}

}
