package br.usp.ime.jdx.entity.relationship.dependency.m2t;

import br.usp.ime.jdx.entity.relationship.dependency.DependencyType;
import br.usp.ime.jdx.entity.system.Method;
import br.usp.ime.jdx.entity.system.Type;

public class ThrowDependency extends MethodToTypeDependency{

	private static final long serialVersionUID = -1761682976171581027L;

	public ThrowDependency(Method client, Type supplier) {
		super(client, supplier, "throws");
	}
	
	public ThrowDependency(Method client, Type supplier, int strength) {
		super(client, supplier, "throws", strength);
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.THROWN_EXCEPTION;
	}
	
}
