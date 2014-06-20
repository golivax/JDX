package br.usp.ime.jdx.processor.extractor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.nio.file.Paths;

import org.junit.Test;

import br.usp.ime.jdx.app.JDX;
import br.usp.ime.jdx.entity.CompUnit;
import br.usp.ime.jdx.entity.Dependency;
import br.usp.ime.jdx.entity.DependencyReport;
import br.usp.ime.jdx.entity.Type;
import br.usp.ime.jdx.filter.JavaFileFilter;
import br.usp.ime.jdx.filter.JavaNativeClassFilter;

public class DependencyExtractorTest {

	private String rootDir = 
			Paths.get("").toAbsolutePath()+"\\src\\test\\java\\br\\usp\\ime"
					+ "\\jdx\\processor\\extractor\\methodinv";

	//No dep
	
	@Test
	public void shouldFindNoDeps(){
		String rootDir = this.rootDir + "\\nodep";
			
		JDX jdx = new JDX();
		DependencyReport depReport = jdx.calculateDepsFrom(rootDir, false, 
				new JavaFileFilter(), new JavaNativeClassFilter());
		
		assertTrue(depReport.getTypeDependencies().isEmpty());		
	}
	
	//Local variable tests
	
	@Test
	public void shouldFindMethodInvocationsInsideConstructors(){
		String rootDir = this.rootDir + "\\localvariable\\constructorbody";
		
		JDX jdx = new JDX();
		DependencyReport depReport = jdx.calculateDepsFrom(rootDir, false, 
				new JavaFileFilter(), new JavaNativeClassFilter());
		
		System.out.println(depReport);
		
		//One compilation unit dependency
		assertEquals(1, depReport.getCompUnitDependencies().size());
		
		Dependency<CompUnit> dep = 
				depReport.getCompUnitDependencies().iterator().next();
		
		//from A
		assertEquals(rootDir + "\\A.java", dep.getClient().getName());
		//to B
		assertEquals(rootDir + "\\B.java", dep.getSupplier().getName());
		//with 8 calls (2 from each constructor)
		assertEquals(new Integer(8), dep.getStrength());
	}
	
	@Test
	public void shouldFindMethodInvocationsInsideMethods(){
		String rootDir = this.rootDir + "\\localvariable\\methodbody";
		
		JDX jdx = new JDX();
		DependencyReport depReport = jdx.calculateDepsFrom(rootDir, false, 
				new JavaFileFilter(), new JavaNativeClassFilter());
		
		System.out.println(depReport);
		
		//One compilation unit dependency
		assertEquals(1, depReport.getCompUnitDependencies().size());
		
		Dependency<CompUnit> dep = 
				depReport.getCompUnitDependencies().iterator().next();
		
		//from A
		assertEquals(rootDir + "\\A.java", dep.getClient().getName());
		//to B
		assertEquals(rootDir + "\\B.java", dep.getSupplier().getName());
		//with 10 calls (2 from each method)
		assertEquals(new Integer(10), dep.getStrength());
	}
	
	@Test
	public void shouldFindMethodInvocationsInDoWhile(){
		String rootDir = this.rootDir + "\\localvariable\\methodbody\\dowhileexp";
		
		JDX jdx = new JDX();
		DependencyReport depReport = jdx.calculateDepsFrom(rootDir, false, 
				new JavaFileFilter(), new JavaNativeClassFilter());
		
		System.out.println(depReport);
		
		//One compilation unit dependency
		assertEquals(1, depReport.getCompUnitDependencies().size());
		
		Dependency<CompUnit> dep = 
				depReport.getCompUnitDependencies().iterator().next();
		
		//from A
		assertEquals(rootDir + "\\A.java", dep.getClient().getName());
		//to B
		assertEquals(rootDir + "\\B.java", dep.getSupplier().getName());
		//with 3 calls (instantiation, dowhile body, dowhile condition)
		assertEquals(new Integer(3), dep.getStrength());
	}
	
	@Test
	public void shouldFindMethodInvocationsInFor(){
		String rootDir = this.rootDir + "\\localvariable\\methodbody\\forexp";
		
		JDX jdx = new JDX();
		DependencyReport depReport = jdx.calculateDepsFrom(rootDir, false, 
				new JavaFileFilter(), new JavaNativeClassFilter());
		
		System.out.println(depReport);
		
		//One compilation unit dependency
		assertEquals(1, depReport.getCompUnitDependencies().size());
		
		Dependency<CompUnit> dep = 
				depReport.getCompUnitDependencies().iterator().next();
		
		//from A
		assertEquals(rootDir + "\\A.java", dep.getClient().getName());
		//to B
		assertEquals(rootDir + "\\B.java", dep.getSupplier().getName());
		//with 3 calls
		assertEquals(new Integer(3), dep.getStrength());
	}
	
	@Test
	public void shouldFindMethodInvocationsInIfThenElse(){
		String rootDir = this.rootDir + "\\localvariable\\methodbody\\ifthenelseexp";
		
		JDX jdx = new JDX();
		DependencyReport depReport = jdx.calculateDepsFrom(rootDir, false, 
				new JavaFileFilter(), new JavaNativeClassFilter());
		
		System.out.println(depReport);
		
		//One compilation unit dependency
		assertEquals(1, depReport.getCompUnitDependencies().size());
		
		Dependency<CompUnit> dep = 
				depReport.getCompUnitDependencies().iterator().next();
		
		//from A
		assertEquals(rootDir + "\\A.java", dep.getClient().getName());
		//to B
		assertEquals(rootDir + "\\B.java", dep.getSupplier().getName());
		//with 6 calls
		assertEquals(new Integer(6), dep.getStrength());
	}
	
	
	@Test
	public void shouldFindMethodInvocationInReturn(){
		String rootDir = this.rootDir + "\\localvariable\\methodbody\\returnexp";
		
		JDX jdx = new JDX();
		DependencyReport depReport = jdx.calculateDepsFrom(rootDir, false, 
				new JavaFileFilter(), new JavaNativeClassFilter());
		
		System.out.println(depReport);
		
		//One compilation unit dependency
		assertEquals(1, depReport.getCompUnitDependencies().size());
		
		Dependency<CompUnit> dep = 
				depReport.getCompUnitDependencies().iterator().next();
		
		//from A
		assertEquals(rootDir + "\\A.java", dep.getClient().getName());
		//to B
		assertEquals(rootDir + "\\B.java", dep.getSupplier().getName());
		//with 1 call
		assertEquals(new Integer(1), dep.getStrength());
	}
	
	@Test
	public void shouldFindMethodInvocationsInSwitch(){
		String rootDir = this.rootDir + "\\localvariable\\methodbody\\switchexp";
		
		JDX jdx = new JDX();
		DependencyReport depReport = jdx.calculateDepsFrom(rootDir, false, 
				new JavaFileFilter(), new JavaNativeClassFilter());
		
		System.out.println(depReport);
		
		//One compilation unit dependency
		assertEquals(1, depReport.getCompUnitDependencies().size());
		
		Dependency<CompUnit> dep = 
				depReport.getCompUnitDependencies().iterator().next();
		
		//from A
		assertEquals(rootDir + "\\A.java", dep.getClient().getName());
		//to B
		assertEquals(rootDir + "\\B.java", dep.getSupplier().getName());
		//with 5 calls (instantiation, switch parm, case 1, case3, default)
		assertEquals(new Integer(5), dep.getStrength());
	}
	
	@Test
	public void shouldFindMethodInvocationsInTryCatch(){
		String rootDir = this.rootDir + "\\localvariable\\methodbody\\trycatchexp";
		
		JDX jdx = new JDX();
		DependencyReport depReport = jdx.calculateDepsFrom(rootDir, false, 
				new JavaFileFilter(), new JavaNativeClassFilter());
		
		System.out.println(depReport);
		
		//One compilation unit dependency
		assertEquals(1, depReport.getCompUnitDependencies().size());
		
		Dependency<CompUnit> dep = 
				depReport.getCompUnitDependencies().iterator().next();
		
		//from A
		assertEquals(rootDir + "\\A.java", dep.getClient().getName());
		//to B
		assertEquals(rootDir + "\\B.java", dep.getSupplier().getName());
		//with 4 calls (instantiation, inside try, inside catch, inside finally)
		assertEquals(new Integer(4), dep.getStrength());
	}
	
	@Test
	public void shouldFindMethodInvocationsInWhile(){
		String rootDir = this.rootDir + "\\localvariable\\methodbody\\whileexp";
		
		JDX jdx = new JDX();
		DependencyReport depReport = jdx.calculateDepsFrom(rootDir, false, 
				new JavaFileFilter(), new JavaNativeClassFilter());
		
		System.out.println(depReport);
		
		//One compilation unit dependency
		assertEquals(1, depReport.getCompUnitDependencies().size());
		
		Dependency<CompUnit> dep = 
				depReport.getCompUnitDependencies().iterator().next();
		
		//from A
		assertEquals(rootDir + "\\A.java", dep.getClient().getName());
		//to B
		assertEquals(rootDir + "\\B.java", dep.getSupplier().getName());
		//with 3 calls (instantiation, while condition, while body)
		assertEquals(new Integer(3), dep.getStrength());
	}
	
	@Test
	public void shouldFindMultipleMethodInvocations(){
		String rootDir = this.rootDir + "\\localvariable\\methodbody\\misc\\multiplecalls";
		
		JDX jdx = new JDX();
		DependencyReport depReport = jdx.calculateDepsFrom(rootDir, false, 
				new JavaFileFilter(), new JavaNativeClassFilter());
		
		System.out.println(depReport);
		
		//One compilation unit dependency
		assertEquals(1, depReport.getCompUnitDependencies().size());
		
		Dependency<CompUnit> dep = 
				depReport.getCompUnitDependencies().iterator().next();
		
		//from A
		assertEquals(rootDir + "\\A.java", dep.getClient().getName());
		//to B
		assertEquals(rootDir + "\\B.java", dep.getSupplier().getName());
		//with 5 calls (3 from foo(), 2 from the other)
		assertEquals(new Integer(5), dep.getStrength());
	}
	
	@Test
	public void shouldFindChainedMethodInvocations(){
		String rootDir = this.rootDir + "\\localvariable\\methodbody\\misc\\chainedcalls";
		
		JDX jdx = new JDX();
		DependencyReport depReport = jdx.calculateDepsFrom(rootDir, false, 
				new JavaFileFilter(), new JavaNativeClassFilter());
		
		System.out.println(depReport);
		
		//One compilation unit dependency
		assertEquals(3, depReport.getCompUnitDependencies().size());
		
		//From A to B
		Dependency<Type> dep = 
			depReport.getTypeDependency("br.usp.ime.jdx.processor.extractor."
			+ "methodinv.localvariable.methodbody.misc.chainedcalls.A",
			"br.usp.ime.jdx.processor.extractor."
			+ "methodinv.localvariable.methodbody.misc.chainedcalls.B");
	
		assertNotNull(dep);
		
		//checking that it is from A
		assertEquals(rootDir + "\\A.java", dep.getClient().getCompUnit().getName());
		//checking that it is to B
		assertEquals(rootDir + "\\B.java", dep.getSupplier().getCompUnit().getName());
		//with 1 call (instantiation of B)
		assertEquals(new Integer(1), dep.getStrength());
		
		//From A to C
		dep =	depReport.getTypeDependency("br.usp.ime.jdx.processor.extractor."
				+ "methodinv.localvariable.methodbody.misc.chainedcalls.A",
				"br.usp.ime.jdx.processor.extractor."
				+ "methodinv.localvariable.methodbody.misc.chainedcalls.C");
			
		assertNotNull(dep);
				
		//checking that it is from A
		assertEquals(rootDir + "\\A.java", dep.getClient().getCompUnit().getName());
		//checking that it is to C
		assertEquals(rootDir + "\\C.java", dep.getSupplier().getCompUnit().getName());
		//with 1 call (the chained call is evaluated as a single statement, i.e.
		//it is treated as a direct call from A to C)
		assertEquals(new Integer(1), dep.getStrength());
		
		//From B to C
		dep =	depReport.getTypeDependency("br.usp.ime.jdx.processor.extractor."
				+ "methodinv.localvariable.methodbody.misc.chainedcalls.B",
				"br.usp.ime.jdx.processor.extractor."
				+ "methodinv.localvariable.methodbody.misc.chainedcalls.C");

		assertNotNull(dep);

		//checking that it is from B
		assertEquals(rootDir + "\\B.java", dep.getClient().getCompUnit().getName());
		//checking that it is to C
		assertEquals(rootDir + "\\C.java", dep.getSupplier().getCompUnit().getName());
		//with 1 call (instantiation)
		assertEquals(new Integer(1), dep.getStrength());
	}
	
	@Test
	public void shouldFindCallToAttrib(){
		String rootDir = this.rootDir + "\\localvariable\\methodbody\\misc\\callatrib";
		
		JDX jdx = new JDX();
		DependencyReport depReport = jdx.calculateDepsFrom(rootDir, false, 
				new JavaFileFilter(), new JavaNativeClassFilter());
		
		System.out.println(depReport);
		
		//One compilation unit dependency
		assertEquals(3, depReport.getCompUnitDependencies().size());
		
		//From A to B
		Dependency<Type> dep = 
			depReport.getTypeDependency("br.usp.ime.jdx.processor.extractor."
			+ "methodinv.localvariable.methodbody.misc.callatrib.A",
			"br.usp.ime.jdx.processor.extractor."
			+ "methodinv.localvariable.methodbody.misc.callatrib.B");
	
		assertNotNull(dep);
		
		//checking that it is from A
		assertEquals(rootDir + "\\A.java", dep.getClient().getCompUnit().getName());
		//checking that it is to B
		assertEquals(rootDir + "\\B.java", dep.getSupplier().getCompUnit().getName());
		//with 2 calls (instantiation of B, b.getC())
		assertEquals(new Integer(2), dep.getStrength());
		
		//From A to C
		dep =	depReport.getTypeDependency("br.usp.ime.jdx.processor.extractor."
				+ "methodinv.localvariable.methodbody.misc.callatrib.A",
				"br.usp.ime.jdx.processor.extractor."
				+ "methodinv.localvariable.methodbody.misc.callatrib.C");
			
		assertNotNull(dep);
				
		//checking that it is from A
		assertEquals(rootDir + "\\A.java", dep.getClient().getCompUnit().getName());
		//checking that it is to C
		assertEquals(rootDir + "\\C.java", dep.getSupplier().getCompUnit().getName());
		//with 1 call (c.bar())
		assertEquals(new Integer(1), dep.getStrength());
		
		//From B to C
		dep =	depReport.getTypeDependency("br.usp.ime.jdx.processor.extractor."
				+ "methodinv.localvariable.methodbody.misc.callatrib.B",
				"br.usp.ime.jdx.processor.extractor."
				+ "methodinv.localvariable.methodbody.misc.callatrib.C");

		assertNotNull(dep);

		//checking that it is from B
		assertEquals(rootDir + "\\B.java", dep.getClient().getCompUnit().getName());
		//checking that it is to C
		assertEquals(rootDir + "\\C.java", dep.getSupplier().getCompUnit().getName());
		//with 1 call (instantiation)
		assertEquals(new Integer(1), dep.getStrength());
	}
	
	@Test
	public void shouldFindMethodInvocationsInsideInnerClass(){
		String rootDir = this.rootDir + "\\localvariable\\innerclassbody";
		
		JDX jdx = new JDX();
		DependencyReport depReport = jdx.calculateDepsFrom(rootDir, false, 
				new JavaFileFilter(), new JavaNativeClassFilter());
		
		System.out.println(depReport);
		
		//One compilation unit dependency
		assertEquals(1, depReport.getCompUnitDependencies().size());
		
		Dependency<CompUnit> dep = 
				depReport.getCompUnitDependencies().iterator().next();
		
		//from A
		assertEquals(rootDir + "\\A.java", dep.getClient().getName());
		//to B
		assertEquals(rootDir + "\\B.java", dep.getSupplier().getName());
		//with 12 calls (2 from each method)
		assertEquals(new Integer(12), dep.getStrength());
	}
	
	@Test
	public void shouldFindMethodInvocationsInsideLocalClass(){
		String rootDir = this.rootDir + "\\localvariable\\localclassbody";
		
		JDX jdx = new JDX();
		DependencyReport depReport = jdx.calculateDepsFrom(rootDir, false,
				new JavaFileFilter(), new JavaNativeClassFilter());
		
		System.out.println(depReport);
		
		//One compilation unit dependency
		assertEquals(1, depReport.getCompUnitDependencies().size());
		
		Dependency<CompUnit> dep = 
				depReport.getCompUnitDependencies().iterator().next();
		
		//from A
		assertEquals(rootDir + "\\A.java", dep.getClient().getName());
		//to B
		assertEquals(rootDir + "\\B.java", dep.getSupplier().getName());
		//with 2 calls
		assertEquals(new Integer(2), dep.getStrength());
	}
	
	@Test
	public void shouldFindAttributeInitialization(){
		String rootDir = this.rootDir + "\\attribute\\declaration";
		
		JDX jdx = new JDX();
		DependencyReport depReport = jdx.calculateDepsFrom(rootDir, false,
				new JavaFileFilter(), new JavaNativeClassFilter());
		
		System.out.println(depReport);
		
		//One compilation unit dependency
		assertEquals(1, depReport.getCompUnitDependencies().size());
		
		Dependency<CompUnit> dep = 
				depReport.getCompUnitDependencies().iterator().next();
		
		//from A
		assertEquals(rootDir + "\\A.java", dep.getClient().getName());
		//to B
		assertEquals(rootDir + "\\B.java", dep.getSupplier().getName());
		//with 4 calls (the instantiation of the four attributes)
		assertEquals(new Integer(4), dep.getStrength());
	}
	
	@Test
	public void shouldFindAttributeMethodInvocationsInMethodBody(){
		String rootDir = this.rootDir + "\\attribute\\methodbody";
		
		JDX jdx = new JDX();
		DependencyReport depReport = jdx.calculateDepsFrom(rootDir, false,
				new JavaFileFilter(), new JavaNativeClassFilter());
		
		System.out.println(depReport);
		
		//One compilation unit dependency
		assertEquals(1, depReport.getCompUnitDependencies().size());
		
		Dependency<CompUnit> dep = 
				depReport.getCompUnitDependencies().iterator().next();
		
		//from A
		assertEquals(rootDir + "\\A.java", dep.getClient().getName());
		//to B
		assertEquals(rootDir + "\\B.java", dep.getSupplier().getName());
		//with 2 calls
		assertEquals(new Integer(2), dep.getStrength());
	}
	
	/**
	@Test
	public void shouldCountDifferentReferencedTypesInClassAttributes() {
		SourceCodeUnit source = new SourceCodeUnit();
		source.setCode(
				(classDeclaration(
					"private ClassA a;\r\n"+
					"private ClassA b;\r\n"+
					"private ClassB a;\r\n"+
					"private ClassC a;\r\n"
				)));
		
		extractor.extractFromSourceCode(source);	
		//assertEquals(3, metric.coupling());
	}
	
	@Test
	public void shouldCountDifferentReferencedTypesInsideMethods() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"public void method1() { ClassA a; ClassB b; x(); }"+
								"public void method2() { ClassA a; ClassC c; y(); }"+
								"public void method3() { ClassD d; ClassE e; z(); }"
								)));
	
		assertEquals(5, metric.coupling());
	}
	
	@Test
	public void shouldCountDifferentImportedReferencedTypesInsideMethods() {
		metric.calculate(
				toInputStream(
						imports("import bla.ble.ClassA;import bli.blo.ClassC;", classDeclaration(
								"public void method1() { ClassA a; ClassB b; x(); }"+
								"public void method2() { ClassA a; ClassC c; y(); }"+
								"public void method3() { ClassD d; ClassE e; z(); }"
								))));
	
		assertEquals(5, metric.coupling());
	}
	
	@Test
	public void shouldCountDifferentReferencedTypesInMethodSignatura() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								")public void method1(ClassA a){}\r\n"+
								"public void method2(ClassA a){}\r\n"+
								"public void method3(ClassB b){}\r\n"+
								"public void method4(ClassC c){}\r\n"
								)));
	
		assertEquals(3, metric.coupling());

	}
	
	@Test
	public void shouldCountDifferentReferencedTypesInsideInnerClasses() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"public void method1() { new Thread(new Runnable() { public void run() {" + 
								"ClassA a; ClassB b; a = new ClassA();"+
								"} }).start(); }"
								)));
	
		assertEquals(4, metric.coupling());
	}
	
	@Test
	public void shouldIgnoreItsOwnClass() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"public void method1() { new A(); new Program(); }"
								)));
	
		assertEquals(1, metric.coupling());
	}
	
	@Test
	public void shouldCountReferencedInsideMethodsAndAttributes() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"private ClassF a;\r\n"+
								"private ClassG a;\r\n"+
								"public void method1() { ClassA a; ClassB b; x(); }"+
								"public void method2() { ClassA a; ClassC c; y(); }"+
								"public void method3() { ClassD d; ClassE e; z(); }"
								)));
	
		assertEquals(7, metric.coupling());
	}
	
	@Test
	public void shouldCountTypesOfInstantiatedObjects() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"public void method1() { ClassA a = new ConcreteClassA(); }"
								)));
	
		assertEquals(2, metric.coupling());
	}
	
	@Test
	public void shouldIgnorePrimitipeTypes() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"private int a;\r\n"+
								"public void method1() { double a; float b; x(); }"+
								"public void method2() { boolean a; long c; y(); }"+
								"public void method3() { char d; int e; z(); }"
								)));
	
		assertEquals(0, metric.coupling());
	}
	
	@Test
	public void shouldCountStaticImports() {
		metric.calculate(
				toInputStream(
						imports("import static org.mockito.Mockito.*;", classDeclaration(
								"public void method3() { ClassA x = mock(bla); }"
								))));
	
		assertEquals(2, metric.coupling());
	}

	@Test
	public void shouldCountStaticImportsInDefaultPackage() {
		metric.calculate(
				toInputStream(
						imports("import static Mockito.*;", classDeclaration(
								"public void method3() { ClassA x = mock(bla); }"
								))));
	
		assertEquals(2, metric.coupling());
	}
	
	@Test
	public void shouldIgnoreUnusedImports() {
		metric.calculate(
				toInputStream(
						imports("import br.com.packet.NonUsedClass;", classDeclaration(
								"public void method3() { new ClassA().doSomething(); }"
								))));
		
		assertEquals(1, metric.coupling());
	}
	
	@Test
	public void shouldCountStaticInvocations() {
		metric.calculate(
				toInputStream(
						imports("import org.junit.Assert;",
						classDeclaration(
								"public void method1() { Assert.assertEquals(x,y); junit.xyz(); }"
								))));
	
		assertEquals(1, metric.coupling());
	}
	
	@Test
	public void shouldCountInterfaces() {
		metric.calculate(
				toInputStream(
						"import p1.p2.InterfaceA;"+
						"class Program implements InterfaceA, InterfaceB {"+
								"public void method1() {  }"+
						"}"
								));
	
		assertEquals(2, metric.coupling());
	}
	
	@Test
	public void shouldCountInheritance() {

		metric.calculate(
				toInputStream(
						"import p1.p2.ClassA;"+
						"class Program extends ClassA {"+
								"public void method1() {  }"+
						"}"
								));
	
		assertEquals(1, metric.coupling());

	}

	@Test // too hard to implement! ;(
	public void unfortunatelyShouldIgnoreStaticInvocationsImportedWithStart() {
		metric.calculate(
				toInputStream(
						imports("import org.junit.*;",
						classDeclaration(
								"public void method1() { Assert.assertEquals(x,y); }"
								))));
	
		assertEquals(0, metric.coupling());
	}
	
	@Test
	public void shouldUnderstandAHugeClass() {
		String code = 
				"package br.com.caelum.revolution.analyzers;"+
						""+
						"import java.util.ArrayList;"+
						"import java.util.List;"+
						""+
						"import org.apache.log4j.Logger;"+
						""+
						"import br.com.caelum.revolution.builds.Build;"+
						"import br.com.caelum.revolution.builds.BuildResult;"+
						"import br.com.caelum.revolution.changesets.ChangeSet;"+
						"import br.com.caelum.revolution.changesets.ChangeSetCollection;"+
						"import br.com.caelum.revolution.domain.Commit;"+
						"import br.com.caelum.revolution.domain.PersistedCommitConverter;"+
						"import br.com.caelum.revolution.persistence.HibernatePersistence;"+
						"import br.com.caelum.revolution.persistence.ToolThatPersists;"+
						"import br.com.caelum.revolution.scm.CommitData;"+
						"import br.com.caelum.revolution.scm.GoToChangeSet;"+
						"import br.com.caelum.revolution.scm.SCM;"+
						"import br.com.caelum.revolution.scm.ToolThatUsesSCM;"+
						"import br.com.caelum.revolution.tools.Tool;"+
						""+
						"public class DefaultAnalyzer implements Analyzer {"+
						""+
						"	private final Build sourceBuilder;"+
						"	private final List<Tool> tools;"+
						"	private final SCM scm;"+
						"	private final HibernatePersistence persistence;"+
						"	private static Logger log = Logger.getLogger(DefaultAnalyzer.class);"+
						"	private final PersistedCommitConverter convert;"+
						"	public DefaultAnalyzer(SCM scm, Build build, List<Tool> tools,"+
						"			PersistedCommitConverter convert, HibernatePersistence persistence) {"+
						"		this.scm = scm;"+
						"		this.sourceBuilder = build;"+
						"		this.tools = tools;"+
						"		this.convert = convert;"+
						"		this.persistence = persistence;"+
						"	}"+
						"	public void start(ChangeSetCollection collection) {"+
						"		startPersistenceEngine();"+
						"		giveSCMToTools();"+
						"		for (ChangeSet changeSet : collection.get()) {"+
						"			try {"+
						"				log.info(\"--------------------------\");"+
						"				log.info(\"Starting analyzing changeset \" + changeSet.getId());"+
						"				"+
						"				CommitData data = scm.detail(changeSet.getId());"+
						"				log.info(\"Message: \" + data.getMessage());"+
						"				"+
						"				BuildResult currentBuild = build(changeSet);"+
						"				persistence.beginTransaction();"+
						"				Commit commit = convert.toDomain(data, persistence.getSession());"+
						"				log.info(\"Author: \" + commit.getAuthor().getName() + \" on \" + commit.getDate().getTime() + \" with \" + commit.getArtifacts().size() + \" artifacts\");"+
						"				giveSessionToTools();"+
						"				runTools(commit, currentBuild);"+
						"				persistence.commit();"+
						"			} catch (Exception e) {"+
						"				persistence.rollback();"+
						"				log.warn(\"Something went wrong in changeset \" + changeSet.getId(), e);"+
						"			}"+
						"		}"+
						""+
						"		persistence.end();"+
						"	}"+
						""+
						"	private BuildResult build(ChangeSet changeSet) {"+
						"		try {"+
						"		BuildResult currentBuild = sourceBuilder.build(changeSet.getId(), scm);"+
						"		return currentBuild;"+
						"		}"+
						"		catch(Exception e) {"+
						"			log.warn(\"build failed: \" + changeSet.getId(), e);"+
						"			return null;"+
						"		}"+
						"	}"+
						""+
						""+
						"	private void giveSCMToTools() {"+
						"		for (Tool tool : tools) {"+
						"			if (tool instanceof ToolThatUsesSCM) {"+
						"				ToolThatUsesSCM theTool = (ToolThatUsesSCM) tool;"+
						"				theTool.setSCM(scm);"+
						"			}"+
						"		}"+
						"	}"+
						""+
						"	private void startPersistenceEngine() {"+
						"		List<Class<?>> classes = new ArrayList<Class<?>>();"+
						""+
						"		for (Tool tool : tools) {"+
						"			if (tool instanceof ToolThatPersists) {"+
						"				ToolThatPersists theTool = (ToolThatPersists) tool;"+
						"				for (Class<?> clazz : theTool.classesToPersist()) {"+
						"					classes.add(clazz);"+
						"				}"+
						"			}"+
						"		}"+
						""+
						"		persistence.initMechanism(classes);"+
						"	}"+
						""+
						"	private void giveSessionToTools() {"+
						""+
						"		for (Tool tool : tools) {"+
						"			if (tool instanceof ToolThatPersists) {"+
						"				ToolThatPersists theTool = (ToolThatPersists) tool;"+
						"				theTool.setSession(persistence.getSession());"+
						"			}"+
						"		}"+
						""+
						"	}"+
						""+
						"	private void runTools(Commit commit, BuildResult currentBuild) {"+
						"		for (Tool tool : tools) {"+
						"			try {"+
						"				"+
						"				if(tool.getClass().isAnnotationPresent(GoToChangeSet.class)) {"+
						"					scm.goTo(commit.getCommitId());"+
						"				}"+
						"				"+
						"				log.info(\"running tool: \" + tool.getName());"+
						"				tool.calculate(commit, currentBuild);"+
						"			} catch (Exception e) {"+
						"				log.error(\"error in tool \" + tool.getName(), e);"+
						"			}"+
						"		}"+
						"	}"+
						""+
						"}";
		
		SourceCodeUnit sourceCode = new SourceCodeUnit();
		sourceCode.setCode(code);
		
		extractor.extractFromSourceCode(sourceCode);
		
		//assertEquals(19, metric.coupling());

	}
*/
}
