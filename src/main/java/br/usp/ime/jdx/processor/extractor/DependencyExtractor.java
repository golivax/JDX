package br.usp.ime.jdx.processor.extractor;

import java.util.List;

import br.usp.ime.jdx.entity.dependency.DependencyReport;
import br.usp.ime.jdx.filter.Filter;
import br.usp.ime.jdx.processor.BatchCompilationUnitProcessor;
import br.usp.ime.jdx.processor.extractor.call.CallDependencyExtractor;

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

		CallDependencyExtractor callDepExtractor = 
				new CallDependencyExtractor(cacher);

		DependencyReport depReport = callDepExtractor.run(classFilter);
		
		return depReport;
	}


}