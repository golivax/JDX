package br.usp.ime.jdx.entity;

public class Dependency {

	
	private String client;
	private String supplier;
	private Integer strength;
	
	public Dependency(String client, String supplier){
		
		this.client = client;
		this.supplier = supplier;
		this.strength = 1;
		
	}

	public String getClient() {
		return client;
	}

	public String getSupplier() {
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
