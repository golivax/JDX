package br.usp.ime.jdx.processor.extractor;

import java.util.List;

import br.usp.ime.jdx.entity.dependency.DependencyReport;
import br.usp.ime.jdx.filter.Filter;
import br.usp.ime.jdx.processor.BatchCompilationUnitProcessor;

public class DependencyExtractor{

	private Cacher cacher;

	public DependencyExtractor(){
		cacher = new Cacher();
	}
	
	public DependencyReport run(List<String> sourceDirs, String[] paths, 
			Filter classFilter){

		BatchCompilationUnitProcessor batchCompilationUnitProcessor = 
				new BatchCompilationUnitProcessor();

		batchCompilationUnitProcessor.run(sourceDirs, cacher, paths);

		DependencyReport depReport = new DependencyReport();
		
		CallDependencyExtractor callDepExtractor = 
				new CallDependencyExtractor(cacher); 
				
		callDepExtractor.run(depReport, classFilter);

		TypeDependencyExtractor typeDepExtractor = 
				new TypeDependencyExtractor(cacher);
		
		typeDepExtractor.run(depReport, classFilter);
		
		return depReport;
	}


}