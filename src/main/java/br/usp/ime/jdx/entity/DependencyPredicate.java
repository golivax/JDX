package br.usp.ime.jdx.entity;

import org.apache.commons.collections4.Predicate;

public class DependencyPredicate<E extends NamedEntity> implements Predicate<Dependency<E>>{

	private String clientName;
	private String supplierName;
	
	public DependencyPredicate(String clientName, String supplierName){
		this.clientName = clientName;
		this.supplierName = supplierName;
	}

	public boolean evaluate(Dependency<E> dep) {
		if(clientName.equals(dep.getClient().getName()) &&
		   supplierName.equals(dep.getSupplier().getName())){
			
			return true;	
		}
		else{
			return false;
		}
	}	

}
