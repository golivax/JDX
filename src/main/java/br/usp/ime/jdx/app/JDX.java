package br.usp.ime.jdx.app;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;

import br.usp.ime.jdx.entity.relationship.dependency.DependencyReport;
import br.usp.ime.jdx.entity.system.SourceCodeUnit;
import br.usp.ime.jdx.filter.JavaAPIMatcher;
import br.usp.ime.jdx.filter.StringMatcher;
import br.usp.ime.jdx.processor.extractor.DependencyExtractor;
import br.usp.ime.jdx.util.filesystem.FilesystemUtils;

public class JDX {
	
	public JDX(){
		
	}
	
	//Defaults:
	//Recursive: true
	//Glob Pattern: *.java
	//Class Filter out: Java Native Classes
	public DependencyReport calculateDepsFrom(String sourceDir, 
			boolean recursive, String globPattern, StringMatcher classFilter, 
			boolean recoverSourceCode) throws IOException{
				
		DependencyExtractor extractor = new DependencyExtractor();
		
		String[] paths = FilesystemUtils.getPathsFromSourceDir(
				sourceDir, globPattern, recursive);
		
		DependencyReport depReport = extractor.run(sourceDir, paths, classFilter, recoverSourceCode);
		
		return depReport;
	}

	public DependencyReport calculateDepsFrom(File file, List<File> allFiles) {
		throw new UnsupportedOperationException(
				"This operation has not been implemented yet");
	}
	
	public DependencyReport calculateDepsFrom(InputStream is) {
		throw new UnsupportedOperationException(
				"This operation has not been implemented yet");
	}
	
	public DependencyReport calculateDepsFrom(List<SourceCodeUnit> codeBase) {
		throw new UnsupportedOperationException(
				"This operation has not been implemented yet");
	}
	
	public DependencyReport calculateDepsFrom(SourceCodeUnit sourceCode) {
		throw new UnsupportedOperationException(
				"This operation has not been implemented yet");
	}
	
}