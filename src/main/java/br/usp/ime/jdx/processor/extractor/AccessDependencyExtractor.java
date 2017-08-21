package br.usp.ime.jdx.processor.extractor;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SuperFieldAccess;

import br.usp.ime.jdx.entity.relationship.dependency.RawDependencyReport;
import br.usp.ime.jdx.entity.system.Method;
import br.usp.ime.jdx.entity.system.Type;
import br.usp.ime.jdx.filter.StringMatcher;
import br.usp.ime.jdx.processor.parser.CodeParser;

public class AccessDependencyExtractor implements ExpressionProcessor{
	
	private CodeParser cache;
	private RawDependencyReport depReport;
	private StringMatcher classFilter;
		
	public AccessDependencyExtractor(CodeParser cache, RawDependencyReport depReport, 
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
			processITypeBinding(clientMethod, iTypeBinding);
		}
		else if(expression instanceof SuperFieldAccess) {
			SuperFieldAccess superFieldAccess = (SuperFieldAccess)expression;
						
			//Resolves the "super" keyword (not the field)
			ITypeBinding bindingForSuper = superFieldAccess.resolveFieldBinding().getDeclaringClass();			
			processITypeBinding(clientMethod, bindingForSuper);
		}
	}
	
	private void processITypeBinding(Method clientMethod, ITypeBinding iTypeBinding) {
		if(iTypeBinding != null){
			
			Type type = BindingResolver.resolveTypeBinding(classFilter, cache, iTypeBinding);
			
			if(type != null){
				Method attribMethod = type.getAttribMethod();
				depReport.addAccessDependency(clientMethod, attribMethod);
			}
		}
	}
}