package br.usp.ime.jdx.entity.relationship;

import java.io.Serializable;

import br.usp.ime.jdx.entity.SystemEntity;

public abstract class Relationship<E extends SystemEntity, T extends SystemEntity> implements Serializable{

	private static final long serialVersionUID = -7918339901225257581L;
	
	public abstract RelationshipType getRelationshipType();
}
