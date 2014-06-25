package br.usp.ime.jdx.processor;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.FileASTRequestor;

public class BatchCompilationUnitProcessor {
		
	public void run(List<String> sourceDirs, FileASTRequestor req, 
			String[] paths){
		
		try{
			
			// creating the parser
			ASTParser parser = ASTParser.newParser(AST.JLS4);
			
			// should resolve bindings?
			parser.setResolveBindings(true); 
			
			System.out.println("Sourcepath Entries: " + sourceDirs);
			String[] sourceDirsArray = sourceDirs.toArray(new String[sourceDirs.size()]);
			
			// setting the environment			
			parser.setEnvironment(null, sourceDirsArray, null, true);			
			
			parser.createASTs(paths, null, new String[0], req, null);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	//Might be used in the future...
	/*
	private String getSourcePathEntry(String[] paths) {
		
		String sourcepathEntry = StringUtils.getCommonPrefix(paths);
		
		String slash = "\\";
		if (!sourcepathEntry.contains(slash)){
			slash = "/";
		}
		
		//If the last char is not a (back/forward) slash, then we
		//take the substring until the last slash inclusive
		String lastChar = StringUtils.right(sourcepathEntry, 1);
		if(!lastChar.equals(slash)){
			sourcepathEntry = sourcepathEntry.substring(
					0,sourcepathEntry.lastIndexOf(slash)+1);
		}

		return sourcepathEntry;
	}*/
}