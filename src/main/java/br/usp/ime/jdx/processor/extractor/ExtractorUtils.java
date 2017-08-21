package br.usp.ime.jdx.processor.extractor;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class ExtractorUtils {

	public static String getQualifiedTypeName(ITypeBinding typeBinding) {
		String qualifiedName = typeBinding.getQualifiedName();
		
		String providerTypeName = qualifiedName;
		
		//Removing generic's type parameter
		providerTypeName = StringUtils.substringBefore(providerTypeName, "<");
		
		//Removing [] array notation
		providerTypeName = StringUtils.substringBefore(providerTypeName, "[]");
		
		//Adding "(default.package)" if type is in default package
		if(typeBinding.getPackage() != null && typeBinding.getPackage().isUnnamed()){
			providerTypeName = "(default package)." + providerTypeName;
		}
		
		return providerTypeName;
	}
	
}
