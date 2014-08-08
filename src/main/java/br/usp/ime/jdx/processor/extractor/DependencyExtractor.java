package br.usp.ime.jdx.processor.extractor;

import java.util.List;

import br.usp.ime.jdx.entity.JavaProject;
import br.usp.ime.jdx.entity.dependency.DependencyReport;
import br.usp.ime.jdx.filter.Filter;
import br.usp.ime.jdx.processor.BatchCompilationUnitProcessor;

public class DependencyExtractor{
	
	public DependencyReport run(List<String> sourceDirs, String[] paths, 
			Filter classFilter, boolean recoverSourceCode){
		
		Cacher cacher = new Cacher(recoverSourceCode);

		BatchCompilationUnitProcessor batchCompilationUnitProcessor = 
				new BatchCompilationUnitProcessor();

		batchCompilationUnitProcessor.run(sourceDirs, cacher, paths);

		
		DependencyReport depReport = new DependencyReport(
				new JavaProject(
						cacher.getPackages(),
						cacher.getCompUnits(), 
						cacher.getTypes(),
						cacher.getMethods()));
		
		CallDependencyExtractor callDepExtractor = 
				new CallDependencyExtractor(cacher); 
				
		callDepExtractor.run(depReport, classFilter);

		TypeDependencyExtractor typeDepExtractor = 
				new TypeDependencyExtractor(cacher);
		
		typeDepExtractor.run(depReport, classFilter);		
		
		return depReport;
	}


}