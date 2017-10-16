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
			logger.error("Eclipse Code Formatter threw an exception while formatting the following code");
			logger.error(rawVersion);
			logger.error("The raw version of the source code will be returned");
			logger.error("Stacktrace as follows:",e);
			
			return rawVersion;
		//This will take care of NullPointerException or any other unexpected exception. The idea is to always return 
		//the raw code in case anything goes bad.
		} catch (Exception e) {
			logger.error("Some weird error occurred while formatting the following code with Eclipse Code Formatter");
			logger.error(rawVersion);
			logger.error("The raw version of the source code will be returned");
			logger.error("Stacktrace as follows:",e);
			
			return rawVersion;
		}
		
		return formattedVersion;
	}
	
	public int[] getCodeLocation() {
		return codeLocation;
	}
	
	public String toString() {
		return getFormattedVersion();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getFormattedVersion() == null) ? 0 : this.getFormattedVersion().hashCode());
		return result;
	}

	@Override
	/**
	 * This is an important design decision. Pieces of source code are compared using their formatted version.
	 * This means that indentation and layout changes are not enough to make methods be deemed different 
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SourceCode other = (SourceCode) obj;
		if (this.getFormattedVersion() == null) {
			if (other.getFormattedVersion() != null)
				return false;
		} else if (!this.getFormattedVersion().equals(other.getFormattedVersion()))
			return false;
		return true;
	}
		
	
}