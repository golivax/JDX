package br.usp.ime.jdx.processor.extractor;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;

import br.usp.ime.jdx.entity.relationship.dependency.DependencyReport;
import br.usp.ime.jdx.entity.system.CompUnit;
import br.usp.ime.jdx.entity.system.Method;
import br.usp.ime.jdx.entity.system.Package;
import br.usp.ime.jdx.entity.system.Type;
import br.usp.ime.jdx.filter.StringMatcher;

public class ImportDependencyExtractor implements ImportDeclarationProcessor {

	private CompUnit clientCompUnit;
	
	private Cache cache;
	private DependencyReport depReport;
	private StringMatcher classFilter;
		
	public ImportDependencyExtractor(Cache cache, DependencyReport depReport, 
			StringMatcher classFilter){
		
		this.cache = cache;
		this.depReport = depReport;
		this.classFilter = classFilter;
	}
	
	@Override
	public void processImportDeclaration(ImportDeclaration importDeclaration,
			CompUnit clientCompUnit) {
		
		this.clientCompUnit = clientCompUnit;
		IBinding iBinding = importDeclaration.resolveBinding();
		
		//Sometimes the import class is not in the JDT Core environment, so
		//we have to check for null 
		if(iBinding != null){
			switch(iBinding.getKind()){
				case IBinding.PACKAGE:
					IPackageBinding packageBinding = (IPackageBinding) iBinding;
					processPackageBinding(packageBinding);
					break;
				case IBinding.TYPE:
					ITypeBinding typeBinding = (ITypeBinding) iBinding;
					processTypeBinding(typeBinding);
					break;
				case IBinding.VARIABLE:
					IVariableBinding fieldBinding = (IVariableBinding) iBinding;
					processFieldBinding(fieldBinding);
					break;
				case IBinding.METHOD:
					IMethodBinding methodBinding = (IMethodBinding) iBinding;
					processMethodBinding(methodBinding);
					break;
			}
		}
	}

	private void processPackageBinding(IPackageBinding packageBinding) {
		String packageName = packageBinding.getName();
				
		if(!classFilter.matches(packageName + ".")){
			Package pkg = cache.getPackage(packageName);
			depReport.addPackageImportDependency(clientCompUnit, pkg);
		}
		
	}
	
	private void processTypeBinding(ITypeBinding iTypeBinding) {
		
		Type type = BindingResolver.resolveTypeBinding(
				classFilter, cache, iTypeBinding);
	
		if(type != null){
			depReport.addTypeImportDependency(clientCompUnit, type);
		}
	}
	
	private void processFieldBinding(IVariableBinding fieldBinding) {

		ITypeBinding iTypeBinding = fieldBinding.getDeclaringClass();
		
		Type type = BindingResolver.resolveTypeBinding(
				classFilter, cache, iTypeBinding);
		
		if (type != null){
			Method attribMethod = type.getAttribMethod();
			depReport.addMethodImportDependency(clientCompUnit, attribMethod);
		}
	}
	
	private void processMethodBinding(IMethodBinding binding) {
		
		Method providerMethod = BindingResolver.resolveMethodBinding(
				classFilter, cache, binding);
		
		if(providerMethod != null){
			depReport.addMethodImportDependency(clientCompUnit, providerMethod);	
		}	
		
	}
}