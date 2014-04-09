package br.usp.ime.jdx.processor.visitor;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

import br.usp.ime.jdx.filter.Filter;
import br.usp.ime.jdx.util.filesystem.FilesystemUtils;

public abstract class BatchCompilationUnitVisitor {

	private boolean resolveBindings;
	
	public BatchCompilationUnitVisitor(boolean resolveBindings){
		this.resolveBindings = resolveBindings;
	}
	
	protected void processCode(final String sourceDir, Filter fileFilter){
				
		try{
			
			// creating the parser
			ASTParser parser = ASTParser.newParser(AST.JLS4);
			
			// should resolve bindings?
			parser.setResolveBindings(resolveBindings); 
			
			// setting the environment			
			parser.setEnvironment(null, new String[]{sourceDir}, null, true);
								
			// defining the file ast requestor
			FileASTRequestor req = new FileASTRequestor() {
				
				@Override
				public void acceptAST(String sourceFilePath, 
						CompilationUnit compilationUnit) {

					processCompilationUnit(sourceDir, sourceFilePath, 
							compilationUnit);										
				}
			};
			
			String[] paths = FilesystemUtils.getPathsFromFilesRecursively(
							sourceDir, fileFilter);
			
			parser.createASTs(paths, null, new String[0], req, null);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	protected abstract void processCompilationUnit(String sourceDir, 
			String sourceFilePath,	CompilationUnit compilationUnit);
	
}