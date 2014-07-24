package br.usp.ime.jdx.processor.extractor;

import java.util.List;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import br.usp.ime.jdx.entity.dependency.DependencyReport;
import br.usp.ime.jdx.filter.Filter;

/**
 * Extracts relationships between types. For now it detects
 * inheritance and interface implementation
 * @author Gustavo Ansaldi Oliva {@link goliva@ime.usp.br}
 *
 */
public class TypeDependencyExtractor {

	private Cacher cacher;
	private Filter classFilter;
	private DependencyReport dependencyReport;
	
	public TypeDependencyExtractor(Cacher cacher){
		this.cacher = cacher;
	}
	
	public DependencyReport run(DependencyReport dependencyReport, 
			Filter classFilter) {
		
		this.classFilter = classFilter;
		this.dependencyReport = dependencyReport;
		
		for(TypeDeclaration typeDeclaration : cacher.getTypeDeclarations()){	
			detectClassInheritance(typeDeclaration);
			detectInterfaceImplementationAndExtension(typeDeclaration);
		}
	
		return dependencyReport;
	}
	
	private void detectClassInheritance(TypeDeclaration typeDeclaration) {

		Type superClassType = typeDeclaration.getSuperclassType();

		if(superClassType != null){

			String superClassName = getBindingName(
					typeDeclaration, superClassType);

			if (superClassName != null && !classFilter.matches(superClassName)){

				dependencyReport.addInheritanceDependency(
						cacher.getClazz(typeDeclaration), 
						cacher.getClazz(superClassName));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void detectInterfaceImplementationAndExtension(
			TypeDeclaration typeDeclaration) {
			
		if (typeDeclaration.isInterface()){

			//For an interface declaration, these are the interfaces 
			//that this interface extends.	
			List<Type> extendedInterfaces = 
					typeDeclaration.superInterfaceTypes();

			for(Type extendedInterface : extendedInterfaces){

				String extendedInterfaceName = 
						getBindingName(typeDeclaration, extendedInterface);

				if (extendedInterfaceName != null && 
						!classFilter.matches(extendedInterfaceName)){

					dependencyReport.addInheritanceDependency(
							cacher.getInterface(typeDeclaration), 
							cacher.getInterface(extendedInterfaceName));
				}
			}			
		}
		else{

			//For a class declaration, these are the interfaces that 
			//this class implements
			List<Type> implementedInterfaces = 
					typeDeclaration.superInterfaceTypes();

			for(Type implementedInterface :	implementedInterfaces){

				String implementedInterfaceName = 
						getBindingName(typeDeclaration, implementedInterface);

				if (implementedInterfaceName != null && 
						!classFilter.matches(implementedInterfaceName)){

					dependencyReport.addImplementsDependency(
							cacher.getClazz(typeDeclaration), 
							cacher.getInterface(implementedInterfaceName));
				}
			}			

		}
		
	}

	private String getBindingName(TypeDeclaration typeDeclaration, Type type) {
		
		ITypeBinding typeBinding = type.resolveBinding();
		
		if(typeBinding == null){
			
			System.out.println("WARNING: Could not find binding for " + 
					type + " in class " + 
					cacher.getType(typeDeclaration).getCompUnit());
			
			return null;
		}
		else{
			return ExtractorUtils.getQualifiedTypeName(typeBinding);
		}
		
	}
	
}