package br.usp.ime.jdx.entity.relationship.dependency.m2t;

import br.usp.ime.jdx.entity.relationship.dependency.Dependency;
import br.usp.ime.jdx.entity.system.Method;
import br.usp.ime.jdx.entity.system.Type;

public abstract class MethodToTypeDependency extends Dependency<Method,Type>{

	private static final long serialVersionUID = 6006682481741583808L;

	public MethodToTypeDependency(Method client, Type supplier, String label) {
		super(client, supplier, label);
	}
	
	public MethodToTypeDependency(Method client, Type supplier, String label, int strength) {
		super(client, supplier, label, strength);
	}

}