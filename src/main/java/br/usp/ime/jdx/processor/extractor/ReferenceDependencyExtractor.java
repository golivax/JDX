package br.usp.ime.jdx.processor.extractor;

import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import br.usp.ime.jdx.entity.relationship.dependency.RawDependencyReport;
import br.usp.ime.jdx.entity.system.Method;
import br.usp.ime.jdx.entity.system.Type;
import br.usp.ime.jdx.filter.StringMatcher;
import br.usp.ime.jdx.processor.parser.CodeParser;

public class ReferenceDependencyExtractor implements FieldDeclarationProcessor,
	ExpressionProcessor, VariableDeclarationStatementProcessor{
	
	private CodeParser cache;
	private RawDependencyReport depReport;
	private StringMatcher classFilter;
		
	public ReferenceDependencyExtractor(CodeParser cache, RawDependencyReport depReport, 
			StringMatcher classFilter){
		
		this.cache = cache;
		this.depReport = depReport;
		this.classFilter = classFilter;
	}
	
	@Override
	public void processExpression(Expression expression, Method clientMethod){

		if(expression instanceof ArrayCreation) {
			ArrayCreation arrayCreation = (ArrayCreation)expression;
			ITypeBinding iTypeBinding = arrayCreation.resolveTypeBinding();
			
			processITypeBinding(clientMethod, iTypeBinding);
		}
		else if(expression instanceof CastExpression){ 
			
			CastExpression castExpression = (CastExpression)expression;
			ITypeBinding iTypeBinding = castExpression.resolveTypeBinding();
			
			processITypeBinding(clientMethod, iTypeBinding);
		}
		else if(expression instanceof InstanceofExpression){ 
			
			InstanceofExpression instanceofExpression = (InstanceofExpression)expression;
			ITypeBinding iTypeBinding = instanceofExpression.getRightOperand().resolveBinding();
			
			processITypeBinding(clientMethod, iTypeBinding);
		}
		else if(expression instanceof TypeLiteral){ 
			
			TypeLiteral typeLiteral = (TypeLiteral)expression;
			ITypeBinding iTypeBinding = typeLiteral.getType().resolveBinding();
			
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
		
		//We don't want bindings to primitive types
		if(iTypeBinding != null && !iTypeBinding.isPrimitive()){
			
			Type type = BindingResolver.resolveTypeBinding(classFilter, cache, iTypeBinding);

			if(type != null){
				depReport.addReferenceDependency(clientMethod, type);
			}
		}
	}
}