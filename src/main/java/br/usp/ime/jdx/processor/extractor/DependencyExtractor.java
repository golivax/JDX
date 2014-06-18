package br.usp.ime.jdx.processor.extractor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.FileASTRequestor;
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

import br.usp.ime.jdx.entity.CompUnit;
import br.usp.ime.jdx.entity.DependencyReport;
import br.usp.ime.jdx.entity.Type;
import br.usp.ime.jdx.filter.Filter;
import br.usp.ime.jdx.processor.BatchCompilationUnitProcessor;


public class DependencyExtractor extends FileASTRequestor{

	private List<CompilationUnit> compilationUnits = 
			new ArrayList<CompilationUnit>();
	
	private Filter classFilter;	
	private DependencyReport dependencyReport;
	
	private Map<String,Type> typeCache = 
			new HashMap<String, Type>();
		
	private String sourceDir;
	
	private Type clientType;
	
	
	@Override
	public void acceptAST(String sourceFilePath, 
			CompilationUnit compilationUnit) {

		cacheCompilationUnit(sourceDir, sourceFilePath, compilationUnit);	
	}	
	
	private void cacheCompilationUnit(String sourceDir,
			String sourceFilePath, CompilationUnit compilationUnit) {
		
		this.compilationUnits.add(compilationUnit);
				
		Stack<TypeDeclaration> typeStack = new Stack<TypeDeclaration>();
		typeStack.addAll(compilationUnit.types());
		
		while(!typeStack.isEmpty()){
			TypeDeclaration typeDeclaration = typeStack.pop();				
			
			String typeName = getTypeName(typeDeclaration);
			Type type = new Type(typeName,new CompUnit(sourceFilePath));
			typeCache.put(typeName, type);
			
			
			for(TypeDeclaration subType : getSubTypes(typeDeclaration)){
				typeStack.push(subType);
			}
		}
	}
	

	private List<TypeDeclaration> getSubTypes(TypeDeclaration typeDeclaration) {
		List<TypeDeclaration> subTypes = new ArrayList<TypeDeclaration>();
		
		try{
			//Recovers static nested classes and "regular" inner classes
			Collections.addAll(subTypes, typeDeclaration.getTypes());
			
			//Recovers local classes
			for(MethodDeclaration methodDeclaration : typeDeclaration.getMethods()){
				Block block = methodDeclaration.getBody();
				if (block != null) {
					List<Statement> statements = block.statements();
					for(Statement statement : statements){
						if (statement.getNodeType() == ASTNode.TYPE_DECLARATION_STATEMENT){
							TypeDeclarationStatement tds = (TypeDeclarationStatement)statement;
							if (tds.getDeclaration() instanceof TypeDeclaration){
								TypeDeclaration t = (TypeDeclaration)tds.getDeclaration();
								
								subTypes.add((TypeDeclaration)tds.getDeclaration());
							}
						}
					}
				}									
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return subTypes;
	}
	
	public DependencyReport run(String[] paths, Filter classFilter){
		this.classFilter = classFilter;
		
		//Creates a new dependency report and extracts the dependencies		
		dependencyReport = new DependencyReport();
		
		//This is used to cache compilation units (e.g., so that
		//we can retrieve the compilation units from provider types
		//during dependency extraction)
		BatchCompilationUnitProcessor batchCuProcessor = 
				new BatchCompilationUnitProcessor();
		
		batchCuProcessor.run(this, paths);
		
		System.out.println("Number of types detected: " + typeCache.size());	
		
		for(CompilationUnit compilationUnit : compilationUnits){
			processCompilationUnit(compilationUnit);
		}		
		
		return dependencyReport;
	}

	protected void processCompilationUnit(CompilationUnit compilationUnit) {
		
		Stack<TypeDeclaration> typeStack = new Stack<TypeDeclaration>();
		typeStack.addAll(compilationUnit.types());
		
		while(!typeStack.isEmpty()){					
			TypeDeclaration typeDeclaration = typeStack.pop();
			
			this.clientType = getType(getTypeName(typeDeclaration));
			
			//handling method declarations within this type
			processFieldsAndMethods(typeDeclaration);
			
			for(TypeDeclaration subTypeDeclaration : getSubTypes(typeDeclaration)){
				typeStack.push(subTypeDeclaration);
			}
		}
	}
	
	private void processFieldsAndMethods(TypeDeclaration typeDeclaration){

		for (FieldDeclaration fieldDeclaration : typeDeclaration.getFields()){
			// processing fields
			List<VariableDeclarationFragment> fragments = fieldDeclaration.fragments();

			for(VariableDeclarationFragment fragment : fragments){
				processVariableDeclarationFragment(fragment);
			}
		}
		
		for (MethodDeclaration methodDeclaration : typeDeclaration.getMethods()){
			// getting the blocks inside the current method
			Block codeBlock = methodDeclaration.getBody();
			if (codeBlock != null) processBlock(codeBlock);			
		}
	}

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
	
			case ASTNode.TYPE_DECLARATION_STATEMENT:
				processTypeDeclarationStatement(
						(TypeDeclarationStatement) currentStatement);
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
	
	private void processTypeDeclarationStatement(TypeDeclarationStatement tds){
		ITypeBinding binding = tds.resolveBinding();
		processTypeBinding(binding);
	}

	private void processTypeBinding(ITypeBinding binding){
		if(binding != null){		
				
			String providerTypeName = 
					getProviderTypeName(binding.getDeclaringClass());
				
			setUse(providerTypeName);
		}
	}

	@SuppressWarnings("unchecked")
	private void processConstructorInvocation(
			ConstructorInvocation constructorInvocation){
		
		List<Expression> expressions = constructorInvocation.arguments();
		
		for(Expression expression : expressions){
			processExpression(expression);
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
		processExpression(expression);

	}

	private void processDoStatement(DoStatement doStatement){

		Statement statement = doStatement.getBody();
		Expression doExpression = doStatement.getExpression();

		processStatement(statement);
		processExpression(doExpression);

	}

	private void processWhileStatement(WhileStatement whileStatement){

		Statement whileBody = whileStatement.getBody();
		Expression whileExpression = whileStatement.getExpression();

		processStatement(whileBody);
		processExpression(whileExpression);
	}

	@SuppressWarnings("unchecked")
	private void processSwitchStatement(SwitchStatement switchStatement){
		
		Expression switchExpression = switchStatement.getExpression();
		processExpression(switchExpression);

		List<Statement> switchStatements = switchStatement.statements();
		for(Statement statement : switchStatements){
			processStatement(statement);
		}
	}

	private void processSwitchCase(SwitchCase switchCase){
		Expression switchCaseExpression = switchCase.getExpression();
		processExpression(switchCaseExpression);
	}

	@SuppressWarnings("unchecked")
	private void processForStatement(ForStatement forStatement){
		
		Statement forBody = forStatement.getBody();
		Expression forExpression = forStatement.getExpression();
		List<Expression> initializerExpressions = forStatement.initializers();
		List<Expression> updaterExpressions = forStatement.updaters();

		processStatement(forBody);
		processExpression(forExpression);

		for(Expression initializerExpression : initializerExpressions){
			processExpression(initializerExpression);
		}

		for(Expression updaterExpression : updaterExpressions){
			processExpression(updaterExpression);
		}
	}

	private void processIfStatement(IfStatement ifStatement){

		Expression ifExpression = ifStatement.getExpression();
		processExpression(ifExpression);

		Statement thenStatement = ifStatement.getThenStatement();
		if (thenStatement != null) processStatement(thenStatement);
		
		Statement elseStatement = ifStatement.getElseStatement();
		if (elseStatement != null) processStatement(elseStatement);

	}

	private void processExpressionStatement(
			ExpressionStatement	expressionStatement){

		Expression expression = expressionStatement.getExpression();
		processExpression(expression);
			
	}

	private void processReturnStatement(ReturnStatement returnStatement){

		if(returnStatement != null){
			Expression expression = returnStatement.getExpression();
			if (expression != null){
				processExpression(expression);
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
		processExpression(initializer);
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
			processExpression(syncExpression);
		}
	}

	private void processAssertStatement(AssertStatement as){
		
		Expression expression = as.getExpression();
		processExpression(expression);
		
		Expression message = as.getMessage();		
		processExpression(message);		
	}

	private void processExpression(Expression expression){

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
		}else if(expression instanceof Assignment){
			
			Assignment assignment = (Assignment)expression;
			processExpression(assignment.getRightHandSide());

			
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
			processMethodBinding(methodBinding);
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
				
				String providerTypeName = 
						getProviderTypeName(binding.getDeclaringClass());
				
				setUse(providerTypeName);
			}
		}
	}

	private String getProviderTypeName(ITypeBinding typeBinding) {
	
		String qualifiedName = typeBinding.getQualifiedName();
		//Removing generic's type parameter
		String providerTypeName = StringUtils.substringBefore(qualifiedName, "<");
		return providerTypeName;
	}

	private void setUse(String providerTypeName){
				
		if (!classFilter.matches(providerTypeName)){			
			
			Type providerType = getType(providerTypeName);
			if(providerType == null){
				System.out.println("WARNING: Could not find binding for " + 
					providerTypeName + " in class " + 
					clientType.getCompUnit().getName());
			}
			else{
				dependencyReport.addDependency(clientType, providerType);	
			}
		}
	}
	
	private Type getType(String typeName){
		return typeCache.get(typeName);
	}

	private String getTypeName(TypeDeclaration typeDeclaration) {
		
		String typeName = typeDeclaration.getName().toString();
		
		ASTNode node = typeDeclaration.getParent();
		while(node != null){
			if(node instanceof TypeDeclaration){
				TypeDeclaration superType = (TypeDeclaration)node;
				typeName = superType.getName().toString() + "." + typeName;
			}
			if(node instanceof CompilationUnit){
				CompilationUnit compilationUnit = (CompilationUnit)node;
				typeName = compilationUnit.getPackage().getName() + 
						"." + typeName;
			}
			node = node.getParent();
		}
		
		return typeName;
	}	
}