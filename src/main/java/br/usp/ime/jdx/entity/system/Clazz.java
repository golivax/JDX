package br.usp.ime.jdx.entity.system;

public class Clazz extends Type{

	private static final long serialVersionUID = 7843800618081463626L;

	public Clazz(String fqn, CompUnit parentCompUnit) {
		super(fqn, parentCompUnit);
	}
	
	public Clazz(String fqn, int[] javaDocLocation, int[] sourceCodeLocation, CompUnit parentCompUnit) {
		super(fqn, javaDocLocation, sourceCodeLocation, parentCompUnit);
	}
	
	public Clazz(String fqn, String rawJavaDoc, String rawSourceCode, CompUnit parentCompUnit) {
		super(fqn, rawJavaDoc, rawSourceCode, parentCompUnit);
	}

}