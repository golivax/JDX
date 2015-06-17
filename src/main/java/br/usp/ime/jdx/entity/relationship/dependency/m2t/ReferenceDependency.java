package br.usp.ime.jdx.entity.relationship.dependency.m2t;

import br.usp.ime.jdx.entity.relationship.dependency.DependencyType;
import br.usp.ime.jdx.entity.system.Method;
import br.usp.ime.jdx.entity.system.Type;

public class ReferenceDependency extends MethodToTypeDependency{
	
	private static final long serialVersionUID = 6559277010347073386L;

	public ReferenceDependency(Method client, Type supplier) {
		super(client, supplier, "references");
	}
	
	public ReferenceDependency(Method client, Type supplier, int strength) {
		super(client, supplier, "references", strength);
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.REFERENCE;
	}
	
}
