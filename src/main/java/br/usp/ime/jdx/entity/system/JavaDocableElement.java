package br.usp.ime.jdx.entity.system;

public abstract class JavaDocableElement {

	public SourceCode getSourceCodeWithoutJavaDoc() {
		
		//If there is no javaDoc, we simply return the sourceCode
		if(this.getJavaDoc() == null) return this.getSourceCode();
		
		//If the source code was provided with a location, we return a source code without javadoc that also has a
		//location in the parent compilation unit
		if(this.getSourceCode().getCodeLocation() != null) {
			//end of javadoc + 1
			int start = this.getJavaDoc().getCodeLocation()[1] + 1;
			
			//end of source code		
			int end = this.getSourceCode().getCodeLocation()[1];
			
			int[] codeLocation = new int[] {start,end};		
			SourceCode sourceCodeWithoutJavaDoc = new SourceCode(codeLocation, this.getParentCompUnit());
			return sourceCodeWithoutJavaDoc;
		}
		//If the source was NOT provided with a location, we simply remove the javadoc from the beginning of the 
		//source code and return it.
		else {
						
			int lengthJavaDoc = this.getJavaDoc().getRawVersion().length();
						
			SourceCode rawSourceWithoutJavaDoc = new SourceCode(
					this.getSourceCode().getRawVersion().substring(lengthJavaDoc + 1));
			
			return rawSourceWithoutJavaDoc;
		}		
	}
	
	protected abstract SourceCode getJavaDoc();
	protected abstract SourceCode getSourceCode();
	protected abstract CompUnit getParentCompUnit();

}