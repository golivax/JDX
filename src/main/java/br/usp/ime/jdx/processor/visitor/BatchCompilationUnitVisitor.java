package br.usp.ime.jdx.processor.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

import br.usp.ime.jdx.filter.Filter;
import br.usp.ime.jdx.util.filesystem.FilesystemUtils;

public abstract class BatchCompilationUnitVisitor {

	private List<CompilationUnit> compilationUnits = 
			new ArrayList<CompilationUnit>();
	
	private boolean resolveBindings;
	
	public BatchCompilationUnitVisitor(boolean resolveBindings){
		this.resolveBindings = resolveBindings;
	}
	
	protected void processCodebase(final String sourceDir, Filter fileFilter){
				
		try{
			
			// creating the parser
			ASTParser parser = ASTParser.newParser(AST.JLS4);
			
			// should resolve bindings?
			parser.setResolveBindings(resolveBindings); 
			
			// setting the environment			
			parser.setEnvironment(null, new String[]{sourceDir}, null, true);
			
			//cache compilation units
			FileASTRequestor req = new FileASTRequestor() {
				
				@Override
				public void acceptAST(String sourceFilePath, 
						CompilationUnit compilationUnit) {

					compilationUnits.add(compilationUnit);
					
					cacheCompilationUnit(sourceDir, sourceFilePath, 
							compilationUnit);	
				}
			};
			
			String[] paths = FilesystemUtils.getPathsFromFilesRecursively(
							sourceDir, fileFilter);
			
			parser.createASTs(paths, null, new String[0], req, null);
			
			for(CompilationUnit compilationUnit : compilationUnits){
				processCompilationUnit(compilationUnit);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	protected abstract void cacheCompilationUnit(String sourceDir,
			String sourceFilePath, CompilationUnit compilationUnit);

	protected abstract void processCompilationUnit(
			CompilationUnit compilationUnit);
}