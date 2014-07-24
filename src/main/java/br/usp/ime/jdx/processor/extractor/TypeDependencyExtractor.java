package br.usp.ime.jdx.processor.extractor;

import java.util.List;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import br.usp.ime.jdx.entity.Type;
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
		
		detectClassInheritance();
		detectInterfaceImplementationAndExtension();
	
		return dependencyReport;
	}
	
	private void detectClassInheritance() {
		for(TypeDeclaration typeDeclaration : cacher.getTypeDeclarations()){
						
			if(typeDeclaration.getSuperclassType() != null){
				
				Type type = cacher.getType(typeDeclaration);
				
				if(typeDeclaration.getSuperclassType().resolveBinding() == null){
					
					System.out.println("WARNING: Could not find binding for " + 
							typeDeclaration.getSuperclassType() + " in class " + 
							type.getCompUnit());					
				}
				else{
					String parentTypeName = ExtractorUtils.getQualifiedTypeName(
						typeDeclaration.getSuperclassType().resolveBinding());
					
					addDependency(type, parentTypeName);
				}				
			}
		}
	}

	private void detectInterfaceImplementationAndExtension() {
		for(TypeDeclaration typeDeclaration : cacher.getTypeDeclarations()){
			
			//For a class declaration, these are the interfaces that this class 
			//implements; for an interface declaration, these are the interfaces 
			//that this interface extends.
			List<org.eclipse.jdt.core.dom.Type> implementedInterfaces = 
					typeDeclaration.superInterfaceTypes();
			
			for(org.eclipse.jdt.core.dom.Type implementedInterface : implementedInterfaces){
				
				Type type = cacher.getType(typeDeclaration);
				
				if(implementedInterface.resolveBinding() == null){
					
					System.out.println("WARNING: Could not find binding for " + 
							implementedInterface + " in class " + 
							type.getCompUnit());	
				}
				else{
					String implementedInterfaceName = 
						ExtractorUtils.getQualifiedTypeName(
							implementedInterface.resolveBinding());
					
					addDependency(type, implementedInterfaceName);
				}
			}
		}
		
	}

	private void addDependency(Type clientType, String supplierTypeName){

		if (!classFilter.matches(supplierTypeName)){	
			
			Type providerType = cacher.getType(supplierTypeName);			
			dependencyReport.addDependency(clientType,providerType);
		}		
	}
}