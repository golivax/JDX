package br.usp.ime.jdx.processor;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.FileASTRequestor;

public class BatchCompilationUnitProcessor {
		
	private static final Logger logger = LogManager.getLogger();
	
	public void run(String sourcePathEntry, FileASTRequestor req, 
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
			if(!recoverSourceCode) {
				parser.setIgnoreMethodBodies(true);
			}
			
			logger.info("Source directory for dependency extraction: {}", sourcePathEntry);
			
			String[] sourcePathEntries = {sourcePathEntry};
			
			//Sets the environment			
			parser.setEnvironment(null, sourcePathEntries, null, true);			
			parser.createASTs(paths, null, new String[0], req, null);
			
		}catch(Exception e){
			logger.error("Exception log", e);
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
		for(Object entry : options.entrySet()) {
			System.out.println(entry);
		}
		System.out.println(options);
	}
}