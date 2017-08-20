package br.usp.ime.jdx.processor.extractor;

import java.util.List;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import br.usp.ime.jdx.entity.relationship.dependency.RawDependencyReport;
import br.usp.ime.jdx.entity.system.Clazz;
import br.usp.ime.jdx.entity.system.Interface;
import br.usp.ime.jdx.filter.StringMatcher;
import br.usp.ime.jdx.processor.parser.CodeParser;

/**
 * Extracts relationships between types. For now it detects
 * inheritance and interface implementation
 * @author Gustavo Ansaldi Oliva {@link goliva@ime.usp.br}
 *
 */

//FIXME: Refactor this class in order to make it use "Binding Resolver"
public class TypeDependencyExtractor {

	private CodeParser cache;
	private StringMatcher classFilter;
	private RawDependencyReport depReport;
	
	public TypeDependencyExtractor(CodeParser cacher){
		this.cache = cacher;
	}
	
	public RawDependencyReport run(RawDependencyReport depReport, 
			StringMatcher classFilter) {
		
		this.classFilter = classFilter;
		this.depReport = depReport;
		
		for(TypeDeclaration clazzTypeDeclaration : 
			cache.getClazzTypeDeclarations()){
			
			detectClassInheritance(clazzTypeDeclaration);
			detectInterfaceImplementation(clazzTypeDeclaration);
		}
		
		for(TypeDeclaration interfaceTypeDeclaration : 
			cache.getInterfaceTypeDeclarations()){

			detectInterfaceInheritance(interfaceTypeDeclaration);
		}
	
		return depReport;
	}
	
	private void detectClassInheritance(TypeDeclaration clazzTypeDeclaration) {

		Type superClassType = clazzTypeDeclaration.getSuperclassType();

		if(superClassType != null){
			
			String superClassName = getBindingName(
					clazzTypeDeclaration, superClassType);

			if (superClassName != null && !classFilter.matches(superClassName)){

				Clazz client = cache.getClazz(clazzTypeDeclaration);
				Clazz supplier = cache.getClazz(superClassName);

				//In a weird situation in which a class C1 extends a class C2 (that is not part of the environment) 
				//and the system has a class C3 with the same name as C2. 
				//JDT ends up binding the C1 to C3, which is wrong. 
				//Worse than that, if C3 is an annotation, if won't be found in the cache 
				//and an invalid dependency with no supplier will be build
				//Example: ASF commit 1580656
				//tomee\trunk\container\openejb-loader\src\test\java\org\apache\openejb\observer\EventSpeedTest.java
				//tomee\trunk\container\openejb-loader\src\test\java\org\apache\openejb\observer\Assert.java
				//JDT incorrectly binds EventSpeedTest to this Assert class and then does find this Assert class in the cache (because it is an annotation)
				
				if(client != null && supplier != null){				
					depReport.addClazzInheritanceDependency(client,supplier);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void detectInterfaceImplementation(
			TypeDeclaration clazzTypeDeclaration){
		
		//For a class declaration, these are the interfaces that 
		//this class implements
		List<Type> implementedInterfaces = 
				clazzTypeDeclaration.superInterfaceTypes();

		for(Type implementedInterface :	implementedInterfaces){

			String implementedInterfaceName = 
					getBindingName(clazzTypeDeclaration, implementedInterface);

			if (implementedInterfaceName != null && 
				!classFilter.matches(implementedInterfaceName)){

				Clazz clazz = cache.getClazz(clazzTypeDeclaration);
				
				//Java does not issue a compilation error when a class implements an Annotation.
				//In this case, cacher does not find the annotation
				//Example: ASF commit 1498347
				//bval\branches\bval-11\bval-jsr303\src\main\java\org\apache\bval\cdi\BValAnnotatedType.java
				//BValBindingLitteral implements an annotation (BValBinding).
				Interface interf = cache.getInterface(implementedInterfaceName); 
				 
				if(clazz != null && interf != null){
					depReport.addImplementsDependency(clazz,interf);	
				}				
			}
		}					
	}
	
	@SuppressWarnings("unchecked")
	private void detectInterfaceInheritance(
			TypeDeclaration interfaceTypeDeclaration){

		//For an interface declaration, these are the interfaces 
		//that this interface extends.	
		List<Type> extendedInterfaces = 
				interfaceTypeDeclaration.superInterfaceTypes();

		for(Type extendedInterface : extendedInterfaces){

			String extendedInterfaceName = 
					getBindingName(interfaceTypeDeclaration, extendedInterface);

			if (extendedInterfaceName != null && 
					!classFilter.matches(extendedInterfaceName)){
				
				Interface client = cache.getInterface(interfaceTypeDeclaration);
				Interface supplier = cache.getInterface(extendedInterfaceName);

				//See comment in "detectClassInheritance". Same thing can happen for interface inheritance.
				//This happens when processing commit 1385407 in ASF for project 'ctakes' (asf/ctakes) for class
				//'trunk/cTAKES/core/src/edu/mayo/bmi/uima/core/cc/NonTerminalConsumer.java'
				if(client != null && supplier != null){
					depReport.addInterfaceInheritanceDependency(client,supplier);
				}
				
			}
		}			
	}

	private String getBindingName(TypeDeclaration typeDeclaration, Type type) {
		
		ITypeBinding iTypeBinding = type.resolveBinding();
		
		if(iTypeBinding == null){
		
			/**
			System.out.println("WARNING: Could not find binding for " + 
					type + " in class " + 
					cache.getType(typeDeclaration).getCompUnit());
			*/
			
			return null;
		}
		else{
			return ExtractorUtils.getQualifiedTypeName(iTypeBinding);
		}
		
	}
	
}