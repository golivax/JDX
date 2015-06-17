package br.usp.ime.jdx.entity.relationship.dependency.m2t;

import br.usp.ime.jdx.entity.relationship.dependency.DependencyType;
import br.usp.ime.jdx.entity.system.Method;
import br.usp.ime.jdx.entity.system.Type;

public class ReturnDependency extends MethodToTypeDependency{

	private static final long serialVersionUID = -1761682976171581027L;

	public ReturnDependency(Method client, Type supplier) {
		super(client, supplier, "returns");
	}
	
	public ReturnDependency(Method client, Type supplier, int strength) {
		super(client, supplier, "returns", strength);
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.METHOD_RETURN;
	}
	
}
