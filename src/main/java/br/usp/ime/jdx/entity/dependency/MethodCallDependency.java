package br.usp.ime.jdx.entity.dependency;

import br.usp.ime.jdx.entity.Method;

public class MethodCallDependency extends Dependency<Method>{

	private static final long serialVersionUID = -7891072247689345065L;

	public MethodCallDependency(Method client, Method supplier) {
		super(client, supplier);
	}
	
	public MethodCallDependency(Method client, Method supplier, int strength) {
		super(client, supplier, strength);
	}

}
