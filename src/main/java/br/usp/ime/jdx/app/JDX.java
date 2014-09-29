package br.usp.ime.jdx.app;

import java.io.File;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.usp.ime.jdx.entity.SourceCodeUnit;
import br.usp.ime.jdx.entity.relationship.dependency.DependencyReport;
import br.usp.ime.jdx.filter.JavaAPIMatcher;
import br.usp.ime.jdx.filter.SimpleStringMatcher;
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

	//TODO: Change to truly fluent API
	public DependencyReport calculateDepsFrom(String sourceDir, 
			boolean recursive, String globPattern, StringMatcher classFilter, 
			boolean recoverSourceCode) {
		
		DependencyExtractor extractor = new DependencyExtractor();
		
		String[] paths = FilesystemUtils.getPathsFromSourceDir(
				sourceDir, globPattern, recursive);
		
		List<String> sourceDirs = new ArrayList<String>();
		sourceDirs.add(sourceDir);
				
		return extractor.run(sourceDirs, paths, classFilter, recoverSourceCode);
	}
	
	//TODO: Change to truly fluent API
	public DependencyReport calculateDepsFrom(List<String> sourceDirs, 
			boolean recursive, String globPattern, StringMatcher classFilter,
			boolean recoverSourceCode) {

		DependencyExtractor extractor = new DependencyExtractor();

		String[] paths = FilesystemUtils.getPathsFromSourceDirs(
				sourceDirs, globPattern, recursive);
		
		return extractor.run(sourceDirs, paths, classFilter, recoverSourceCode);
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
	
	public static void main(String[] args) {
		
		System.out.println(new Date(System.currentTimeMillis()));
		
		String fileSeparator = FileSystems.getDefault().getSeparator();
		System.out.println(fileSeparator);
		
		String rootDir = "C:/tmp/moenia";
				
		JDX jdx = new JDX();
		
		DependencyReport depReport = jdx.calculateDepsFrom(
				rootDir, true, "*.java", new JavaAPIMatcher(),true);

		System.out.println(depReport.getJavaProject().getMethods());
		
		System.out.println(new Date(System.currentTimeMillis()));
	}
	
}