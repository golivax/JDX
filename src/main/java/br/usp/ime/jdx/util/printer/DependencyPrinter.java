package br.usp.ime.jdx.util.printer;

import br.usp.ime.jdx.entity.dependency.Dependency;
import br.usp.ime.jdx.entity.dependency.DependencyReport;

public class DependencyPrinter {

	public static String printCSV(DependencyReport dependencyReport){
		StringBuilder builder = new StringBuilder();
		for(Dependency dependency : dependencyReport.getTypeDependencies()){
			builder.append(
					dependency.getClient() + ";" + 
					dependency.getSupplier() + ";" + 
					dependency.getStrength()
			);
		}
		
		return builder.toString();
	}
	
}
