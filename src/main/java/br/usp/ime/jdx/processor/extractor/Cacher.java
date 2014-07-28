package br.usp.ime.jdx.processor.extractor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;

import br.usp.ime.jdx.entity.Clazz;
import br.usp.ime.jdx.entity.CompUnit;
import br.usp.ime.jdx.entity.Interface;
import br.usp.ime.jdx.entity.Method;
import br.usp.ime.jdx.entity.Type;

public class Cacher extends FileASTRequestor{
	
	private Map<CompilationUnit,CompUnit> compUnitMap =	new HashMap<>();	
	
	private Map<TypeDeclaration,Interface> interfaceMap = new HashMap<>();
	private Map<String,Interface> interfaceNameMap = new HashMap<>();
	
	private Map<TypeDeclaration,Clazz> clazzMap = new HashMap<>();
	private Map<String,Clazz> clazzNameMap = new HashMap<>();
	
	private Map<MethodDeclaration,Method> methodDeclarationMap = 
			new HashMap<>();
	
	@Override
	public void acceptAST(String sourceFilePath, 
			CompilationUnit compilationUnit) {

		this.cache(sourceFilePath, compilationUnit);	
	}	
	
	private void cache(String sourceFilePath, CompilationUnit compilationUnit) {

		cacheCompilationUnit(sourceFilePath, compilationUnit);
		cacheTypes(compilationUnit);
		cacheMethods(compilationUnit); 
	}

	private void cacheCompilationUnit(String sourceFilePath,
			CompilationUnit compilationUnit) {
		
		String compUnitName = sourceFilePath;
		String compUnitSourceCode = compilationUnit.toString();
		CompUnit compUnit = new CompUnit(compUnitName,compUnitSourceCode);
		compUnitMap.put(compilationUnit,compUnit);
		
	}
	
	@SuppressWarnings("unchecked")
	private void cacheTypes(CompilationUnit compilationUnit){
		Stack<TypeDeclaration> typeStack = new Stack<TypeDeclaration>();
		typeStack.addAll(compilationUnit.types());

		while(!typeStack.isEmpty()){
			TypeDeclaration typeDeclaration = typeStack.pop();

			String typeName = getTypeName(typeDeclaration);
			String sourceCode = typeDeclaration.toString();
			
			Type type;
			
			if(typeDeclaration.isInterface()){
				Interface interf = new Interface(typeName, sourceCode);
				interfaceMap.put(typeDeclaration, interf);
				interfaceNameMap.put(typeName, interf);
				type = interf;
			}
			else{
				Clazz clazz = new Clazz(typeName, sourceCode);
				clazzMap.put(typeDeclaration, clazz);
				clazzNameMap.put(typeName, clazz);
				type = clazz;
			}
			
			CompUnit compUnit = compUnitMap.get(compilationUnit);
			compUnit.addType(type);
			

			for(TypeDeclaration subType : getSubTypes(typeDeclaration)){
				typeStack.push(subType);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void cacheMethods(CompilationUnit compilationUnit) {
		
		Stack<TypeDeclaration> typeStack = new Stack<TypeDeclaration>();
		typeStack.addAll(compilationUnit.types());

		while(!typeStack.isEmpty()){
			TypeDeclaration typeDeclaration = typeStack.pop();
			
			cacheMethods(typeDeclaration);
			
			for(TypeDeclaration subType : getSubTypes(typeDeclaration)){
				typeStack.push(subType);
			}
		}
	}
	
	private void cacheMethods(TypeDeclaration typeDeclaration) {
				
		Type type = getType(typeDeclaration); 
		
		for(MethodDeclaration methodDeclaration : 
			typeDeclaration.getMethods()){

			String methodName = methodDeclaration.getName().toString();

			List<String> parameterTypes = 
					getParameterTypes(methodDeclaration);

			boolean isConstructor = methodDeclaration.isConstructor();
			
			String sourceCode = methodDeclaration.toString();

			//Might be useful in the future
			//Return type: methodDeclaration.getReturnType2();
			//Type parameters (generic): methodDeclaration.typeParameters();

			Method method = new Method(
					methodName,parameterTypes,isConstructor,sourceCode);
			
			type.addMethod(method);

			methodDeclarationMap.put(methodDeclaration, method);
		}

		//Add implicit constructor when no explicit constructors exist
		if(type.getConstructors().isEmpty()){
			
			Method constructor = new Method(type.getName(), 
					new ArrayList<String>(),true,null);
			
			type.addMethod(constructor);
		}
		
		//Add artificial "attrib<>" method
		Method attribMethod = new Method("attrib<>",new ArrayList<String>(),null);
		type.addMethod(attribMethod);
	}

	@SuppressWarnings("unchecked")
	private List<String> getParameterTypes(MethodDeclaration methodDeclaration) {
		List<String> parameterTypes = new ArrayList<>();
		
		List<SingleVariableDeclaration> parameters = 
				methodDeclaration.parameters();
		
		for(SingleVariableDeclaration svd : parameters){
			parameterTypes.add(svd.getType().toString());
		}
		return parameterTypes;
	}

	private String getTypeName(TypeDeclaration typeDeclaration) {

		String typeName = typeDeclaration.getName().toString();

		ASTNode node = typeDeclaration.getParent();

		while(node != null){
			if(node instanceof TypeDeclaration){
				TypeDeclaration superType = (TypeDeclaration)node;
				typeName = superType.getName().toString() + "." + typeName;
			}
			if(node instanceof CompilationUnit){
				CompilationUnit compilationUnit = (CompilationUnit)node;
				typeName = compilationUnit.getPackage().getName() + 
						"." + typeName;
			}
			node = node.getParent();
		}

		return typeName;
	}	

	@SuppressWarnings("unchecked")
	private List<TypeDeclaration> getSubTypes(TypeDeclaration typeDeclaration) {
		List<TypeDeclaration> subTypes = new ArrayList<TypeDeclaration>();

		try{
			//Recovers static nested classes and "regular" inner classes
			Collections.addAll(subTypes, typeDeclaration.getTypes());

			//Recovers local classes
			for(MethodDeclaration methodDeclaration : typeDeclaration.getMethods()){
				Block block = methodDeclaration.getBody();
				if (block != null) {
					List<Statement> statements = block.statements();
					for(Statement statement : statements){
						if (statement.getNodeType() == ASTNode.TYPE_DECLARATION_STATEMENT){
							TypeDeclarationStatement tds = (TypeDeclarationStatement)statement;
							if (tds.getDeclaration() instanceof TypeDeclaration){								
								subTypes.add((TypeDeclaration)tds.getDeclaration());
							}
						}
					}
				}									
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return subTypes;
	}

	public Method getMethod(MethodDeclaration methodDeclaration) {
		return methodDeclarationMap.get(methodDeclaration);
	}
	
	public Interface getInterface(TypeDeclaration typeDeclaration){
		return interfaceMap.get(typeDeclaration);
	}
	
	public Clazz getClazz(TypeDeclaration typeDeclaration){
		return clazzMap.get(typeDeclaration);
	}

	public Type getType(TypeDeclaration typeDeclaration) {
		Type type = interfaceMap.get(typeDeclaration);
		if (type == null) type = clazzMap.get(typeDeclaration);
		return type;		
	}
	
	public Set<Type> getTypes() {
		Set<Type> types = new HashSet<>();
		types.addAll(interfaceMap.values());
		types.addAll(clazzMap.values());
		return types;		
	}
	
	public Set<TypeDeclaration> getTypeDeclarations() {
		Set<TypeDeclaration> typeDeclarations = new HashSet<>();
		typeDeclarations.addAll(interfaceMap.keySet());
		typeDeclarations.addAll(clazzMap.keySet());
		return typeDeclarations;
	}
	
	public Set<TypeDeclaration> getInterfaceTypeDeclarations(){
		return interfaceMap.keySet();
	}
	
	public Set<TypeDeclaration> getClazzTypeDeclarations(){
		return clazzMap.keySet();
	}

	public Clazz getClazz(String clazzQualifiedName){
		return clazzNameMap.get(clazzQualifiedName);
	}
	
	public Interface getInterface(String interfaceQualifiedName){
		return interfaceNameMap.get(interfaceQualifiedName);
	}
	
	public Type getType(String typeQualifiedName){
		Type type = interfaceNameMap.get(typeQualifiedName);
		if (type == null) type = clazzNameMap.get(typeQualifiedName);
		return type;
	}
	
}