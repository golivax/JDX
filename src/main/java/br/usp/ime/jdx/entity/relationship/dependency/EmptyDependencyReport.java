package br.usp.ime.jdx.entity.relationship.dependency;

import java.util.HashSet;

import br.usp.ime.jdx.entity.system.JavaProject;

public class EmptyDependencyReport extends DependencyReport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3093800432687149620L;

	public EmptyDependencyReport(JavaProject javaProject) {
		super(javaProject, 
				new HashSet<>(),
				new HashSet<>(),
				new HashSet<>(),
				new HashSet<>(),
				new HashSet<>(),
				new HashSet<>(),
				new HashSet<>(),
				new HashSet<>(),
				new HashSet<>(),
				new HashSet<>(),
				new HashSet<>(),
				new HashSet<>());
	}
	
}
