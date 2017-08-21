package br.usp.ime.jdx.processor.extractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.CreationReference;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionMethodReference;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodReference;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodReference;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.TypeMethodReference;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import br.usp.ime.jdx.entity.system.CompUnit;
import br.usp.ime.jdx.entity.system.Method;
import br.usp.ime.jdx.processor.parser.CodeParser;

public class CompUnitTraverser {
	
	private CodeParser cache;
	
	private List<FieldDeclarationProcessor> fdProcessors = new ArrayList<>();
	private List<VariableDeclarationStatementProcessor> vdsProcessors = new ArrayList<>();
	private List<ExpressionProcessor> expProcessors = new ArrayList<>();
	private List<ConstructorInvocationProcessor> ciProcessors = new ArrayList<>();
	private List<MethodDeclarationProcessor> mdProcessors = new ArrayList<>();
	private List<ImportDeclarationProcessor> idProcessors = new ArrayList<>();
	
	private CompUnit clientCompUnit;
	private Method clientMethod;
	
	public CompUnitTraverser(CodeParser cache){
		
		this.cache = cache;
	}
	
	public void addFieldDeclarationProcessor(
			FieldDeclarationProcessor... fdProcessors){
		
		this.fdProcessors = Arrays.asList(fdProcessors);
	}
	
	public void addVariableDeclarationStatementProcessor(
			VariableDeclarationStatementProcessor... vdsProcessors){
		
		this.vdsProcessors = Arrays.asList(vdsProcessors);
	}
	
	public void addExpressionProcessor(
			ExpressionProcessor... expProcessors){
		
		this.expProcessors = Arrays.asList(expProcessors);
	}
	
	public void addConstructorInvocationProcessor(
			ConstructorInvocationProcessor... ciProcessors){
		
		this.ciProcessors = Arrays.asList(ciProcessors);
	}
	
	public void addMethodDeclarationProcessor(
			MethodDeclarationProcessor... mdProcessors){
		
		this.mdProcessors = Arrays.asList(mdProcessors);
	}
	
	public void addImportDeclarationProcessor(
			ImportDeclarationProcessor... idProcessors){
		
		this.idProcessors = Arrays.asList(idProcessors);
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
			
		//Process the import declarations of each compilation unit
		for(CompilationUnit compUnit : cache.getCompilationUnits()){
			this.clientCompUnit = cache.getJDXCompilationUnit(compUnit);
			List<ImportDeclaration> importDeclarations = compUnit.imports();
			for(ImportDeclaration importDeclaration : importDeclarations){
				processImportDeclarations(importDeclaration);
			}
		}
		
		//Now we process the fields and methods of each type
		for(TypeDeclaration typeDeclaration : cache.getTypeDeclarations()){
			processFieldsAndMethods(typeDeclaration);
		}
	}
	
	private void processImportDeclarations(ImportDeclaration importDeclaration) {
		delegateImportDeclarationProcessing(importDeclaration);
	}
	
	private void delegateImportDeclarationProcessing(
			ImportDeclaration importDeclaration) {
		
		for(ImportDeclarationProcessor idProcessor : idProcessors){
	
			idProcessor.processImportDeclaration(
					importDeclaration, clientCompUnit);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void processFieldsAndMethods(TypeDeclaration typeDeclaration){

		//Processing fields
		for (FieldDeclaration fieldDeclaration : typeDeclaration.getFields()){

			this.clientMethod = 
					cache.getType(typeDeclaration).getAttribMethod();
			
			processFieldDeclaration(fieldDeclaration);
		}
		
		//Processing method declarations
		for (MethodDeclaration methodDeclaration : typeDeclaration.getMethods()){
			
			this.clientMethod = cache.getMethod(methodDeclaration);
		
			processMethodSignature(methodDeclaration);
			processMethodBody(methodDeclaration);
		}
	}

	@SuppressWarnings("unchecked")
	private void processFieldDeclaration(FieldDeclaration fieldDeclaration) {
		
		delegateFieldDeclarationProcessing(fieldDeclaration);
		
		List<VariableDeclarationFragment> fragments = 
				fieldDeclaration.fragments();

		for(VariableDeclarationFragment fragment : fragments){
			processVariableDeclarationFragment(fragment);
		}
	}
	
	private void processMethodSignature(MethodDeclaration methodDeclaration) {
		delegateMethodDeclarationProcessing(methodDeclaration);
	}

	private void delegateMethodDeclarationProcessing(
			MethodDeclaration methodDeclaration) {
		
		for(MethodDeclarationProcessor mdProcessor : mdProcessors){
			
			mdProcessor.processMethodDeclaration(
					methodDeclaration, clientMethod);
		}
	}

	private void processMethodBody(MethodDeclaration methodDeclaration){
		Block codeBlock = methodDeclaration.getBody();
		if (codeBlock != null) processBlock(codeBlock);
	}

	@SuppressWarnings("unchecked")
	private void processBlock(Block codeBlock){
		List<Statement> statements = codeBlock.statements();
		for(Statement currentStatement : statements){
			processStatement(currentStatement);
		}
	}

	private void processStatement(Statement currentStatement){

		//TODO: Order alphabetically
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
	
			//TODO: (Likely) implement this
			case ASTNode.SUPER_CONSTRUCTOR_INVOCATION: break;	
	
			//TODO: Implement this
			case ASTNode.THROW_STATEMENT: break;
	
			// For now, do nothing for the following statements/cases
			case ASTNode.TYPE_DECLARATION_STATEMENT: break; // Local classes
			
			case ASTNode.EMPTY_STATEMENT: break;
			
			case ASTNode.CONTINUE_STATEMENT: break;
	
			case ASTNode.BREAK_STATEMENT: break;
			
			default : System.err.println("\t" + currentStatement.getClass());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void processConstructorInvocation(
			ConstructorInvocation constructorInvocation){ //this()
		
		List<Expression> expressions = constructorInvocation.arguments();
		
		for(Expression expression : expressions){
			delegateExpressionProcessing(expression);
		}
		
		delegateConstructorInvocationProcessing(constructorInvocation);		
	}

	private void processLabeledStatement(LabeledStatement ls){
		
		Statement body = ls.getBody();
		processStatement(body);
	}

	private void processEnhancedForStatement(EnhancedForStatement efs){

		Statement body = efs.getBody();
		Expression expression = efs.getExpression();

		processStatement(body);
		delegateExpressionProcessing(expression);

	}

	private void processDoStatement(DoStatement doStatement){

		Statement statement = doStatement.getBody();
		Expression doExpression = doStatement.getExpression();

		processStatement(statement);
		delegateExpressionProcessing(doExpression);

	}

	private void processWhileStatement(WhileStatement whileStatement){

		Statement whileBody = whileStatement.getBody();
		Expression whileExpression = whileStatement.getExpression();

		processStatement(whileBody);
		delegateExpressionProcessing(whileExpression);
	}

	@SuppressWarnings("unchecked")
	private void processSwitchStatement(SwitchStatement switchStatement){
		
		Expression switchExpression = switchStatement.getExpression();
		delegateExpressionProcessing(switchExpression);

		List<Statement> switchStatements = switchStatement.statements();
		for(Statement statement : switchStatements){
			processStatement(statement);
		}
	}

	private void processSwitchCase(SwitchCase switchCase){
		Expression switchCaseExpression = switchCase.getExpression();
		delegateExpressionProcessing(switchCaseExpression);
	}

	@SuppressWarnings("unchecked")
	private void processForStatement(ForStatement forStatement){
		
		Statement forBody = forStatement.getBody();
		Expression forExpression = forStatement.getExpression();
		List<Expression> initializerExpressions = forStatement.initializers();
		List<Expression> updaterExpressions = forStatement.updaters();

		processStatement(forBody);
		
		delegateExpressionProcessing(forExpression);
		
		for(Expression initializerExpression : initializerExpressions){
			delegateExpressionProcessing(initializerExpression);
		}

		for(Expression updaterExpression : updaterExpressions){
			delegateExpressionProcessing(updaterExpression);
		}
	}

	private void processIfStatement(IfStatement ifStatement){

		Expression ifExpression = ifStatement.getExpression();
		delegateExpressionProcessing(ifExpression);

		Statement thenStatement = ifStatement.getThenStatement();
		if (thenStatement != null) processStatement(thenStatement);
		
		Statement elseStatement = ifStatement.getElseStatement();
		if (elseStatement != null) processStatement(elseStatement);

	}

	private void processExpressionStatement(
			ExpressionStatement	expressionStatement){
		
		Expression expression = expressionStatement.getExpression();
		delegateExpressionProcessing(expression);
	}

	private void processReturnStatement(ReturnStatement returnStatement){

		if(returnStatement != null){
			Expression expression = returnStatement.getExpression();
			if (expression != null){
				delegateExpressionProcessing(expression);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void processVariableDeclarationStatement(
			VariableDeclarationStatement variableDeclarationSt){
	
		delegateVariableDeclarationStatementProcessing(variableDeclarationSt);
		
		List<VariableDeclarationFragment> fragments = 
				variableDeclarationSt.fragments();

		for(VariableDeclarationFragment fragment : fragments){
			processVariableDeclarationFragment(fragment);
		}
	}

	private void processVariableDeclarationFragment(
			VariableDeclarationFragment fragment){

		Expression initializer = fragment.getInitializer();
		if(initializer != null) delegateExpressionProcessing(initializer);
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
			delegateExpressionProcessing(syncExpression);
		}
	}

	private void processAssertStatement(AssertStatement as){
		
		Expression expression = as.getExpression();
		delegateExpressionProcessing(expression);
		
		Expression message = as.getMessage();		
		delegateExpressionProcessing(message);		
	}

	private void delegateExpressionProcessing(Expression expression){
		
		List<Expression> expressions = extractInnerExpressions(expression);
		
		//Each expression processor processes each expression
		for(ExpressionProcessor expProcessor : expProcessors){
			for(Expression _expression : expressions){
				expProcessor.processExpression(_expression,clientMethod);
			}
		}
	}	
	
	private List<Expression> extractInnerExpressions(Expression expression) {
		List<Expression> expressions = new ArrayList<>();
		
		if(expression instanceof Annotation){
			//TODO: Add support to annotations (shouldn't be too difficult)
		}
		else if(expression instanceof ArrayAccess){
			ArrayAccess arrayAccess = (ArrayAccess)expression;
			
			Expression array = arrayAccess.getArray();
			if(array != null) {
				expressions.addAll(extractInnerExpressions(array));
			}
			
			Expression index = arrayAccess.getIndex();
			if(index != null) {
				expressions.addAll(extractInnerExpressions(index));
			}
		}
		//e.g. new int[16]; new B[10]; new B[C.VALUE]
		else if(expression instanceof ArrayCreation){
			ArrayCreation arrayCreation = (ArrayCreation)expression;
			expressions.add(arrayCreation);
			
			List<Expression> dimensions = arrayCreation.dimensions();
			for(Expression dimension : dimensions) {
				expressions.addAll(extractInnerExpressions(dimension));
			}
			
			ArrayInitializer arrayInitializer = arrayCreation.getInitializer();
			if(arrayInitializer != null) {
				expressions.addAll(extractInnerExpressions(arrayInitializer));
			}
		}
		else if(expression instanceof ArrayInitializer){
			ArrayInitializer arrayInitializer = (ArrayInitializer)expression;
			List<Expression> expressionsInsideInitialzer = arrayInitializer.expressions();
			if(expressionsInsideInitialzer != null) {
				for(Expression expressionInsideInitializer : expressionsInsideInitialzer) {
					expressions.addAll(extractInnerExpressions(expressionInsideInitializer));
				}
			}
			
		}
		else if(expression instanceof Assignment){
			Assignment assignment = (Assignment)expression;
			
			Expression lhs = assignment.getLeftHandSide();
			if(lhs != null) {
				expressions.addAll(extractInnerExpressions(lhs));
			}
			
			Expression rhs = assignment.getRightHandSide();
			if(rhs != null) {
				expressions.addAll(extractInnerExpressions(rhs));
			}
		}
		else if(expression instanceof BooleanLiteral){
			//Primary expression. No need to do anything
		}
		else if(expression instanceof CastExpression){
			
			CastExpression castExpression = (CastExpression)expression;
			
			//Adding the castExpression as a whole
			expressions.add(castExpression);
			
			//Adding the expression of this cast expression 
			expressions.addAll(extractInnerExpressions(castExpression.getExpression()));		
		}
		else if(expression instanceof CharacterLiteral){
			//Primary expression. No need to do anything
		}
		else if(expression instanceof ClassInstanceCreation){
			ClassInstanceCreation classInstanceCreation = (ClassInstanceCreation) expression;
			
			//The arguments (parameters)
			List<Expression> arguments = classInstanceCreation.arguments();
			if(arguments != null) {
				for(Expression argument : arguments) {
					expressions.addAll(extractInnerExpressions(argument));
				}
			}
			
			//The expression preceding the new()
			Expression classInstanceCreationExpression = classInstanceCreation.getExpression();
			if(classInstanceCreationExpression != null) {
				expressions.addAll(extractInnerExpressions(classInstanceCreationExpression));	
			}
			
			//The expression itself
			expressions.add(classInstanceCreation);
			
		}
		else if(expression instanceof ConditionalExpression){
			ConditionalExpression conditionalExpression = (ConditionalExpression)expression;
			
			Expression condExpression = conditionalExpression.getExpression();
			expressions.addAll(extractInnerExpressions(condExpression));
			
			Expression thenExpression = conditionalExpression.getThenExpression();
			expressions.addAll(extractInnerExpressions(thenExpression));
			
			Expression elseExpression = conditionalExpression.getElseExpression();
			expressions.addAll(extractInnerExpressions(elseExpression));
		}
		else if(expression instanceof CreationReference){
			//TODO: Added in Java 8
		}
		else if(expression instanceof ExpressionMethodReference){
			//TODO: Added in Java 8
		}
		else if(expression instanceof FieldAccess){
			FieldAccess fieldAccess = (FieldAccess)expression;
			
			Expression fieldAccessExpression = fieldAccess.getExpression();
			expressions.addAll(extractInnerExpressions(fieldAccessExpression));
			
			//Maybe it is not necessary to process getName()
			Expression name = fieldAccess.getName();
			expressions.add(name);
		}
		else if(expression instanceof InfixExpression){
			InfixExpression infixExpression = (InfixExpression)expression;
			
			Expression leftOperand = infixExpression.getLeftOperand();
			if(leftOperand != null) {
				expressions.addAll(extractInnerExpressions(leftOperand));	
			}
			
			Expression rightOperand = infixExpression.getRightOperand();
			if(rightOperand != null) {
				expressions.addAll(extractInnerExpressions(rightOperand));
			}
			
			List<Expression> extendedOperands = infixExpression.extendedOperands();
			if(extendedOperands != null) {
				for(Expression extendedOperand : extendedOperands) {
					expressions.addAll(extractInnerExpressions(extendedOperand));
				}
			}
		}
		else if(expression instanceof InstanceofExpression){
			InstanceofExpression instanceofExpression = (InstanceofExpression)expression;
			
			Expression leftOperand = instanceofExpression.getLeftOperand();
			if(leftOperand != null) {
				expressions.addAll(extractInnerExpressions(leftOperand));	
			}
			
			//The right operand is a type, so we just add the whole expression to the list
			expressions.add(expression);			
		}
		else if(expression instanceof LambdaExpression){
			//TODO: Added in Java 8
		}
		else if(expression instanceof MethodInvocation){
			
			MethodInvocation method = (MethodInvocation)expression;
			
			//Process the parameters
			List<Expression> parameters = method.arguments(); 
			if(parameters != null){
				for(Expression parameter : parameters){
					expressions.addAll(extractInnerExpressions(parameter));
				}
			}
			
			//TODO: Deal with these type arguments. I think the correct way is to have 
			//ReferenceDepExtractor deal with method invocation expressions and extract these types
			//List<Type> typeArguments = method.typeArguments();
		
			//Process the method invocation itself
			expressions.add(method);
		}
		else if(expression instanceof MethodReference){
			//TODO: Added in Java 8
		}
		//e.g. C.SOMENUMBER (accessing the static field SOMENUMBER of class C 
		else if(expression instanceof Name){
			expressions.add(expression);			
		}
		else if(expression instanceof NullLiteral){
			//Primary expression. No need to do anything
		}
		else if(expression instanceof NumberLiteral){
			//Primary expression. No need to do anything
		}
		else if(expression instanceof ParenthesizedExpression){
			ParenthesizedExpression parenthesizedExpression = (ParenthesizedExpression)expression;
			Expression expressionInParenthesis = parenthesizedExpression.getExpression();
			expressions.addAll(extractInnerExpressions(expressionInParenthesis));
		}
		else if(expression instanceof PostfixExpression){
			PostfixExpression postfixExpression = (PostfixExpression)expression;
			Expression operand = postfixExpression.getOperand();
			expressions.addAll(extractInnerExpressions(operand));
		}
		else if(expression instanceof PrefixExpression){
			PrefixExpression prefixExpression = (PrefixExpression)expression;
			Expression operand = prefixExpression.getOperand();
			expressions.addAll(extractInnerExpressions(operand));
		}
		else if(expression instanceof StringLiteral){
			//Primary expression. No need to do anything
		}
		//[ ClassName . ] super . Identifier
		else if(expression instanceof SuperFieldAccess){
			SuperFieldAccess superFieldAccess = (SuperFieldAccess)expression;
			
			Name qualifier = superFieldAccess.getQualifier();
			if(qualifier != null) {
				expressions.add(qualifier);	
			}
			
			//Maybe it is not necessary to process getName()
			Expression name = superFieldAccess.getName();
			expressions.add(name);
			
			//Adding the expression as a whole to be dealt with by ReferenceDepExtractor
			expressions.add(superFieldAccess);
		}
		else if(expression instanceof SuperMethodInvocation){
			SuperMethodInvocation superMethodInvocation = (SuperMethodInvocation)expression;
			
			//Process the parameters
			List<Expression> parameters = superMethodInvocation.arguments(); 
			if(parameters != null){
				for(Expression parameter : parameters){
					expressions.addAll(extractInnerExpressions(parameter));
				}
			}
			
			//TODO: Deal with these type arguments. I think the correct way is to have 
			//ReferenceDepExtractor deal with method invocation expressions and extract these types
			//List<Type> typeArguments = superMethodInvocation.typeArguments();
			
			Expression qualifier = superMethodInvocation.getQualifier();
			if(qualifier != null) {
				expressions.add(qualifier);
			}
		
			//Process the method invocation itself
			expressions.add(superMethodInvocation);
		}
		else if(expression instanceof SuperMethodReference){
			//TODO: Added in Java 8
		}
		else if(expression instanceof ThisExpression){
			ThisExpression thisExpression = (ThisExpression)expression;
			Expression qualifier = thisExpression.getQualifier();
			if(qualifier != null) {
				expressions.add(qualifier);
			}
		}
		else if(expression instanceof TypeLiteral){
			expressions.add(expression);
		}
		else if(expression instanceof TypeMethodReference){
			//TODO: Added in Java 8
		}
		else if(expression instanceof VariableDeclarationExpression){
			VariableDeclarationExpression varDeclarationExpression = (VariableDeclarationExpression)expression;
			
			//TODO: Deal with this getType(). I think the correct way is to have 
			//ReferenceDepExtractor deal with Variable Declaration Expressions and process this type
			//varDeclarationExpression.getType()
			
			List<VariableDeclarationFragment> fragments = varDeclarationExpression.fragments();
			if(fragments != null) {
				for(VariableDeclarationFragment fragment : fragments){
					processVariableDeclarationFragment(fragment);
				}
			}
		}
	
		return expressions;
	}

	private void delegateConstructorInvocationProcessing(
			ConstructorInvocation constructorInvocation) {
		
		for(ConstructorInvocationProcessor ciProcessor : ciProcessors){
			ciProcessor.processConstructorInvocation(
					constructorInvocation,clientMethod);
		}
		
	}
	
	private void delegateVariableDeclarationStatementProcessing(
			VariableDeclarationStatement variableDeclarationSt) {
		
		for(VariableDeclarationStatementProcessor vdsProcessor : vdsProcessors){
			vdsProcessor.processVariableDeclarationStatement(
					variableDeclarationSt, clientMethod);
		}
		
	}
	

	private void delegateFieldDeclarationProcessing(
			FieldDeclaration fieldDeclaration) {

		for(FieldDeclarationProcessor fdProcessor : fdProcessors){
			fdProcessor.processFieldDeclaration(
					fieldDeclaration, clientMethod);
		}
		
	}	
}