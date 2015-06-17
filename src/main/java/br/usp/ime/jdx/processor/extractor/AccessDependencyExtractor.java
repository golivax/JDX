package br.usp.ime.jdx.processor.extractor;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;

import br.usp.ime.jdx.entity.relationship.dependency.DependencyReport;
import br.usp.ime.jdx.entity.system.Method;
import br.usp.ime.jdx.entity.system.Type;
import br.usp.ime.jdx.filter.StringMatcher;

public class AccessDependencyExtractor implements ExpressionProcessor{
	
	private Cache cache;
	private DependencyReport depReport;
	private StringMatcher classFilter;
		
	public AccessDependencyExtractor(Cache cache, DependencyReport depReport, 
			StringMatcher classFilter){
		
		this.cache = cache;
		this.depReport = depReport;
		this.classFilter = classFilter;
	}
	
	@Override
	public void processExpression(Expression expression, Method clientMethod){
				
		if(expression instanceof QualifiedName){ 
			
			QualifiedName qualifiedName = (QualifiedName)expression;
			Name qualifier = qualifiedName.getQualifier();
			ITypeBinding iTypeBinding = qualifier.resolveTypeBinding();
			
			if(iTypeBinding != null){
				Type type = BindingResolver.resolveTypeBinding(
						classFilter, cache, iTypeBinding);
				
				if(type != null){
					Method attribMethod = type.getAttribMethod();
					depReport.addAccessDependency(clientMethod, attribMethod);
				}
			}
		}
	}
	
}