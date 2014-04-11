package br.usp.ime.jdx.processor;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.FileASTRequestor;

public class BatchCompilationUnitProcessor {
		
	public void run(FileASTRequestor req, String[] paths){
		
		try{
			
			// creating the parser
			ASTParser parser = ASTParser.newParser(AST.JLS4);
			
			// should resolve bindings?
			parser.setResolveBindings(true); 
			
			String sourcepathEntry = StringUtils.getCommonPrefix(paths);
			System.out.println("SourcepathEntry: " + sourcepathEntry);
			
			// setting the environment			
			parser.setEnvironment(null, new String[]{sourcepathEntry}, null, true);			
			
			parser.createASTs(paths, null, new String[0], req, null);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}