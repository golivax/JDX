package br.usp.ime.jdx.processor.extractor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;

import br.usp.ime.jdx.entity.system.Clazz;
import br.usp.ime.jdx.entity.system.CompUnit;
import br.usp.ime.jdx.entity.system.Interface;
import br.usp.ime.jdx.entity.system.Method;
import br.usp.ime.jdx.entity.system.Package;
import br.usp.ime.jdx.entity.system.Type;

public class Cache extends FileASTRequestor{
	
	private boolean recoverSourceCode = false;
	
	private Map<String, Package> pkgMap = new HashMap<>();
	
	private Map<CompilationUnit,CompUnit> compUnitMap =	new HashMap<>();	
	
	private Map<TypeDeclaration,Interface> interfaceMap = new HashMap<>();
	private Map<String,Interface> interfaceNameMap = new HashMap<>();
	
	private Map<TypeDeclaration,Clazz> clazzMap = new HashMap<>();
	private Map<String,Clazz> clazzNameMap = new HashMap<>();
	
	private Map<MethodDeclaration,Method> methodDeclarationMap = 
			new HashMap<>();
	
	public Cache(boolean recoverSourceCode) {
		this.recoverSourceCode = recoverSourceCode;
	}

	@Override
	public void acceptAST(String sourceFilePath, 
			CompilationUnit compilationUnit) {

		this.cache(sourceFilePath, compilationUnit);	
	}	
	
	private void cache(String sourceFilePath, CompilationUnit compilationUnit) {
		if(!compilationUnit.toString().isEmpty()){
			cachePackage(sourceFilePath, compilationUnit);
			cacheCompilationUnit(sourceFilePath, compilationUnit);
			cacheTypes(compilationUnit);
			cacheMethods(compilationUnit);
		}
	}

	private void cachePackage(String sourceFilePath, CompilationUnit compilationUnit) {
			 
		String packageFQN = getPackageName(compilationUnit);
		
		if(!pkgMap.containsKey(packageFQN)){
			Package pkg = new Package(packageFQN);
			pkgMap.put(packageFQN, pkg);
			
			if(packageFQN.contains(".")) cacheParentPackage(pkg);	
		}		
	}

	private String getPackageName(CompilationUnit compilationUnit) {
		PackageDeclaration packageDeclaration = compilationUnit.getPackage();
		
		//If compilation Unit lies in the default package
		if(packageDeclaration == null){
			return "(default package)";
		}
		
		return packageDeclaration.getName().toString();
	}

	private void cacheParentPackage(Package pkg) {
		
		String parentPkgFQN = 
				StringUtils.substringBeforeLast(pkg.getFQN(), ".");
		
		if(!pkgMap.containsKey(parentPkgFQN)){
			Package parentPkg = new Package(parentPkgFQN);
			pkg.setParent(parentPkg);
			
			pkgMap.put(parentPkgFQN, parentPkg);
			
			if(parentPkgFQN.contains(".")) cacheParentPackage(parentPkg);
		}
		else{
			pkg.setParent(pkgMap.get(parentPkgFQN));
		}
		
	}

	private void cacheCompilationUnit(String sourceFilePath,
			CompilationUnit compilationUnit) {
		
		String pkgFQN = getPackageName(compilationUnit);		
		Package pkg = pkgMap.get(pkgFQN);
		
		String compUnitName = sourceFilePath;
		
		String compUnitSourceCode = null;		
		if(recoverSourceCode){
			compUnitSourceCode = compilationUnit.toString();
		}
		
		CompUnit compUnit = new CompUnit(pkg, compUnitName,compUnitSourceCode);
		compUnitMap.put(compilationUnit,compUnit);
	}
	
	@SuppressWarnings("unchecked")
	private void cacheTypes(CompilationUnit compilationUnit){
		Stack<AbstractTypeDeclaration> typeStack = new Stack<>();
		typeStack.addAll(compilationUnit.types());

		while(!typeStack.isEmpty()){
			AbstractTypeDeclaration abstractTypeDecl = typeStack.pop();
			
			//FIXME: Add support for enums (EnumDeclaration)
			if(abstractTypeDecl.getNodeType() == ASTNode.TYPE_DECLARATION){
				
				TypeDeclaration typeDeclaration = (TypeDeclaration) abstractTypeDecl;
				String typeFQN = getTypeFQN(typeDeclaration);
				
				String sourceCode = null;
				if(recoverSourceCode){
					sourceCode = typeDeclaration.toString();
				}
				
				Type type;
				
				if(typeDeclaration.isInterface()){
					Interface interf = new Interface(typeFQN, sourceCode);
					interfaceMap.put(typeDeclaration, interf);
					interfaceNameMap.put(typeFQN, interf);
					type = interf;
				}
				else{
					Clazz clazz = new Clazz(typeFQN, sourceCode);
					clazzMap.put(typeDeclaration, clazz);
					clazzNameMap.put(typeFQN, clazz);
					type = clazz;
				}
				
				CompUnit compUnit = compUnitMap.get(compilationUnit);
				compUnit.addType(type);
				
				for(TypeDeclaration subType : getSubTypes(typeDeclaration)){
					typeStack.push(subType);
				}				
			}
			
		}
	}
	
	@SuppressWarnings("unchecked")
	private void cacheMethods(CompilationUnit compilationUnit) {
		
		Stack<AbstractTypeDeclaration> typeStack = new Stack<>();
		typeStack.addAll(compilationUnit.types());

		while(!typeStack.isEmpty()){
			
			AbstractTypeDeclaration abstractTypeDecl = typeStack.pop();
			
			//FIXME: Add support for enums (EnumDeclaration)
			if(abstractTypeDecl.getNodeType() == ASTNode.TYPE_DECLARATION){
				
				TypeDeclaration typeDeclaration = 
						(TypeDeclaration)abstractTypeDecl;
				
				cacheMethods(typeDeclaration);
				
				for(TypeDeclaration subType : getSubTypes(typeDeclaration)){
					typeStack.push(subType);
				}
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
			
			String returnType;
			//Constructors have null returnType. In this case, we set the
			//return type to be the type itself.
			if(methodDeclaration.getReturnType2() == null){
				returnType = type.getName();
			}
			else{
				returnType = methodDeclaration.getReturnType2().toString();
			}
			
			String body = null;
			String sourceCode = null;
			if(recoverSourceCode){
				if(methodDeclaration.getBody() != null){
					body = methodDeclaration.getBody().toString();	
				}
				else{
					//FIXME: for now, null bodies (e.g. from abstract methods) and
					//empty bodies will be treated as if they were the same thing.
					//This is because some applications that use JDX compare method bodies
					//and having a NULL value would break it. In the future, provide a 
					//more elegant solution like a NullBody or a Body class that answers
					//if its NULL or not, whatever.
					body = new String();
				}				
				sourceCode = methodDeclaration.toString();
			}
			
			Method method = new Method(
					methodName, parameterTypes, returnType, 
					body, sourceCode, isConstructor);
			
			type.addMethod(method);

			methodDeclarationMap.put(methodDeclaration, method);
		}

		//Add implicit constructor when no explicit constructors exist
		if(type.getConstructors().isEmpty()){
			
			Method constructor = new Method(type.getName(), 
					new ArrayList<String>(), type.getName(), 
					new String(), new String(), true);
			
			type.addMethod(constructor);
		}
		
		//Recovering attributes
		StringBuilder attributesBuilder = new StringBuilder();
		for(FieldDeclaration fieldDeclaration : typeDeclaration.getFields()){
			attributesBuilder.append(fieldDeclaration.toString().trim());
			attributesBuilder.append(System.lineSeparator());
		}
		String attributes = attributesBuilder.toString();
		
		//Adding artificial "attrib<>" method
		Method attribMethod = new Method(
				"attrib<>", new ArrayList<String>(), new String(), 
				attributes,attributes, false);
		
		type.addMethod(attribMethod);
	}

	@SuppressWarnings("unchecked")
	private List<String> getParameterTypes(MethodDeclaration methodDeclaration) {
		
		List<String> parameterTypes = new ArrayList<>();
		
		List<SingleVariableDeclaration> parameters = 
				methodDeclaration.parameters();
		
		for(SingleVariableDeclaration svd : parameters){
			//Maybe it's better to resolve binding and then get type name via 
			//svd.resolveBinding().getType()
									
			String typeName = svd.getType().toString();
			
			//Deals with static nested classes inside brackets
			String generics = StringUtils.substringBetween(typeName,"<",">");
			if(StringUtils.isNotEmpty(generics) && generics.contains(".")){
				
				String simplifiedGenerics = 
						StringUtils.substringAfterLast(generics, ".");

				typeName = StringUtils.replace(typeName, 
						"<" + generics + ">", 
						"<" + simplifiedGenerics + ">");		
			}
			
			//Deals with static nested classes
			if(typeName.contains(".")){
				typeName = StringUtils.substringAfterLast(typeName, ".");
			}
						
			//Sometimes the '[]' accompanies the variable (instead of the type)
			//so we need to add it manually to the typename
			String variable = 
					StringUtils.substringAfterLast(svd.toString(), " "); 
			
			if(variable.contains("[]")){
				typeName+="[]";
			}
			
			//If the parameter is varargs, we also add the brackets
			if(svd.isVarargs()){
				typeName+="[]";
			}
			
			parameterTypes.add(typeName);
		}
		return parameterTypes;
	}

	private String getTypeFQN(AbstractTypeDeclaration typeDeclaration) {

		String typeName = typeDeclaration.getName().toString();

		ASTNode node = typeDeclaration.getParent();

		while(node != null){
			if(node instanceof TypeDeclaration){
				TypeDeclaration superType = (TypeDeclaration)node;
				typeName = superType.getName().toString() + "." + typeName;
			}
			if(node instanceof CompilationUnit){
				CompilationUnit compilationUnit = (CompilationUnit)node;
				typeName = getPackageName(compilationUnit) + "." + typeName;
			}
			node = node.getParent();
		}

		return typeName;
	}	

	@SuppressWarnings("unchecked")
	private List<TypeDeclaration> getSubTypes(TypeDeclaration typeDeclaration) {
		List<TypeDeclaration> subTypes = new ArrayList<>();

		try{
			//Recovers static nested classes and "regular" inner classes
			Collections.addAll(subTypes, typeDeclaration.getTypes());

			//Recovers local classes
			for(MethodDeclaration methodDecl : typeDeclaration.getMethods()){
				Block block = methodDecl.getBody();
				if (block != null) {
					List<Statement> statements = block.statements();
					for(Statement statement : statements){
						if (statement.getNodeType() 
								== ASTNode.TYPE_DECLARATION_STATEMENT){
							
							TypeDeclarationStatement tds = 
									(TypeDeclarationStatement)statement;
							
							AbstractTypeDeclaration atd = tds.getDeclaration();
							if (atd instanceof TypeDeclaration){								
								subTypes.add((TypeDeclaration)atd);
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
	
	public Set<Method> getMethods(){
		Set<Method> methods = new HashSet<>();
		for(Type type : getTypes()){
			methods.addAll(type.getMethods());
		}
		return methods;
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
	
	public Set<CompUnit> getJDXCompilationUnits(){
		Set<CompUnit> compUnits = new HashSet<>();
		compUnits.addAll(compUnitMap.values());
		return compUnits;
	}
	
	public Set<CompilationUnit> getCompilationUnits(){
		return compUnitMap.keySet();
	}
	
	public CompUnit getJDXCompilationUnit(CompilationUnit compilationUnit){
		return compUnitMap.get(compilationUnit);
	}
	
	public Package getPackage(String packageName){
		return pkgMap.get(packageName);
	}
	
	public Set<Package> getPackages(){
		Set<Package> pkgs = new HashSet<>();
		pkgs.addAll(pkgMap.values());
		return pkgs;
	}
	
}