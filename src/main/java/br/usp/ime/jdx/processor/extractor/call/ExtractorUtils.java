package br.usp.ime.jdx.processor.extractor.call;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class ExtractorUtils {

	public static String getQualifiedTypeName(ITypeBinding typeBinding) {
		
		String qualifiedName = typeBinding.getQualifiedName();
		//Removing generic's type parameter
		String providerTypeName = StringUtils.substringBefore(qualifiedName, "<");
		return providerTypeName;
	}
	
}
