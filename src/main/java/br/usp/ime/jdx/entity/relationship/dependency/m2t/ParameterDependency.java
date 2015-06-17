package br.usp.ime.jdx.entity.relationship.dependency.m2t;

import br.usp.ime.jdx.entity.relationship.dependency.DependencyType;
import br.usp.ime.jdx.entity.system.Method;
import br.usp.ime.jdx.entity.system.Type;

public class ParameterDependency extends MethodToTypeDependency{
	
	private static final long serialVersionUID = -6201193398598335963L;

	public ParameterDependency(Method client, Type supplier) {
		super(client, supplier, "is parameterized with");
	}
	
	public ParameterDependency(Method client, Type supplier, int strength) {
		super(client, supplier, "is parameterized with", strength);
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.METHOD_PARAM;
	}
	
}
