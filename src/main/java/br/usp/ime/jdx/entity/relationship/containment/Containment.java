package br.usp.ime.jdx.entity.relationship.containment;

import java.io.Serializable;

import br.usp.ime.jdx.entity.relationship.Relationship;
import br.usp.ime.jdx.entity.relationship.RelationshipType;
import br.usp.ime.jdx.entity.system.JavaElement;

public class Containment<E extends JavaElement,T extends JavaElement> extends Relationship<E,T> implements Serializable{

	private static final long serialVersionUID = -320459234337627245L;

	private E owner;
	private T owned;
	protected String label = new String("contains");
	
	public Containment(E owner, T owned){
		this.owner = owner;
		this.owned = owned;
	}
	
	public Containment(E owner, T owned, String label){
		this(owner,owned);
		this.label = label;
	}

	public E getOwner() {
		return owner;
	}

	public T getOwned() {
		return owned;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString(){
		return "(" + owner + "," + label + "," + owned + ")";
	}
	
	@Override
	public RelationshipType getRelationshipType(){
		return RelationshipType.CONTAINMENT;
	}
	
}
