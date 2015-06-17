package br.usp.ime.jdx.entity.relationship.dependency.importdep;

import br.usp.ime.jdx.entity.relationship.dependency.Dependency;
import br.usp.ime.jdx.entity.relationship.dependency.DependencyType;
import br.usp.ime.jdx.entity.system.CompUnit;
import br.usp.ime.jdx.entity.system.Type;

public class TypeImportDependency extends Dependency<CompUnit,Type>{

	private static final long serialVersionUID = 1240209113058480204L;
	

	public TypeImportDependency(CompUnit client, Type supplier) {
		super(client, supplier, "imports type");
	}
	
	public TypeImportDependency(CompUnit client, Type supplier, int strength) {
		super(client, supplier, "imports type", strength);
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.TYPE_IMPORT;
	}
	
}
