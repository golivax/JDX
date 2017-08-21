package br.usp.ime.jdx.processor.extractor;

import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

import br.usp.ime.jdx.entity.relationship.dependency.RawDependencyReport;
import br.usp.ime.jdx.entity.system.Method;
import br.usp.ime.jdx.filter.StringMatcher;
import br.usp.ime.jdx.processor.parser.CodeParser;

public class ExplicitCallDependencyExtractor implements ExpressionProcessor, 
	ConstructorInvocationProcessor{
	
	private CodeParser cache;
	private RawDependencyReport depReport;
	private StringMatcher classFilter;
	
	private Method clientMethod;
	
	public ExplicitCallDependencyExtractor(CodeParser cache, RawDependencyReport depReport, 
			StringMatcher classFilter){
		
		this.cache = cache;
		this.depReport = depReport;
		this.classFilter = classFilter;
	}
	
	@Override
	public void processExpression(Expression expression, Method clientMethod){
		
		this.clientMethod = clientMethod;
		
		IMethodBinding methodBinding = null;
		
		//Dependency due to method invocation
		if(expression instanceof MethodInvocation){ 
			
			MethodInvocation methodInv = (MethodInvocation)expression;
			methodBinding = methodInv.resolveMethodBinding();
		
		//Dependency due to method invocation
		}else if (expression instanceof SuperMethodInvocation){
			
			SuperMethodInvocation superMethodInv = (SuperMethodInvocation)expression;
			methodBinding = superMethodInv.resolveMethodBinding();
		
		//Dependency due to instance creation
		}else if (expression instanceof ClassInstanceCreation){
			
			ClassInstanceCreation instanceCreation = 
					(ClassInstanceCreation)expression;
			
			methodBinding = instanceCreation.resolveConstructorBinding();
			
		}else if(expression != null){
			//There are other types of expressions, including
			//class org.eclipse.jdt.core.dom.ThisExpression, 
			//class org.eclipse.jdt.core.dom.VariableDeclarationExpression, 
			//class org.eclipse.jdt.core.dom.Assignment, 
			//class org.eclipse.jdt.core.dom.NullLiteral, 
			//class org.eclipse.jdt.core.dom.ArrayCreation, 
			//class org.eclipse.jdt.core.dom.SimpleName, 
			//class org.eclipse.jdt.core.dom.FieldAccess, 
			//class org.eclipse.jdt.core.dom.ArrayInitializer, 
			//class org.eclipse.jdt.core.dom.InstanceofExpression, 
			//class org.eclipse.jdt.core.dom.CharacterLiteral, 
			//class org.eclipse.jdt.core.dom.QualifiedName, 
			//class org.eclipse.jdt.core.dom.SuperMethodInvocation, 
			//class org.eclipse.jdt.core.dom.ConditionalExpression, 
			//class org.eclipse.jdt.core.dom.NumberLiteral, 
			//class org.eclipse.jdt.core.dom.CastExpression, 
			//class org.eclipse.jdt.core.dom.StringLiteral, 
			//class org.eclipse.jdt.core.dom.BooleanLiteral, 
			//class org.eclipse.jdt.core.dom.ArrayAccess, 
			//class org.eclipse.jdt.core.dom.ParenthesizedExpression, 
		}
		
		//MethodBinding may be null when it involves external classes 
		//(JAR files, etc). That is, class files which are not available 
		//in the provided context (environment)
		if (methodBinding != null){	
			
			//We actually process the method declaration to avoid 
			//having parameter types resolved
			if(methodBinding.getMethodDeclaration() != null){
				processMethodBinding(methodBinding.getMethodDeclaration());	
			}
			//In the weird case where getMethodDeclaration returns null, we
			//process the method binding
			else{
				processMethodBinding(methodBinding);
			}
		}
	}
	
	@Override
	public void processConstructorInvocation(
			ConstructorInvocation constructorInvocation, 
			Method clientMethod){
		
		this.clientMethod = clientMethod;
		
		IMethodBinding methodBinding = 
				constructorInvocation.resolveConstructorBinding();
		
		//MethodBinding may be null when it involves external classes 
		//(JAR files, etc). That is, class files which are not available 
		//in the provided context (environment). 
		//Example: Lucene project, revision 921480, the class IndexWriterConfig
		//referenced in IndexWriter does not exist in org.apache.lucene.index
		if(methodBinding != null){
			processMethodBinding(methodBinding);
		}
	}


	private void processMethodBinding(IMethodBinding iMethodBinding){
	
		Method providerMethod = BindingResolver.resolveMethodBinding(
				classFilter, cache, iMethodBinding);
		
		//FIXME: Check why this is happening in JHotDraw
		if(providerMethod != null){
			depReport.addMethodCallDependency(clientMethod, providerMethod);
		}			
	}
	
}