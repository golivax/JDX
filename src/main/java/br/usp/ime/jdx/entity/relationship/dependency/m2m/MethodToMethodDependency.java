package br.usp.ime.jdx.entity.relationship.dependency.m2m;

import br.usp.ime.jdx.entity.relationship.dependency.Dependency;
import br.usp.ime.jdx.entity.system.Method;

public abstract class MethodToMethodDependency extends Dependency<Method,Method>{

	private static final long serialVersionUID = -2768383342776173689L;

	public MethodToMethodDependency(Method client, Method supplier, String label) {
		super(client, supplier, label);
	}
	
	public MethodToMethodDependency(Method client, Method supplier, String label, int strength) {
		super(client, supplier, label, strength);
	}

}
