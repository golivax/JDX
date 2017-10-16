package br.usp.ime.jdx.entity.system;

import java.io.Serializable;
import java.util.List;

//TODO: getAnnotations()
public class Method extends JavaDocableElement implements Serializable, JavaElement{

	private static final long serialVersionUID = -4865185051823887456L;

	private String name;
	private List<String> parameters;
	private String returnType;

	private boolean isConstructor = false;	

	private SourceCode javaDoc;
	private SourceCode sourceCode;
	
	private Type parentType;
	
	public Method(String name, List<String> parameters, String returnType, boolean isConstructor, 
			Type parentType) {
		
		this.name = name;
		this.parameters = parameters;
		this.returnType = returnType;
		this.isConstructor = isConstructor;
		this.parentType = parentType;
		
	}
	
	public Method(String name, List<String> parameters, String returnType, boolean isConstructor, 
			String rawJavaDoc, String rawSourceCode, Type parentType) {
		
		this(name,parameters,returnType,isConstructor,parentType);
		
		if(rawJavaDoc != null) {
			this.javaDoc = new SourceCode(rawJavaDoc);	
		}
		
		this.sourceCode = new SourceCode(rawSourceCode);		
	}
	
	public Method(String name, List<String> parameters, String returnType, boolean isConstructor,
			int[] javaDocLocation, int[] codeLocation, Type parentType) {
		
		this(name,parameters,returnType,isConstructor,parentType);
		
		if(javaDocLocation != null) {
			this.javaDoc = new SourceCode(javaDocLocation, parentType.getParentCompUnit());
		}
		
		this.sourceCode = new SourceCode(codeLocation, parentType.getParentCompUnit());
	}
	
	public String getReturnType() {
		return returnType;
	}
	
	/**
	 * Ugly hack!
	 * @return
	 */
	public boolean isAttributeCollection() {
		return this.name.equals("attrib<>");
	}
	
	public SourceCode getBody() {
		
		//FIXME: for now, null bodies (e.g. from abstract methods) and
		//empty bodies will be treated as if they were the same thing.
		//This is because some applications that use JDX compare method bodies
		//and having a NULL value would break it. In the future, provide a 
		//more elegant solution like a NullBody or a Body class that answers
		//if its NULL or not, whatever.
		
		if(isAttributeCollection()) {
			return this.sourceCode;
		}
		
		SourceCode body = new SourceCode("");
		SourceCode sourceWithoutJavaDoc = this.getSourceCodeWithoutJavaDoc();	

		int indexOfBraces = sourceWithoutJavaDoc.getRawVersion().indexOf('{');

		//If method has body
		if(indexOfBraces != -1) {

			//If it has a location, we return the body with its location relative to the parent comp unit
			if(sourceWithoutJavaDoc.getCodeLocation() != null) {

				int start = sourceWithoutJavaDoc.getCodeLocation()[0] + indexOfBraces;
				int end = sourceWithoutJavaDoc.getCodeLocation()[1];
				int[] codeLocation = new int[]{start,end};
				body = new SourceCode(codeLocation, parentType.getParentCompUnit());

			}
			//If it does not have a location, we return the body without any location
			else {
				String startingAtCurlyBraces = sourceWithoutJavaDoc.getRawVersion().substring(indexOfBraces);
				body = new SourceCode(startingAtCurlyBraces);
			}
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
	
	
	public String toString(){
		StringBuilder idBuilder = new StringBuilder();
		idBuilder.append("[");
		
		if(!this.getParentCompUnit().getRelativePath().isEmpty()) {
			idBuilder.append(this.getParentCompUnit().getRelativePath());	
		}
		else {
			idBuilder.append(this.getParentCompUnit().getAbsolutePath());
		}
		
		idBuilder.append("]");
		idBuilder.append(this.getParentType().getFullName());
		idBuilder.append(".");
		idBuilder.append(this.getSignature());
		return idBuilder.toString();
	}
	
	public Type getParentType(){
		return parentType;
	}
	
	public List<String> getParameters() {
		return parameters;
	}
	
	public boolean isConstructor(){
		return isConstructor;
	}

	@Override
	public SourceCode getSourceCode(){	
		return sourceCode;
	}
	
	@Override
	public SourceCode getJavaDoc() {
		return javaDoc;
	}
	
	public boolean hasJavaDoc() {
		return javaDoc != null;
	}
	
	@Override
	public CompUnit getParentCompUnit(){
		return getParentType().getParentCompUnit();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((parentType == null) ? 0 : parentType.hashCode());
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
		if (parentType == null) {
			if (other.parentType != null)
				return false;
		} else if (!parentType.equals(other.parentType))
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