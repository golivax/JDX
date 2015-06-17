package br.usp.ime.jdx.processor.extractor;

import br.usp.ime.jdx.entity.relationship.dependency.DependencyReport;
import br.usp.ime.jdx.entity.system.Method;
import br.usp.ime.jdx.entity.system.Type;

public class ImplicitCallDependencyExtractor{
	
	private Cache cacher;
	
	public ImplicitCallDependencyExtractor(Cache cacher){
		this.cacher = cacher;
	}
	
	public void run(DependencyReport depReport) {
		
		//First off, we add implicit dependencies from constructors to attrib<>
		for(Type type : cacher.getTypes()){
			for(Method constructor : type.getConstructors()){
				Method clientMethod = constructor;
				Method providerMethod = type.getAttribMethod();
				depReport.addMethodCallDependency(clientMethod, providerMethod);
			}
		}
		
		//TODO: Deal with superclasses
		//if G extends A and a constructor from G does not invoke any 
		//constructor from A, then the constructor from G implicitly calls the
		//default constructor from A
	}
	
}