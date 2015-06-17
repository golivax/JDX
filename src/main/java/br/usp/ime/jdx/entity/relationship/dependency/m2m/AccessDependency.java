package br.usp.ime.jdx.entity.relationship.dependency.m2m;

import br.usp.ime.jdx.entity.relationship.dependency.DependencyType;
import br.usp.ime.jdx.entity.system.Method;

public class AccessDependency extends MethodToMethodDependency{

	private static final long serialVersionUID = 1171398161919435167L;

	public AccessDependency(Method client, Method supplier) {
		super(client, supplier, "accesses");
	}
	
	public AccessDependency(Method client, Method supplier, int strength) {
		super(client, supplier, "accesses", strength);
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.ACCESS;
	}
	
}
