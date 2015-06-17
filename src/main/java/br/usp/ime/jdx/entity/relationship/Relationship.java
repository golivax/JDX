package br.usp.ime.jdx.entity.relationship;

import java.io.Serializable;

import br.usp.ime.jdx.entity.system.JavaElement;

public abstract class Relationship<E extends JavaElement, T extends JavaElement> implements Serializable{

	private static final long serialVersionUID = -7918339901225257581L;
	
	public abstract RelationshipType getRelationshipType();
}
