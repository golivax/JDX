package br.usp.ime.jdx.entity.relationship.dependency;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import br.usp.ime.jdx.entity.relationship.dependency.importdep.MethodImportDependency;
import br.usp.ime.jdx.entity.relationship.dependency.importdep.PackageImportDependency;
import br.usp.ime.jdx.entity.relationship.dependency.importdep.TypeImportDependency;
import br.usp.ime.jdx.entity.relationship.dependency.m2m.AccessDependency;
import br.usp.ime.jdx.entity.relationship.dependency.m2m.MethodCallDependency;
import br.usp.ime.jdx.entity.relationship.dependency.m2m.MethodToMethodDependency;
import br.usp.ime.jdx.entity.relationship.dependency.m2t.MethodToTypeDependency;
import br.usp.ime.jdx.entity.relationship.dependency.m2t.ParameterDependency;
import br.usp.ime.jdx.entity.relationship.dependency.m2t.ReferenceDependency;
import br.usp.ime.jdx.entity.relationship.dependency.m2t.ReturnDependency;
import br.usp.ime.jdx.entity.relationship.dependency.m2t.ThrowDependency;
import br.usp.ime.jdx.entity.relationship.dependency.meta.CompUnitMetaDepFactory;
import br.usp.ime.jdx.entity.relationship.dependency.meta.CompUnitMetaDependency;
import br.usp.ime.jdx.entity.relationship.dependency.meta.TypeMetaDepFactory;
import br.usp.ime.jdx.entity.relationship.dependency.meta.TypeMetaDependency;
import br.usp.ime.jdx.entity.relationship.dependency.t2t.ClazzInheritanceDependency;
import br.usp.ime.jdx.entity.relationship.dependency.t2t.ImplementsDependency;
import br.usp.ime.jdx.entity.relationship.dependency.t2t.InterfaceInheritanceDependency;
import br.usp.ime.jdx.entity.relationship.dependency.t2t.TypeToTypeDependency;
import br.usp.ime.jdx.entity.system.JavaProject;

public class DependencyReport implements Serializable{

	private static final long serialVersionUID = -5193207150150151958L;

	private JavaProject javaProject;
	
	private Set<ClazzInheritanceDependency> clazzInheritanceDeps;
	private Set<InterfaceInheritanceDependency> interfaceInheritanceDeps;
	private Set<ImplementsDependency> implementsDeps;
	private Set<MethodCallDependency> callDeps;
	private Set<ReferenceDependency> referenceDeps;
	private Set<AccessDependency> accessDeps;
	private Set<ReturnDependency> returnDeps;
	private Set<ParameterDependency> parameterDeps;
	private Set<PackageImportDependency> pkgImportDeps;
	private Set<TypeImportDependency> typeImportDeps;
	private Set<MethodImportDependency> methodImportDeps;
	private Set<ThrowDependency> throwDeps;
	
	public DependencyReport(JavaProject javaProject, 
			Collection<ClazzInheritanceDependency> clazzInheritanceDeps,
			Collection<InterfaceInheritanceDependency> interfaceInheritanceDeps,
			Collection<ImplementsDependency> implementsDeps,
			Collection<MethodCallDependency> callDeps,
			Collection<ReferenceDependency> referenceDeps,
			Collection<AccessDependency> accessDeps,
			Collection<ReturnDependency> returnDeps,
			Collection<ParameterDependency> parameterDeps,
			Collection<PackageImportDependency> pkgImportDeps,
			Collection<TypeImportDependency> typeImportDeps,
			Collection<MethodImportDependency> methodImportDeps,
			Collection<ThrowDependency> throwDeps){
		
		this.javaProject = javaProject;
		
		this.clazzInheritanceDeps = new HashSet<>(clazzInheritanceDeps);
		this.interfaceInheritanceDeps = new HashSet<>(interfaceInheritanceDeps);
		this.implementsDeps = new HashSet<>(implementsDeps);
		this.callDeps = new HashSet<>(callDeps);
		this.referenceDeps = new HashSet<>(referenceDeps);
		this.accessDeps = new HashSet<>(accessDeps);
		this.returnDeps = new HashSet<>(returnDeps);
		this.parameterDeps = new HashSet<>(parameterDeps);
		this.pkgImportDeps = new HashSet<>(pkgImportDeps);
		this.typeImportDeps = new HashSet<>(typeImportDeps);
		this.methodImportDeps = new HashSet<>(methodImportDeps);
		this.throwDeps = new HashSet<>(throwDeps);
	}

	public JavaProject getJavaProject() {
		return javaProject;
	}

	public Set<ClazzInheritanceDependency> getClazzInheritanceDependencies() {
		return clazzInheritanceDeps;
	}

	public Set<InterfaceInheritanceDependency> getInterfaceInheritanceDependencies() {
		return interfaceInheritanceDeps;
	}

	public Set<ImplementsDependency> getImplementsDependencies() {
		return implementsDeps;
	}

	public Set<MethodCallDependency> getMethodCallDependencies() {
		return callDeps;
	}

	public Set<ReferenceDependency> getReferenceDependencies() {
		return referenceDeps;
	}

	public Set<AccessDependency> getAccessDependencies() {
		return accessDeps;
	}

	public Set<ReturnDependency> getReturnTypeDependencies() {
		return returnDeps;
	}

	public Set<ParameterDependency> getMethodParameterDependencies() {
		return parameterDeps;
	}

	public Set<PackageImportDependency> getPackageImportDependencies() {
		return pkgImportDeps;
	}

	public Set<TypeImportDependency> getTypeImportDependencies() {
		return typeImportDeps;
	}

	public Set<MethodImportDependency> getMethodImportDependencies() {
		return methodImportDeps;
	}

	public Set<ThrowDependency> getThrowDependencies() {
		return throwDeps;
	}
	
	public Collection<TypeMetaDependency> getTypeMetaDependencies(){
		
		TypeMetaDepFactory typeMetaDepFactory = new TypeMetaDepFactory();
		return typeMetaDepFactory.getTypeMetaDependencies(this);
	}
	
	public Collection<CompUnitMetaDependency> getCompUnitMetaDependencies(boolean skipSelfDependencies){
		
		CompUnitMetaDepFactory compUnitMetaDepFactory = new CompUnitMetaDepFactory();
		return compUnitMetaDepFactory.getCompUnitMetaDependencies(this, skipSelfDependencies);
	}

	public TypeMetaDependency getTypeMetaDependency(String clientTypeName, String supplierTypeName){
		
		Collection<TypeMetaDependency> metaTypeDeps = getTypeMetaDependencies();
		
		for(TypeMetaDependency typeMetaDep : metaTypeDeps){
			
			if(typeMetaDep.getClient().getFQN().equals(clientTypeName) && 
				typeMetaDep.getSupplier().getFQN().equals(supplierTypeName)){
			
				return typeMetaDep;
			}
		}
		
		return null;
	}
	
	public TypeToTypeDependency getTypeDependency(
			String clientName, String supplierName){

		Collection<TypeToTypeDependency> typeDependencies = getTypeToTypeDependencies();

		for(TypeToTypeDependency typeDependency : typeDependencies){

			if(typeDependency.getClient().getFQN().equals(clientName) && 
					typeDependency.getSupplier().getFQN().equals(supplierName)){

				return typeDependency;
			}
		}

		return null;
	}
	

	public Set<MethodToMethodDependency> getMethodToMethodDependencies(){
		
		Set<MethodToMethodDependency> methodDependencies = new HashSet<>();
		
		methodDependencies.addAll(getAccessDependencies());
		methodDependencies.addAll(getMethodCallDependencies());
				
		return methodDependencies;
	}
	
	public Collection<MethodToTypeDependency> getMethodToTypeDependencies(){
		
		Set<MethodToTypeDependency> methodToTypeDependencies = new HashSet<>();
		methodToTypeDependencies.addAll(getMethodParameterDependencies());
		methodToTypeDependencies.addAll(getReferenceDependencies());
		methodToTypeDependencies.addAll(getReturnTypeDependencies());
		methodToTypeDependencies.addAll(getThrowDependencies());
			
		return methodToTypeDependencies;
	}
	
	public Collection<TypeToTypeDependency> getTypeToTypeDependencies(){
		
		Set<TypeToTypeDependency> typeDependencies = new HashSet<>();
		typeDependencies.addAll(getClazzInheritanceDependencies());
		typeDependencies.addAll(getInterfaceInheritanceDependencies());
		typeDependencies.addAll(getImplementsDependencies());
		return typeDependencies;
	}
	
}
