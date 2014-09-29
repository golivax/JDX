package br.usp.ime.jdx.processor.extractor;

import java.util.List;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import br.usp.ime.jdx.entity.relationship.dependency.DependencyReport;
import br.usp.ime.jdx.filter.StringMatcher;

/**
 * Extracts relationships between types. For now it detects
 * inheritance and interface implementation
 * @author Gustavo Ansaldi Oliva {@link goliva@ime.usp.br}
 *
 */
public class TypeDependencyExtractor {

	private Cacher cacher;
	private StringMatcher classFilter;
	private DependencyReport dependencyReport;
	
	public TypeDependencyExtractor(Cacher cacher){
		this.cacher = cacher;
	}
	
	public DependencyReport run(DependencyReport dependencyReport, 
			StringMatcher classFilter) {
		
		this.classFilter = classFilter;
		this.dependencyReport = dependencyReport;
		
		for(TypeDeclaration clazzTypeDeclaration : 
			cacher.getClazzTypeDeclarations()){
			
			detectClassInheritance(clazzTypeDeclaration);
			detectInterfaceImplementation(clazzTypeDeclaration);
		}
		
		for(TypeDeclaration interfaceTypeDeclaration : 
			cacher.getInterfaceTypeDeclarations()){

			detectInterfaceInheritance(interfaceTypeDeclaration);
		}
	
		return dependencyReport;
	}
	
	private void detectClassInheritance(TypeDeclaration clazzTypeDeclaration) {

		Type superClassType = clazzTypeDeclaration.getSuperclassType();

		if(superClassType != null){

			String superClassName = getBindingName(
					clazzTypeDeclaration, superClassType);

			if (superClassName != null && !classFilter.matches(superClassName)){

				dependencyReport.addClazzInheritanceDependency(
						cacher.getClazz(clazzTypeDeclaration), 
						cacher.getClazz(superClassName));
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

				dependencyReport.addImplementsDependency(
						cacher.getClazz(clazzTypeDeclaration), 
						cacher.getInterface(implementedInterfaceName));
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

				dependencyReport.addInterfaceInheritanceDependency(
						cacher.getInterface(interfaceTypeDeclaration), 
						cacher.getInterface(extendedInterfaceName));
			}
		}			
	}

	private String getBindingName(TypeDeclaration typeDeclaration, Type type) {
		
		ITypeBinding typeBinding = type.resolveBinding();
		
		if(typeBinding == null){
			
			/**
			System.out.println("WARNING: Could not find binding for " + 
					type + " in class " + 
					cacher.getType(typeDeclaration).getCompUnit());
			*/
			return null;
		}
		else{
			return ExtractorUtils.getQualifiedTypeName(typeBinding);
		}
		
	}
	
}