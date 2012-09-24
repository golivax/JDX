package br.usp.ime.jdx.processor.visitor;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public abstract class CompilationUnitVisitor {

	private boolean resolveBindings;
		
	public CompilationUnitVisitor(boolean resolveBindings){
		this.resolveBindings = resolveBindings;
	}
	
	public void processCode(String code){
	
		String[] sourcepathEntries = {"C:\\cygwin\\home\\Gustavo"};
		
		try{
			
			// creating the parser
			ASTParser parser = ASTParser.newParser(AST.JLS3);

			/**Binding information is obtained from the Java model. 
			 * This means that the compilation unit must be located relative to 
			 * the Java model. This happens automatically when the source code 
			 * comes from either setSource(ICompilationUnit) or 
			 * setSource(IClassFile). When source is supplied by 
			 * setSource(char[]), the location must be established explicitly by 
			 * setting an environment using setProject(IJavaProject) or 
			 * setEnvironment(String[], String[], String[], boolean) and a unit 
			 * name setUnitName(String). Note that the compiler options that 
			 * affect doc comment checking may also affect whether any bindings 
			 * are resolved for nodes within doc comments. 
			 */

			// get the bindings
			parser.setResolveBindings(resolveBindings); 
												
			//setting the environment
			parser.setEnvironment(null, sourcepathEntries, null, false);
							
			parser.setSource(code.toCharArray());			
			parser.setUnitName("/teste/C.java");
							
			// creating the compilation unit
			CompilationUnit compilationUnit = 
					(CompilationUnit) parser.createAST(null);
			
			processCompilationUnit(compilationUnit);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected abstract void processCompilationUnit(CompilationUnit 
			compilationUnit);
	
}