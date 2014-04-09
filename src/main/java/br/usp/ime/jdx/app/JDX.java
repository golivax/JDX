package br.usp.ime.jdx.app;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import br.usp.ime.jdx.entity.DependencyReport;
import br.usp.ime.jdx.entity.SourceCodeUnit;
import br.usp.ime.jdx.filter.Filter;
import br.usp.ime.jdx.filter.JavaFileFilter;
import br.usp.ime.jdx.filter.JavaNativeClassFilter;
import br.usp.ime.jdx.processor.extractor.DependencyExtractor;

public class JDX {
	
	public JDX(){
		
	}
	
	public DependencyReport calculateDepsFrom(String rootDir, 
			Filter fileFilter, Filter classFilter) {
		
		DependencyExtractor extractor = new DependencyExtractor();
		extractor.run(rootDir, fileFilter, classFilter);
		return extractor.getDependencyReport();
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
		
		String rootDir = "C:\\Users\\Gustavo\\Dropbox\\Documents\\Academia\\IME-USP\\" +
				"Meus artigos\\Accepted\\Durante doutorado\\CRIWG 2012\\Trabalho\\" +
				"ant-core\\rev486637\\trunk";
		
		//String rootDir = "C:\\Users\\Gustavo\\Desktop\\Group\\" +
		//		"ant-core\\rev486637\\trunk\\src\\main";
		
		//String rootDir = "C:\\tmp\\ant\\src\\main";
				
		
		JDX jdx = new JDX();
		
		DependencyReport depReport = jdx.calculateDepsFrom(
				rootDir, new JavaFileFilter(), new JavaNativeClassFilter());
		
		System.out.println(depReport.getDependencies().size());
		System.out.println(depReport.getDependencies());
		System.out.println(new Date(System.currentTimeMillis()));
	}
		
}