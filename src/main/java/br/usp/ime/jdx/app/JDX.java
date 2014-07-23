﻿package br.usp.ime.jdx.app;

import java.io.File;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.usp.ime.jdx.entity.SourceCodeUnit;
import br.usp.ime.jdx.entity.dependency.DependencyReport;
import br.usp.ime.jdx.filter.Filter;
import br.usp.ime.jdx.filter.JavaNativeClassFilter;
import br.usp.ime.jdx.processor.extractor.DependencyExtractor;
import br.usp.ime.jdx.util.filesystem.FilesystemUtils;

public class JDX {
	
	public JDX(){
		
	}

	//TODO: Change to truly fluent API
	public DependencyReport calculateDepsFrom(String sourceDir, 
			boolean recursive, String globPattern, Filter classFilter) {
		
		DependencyExtractor extractor = new DependencyExtractor();
		
		String[] paths = FilesystemUtils.getPathsFromSourceDir(
				sourceDir, globPattern, recursive);
		
		List<String> sourceDirs = new ArrayList<String>();
		sourceDirs.add(sourceDir);
				
		return extractor.run(sourceDirs, paths, classFilter);
	}
	
	//TODO: Change to truly fluent API
	public DependencyReport calculateDepsFrom(List<String> sourceDirs, 
			boolean recursive, String globPattern, Filter classFilter) {

		DependencyExtractor extractor = new DependencyExtractor();

		String[] paths = FilesystemUtils.getPathsFromSourceDirs(
				sourceDirs, globPattern, recursive);
		
		return extractor.run(sourceDirs,paths, classFilter);
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
		
		String rootDir = "C:/tmp/maven/maven/maven-3/trunk/maven-core/src/"
				+ "main/java/org/apache";
				
		JDX jdx = new JDX();
		
		DependencyReport depReport = jdx.calculateDepsFrom(
				rootDir, true, "*.java", new JavaNativeClassFilter());
		
		System.out.println(depReport.getTypeDependencies().size());
		System.out.println(depReport.getTypeDependencies());
		System.out.println(new Date(System.currentTimeMillis()));
	
	}
	
}