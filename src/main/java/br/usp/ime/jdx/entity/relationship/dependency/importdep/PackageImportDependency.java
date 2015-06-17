package br.usp.ime.jdx.entity.relationship.dependency.importdep;

import br.usp.ime.jdx.entity.relationship.dependency.Dependency;
import br.usp.ime.jdx.entity.relationship.dependency.DependencyType;
import br.usp.ime.jdx.entity.system.CompUnit;
import br.usp.ime.jdx.entity.system.Package;

public class PackageImportDependency extends Dependency<CompUnit,Package>{
	
	private static final long serialVersionUID = -1207176703824885476L;

	public PackageImportDependency(CompUnit client, Package supplier) {
		super(client, supplier, "imports package");
	}
	
	public PackageImportDependency(CompUnit client, Package supplier, int strength) {
		super(client, supplier, "imports package", strength);
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.PACKAGE_IMPORT;
	}
	
}
