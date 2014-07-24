package br.usp.ime.jdx.entity.dependency;

import br.usp.ime.jdx.entity.CompUnit;

public class CompUnitMetaDependency extends Dependency<CompUnit>{

	private static final long serialVersionUID = -3038818691650424132L;

	public CompUnitMetaDependency(
			CompUnit client, CompUnit supplier, int strength) {
		
		super(client, supplier, "meta", strength);
	}

}
