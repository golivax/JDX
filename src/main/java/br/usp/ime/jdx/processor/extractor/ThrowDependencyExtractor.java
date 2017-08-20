package br.usp.ime.jdx.processor.extractor;

import java.util.List;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;

import br.usp.ime.jdx.entity.relationship.dependency.RawDependencyReport;
import br.usp.ime.jdx.entity.system.Method;
import br.usp.ime.jdx.entity.system.Type;
import br.usp.ime.jdx.filter.StringMatcher;
import br.usp.ime.jdx.processor.parser.CodeParser;

public class ThrowDependencyExtractor implements MethodDeclarationProcessor{
	
	private CodeParser cache;
	private RawDependencyReport depReport;
	private StringMatcher classFilter;
		
	public ThrowDependencyExtractor(CodeParser cacher, RawDependencyReport depReport, 
			StringMatcher classFilter){
		
		this.cache = cacher;
		this.depReport = depReport;
		this.classFilter = classFilter;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void processMethodDeclaration(MethodDeclaration methodDeclaration, 
			Method clientMethod){

		List<SimpleType> thrownExceptionTypes = methodDeclaration.thrownExceptionTypes();
		
		for(SimpleType thrownExceptionType : thrownExceptionTypes){
			
			ITypeBinding iTypeBinding = thrownExceptionType.resolveBinding();
	
			if(iTypeBinding != null){
				
				Type type = BindingResolver.resolveTypeBinding(
						classFilter, cache, iTypeBinding);
			
				if(type != null){
					depReport.addThrowDependency(clientMethod, type);
				}
			}
		}		
	}	
}