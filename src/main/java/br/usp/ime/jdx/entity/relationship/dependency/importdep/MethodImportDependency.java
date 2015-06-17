package br.usp.ime.jdx.entity.relationship.dependency.importdep;

import br.usp.ime.jdx.entity.relationship.dependency.Dependency;
import br.usp.ime.jdx.entity.relationship.dependency.DependencyType;
import br.usp.ime.jdx.entity.system.CompUnit;
import br.usp.ime.jdx.entity.system.Method;

public class MethodImportDependency extends Dependency<CompUnit,Method>{
	
	private static final long serialVersionUID = 9115557643328082540L;

	public MethodImportDependency(CompUnit client, Method supplier) {
		super(client, supplier, "statically imports");
	}
	
	public MethodImportDependency(CompUnit client, Method supplier, int strength) {
		super(client, supplier, "statically imports", strength);
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.STATIC_IMPORT;
	}
	
}
