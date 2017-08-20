package br.usp.ime.jdx.entity.relationship.dependency;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.map.MultiKeyMap;

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
import br.usp.ime.jdx.entity.system.Clazz;
import br.usp.ime.jdx.entity.system.CompUnit;
import br.usp.ime.jdx.entity.system.Interface;
import br.usp.ime.jdx.entity.system.JavaElement;
import br.usp.ime.jdx.entity.system.JavaProject;
import br.usp.ime.jdx.entity.system.Method;
import br.usp.ime.jdx.entity.system.Package;
import br.usp.ime.jdx.entity.system.Type;

public class RawDependencyReport implements Serializable{

	private static final long serialVersionUID = 2294342917700842739L;

	private JavaProject javaProject;
	
	private MultiKeyMap<Type,ClazzInheritanceDependency> clazzInheritanceMap = 
			new MultiKeyMap<>();
			
	private MultiKeyMap<Type,InterfaceInheritanceDependency> interfaceInheritanceMap = 
			new MultiKeyMap<>();
		
	private MultiKeyMap<Type,ImplementsDependency> implementsMap = 
			new MultiKeyMap<>();
	
	private MultiKeyMap<Method,MethodCallDependency> callMap = 
			new MultiKeyMap<>();
			
	private MultiKeyMap<JavaElement,ReferenceDependency> referenceMap = 
			new MultiKeyMap<>();
			
	private MultiKeyMap<Method,AccessDependency> accessMap = 
			new MultiKeyMap<>();
	
	private MultiKeyMap<JavaElement,ReturnDependency> returnMap = 
			new MultiKeyMap<>();
	
	private MultiKeyMap<JavaElement,ParameterDependency> parameterMap = 
			new MultiKeyMap<>();
			
	private MultiKeyMap<JavaElement,PackageImportDependency> pkgImportMap = 
			new MultiKeyMap<>();
					
	private MultiKeyMap<JavaElement,TypeImportDependency> typeImportMap = 
			new MultiKeyMap<>();
							
	private MultiKeyMap<JavaElement,MethodImportDependency> methodImportMap = 
			new MultiKeyMap<>();
					
	private MultiKeyMap<JavaElement,ThrowDependency> throwMap = 
			new MultiKeyMap<>();
			
	public RawDependencyReport(JavaProject javaProject){
		this.javaProject = javaProject;
	}
	
	public void addMethodCallDependency(Method client, Method supplier){
		if (!callMap.containsKey(client, supplier)){
			MethodCallDependency dependency = new MethodCallDependency(client, supplier);
			callMap.put(client, supplier, dependency);
		}
		else{
			MethodCallDependency dependency = callMap.get(client, supplier);
			dependency.increaseStrength();
		}
	}
	
	public void addClazzInheritanceDependency(Clazz client, Clazz supplier){
		
		//TODO: Check whether two clazz inheritances have the same client and 
		//throw an exception
		
		ClazzInheritanceDependency dependency = 
				new ClazzInheritanceDependency(client, supplier);

		clazzInheritanceMap.put(client, supplier, dependency);
	}
	
	public void addInterfaceInheritanceDependency(
			Interface client, Interface supplier){
			
		InterfaceInheritanceDependency dependency = 
				new InterfaceInheritanceDependency(client, supplier);
			
		interfaceInheritanceMap.put(client, supplier, dependency);
	}
	
	public void addImplementsDependency(Clazz client, Interface supplier){
		ImplementsDependency dependency = 
				new ImplementsDependency(client, supplier);

		implementsMap.put(client, supplier, dependency);	
	}
	
	public JavaProject getJavaProject(){
		return javaProject;
	}

	public void addReferenceDependency(Method client, Type supplier) {
		if (!referenceMap.containsKey(client, supplier)){
			ReferenceDependency dependency = new ReferenceDependency(client, supplier);
			referenceMap.put(client, supplier, dependency);
		}
		else{
			ReferenceDependency dependency = referenceMap.get(client, supplier);
			dependency.increaseStrength();
		}
	}

	public void addAccessDependency(Method client, Method supplier) {
		if (!accessMap.containsKey(client, supplier)){
			AccessDependency dependency = new AccessDependency(client, supplier);
			accessMap.put(client, supplier, dependency);
		}
		else{
			AccessDependency dependency = accessMap.get(client, supplier);
			dependency.increaseStrength();
		}
	}

	public void addReturnDependency(Method client, Type supplier) {
		if (!returnMap.containsKey(client, supplier)){
			ReturnDependency dependency = new ReturnDependency(client, supplier);
			returnMap.put(client, supplier, dependency);
		}
		else{
			ReturnDependency dependency = returnMap.get(client, supplier);
			dependency.increaseStrength();
		}
		
	}

	public void addParameterDependency(Method client, Type supplier) {
		if (!parameterMap.containsKey(client, supplier)){
			ParameterDependency dependency = new ParameterDependency(client, supplier);
			parameterMap.put(client, supplier, dependency);
		}
		else{
			ParameterDependency dependency = parameterMap.get(client, supplier);
			dependency.increaseStrength();
		}	
	}

	public void addThrowDependency(Method client, Type supplier) {
		if (!throwMap.containsKey(client, supplier)){
			ThrowDependency dependency = new ThrowDependency(client, supplier);
			throwMap.put(client, supplier, dependency);
		}
		else{
			ThrowDependency dependency = throwMap.get(client, supplier);
			dependency.increaseStrength();
		}
		
	}

	public void addPackageImportDependency(CompUnit client, Package supplier) {
		if (!pkgImportMap.containsKey(client, supplier)){
			PackageImportDependency dependency = 
					new PackageImportDependency(client, supplier);
			pkgImportMap.put(client, supplier, dependency);
		}
		else{
			PackageImportDependency dependency = 
					pkgImportMap.get(client, supplier);
			dependency.increaseStrength();
		}
	}
	
	public void addTypeImportDependency(CompUnit client, Type supplier) {
		if (!typeImportMap.containsKey(client, supplier)){
			TypeImportDependency dependency = 
					new TypeImportDependency(client, supplier);
			typeImportMap.put(client, supplier, dependency);
		}
		else{
			TypeImportDependency dependency = 
					typeImportMap.get(client, supplier);
			dependency.increaseStrength();
		}		
	}
	
	public void addMethodImportDependency(CompUnit client, Method supplier) {
		if (!methodImportMap.containsKey(client, supplier)){
			MethodImportDependency dependency = 
					new MethodImportDependency(client, supplier);
			methodImportMap.put(client, supplier, dependency);
		}
		else{
			MethodImportDependency dependency = 
					methodImportMap.get(client, supplier);
			dependency.increaseStrength();
		}			
	}
	
	public DependencyReport getDependencyReport() {
		
		DependencyReport depReport = new DependencyReport(
				javaProject, 
				this.clazzInheritanceMap.values(), 
				this.interfaceInheritanceMap.values(), 
				this.implementsMap.values(), 
				this.callMap.values(), 
				this.referenceMap.values(), 
				this.accessMap.values(), 
				this.returnMap.values(), 
				this.parameterMap.values(), 
				this.pkgImportMap.values(), 
				this.typeImportMap.values(), 
				this.methodImportMap.values(), 
				this.throwMap.values());
		
		return depReport;
	}
	
}