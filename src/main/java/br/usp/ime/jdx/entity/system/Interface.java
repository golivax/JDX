package br.usp.ime.jdx.entity.system;

public class Interface extends Type{

	private static final long serialVersionUID = 3317316147717880580L;
	
	public Interface(String fqn, CompUnit parentCompUnit) {
		super(fqn, parentCompUnit);
	}
	
	public Interface(String fqn, int[] javaDocLocation, int[] sourceCodeLocation, CompUnit parentCompUnit) {
		super(fqn, javaDocLocation, sourceCodeLocation, parentCompUnit);
	}
	
	public Interface(String fqn, String rawJavaDoc, String rawSourceCode, CompUnit parentCompUnit) {
		super(fqn, rawJavaDoc, rawSourceCode, parentCompUnit);
	}

}
