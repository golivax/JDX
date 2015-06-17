package br.usp.ime.jdx.entity.relationship.dependency;

import java.io.Serializable;

import br.usp.ime.jdx.entity.relationship.Relationship;
import br.usp.ime.jdx.entity.relationship.RelationshipType;
import br.usp.ime.jdx.entity.system.JavaElement;

public abstract class Dependency<E extends JavaElement,T extends JavaElement> extends Relationship<E,T> implements Serializable{

	private static final long serialVersionUID = -320459234337627245L;

	private E client;
	private T supplier;
	private Integer strength = 1;
	protected String label = new String("depends");
	
	public Dependency(E client, T supplier){
		this.client = client;
		this.supplier = supplier;
	}
	
	public Dependency(E client, T supplier, int strength){
		this(client,supplier);
		this.strength = strength;
	}
	
	public Dependency(E client, T supplier, String label){
		this(client,supplier);
		this.label = label;
	}
	
	public Dependency(E client, T supplier, String label, int strength){
		this(client,supplier,strength);
		this.label = label;
	}

	public E getClient() {
		return client;
	}

	public T getSupplier() {
		return supplier;
	}

	public Integer getStrength() {
		return strength;
	}

	public void increaseStrength(){
		strength++;
	}
	
	public void increaseStrength(int n){
		strength = strength + n;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime * result
				+ ((getDependencyType() == null) ? 0 : getDependencyType().hashCode());
		result = prime * result
				+ ((supplier == null) ? 0 : supplier.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dependency other = (Dependency) obj;
		if (client == null) {
			if (other.client != null)
				return false;
		} else if (!client.equals(other.client))
			return false;
		if (getDependencyType() != other.getDependencyType())
			return false;
		if (supplier == null) {
			if (other.supplier != null)
				return false;
		} else if (!supplier.equals(other.supplier))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return "(" + client + "," + label + "," + supplier + "," + strength + ")";
	}
	
	public final RelationshipType getRelationshipType(){
		return RelationshipType.DEPENDENCY;
	}
	
	public abstract DependencyType getDependencyType();
}
