package br.usp.ime.jdx.entity;

public class Dependency<E> {

	
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
	
	public String toString(){
		return "(" + client + "," + supplier + "," + strength + ")";
	}
	
}
