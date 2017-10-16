package br.usp.ime.jdx.entity.system;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public class EclipseCodeFormatter {

	private static String UNIX_EOL = "\n";
	
	public static String format(String sourceCode) throws MalformedTreeException, BadLocationException {

		//add a dummy class as a shell, to meet the requirement of ASTParser
		String header = "class Dummy {";
		String footer = "}";
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(header);
		stringBuilder.append(sourceCode);
		stringBuilder.append(footer);
		String compUnitCode = stringBuilder.toString();
		
		TextEdit te = ToolFactory.createCodeFormatter(null).format(
				CodeFormatter.K_COMPILATION_UNIT, compUnitCode, 0, compUnitCode.length(), 0, UNIX_EOL);
		
		IDocument dc = new Document(compUnitCode);
		te.apply(dc);
		String formattedCode = dc.get();
		formattedCode = StringUtils.substringAfter(formattedCode, header);
		formattedCode = StringUtils.substringBeforeLast(formattedCode, footer);
		if(formattedCode.startsWith(UNIX_EOL)) formattedCode = formattedCode.substring(1);
		if(formattedCode.endsWith(UNIX_EOL)) formattedCode = formattedCode.substring(0, formattedCode.length()-1);
		return formattedCode;
	}
	
}
