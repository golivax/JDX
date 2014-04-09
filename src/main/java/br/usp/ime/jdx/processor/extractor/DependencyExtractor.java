package br.usp.ime.jdx.processor.extractor;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IfStatement;
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
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import br.usp.ime.jdx.entity.DependencyReport;
import br.usp.ime.jdx.filter.Filter;
import br.usp.ime.jdx.processor.visitor.BatchCompilationUnitVisitor;

public class DependencyExtractor extends BatchCompilationUnitVisitor{

	//TODO: DependencyReport should be [clientType, supplierType] = Strength
	//TODO: DependencyReport should provide a method that returns dependencies between files
	//(que � o que o JDX cospe hoje)
	
	private Filter classFilter;
	private DependencyReport dependencyReport;
	private Integer processedTypes;
	
	private String sourceDir;
	
	public DependencyExtractor(){
		super(true);
	}
	
	public void run(String sourceDir, Filter fileFilter, Filter classFilter){
		this.processedTypes = 0;
		this.sourceDir = sourceDir;
		this.classFilter = classFilter;
		
		//Creates a new dependency report and extracts the dependencies		
		dependencyReport = new DependencyReport();
				
		super.processCode(sourceDir, fileFilter);
		
		System.out.println("Number of types processed: " + processedTypes);	
	}

	@Override
	protected void processCompilationUnit(String sourceDir, 
			String sourceFilePath, CompilationUnit compilationUnit) {
								
		//Obtaining the list of types inside the compilation unit
		List<TypeDeclaration> typeList = compilationUnit.types();
		
		for(TypeDeclaration type : typeList){					
			
			//One more processed type
			processedTypes++;
			
			//TypeName (por enquanto n�o vou usar, mas usarei no futuro)
			String typeName = type.getName().toString(); 
			if (compilationUnit.getPackage() != null){
				typeName = compilationUnit.getPackage().getName() + 
						"." + typeName;
			}
			
			String currentClass = sourceFilePath.replace(sourceDir, "");
			System.out.println("Current class: " + currentClass);
			
			// handling the method declaration within this class
			// TODO: Refatorar isso pelo amor de Deus (ficar carregando o
			// currentClass por todos os m�todos privados abaixo)
			processMethodDeclarations(type.getMethods(), currentClass);
			
			//Do it for subtypes also
			if (type.getTypes().length > 0){
				processMethodsFromSubtypes(typeName, type.getTypes());
			}
		}		
	}

	private void processMethodsFromSubtypes(String typeName, 
			TypeDeclaration[] nestedTypes){

		//These are usually static nested classes
		for(TypeDeclaration nestedType : nestedTypes){

			//One more processed type
			processedTypes++;
			
			String nestedTypeName = typeName + "." + nestedType.getName();
			
			// getting the method declaration within this class
			processMethodDeclarations(nestedType.getMethods(), nestedTypeName);
				
			//Believe it or not: static nested classes may also have
			//other static nested classes
			if (nestedType.getTypes().length > 0){
				
				processMethodsFromSubtypes(nestedTypeName, 
						nestedType.getTypes());				
			}
		}
	}
	
	private void processMethodDeclarations(MethodDeclaration[] methods, 
			String currentClass){

		for (MethodDeclaration methodDeclaration : methods){
			// getting the blocks inside the current method
			Block codeBlock = methodDeclaration.getBody();
			if (codeBlock != null) processBlock(codeBlock, currentClass);			
		}
	}

	private void processBlock(Block codeBlock, String currentClass){
		try{
			List<Statement> statements = codeBlock.statements();
			for(Statement currentStatement : statements){
				processStatement(currentStatement, currentClass);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private void processStatement(Statement currentStatement, String currentClass){
		switch(currentStatement.getNodeType()){

			case ASTNode.RETURN_STATEMENT: 
				processReturnStatement(
						(ReturnStatement) currentStatement, currentClass);
				break;
	
			case ASTNode.EXPRESSION_STATEMENT:
				processExpressionStatement(
						(ExpressionStatement) currentStatement, currentClass);
				break;
	
			case ASTNode.TRY_STATEMENT:
				processTryStatement((TryStatement) currentStatement, currentClass);
				break;
	
			case ASTNode.IF_STATEMENT:
				processIfStatement((IfStatement) currentStatement, currentClass);
				break;
	
			case ASTNode.FOR_STATEMENT:
				processForStatement((ForStatement) currentStatement, currentClass);
				break;
	
			case ASTNode.WHILE_STATEMENT:
				processWhileStatement(
						(WhileStatement) currentStatement, currentClass);
				break;
	
			case ASTNode.BLOCK:
				processBlock((Block) currentStatement, currentClass);
				break;
	
			case ASTNode.VARIABLE_DECLARATION_STATEMENT:
				processVariableDeclarationStatement(
						(VariableDeclarationStatement) currentStatement, currentClass);
				break;
	
			case ASTNode.SWITCH_STATEMENT:
				processSwitchStatement(
						(SwitchStatement) currentStatement, currentClass);
				break;
	
			case ASTNode.SYNCHRONIZED_STATEMENT:
				processSynchronizedStatement(
						(SynchronizedStatement) currentStatement, currentClass);
				break;
	
			case ASTNode.DO_STATEMENT:
				processDoStatement(
						(DoStatement) currentStatement, currentClass);
				break;
	
			case ASTNode.SWITCH_CASE:
				processSwitchCase((SwitchCase) currentStatement, currentClass);
				break;
	
			case ASTNode.ENHANCED_FOR_STATEMENT:
				processEnhancedForStatement(
						(EnhancedForStatement) currentStatement, currentClass);
				break;
	
			case ASTNode.ASSERT_STATEMENT:
				processAssertStatement(
						(AssertStatement) currentStatement, currentClass);
				break;
	
			case ASTNode.LABELED_STATEMENT:
				processLabeledStatement(
						(LabeledStatement) currentStatement, currentClass);
				break;
	
			case ASTNode.CONSTRUCTOR_INVOCATION:
				processConstructorInvocation(
						(ConstructorInvocation) currentStatement, currentClass);
				break;
	
			case ASTNode.TYPE_DECLARATION_STATEMENT:
				processTypeDeclarationStatement(
						(TypeDeclarationStatement) currentStatement, currentClass);
				break;
	
				// do nothing for the following statements/cases
	
			case ASTNode.SUPER_CONSTRUCTOR_INVOCATION: break;	
	
			case ASTNode.BREAK_STATEMENT: break;
	
			case ASTNode.EMPTY_STATEMENT: break;
	
			case ASTNode.THROW_STATEMENT: break;
	
			case ASTNode.CONTINUE_STATEMENT: break;
	
			default : System.err.println("\t" + currentStatement.getClass());
		}

	}
	
	private void processTypeDeclarationStatement(TypeDeclarationStatement tds, 
			String currentClass){
		
		ITypeBinding binding = tds.resolveBinding();
		processTypeBinding(binding, currentClass);
	}

	private void processTypeBinding(ITypeBinding binding, String currentClass){
		if(binding != null){		
				
			String providerClass = 
					getProviderClass(binding.getDeclaringClass());
				
			if (providerClass != null) setUse(currentClass, providerClass);
		}
	}

	@SuppressWarnings("unchecked")
	private void processConstructorInvocation(
			ConstructorInvocation constructorInvocation, String currentClass){
		
		List<Expression> expressions = constructorInvocation.arguments();
		
		for(Expression expression : expressions){
			processExpression(expression, currentClass);
		}
		
		IMethodBinding methodBinding = 
				constructorInvocation.resolveConstructorBinding();
		
		if (methodBinding != null){
			processMethodBinding(methodBinding, currentClass);	
		}
		
	}

	private void processLabeledStatement(LabeledStatement ls, 
			String currentClass){
		
		Statement body = ls.getBody();
		processStatement(body, currentClass);
	}

	private void processEnhancedForStatement(EnhancedForStatement efs,
			String currentClass){

		Statement body = efs.getBody();
		Expression expression = efs.getExpression();

		processStatement(body, currentClass);
		processExpression(expression, currentClass);

	}

	private void processDoStatement(DoStatement doStatement,
			String currentClass){

		Statement statement = doStatement.getBody();
		Expression doExpression = doStatement.getExpression();

		processStatement(statement, currentClass);
		processExpression(doExpression, currentClass);

	}

	private void processWhileStatement(WhileStatement whileStatement,
			String currentClass){

		Statement whileBody = whileStatement.getBody();
		Expression whileExpression = whileStatement.getExpression();

		processStatement(whileBody, currentClass);
		processExpression(whileExpression, currentClass);
	}

	@SuppressWarnings("unchecked")
	private void processSwitchStatement(SwitchStatement switchStatement, 
			String currentClass){
		
		Expression switchExpression = switchStatement.getExpression();
		processExpression(switchExpression, currentClass);

		List<Statement> switchStatements = switchStatement.statements();
		for(Statement statement : switchStatements){
			processStatement(statement, currentClass);
		}
	}

	private void processSwitchCase(SwitchCase switchCase, String currentClass){
		Expression switchCaseExpression = switchCase.getExpression();
		processExpression(switchCaseExpression, currentClass);
	}

	@SuppressWarnings("unchecked")
	private void processForStatement(ForStatement forStatement, 
			String currentClass){
		
		Statement forBody = forStatement.getBody();
		Expression forExpression = forStatement.getExpression();
		List<Expression> initializerExpressions = forStatement.initializers();
		List<Expression> updaterExpressions = forStatement.updaters();

		processStatement(forBody, currentClass);
		processExpression(forExpression, currentClass);

		for(Expression initializerExpression : initializerExpressions){
			processExpression(initializerExpression, currentClass);
		}

		for(Expression updaterExpression : updaterExpressions){
			processExpression(updaterExpression, currentClass);
		}
	}

	private void processIfStatement(IfStatement ifStatement, 
			String currentClass){

		Expression ifExpression = ifStatement.getExpression();
		processExpression(ifExpression, currentClass);
		
		Statement elseStatement = ifStatement.getElseStatement();
		if (elseStatement != null) processStatement(elseStatement, currentClass);

	}

	private void processExpressionStatement(
			ExpressionStatement	expressionStatement,String currentClass){

		Expression expression = expressionStatement.getExpression();
		processExpression(expression, currentClass);
			
	}

	private void processReturnStatement(ReturnStatement currentClass, 
			String currentType){

		if(currentClass != null){
			Expression expression = currentClass.getExpression();
			if (expression != null){
				processExpression(expression, currentType);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void processVariableDeclarationStatement(
			VariableDeclarationStatement currentVariableDeclaration, 
			String currentClass){
	
		List<VariableDeclarationFragment> fragments = 
				currentVariableDeclaration.fragments();

		for(VariableDeclarationFragment fragment : fragments){
			processVariableDeclarationFragment(fragment, currentClass);
		}
	}

	private void processVariableDeclarationFragment(
			VariableDeclarationFragment fragment,
			String currentClass){

		Expression initializer = fragment.getInitializer();
		processExpression(initializer, currentClass);
	}

	@SuppressWarnings("unchecked")
	private void processTryStatement(TryStatement currentStatement,
			String currentClass){

		Block tryBlock = currentStatement.getBody();
		processBlock(tryBlock, currentClass);
			
		List<CatchClause> catchBlock = currentStatement.catchClauses();
		for(CatchClause catchClause : catchBlock){
			processCatchClause(catchClause, currentClass);
		}
			
		Block finallyBlock = currentStatement.getFinally();			
		if (finallyBlock != null) processBlock(finallyBlock, currentClass);
	}

	private void processCatchClause(CatchClause catchClause, 
			String currentClass){
		
		if(catchClause != null){
			Block catchBlock = catchClause.getBody();
			processBlock(catchBlock, currentClass);
		}
	}

	private void processSynchronizedStatement(
			SynchronizedStatement synchronizedStat, 
			String currentClass){

		if(synchronizedStat != null){
			Block synchronizedBlock = synchronizedStat.getBody();
			Expression syncExpression = synchronizedStat.getExpression();

			processBlock(synchronizedBlock, currentClass);
			processExpression(syncExpression, currentClass);
		}
	}

	private void processAssertStatement(AssertStatement as, 
			String currentClass){
		
		Expression expression = as.getExpression();
		processExpression(expression, currentClass);
		
		Expression message = as.getMessage();		
		processExpression(message, currentClass);		
	}

	private void processExpression(Expression expression, 
			String currentClass){

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
			processMethodBinding(methodBinding, currentClass);
		}
	}

	private void processMethodBinding(IMethodBinding binding, 
			String currentClass){
		
		if (!binding.getDeclaringClass().isAnonymous()){
									
			String providerClass = 
					getProviderClass(binding.getDeclaringClass());
			
			if (providerClass != null) 
				setUse(currentClass, providerClass);
		}
	}

	private String getProviderClass(ITypeBinding declaringClass) {
		String providerClass = null;
		
		String key = declaringClass.getKey();
		if (key.contains(sourceDir)){
			String compilationUnit = StringUtils.substringAfterLast(key, sourceDir);
			providerClass = StringUtils.substringBefore(compilationUnit, "~") + ".java";
		}
		
		return providerClass;
	}

	private void setUse(String currentClass, String providerClass){
		
		if (!currentClass.equals(providerClass) &&
			!classFilter.matches(providerClass)){
		
			System.out.println("Provider class: " + providerClass);
			
			dependencyReport.addDependency(currentClass, providerClass);
		}
	}
	
	public DependencyReport getDependencyReport(){
		return dependencyReport;
	}

}