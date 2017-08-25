package br.usp.ime.jdx.processor.extractor;

import br.usp.ime.jdx.entity.relationship.dependency.DependencyReport;
import br.usp.ime.jdx.entity.relationship.dependency.RawDependencyReport;
import br.usp.ime.jdx.entity.system.JavaProject;
import br.usp.ime.jdx.filter.StringMatcher;
import br.usp.ime.jdx.processor.BatchCompilationUnitProcessor;
import br.usp.ime.jdx.processor.parser.CodeParser;
import br.usp.ime.jdx.util.filesystem.FilesystemUtils;

public class DependencyExtractor{
	
	//TODO: Refactor. Idea is that every processor should be added to 
	//CompUnitTraverser, i.e. no independent processors anymore
	public DependencyReport run(String projectDir, String sourceDir, String[] paths, 
			StringMatcher classFilter, boolean recoverSourceCode){
		
		CodeParser cache = buildCache(projectDir, sourceDir, paths, recoverSourceCode);

		RawDependencyReport rawDepReport = new RawDependencyReport(
				new JavaProject(
						projectDir, sourceDir,
						cache.getPackages(),
						cache.getJDXCompilationUnits(), 
						cache.getTypes(),
						cache.getMethods()));
		
		//Running the extractors that rely on the traverser
		CompUnitTraverser compUnitTraverser = 
				buildAndLoadCompUnitTraverser(classFilter, cache, rawDepReport);
		
		compUnitTraverser.run();

		//Running the independent extractors
		ImplicitCallDependencyExtractor implicitCallDepExtractor = 
				new ImplicitCallDependencyExtractor(cache);
		
		implicitCallDepExtractor.run(rawDepReport);
		
		TypeDependencyExtractor typeDepExtractor = 
				new TypeDependencyExtractor(cache);
		
		typeDepExtractor.run(rawDepReport, classFilter);	
		
		return rawDepReport.getDependencyReport();
	}

	//FIXME:Encapsulate in a Factory
	private CompUnitTraverser buildAndLoadCompUnitTraverser(
			StringMatcher classFilter, CodeParser cache, RawDependencyReport depReport){
		
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

	private CodeParser buildCache(String projectDir, String sourceDir, String[] paths,
			boolean recoverSourceCode) {
		
		CodeParser codeParser = new CodeParser(projectDir, recoverSourceCode);

		BatchCompilationUnitProcessor batchCompilationUnitProcessor = 
				new BatchCompilationUnitProcessor();

		String finalDir = FilesystemUtils.getPath(projectDir, sourceDir);
		
		batchCompilationUnitProcessor.run(finalDir, codeParser, paths, recoverSourceCode);		
		return codeParser;
	}

}