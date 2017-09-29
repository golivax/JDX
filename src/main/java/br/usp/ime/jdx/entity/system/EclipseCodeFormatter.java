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

	private static CodeFormatter cf = ToolFactory.createCodeFormatter(null);
	private static String LINE_TERMINATOR = "\n";
	
	public static String format(String sourceCode) throws MalformedTreeException, BadLocationException {

		//add a dummy class as a shell, to meet the requirement of ASTParser
		String header = "class Dummy {";
		String footer = "}";
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(header);
		stringBuilder.append(sourceCode);
		stringBuilder.append(footer);
		String compUnitCode = stringBuilder.toString();
		
		TextEdit te = cf.format(
				CodeFormatter.K_COMPILATION_UNIT, compUnitCode, 0, compUnitCode.length(), 0, LINE_TERMINATOR);
		
		IDocument dc = new Document(compUnitCode);
		te.apply(dc);
		String formattedCode = dc.get();
		formattedCode = StringUtils.substringAfter(formattedCode, LINE_TERMINATOR);
		formattedCode = StringUtils.substringBeforeLast(formattedCode, LINE_TERMINATOR);
		return formattedCode;
	}
	
}
