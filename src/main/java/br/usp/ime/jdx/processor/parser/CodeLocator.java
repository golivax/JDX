package br.usp.ime.jdx.processor.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class CodeLocator {
	
	private String NEWLINE = System.getProperty("line.separator");
	private Pattern EOL_PATTERN = Pattern.compile("\r\n|\r|\n");
	
	private String compUnitCode;
	private String[] compUnitCodeLines;
	private String compUnitCodeWithoutSpace;
	
	public CodeLocator(CompilationUnit compilationUnit) {
		this.compUnitCode = compilationUnit.toString();		
		this.compUnitCodeWithoutSpace = compUnitCode.replace(" ", "");
		this.compUnitCodeLines = compUnitCode.split("\r\n|\r|\n",-1);
	}

	public int[] locateCode(String searchedCode) {
				
		String searchedCodeWithoutSpace = searchedCode.replace(" ", "");
		//System.out.println("First check: " + compUnitCodeWithoutSpace.contains(searchedCodeWithoutSpace));
		
		String beforeSearchedCode = StringUtils.substringBefore(compUnitCodeWithoutSpace, searchedCodeWithoutSpace);
		int numLinesInBeforeSearchedCode = countLines(beforeSearchedCode);
		int numLinesInSearchCode = countLines(searchedCode);
		
		int beginLine = numLinesInBeforeSearchedCode - 1;
		int endLine = beginLine + numLinesInSearchCode - 2;
		
		StringBuilder sb = new StringBuilder();
		for(int i = beginLine; i <= endLine; i++) {
			sb.append(compUnitCodeLines[i]);
			sb.append(NEWLINE);			
		}
		
		String locatedCode = sb.toString().trim(); 
		//System.out.println("Second check: " + compUnitCode.contains(locatedCode));
		
		int beginIndex = compUnitCode.indexOf(locatedCode);
		int endIndex = beginIndex + locatedCode.length();
		
		//System.out.println("Third check: " + compUnitCode.subSequence(beginIndex, endIndex).equals(locatedCode));
		//System.out.println(compUnitCode.substring(beginIndex, endIndex));
		int[] indexes = {beginIndex,endIndex};
		return indexes;
	}
	
	
	private int countLines(String input) {
		Matcher matcher = EOL_PATTERN.matcher(input);
		int lines = 1;
		while (matcher.find()){
		    lines ++;
		}
		return lines;
	}
}