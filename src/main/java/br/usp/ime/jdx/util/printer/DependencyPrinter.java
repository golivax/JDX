package br.usp.ime.jdx.util.printer;

import br.usp.ime.jdx.entity.dependency.DependencyReport;
import br.usp.ime.jdx.entity.dependency.TypeMetaDependency;

public class DependencyPrinter {

	public static String printCSV(DependencyReport dependencyReport){
		StringBuilder builder = new StringBuilder();
		
		for(TypeMetaDependency dependency : 
				dependencyReport.getTypeMetaDependencies()){
		
			builder.append(
					dependency.getClient() + ";" + 
					dependency.getSupplier() + ";" + 
					dependency.getStrength()
			);
		}
		
		return builder.toString();
	}
	
}
