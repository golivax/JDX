package br.usp.ime.jdx.processor;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.FileASTRequestor;

public class BatchCompilationUnitProcessor {
		
	public void run(List<String> sourceDirs, FileASTRequestor req, 
			String[] paths, boolean recoverSourceCode){
		
		try{
			
			//Creates the parser for JLS8 (Java 8)
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			
			//Must resolve bindings
			parser.setResolveBindings(true);
			
			//Set compliance level to Java 1.8
			Map options = JavaCore.getOptions();
			JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
			parser.setCompilerOptions(options);
			
			//If source code is not wanted, then we can ask JDT to skip
			//method bodies (does not impact binding)
			if(!recoverSourceCode) parser.setIgnoreMethodBodies(true);
			
			System.out.println("Sourcepath Entries: " + sourceDirs);
			
			String[] sourceDirsArray = 
					sourceDirs.toArray(new String[sourceDirs.size()]);
			
			//Sets the environment			
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
	
	public static void main(String[] args) {
		Map options = JavaCore.getOptions();
		System.out.println(options);
	}
}