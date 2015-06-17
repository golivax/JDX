package br.usp.ime.jdx.processor.extractor;

import java.util.List;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import br.usp.ime.jdx.entity.relationship.dependency.DependencyReport;
import br.usp.ime.jdx.entity.system.Method;
import br.usp.ime.jdx.entity.system.Type;
import br.usp.ime.jdx.filter.StringMatcher;

public class ParameterDependencyExtractor implements MethodDeclarationProcessor{
	
	private Cache cache;
	private DependencyReport depReport;
	private StringMatcher classFilter;
		
	public ParameterDependencyExtractor(Cache cache, DependencyReport depReport, 
			StringMatcher classFilter){
		
		this.cache = cache;
		this.depReport = depReport;
		this.classFilter = classFilter;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void processMethodDeclaration(MethodDeclaration methodDeclaration, 
			Method clientMethod){

		List<SingleVariableDeclaration> methodParameters = 
				methodDeclaration.parameters();
		
		for(SingleVariableDeclaration methodParameter : methodParameters){
						
			ITypeBinding iTypeBinding = 
					methodParameter.getType().resolveBinding();
		
			if(iTypeBinding != null){
			
				Type type = BindingResolver.resolveTypeBinding(
						classFilter, cache, iTypeBinding );
			
				if(type != null){
					depReport.addParameterDependency(clientMethod, type);
				}
			}
		}		
	}	
}