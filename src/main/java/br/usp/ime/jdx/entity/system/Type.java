package br.usp.ime.jdx.entity.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public abstract class Type implements Serializable, JavaElement{

	private static final long serialVersionUID = -7126906063529157990L;

	private String fqn;
	private SourceCode javaDoc;
	private SourceCode sourceCode;
	private CompUnit parentCompUnit;
	private Set<Method> methods;
	
	public Type(String fqn, CompUnit parentCompUnit) {
		this.fqn = fqn;
		this.parentCompUnit = parentCompUnit;
		this.methods = new HashSet<Method>();
	}
	
	public Type(String fqn, int[] javaDocLocation, int[] sourceCodeLocation, CompUnit parentCompUnit) {
		this(fqn,parentCompUnit);
		
		if(javaDocLocation != null) {
			this.javaDoc = new SourceCode(javaDocLocation, parentCompUnit);
		}
		
		this.sourceCode = new SourceCode(sourceCodeLocation, parentCompUnit);
	}
	
	public Type(String fqn, String rawJavaDoc, String rawSourceCode, CompUnit parentCompUnit){
		this(fqn,parentCompUnit);
		this.javaDoc = new SourceCode(rawJavaDoc);
		this.sourceCode = new SourceCode(rawSourceCode);
	}
	
	public String getFullName() {
		return StringUtils.substringAfter(fqn, this.getParentCompUnit().getPackage().getFQN() + ".");
	}
	
	public String getName(){
		return StringUtils.substringAfterLast(fqn, ".");
	}
	
	public String getFQN(){
		return fqn;
	}
	
	public SourceCode getSourceCode(){
		return sourceCode;
	}
	
	public SourceCode getSourceCodeWithoutJavaDoc() {
		if(javaDoc == null) return sourceCode;
		
		//end of javadoc + 1
		int start = javaDoc.getCodeLocation()[1] + 1;
		
		//end of source code		
		int end = sourceCode.getCodeLocation()[1];
		
		int[] codeLocation = new int[] {start,end};		
		SourceCode sourceCodeWithoutJavaDoc = new SourceCode(codeLocation, parentCompUnit);
		return sourceCodeWithoutJavaDoc;
	}
	
	public CompUnit getParentCompUnit(){
		return parentCompUnit;
	}
	
	public void addMethod(Method method){
		this.methods.add(method);
	}

	@Override
	public String toString(){
		return getFQN();
	}
	
	public Set<Method> getConstructors(){
		Set<Method> constructors = new HashSet<>();
		for(Method method : methods){
			if(method.isConstructor()) {
				constructors.add(method);
			}
		}
		return constructors;
	}
	
	public Set<Method> getMethods(){
		return methods;
	}
	
	public List<Method> getMethods(String methodName) {
		List<Method> methodsFound = new ArrayList<>();
		for (Method method : methods){
			
			if(method.getName().equals(methodName)){
				methodsFound.add(method);
			}
		}
		return methodsFound;
	}

	public Method getMethod(String methodName, List<String> parameters) {
		for (Method method : getMethods(methodName)){			
			if(method.getParameters().equals(parameters)){				
				return method;
			}
		}
		return null;		
	}

	//TODO: Criar atributo para guardar attrib method
	public Method getAttribMethod() {
		return getMethod("attrib<>", new ArrayList<String>());
	}
	
	public SourceCode getJavaDoc() {
		return javaDoc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parentCompUnit == null) ? 0 : parentCompUnit.hashCode());
		result = prime * result + ((fqn == null) ? 0 : fqn.hashCode());
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
		Type other = (Type) obj;
		if (parentCompUnit == null) {
			if (other.parentCompUnit != null)
				return false;
		} else if (!parentCompUnit.equals(other.parentCompUnit))
			return false;
		if (fqn == null) {
			if (other.fqn != null)
				return false;
		} else if (!fqn.equals(other.fqn))
			return false;
		return true;
	}
}