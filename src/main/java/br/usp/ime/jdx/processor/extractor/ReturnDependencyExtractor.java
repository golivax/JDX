package br.usp.ime.jdx.processor.extractor;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import br.usp.ime.jdx.entity.relationship.dependency.RawDependencyReport;
import br.usp.ime.jdx.entity.system.Method;
import br.usp.ime.jdx.entity.system.Type;
import br.usp.ime.jdx.filter.StringMatcher;
import br.usp.ime.jdx.processor.parser.CodeParser;

public class ReturnDependencyExtractor implements MethodDeclarationProcessor{
	
	private CodeParser cache;
	private RawDependencyReport depReport;
	private StringMatcher classFilter;
		
	public ReturnDependencyExtractor(CodeParser cache, RawDependencyReport depReport, 
			StringMatcher classFilter){
		
		this.cache = cache;
		this.depReport = depReport;
		this.classFilter = classFilter;
	}
	
	@Override
	public void processMethodDeclaration(MethodDeclaration methodDeclaration, 
			Method clientMethod){

		//Constructor have "null" return type and 
		//void is a primitive type 
		if(methodDeclaration.getReturnType2() != null && 
		   !methodDeclaration.getReturnType2().isPrimitiveType()){
		
			ITypeBinding iTypeBinding = 
					methodDeclaration.getReturnType2().resolveBinding();
			
			if(iTypeBinding != null){
			
				Type type = BindingResolver.resolveTypeBinding(
						classFilter, cache, iTypeBinding);
				
				if(type != null){
					depReport.addReturnDependency(clientMethod, type);
				}
			}
		}
	}
	
}