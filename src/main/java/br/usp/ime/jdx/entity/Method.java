package br.usp.ime.jdx.entity;

import java.util.List;

public class Method extends NamedEntity{

	private String name;
	private List<String> parameters;
	private String sourceCode;
	private Type containingType;
	
	public Method(String name, List<String> parameters, String sourceCode){
		this.name = name;
		this.parameters = parameters;
		this.sourceCode = sourceCode;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public String getSignature(){
		String signature = name + "(";
		
		if(!parameters.isEmpty()){
			for(int i = 0; i<parameters.size() -1; i++){
				signature += parameters.get(i) + ", ";
			}
			signature += parameters.get(parameters.size()-1);
		}
		
		signature += ")";
		return signature;
	}
	
	public String getSourceCode(){
		return sourceCode;
	}
	
	public String toString(){
		return containingType.getFQN() + "." + getSignature();
	}
	
	public Type getContainingType(){
		return containingType;
	}
	
	public void setContainingType(Type containingType){
		this.containingType = containingType;
	}

	public List<String> getParameters() {
		return parameters;
	}

}
