package br.usp.ime.jdx.entity.system;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

//TODO: getAnnotations()
public class Method implements Serializable, JavaElement{

	private static final long serialVersionUID = -4865185051823887456L;

	private String javaDoc;
	private String name;
	private List<String> parameters;
	private String returnType;
	
	private int[] sourceCodeLocation;
	private String sourceCode;
	
	private Type containingType;
	private boolean isConstructor = false;
	
	public Method(String javaDoc, String name, List<String> parameters, String returnType,
			int[] sourceCodeLocation) {
		
		this.javaDoc = javaDoc;
		this.name = name;
		this.parameters = parameters;
		this.returnType = returnType;
		this.sourceCodeLocation = sourceCodeLocation;	
	}
	
	public Method(String javaDoc, String name, List<String> parameters, String returnType,
			int[] sourceCodeLocation, boolean isConstructor) {
		
		this(javaDoc, name, parameters, returnType, sourceCodeLocation);
		this.isConstructor = isConstructor;
	}

	public Method(String javaDoc, String name, List<String> parameters, String returnType, 
			String sourceCode){
		
		this.javaDoc = javaDoc;
		this.name = name;
		this.parameters = parameters;
		this.returnType = returnType;
		this.sourceCode = sourceCode;	
	}
	
	public Method(String javaDoc, String name, List<String> parameters, String returnType, 
			String sourceCode, boolean isConstructor){
		
		this(javaDoc, name, parameters, returnType, sourceCode);
		this.isConstructor = isConstructor;
	}
	
	
	public String getReturnType() {
		return returnType;
	}
	
	public String getSourceCodeWithoutJavaDoc() {
		String sourceWithoutJavaDoc = StringUtils.substringAfter(this.getSourceCode(), this.getJavaDoc()).trim();
		return sourceWithoutJavaDoc;
	}

	public String getBody() {
		
		//FIXME: for now, null bodies (e.g. from abstract methods) and
		//empty bodies will be treated as if they were the same thing.
		//This is because some applications that use JDX compare method bodies
		//and having a NULL value would break it. In the future, provide a 
		//more elegant solution like a NullBody or a Body class that answers
		//if its NULL or not, whatever.
		
		String body = new String();
		
		String sourceWithoutJavaDoc = this.getSourceCodeWithoutJavaDoc();
		if(sourceWithoutJavaDoc.contains("{")) {
			body = "{" + StringUtils.substringAfter(sourceWithoutJavaDoc, "{");
		}				
		
		return body;
	}

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
		
		if(sourceCode != null) return sourceCode;
		
		String compUnitSourceCode = this.getContainingCompUnit().getSourceCode();
		String sourceCode = compUnitSourceCode.substring(sourceCodeLocation[0], sourceCodeLocation[1]);
		return sourceCode;
	}
	
	public String toString(){
		String s = new String();
		if (containingType != null) s= containingType.getFQN() + "[" + containingType.getClass().getSimpleName() + "].";
		s += getSignature();
		return s;
	}
	
	public CompUnit getContainingCompUnit(){
		return getContainingType().getCompUnit();
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
	
	public boolean isConstructor(){
		return isConstructor;
	}
	
	/*
	 * If there is no JavaDoc, this returns an empty string
	 */
	public String getJavaDoc() {
		return javaDoc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((containingType == null) ? 0 : containingType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((parameters == null) ? 0 : parameters.hashCode());
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
		Method other = (Method) obj;
		if (containingType == null) {
			if (other.containingType != null)
				return false;
		} else if (!containingType.equals(other.containingType))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		return true;
	}
}
