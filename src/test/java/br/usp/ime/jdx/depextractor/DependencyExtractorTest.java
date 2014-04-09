package br.usp.ime.jdx.depextractor;


public class DependencyExtractorTest {

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
								"public void method1(ClassA a){}\r\n"+
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
