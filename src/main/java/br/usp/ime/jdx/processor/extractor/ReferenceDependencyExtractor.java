package br.usp.ime.jdx.processor.extractor;

import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import br.usp.ime.jdx.entity.relationship.dependency.DependencyReport;
import br.usp.ime.jdx.entity.system.Method;
import br.usp.ime.jdx.entity.system.Type;
import br.usp.ime.jdx.filter.StringMatcher;

public class ReferenceDependencyExtractor implements FieldDeclarationProcessor,
	ExpressionProcessor, VariableDeclarationStatementProcessor{
	
	private Cache cache;
	private DependencyReport depReport;
	private StringMatcher classFilter;
		
	public ReferenceDependencyExtractor(Cache cache, DependencyReport depReport, 
			StringMatcher classFilter){
		
		this.cache = cache;
		this.depReport = depReport;
		this.classFilter = classFilter;
	}
	
	@Override
	public void processExpression(Expression expression, Method clientMethod){

		if(expression instanceof CastExpression){ 
			
			CastExpression castExpression = (CastExpression)expression;
			ITypeBinding iTypeBinding = castExpression.resolveTypeBinding();
			
			processITypeBinding(clientMethod, iTypeBinding);
		}
	}

	@Override
	public void processVariableDeclarationStatement(
			VariableDeclarationStatement variableDeclarationSt,
			Method clientMethod) {

		ITypeBinding iTypeBinding = variableDeclarationSt.getType().resolveBinding();
		processITypeBinding(clientMethod, iTypeBinding);
		
	}

	@Override
	public void processFieldDeclaration(FieldDeclaration fieldDeclaration,
			Method clientMethod) {

		ITypeBinding iTypeBinding = fieldDeclaration.getType().resolveBinding();
		processITypeBinding(clientMethod, iTypeBinding);
		
	}

	private void processITypeBinding(Method clientMethod,
			ITypeBinding iTypeBinding) {
		if(iTypeBinding != null){
		
			Type type = BindingResolver.resolveTypeBinding(
					classFilter, cache, iTypeBinding);

			if(type != null){
				depReport.addReferenceDependency(clientMethod, type);
			}
		}
	}
}