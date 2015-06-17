package br.usp.ime.jdx.processor.extractor;

import java.util.List;

import br.usp.ime.jdx.entity.relationship.dependency.DependencyReport;
import br.usp.ime.jdx.entity.system.JavaProject;
import br.usp.ime.jdx.filter.StringMatcher;
import br.usp.ime.jdx.processor.BatchCompilationUnitProcessor;

public class DependencyExtractor{
	
	//TODO: Refactor. Idea is that every processor should be added to 
	//CompUnitTraverser, i.e. no indepent processors anymore
	public DependencyReport run(List<String> sourceDirs, String[] paths, 
			StringMatcher classFilter, boolean recoverSourceCode){
		
		Cache cache = buildCache(sourceDirs, paths, recoverSourceCode);

		DependencyReport depReport = new DependencyReport(
				new JavaProject(
						sourceDirs,
						cache.getPackages(),
						cache.getJDXCompilationUnits(), 
						cache.getTypes(),
						cache.getMethods()));
		
		//Running the extractors that rely on the traverser
		CompUnitTraverser compUnitTraverser = 
				buildAndLoadCompUnitTraverser(classFilter, cache, depReport);
		
		compUnitTraverser.run();

		//Running the independent extractors
		ImplicitCallDependencyExtractor implicitCallDepExtractor = 
				new ImplicitCallDependencyExtractor(cache);
		
		implicitCallDepExtractor.run(depReport);
		
		TypeDependencyExtractor typeDepExtractor = 
				new TypeDependencyExtractor(cache);
		
		typeDepExtractor.run(depReport, classFilter);	
		
		return depReport;
	}

	//FIXME:Encapsulate in a Factory
	private CompUnitTraverser buildAndLoadCompUnitTraverser(
			StringMatcher classFilter, Cache cache, DependencyReport depReport){
		
		CompUnitTraverser compUnitTraverser = new CompUnitTraverser(cache);
		
		ExplicitCallDependencyExtractor explicitCallDepExtractor = 
				new ExplicitCallDependencyExtractor(cache, depReport, classFilter);
		
		ReferenceDependencyExtractor refDepExtractor = 
				new ReferenceDependencyExtractor(cache, depReport, classFilter);
		
		AccessDependencyExtractor accessDepExtractor = 
				new AccessDependencyExtractor(cache, depReport, classFilter);
		
		ParameterDependencyExtractor paramDepExtractor = 
				new ParameterDependencyExtractor(cache, depReport, classFilter);
		
		ReturnDependencyExtractor returnDepExtractor = 
				new ReturnDependencyExtractor(cache, depReport, classFilter);
		
		ThrowDependencyExtractor throwDependencyExtractor =
				new ThrowDependencyExtractor(cache, depReport, classFilter);
		
		ImportDependencyExtractor importDepExtractor = 
				new ImportDependencyExtractor(cache, depReport, classFilter);
		
		//Add FieldDeclaration Processor
		compUnitTraverser.addFieldDeclarationProcessor(
				refDepExtractor);
		
		//Add VariableDeclarationStatement Processor
		compUnitTraverser.addVariableDeclarationStatementProcessor(
				refDepExtractor);
		
		//Add Expression Processors
		compUnitTraverser.addExpressionProcessor(
				explicitCallDepExtractor,refDepExtractor,accessDepExtractor);
		
		//Add Constructor Invocation Processors		
		compUnitTraverser.addConstructorInvocationProcessor(
				explicitCallDepExtractor);
		
		//Add Method Declaration Processors
		compUnitTraverser.addMethodDeclarationProcessor(
				paramDepExtractor, returnDepExtractor, throwDependencyExtractor);
		
		//Add Import Declaration Processors
		compUnitTraverser.addImportDeclarationProcessor(
				importDepExtractor);
				
		return compUnitTraverser;
	}

	private Cache buildCache(List<String> sourceDirs, String[] paths,
			boolean recoverSourceCode) {
		Cache cacher = new Cache(recoverSourceCode);

		BatchCompilationUnitProcessor batchCompilationUnitProcessor = 
				new BatchCompilationUnitProcessor();

		batchCompilationUnitProcessor.run(sourceDirs, cacher, paths);
		return cacher;
	}

}