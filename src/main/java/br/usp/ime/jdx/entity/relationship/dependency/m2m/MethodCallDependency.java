package br.usp.ime.jdx.entity.relationship.dependency;

import br.usp.ime.jdx.entity.Method;

public class MethodCallDependency extends Dependency<Method,Method>{
	
	private static final long serialVersionUID = -7891072247689345065L;
	
	public MethodCallDependency(Method client, Method supplier) {
		super(client, supplier, "calls");
	}
	
	public MethodCallDependency(Method client, Method supplier, int strength) {
		super(client, supplier, "calls", strength);
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.METHOD_CALL;
	}
	
}
