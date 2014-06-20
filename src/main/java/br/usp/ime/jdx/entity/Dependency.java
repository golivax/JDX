package br.usp.ime.jdx.entity;

public class Dependency<E extends NamedEntity> {

	private E client;
	private E supplier;
	private Integer strength;
	
	public Dependency(E client, E supplier){
		this.client = client;
		this.supplier = supplier;
		this.strength = 1;
	}
	
	public Dependency(E client, E supplier, int strength){
		this.client = client;
		this.supplier = supplier;
		this.strength = strength;
	}

	public E getClient() {
		return client;
	}

	public E getSupplier() {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime * result
				+ ((supplier == null) ? 0 : supplier.hashCode());
		return result;
	}

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
		if (supplier == null) {
			if (other.supplier != null)
				return false;
		} else if (!supplier.equals(other.supplier))
			return false;
		return true;
	}

	@Override
	public String toString(){
		return "(" + client + "," + supplier + "," + strength + ")";
	}
	
}
