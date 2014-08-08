package br.usp.ime.jdx.processor.extractor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import br.usp.ime.jdx.entity.Method;
import br.usp.ime.jdx.entity.Type;
import br.usp.ime.jdx.entity.dependency.DependencyReport;
import br.usp.ime.jdx.filter.Filter;

public class CallDependencyExtractor {
	
	private Cacher cacher;
	private Filter classFilter;
	private DependencyReport dependencyReport;
	private Method clientMethod;
	
	public CallDependencyExtractor(Cacher cacher){
		this.cacher = cacher;
	}
	
	public DependencyReport run(DependencyReport dependencyReport, 
			Filter classFilter) {
		
		this.classFilter = classFilter;
		this.dependencyReport = dependencyReport;
		
		extractImplicitDependencies();
		extractExplicitDependencies();	

		return dependencyReport;
	}

	private void extractImplicitDependencies() {
	
		//First off, we add dependencies from constructors to attrib<>
		for(Type type : cacher.getTypes()){
			for(Method constructor : type.getConstructors()){
				this.clientMethod = constructor;
				setUse(type.getAttribMethod());
			}
		}
		
		//TODO: Deal with superclasses
		//if G extends A and a constructor from G does not invoke any 
		//constructor from A, then the constructor from G implicitly calls the
		//default constructor from A
	}
	
	private void extractExplicitDependencies() {
		//Now we process the fields and methods of each type
		for(TypeDeclaration typeDeclaration : cacher.getTypeDeclarations()){
			processFieldsAndMethods(typeDeclaration);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void processFieldsAndMethods(TypeDeclaration typeDeclaration){

		//Processing fields
		for (FieldDeclaration fieldDeclaration : typeDeclaration.getFields()){

			Type type = cacher.getType(typeDeclaration);
			this.clientMethod = type.getAttribMethod();
			
			List<VariableDeclarationFragment> fragments = 
					fieldDeclaration.fragments();

			for(VariableDeclarationFragment fragment : fragments){
				processVariableDeclarationFragment(fragment);
			}
		}
		
		//Processing method declarations
		for (MethodDeclaration methodDeclaration : typeDeclaration.getMethods()){
			
			this.clientMethod = cacher.getMethod(methodDeclaration);
		
			Block codeBlock = methodDeclaration.getBody();
			if (codeBlock != null) processBlock(codeBlock);			
		}
	}

	@SuppressWarnings("unchecked")
	private void processBlock(Block codeBlock){
		try{
			List<Statement> statements = codeBlock.statements();
			for(Statement currentStatement : statements){
				processStatement(currentStatement);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private void processStatement(Statement currentStatement){
		switch(currentStatement.getNodeType()){

			case ASTNode.RETURN_STATEMENT: 
				processReturnStatement(
						(ReturnStatement) currentStatement);
				break;
	
			case ASTNode.EXPRESSION_STATEMENT:
				processExpressionStatement(
						(ExpressionStatement) currentStatement);
				break;
	
			case ASTNode.TRY_STATEMENT:
				processTryStatement((TryStatement) currentStatement);
				break;
	
			case ASTNode.IF_STATEMENT:
				processIfStatement((IfStatement) currentStatement);
				break;
	
			case ASTNode.FOR_STATEMENT:
				processForStatement((ForStatement) currentStatement);
				break;
	
			case ASTNode.WHILE_STATEMENT:
				processWhileStatement(
						(WhileStatement) currentStatement);
				break;
	
			case ASTNode.BLOCK:
				processBlock((Block) currentStatement);
				break;
	
			case ASTNode.VARIABLE_DECLARATION_STATEMENT:
				processVariableDeclarationStatement(
						(VariableDeclarationStatement) currentStatement);
				break;
	
			case ASTNode.SWITCH_STATEMENT:
				processSwitchStatement(
						(SwitchStatement) currentStatement);
				break;
	
			case ASTNode.SYNCHRONIZED_STATEMENT:
				processSynchronizedStatement(
						(SynchronizedStatement) currentStatement);
				break;
	
			case ASTNode.DO_STATEMENT:
				processDoStatement(
						(DoStatement) currentStatement);
				break;
	
			case ASTNode.SWITCH_CASE:
				processSwitchCase((SwitchCase) currentStatement);
				break;
	
			case ASTNode.ENHANCED_FOR_STATEMENT:
				processEnhancedForStatement(
						(EnhancedForStatement) currentStatement);
				break;
	
			case ASTNode.ASSERT_STATEMENT:
				processAssertStatement(
						(AssertStatement) currentStatement);
				break;
	
			case ASTNode.LABELED_STATEMENT:
				processLabeledStatement(
						(LabeledStatement) currentStatement);
				break;
	
			case ASTNode.CONSTRUCTOR_INVOCATION:
				processConstructorInvocation(
						(ConstructorInvocation) currentStatement);
				break;
	
			// do nothing for the following statements/cases
			case ASTNode.TYPE_DECLARATION_STATEMENT: break; // Local classes
			
			case ASTNode.SUPER_CONSTRUCTOR_INVOCATION: break;	
	
			case ASTNode.BREAK_STATEMENT: break;
	
			case ASTNode.EMPTY_STATEMENT: break;
	
			case ASTNode.THROW_STATEMENT: break;
	
			case ASTNode.CONTINUE_STATEMENT: break;
	
			default : System.err.println("\t" + currentStatement.getClass());
		}

	}
	
	@SuppressWarnings("unchecked")
	private void processConstructorInvocation(
			ConstructorInvocation constructorInvocation){
		
		List<Expression> expressions = constructorInvocation.arguments();
		
		for(Expression expression : expressions){
			processBindings(expression);
		}
		
		IMethodBinding methodBinding = 
				constructorInvocation.resolveConstructorBinding();
		
		if (methodBinding != null){
			processMethodBinding(methodBinding);	
		}
		
	}

	private void processLabeledStatement(LabeledStatement ls){
		
		Statement body = ls.getBody();
		processStatement(body);
	}

	private void processEnhancedForStatement(EnhancedForStatement efs){

		Statement body = efs.getBody();
		Expression expression = efs.getExpression();

		processStatement(body);
		processBindings(expression);

	}

	private void processDoStatement(DoStatement doStatement){

		Statement statement = doStatement.getBody();
		Expression doExpression = doStatement.getExpression();

		processStatement(statement);
		processBindings(doExpression);

	}

	private void processWhileStatement(WhileStatement whileStatement){

		Statement whileBody = whileStatement.getBody();
		Expression whileExpression = whileStatement.getExpression();

		processStatement(whileBody);
		processBindings(whileExpression);
	}

	@SuppressWarnings("unchecked")
	private void processSwitchStatement(SwitchStatement switchStatement){
		
		Expression switchExpression = switchStatement.getExpression();
		processBindings(switchExpression);

		List<Statement> switchStatements = switchStatement.statements();
		for(Statement statement : switchStatements){
			processStatement(statement);
		}
	}

	private void processSwitchCase(SwitchCase switchCase){
		Expression switchCaseExpression = switchCase.getExpression();
		processBindings(switchCaseExpression);
	}

	@SuppressWarnings("unchecked")
	private void processForStatement(ForStatement forStatement){
		
		Statement forBody = forStatement.getBody();
		Expression forExpression = forStatement.getExpression();
		List<Expression> initializerExpressions = forStatement.initializers();
		List<Expression> updaterExpressions = forStatement.updaters();

		processStatement(forBody);
		
		if(forExpression instanceof InfixExpression){
			InfixExpression infixExpression = (InfixExpression)forExpression;
			processBindings(infixExpression.getLeftOperand());
			processBindings(infixExpression.getRightOperand());
		}
		
		for(Expression initializerExpression : initializerExpressions){
			processBindings(initializerExpression);
		}

		for(Expression updaterExpression : updaterExpressions){
			processBindings(updaterExpression);
		}
	}

	private void processIfStatement(IfStatement ifStatement){

		Expression ifExpression = ifStatement.getExpression();
		processBindings(ifExpression);

		Statement thenStatement = ifStatement.getThenStatement();
		if (thenStatement != null) processStatement(thenStatement);
		
		Statement elseStatement = ifStatement.getElseStatement();
		if (elseStatement != null) processStatement(elseStatement);

	}

	private void processExpressionStatement(
			ExpressionStatement	expressionStatement){

		Expression expression = expressionStatement.getExpression();
		
		if(expression instanceof Assignment){
			Assignment assignment = (Assignment)expression;
			processBindings(assignment.getLeftHandSide());
			processBindings(assignment.getRightHandSide());
		}
		else{
			processBindings(expression);
		}
		
	}

	private void processReturnStatement(ReturnStatement returnStatement){

		if(returnStatement != null){
			Expression expression = returnStatement.getExpression();
			if (expression != null){
				processBindings(expression);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void processVariableDeclarationStatement(
			VariableDeclarationStatement currentVariableDeclaration){
	
		List<VariableDeclarationFragment> fragments = 
				currentVariableDeclaration.fragments();

		for(VariableDeclarationFragment fragment : fragments){
			processVariableDeclarationFragment(fragment);
		}
	}

	private void processVariableDeclarationFragment(
			VariableDeclarationFragment fragment){

		Expression initializer = fragment.getInitializer();
		processBindings(initializer);
	}

	@SuppressWarnings("unchecked")
	private void processTryStatement(TryStatement currentStatement){

		Block tryBlock = currentStatement.getBody();
		processBlock(tryBlock);
			
		List<CatchClause> catchBlock = currentStatement.catchClauses();
		for(CatchClause catchClause : catchBlock){
			processCatchClause(catchClause);
		}
			
		Block finallyBlock = currentStatement.getFinally();			
		if (finallyBlock != null) processBlock(finallyBlock);
	}

	private void processCatchClause(CatchClause catchClause){
		
		if(catchClause != null){
			Block catchBlock = catchClause.getBody();
			processBlock(catchBlock);
		}
	}

	private void processSynchronizedStatement(
			SynchronizedStatement synchronizedStat){

		if(synchronizedStat != null){
			Block synchronizedBlock = synchronizedStat.getBody();
			Expression syncExpression = synchronizedStat.getExpression();

			processBlock(synchronizedBlock);
			processBindings(syncExpression);
		}
	}

	private void processAssertStatement(AssertStatement as){
		
		Expression expression = as.getExpression();
		processBindings(expression);
		
		Expression message = as.getMessage();		
		processBindings(message);		
	}

	private void processBindings(Expression expression){

		IMethodBinding methodBinding = null;
		
		//Dependency due to method invocation
		if(expression instanceof MethodInvocation){ 
			
			MethodInvocation methodInv = (MethodInvocation)expression;
			methodBinding = methodInv.resolveMethodBinding();
			
		//Dependency due to instance creation
		}else if (expression instanceof ClassInstanceCreation){
			
			ClassInstanceCreation instanceCreation = 
					(ClassInstanceCreation)expression;
			
			methodBinding = instanceCreation.resolveConstructorBinding();
			
		}else if (expression != null){
			//There are other types of expressions, including
			//class org.eclipse.jdt.core.dom.PostfixExpression, 
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
			//class org.eclipse.jdt.core.dom.InfixExpression, 
			//class org.eclipse.jdt.core.dom.ArrayAccess, 
			//class org.eclipse.jdt.core.dom.ParenthesizedExpression, 
			//class org.eclipse.jdt.core.dom.PrefixExpression]
		}
		
		//MethodBinding may be null when it involves external classes 
		//(JAR files, etc). That is, class files which are not available 
		//in the provided context (environment)
		if (methodBinding != null){	
			
			//We actually process the method declaration to avoid resolving
			//parameter types
			processMethodBinding(methodBinding.getMethodDeclaration());
		}
	}

	private void processMethodBinding(IMethodBinding binding){
				
		//It is null when a generic class/interface takes a certain 
		//type parameter that does not exist in source code
		//e.g. "ArrayList<Player> opponents = new ArrayList<Player>();" and
		//the Player class/interface does not exist in source code
		if(binding.getDeclaringClass() != null){			
			if (!binding.getDeclaringClass().isAnonymous() && 
				!binding.getDeclaringClass().isLocal()){		
				
				String providerTypeName = ExtractorUtils.getQualifiedTypeName(
 						binding.getDeclaringClass());
				
				if (!classFilter.matches(providerTypeName)){
					
					String methodName = binding.getName();
					if(binding.getDeclaringClass().isParameterizedType()){
						methodName = StringUtils.substringBefore(methodName, "<");
					}
					
					List<String> parameterTypes = new ArrayList<>();
										
					for(ITypeBinding typeBinding : binding.getParameterTypes()){
						String parameterType = typeBinding.getName();
						parameterTypes.add(parameterType);
					}
										
					Type providerType =	cacher.getType(providerTypeName);
					
					//FIXME: Check why this is happening in JHotDraw
					if(providerType != null){
					
						Method providerMethod =	
								providerType.getMethod(methodName, parameterTypes);

						//FIXME: Check why this is happening in JHotDraw
						if(providerMethod != null){
							setUse(providerMethod);	
						}
						
					}
				}
			}
		}
	}

	private void setUse(Method providerMethod){	
		dependencyReport.addMethodCallDependency(clientMethod, providerMethod);
	}
		
	
}