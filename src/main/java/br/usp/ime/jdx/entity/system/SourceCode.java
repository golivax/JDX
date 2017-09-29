package br.usp.ime.jdx.entity.system;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;

public class SourceCode implements Serializable{
	
	private static final long serialVersionUID = 6163232373687603103L;

	private static final Logger logger = LogManager.getLogger();
	
	private int[] codeLocation;
	private String rawVersion;
	private String formattedVersion;

	public SourceCode(int[] codeLocation, CompUnit compUnit) {
	String compUnitRawSourceCode = compUnit.getRawSourceCode();
		this.rawVersion = compUnitRawSourceCode.substring(codeLocation[0], codeLocation[1]);
		this.codeLocation = codeLocation;
	}
	
	public SourceCode(String rawVersion) {
		this.rawVersion = rawVersion;
	}
	
	public String getRawVersion() {
		return this.rawVersion;
	}
	
	public String getFormattedVersion() {
		if(formattedVersion != null) return formattedVersion;
		
		try {
			this.formattedVersion = EclipseCodeFormatter.format(rawVersion);
		} catch (MalformedTreeException | BadLocationException e) {
			logger.error("Error while formatting the following code with the Eclipse Code Formatter");
			logger.error(rawVersion);
			logger.error("Stacktrace as follows:",e);
		}	  
		
		return formattedVersion;
	}
	
	public int[] getCodeLocation() {
		return codeLocation;
	}
	
	public String toString() {
		return rawVersion;
	}
		
}