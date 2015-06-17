package br.usp.ime.jdx.processor.extractor;

import static org.junit.Assert.assertEquals;

import java.nio.file.Paths;
import java.util.Collection;

import org.junit.Test;

import br.usp.ime.jdx.app.JDX;
import br.usp.ime.jdx.entity.relationship.dependency.DependencyReport;
import br.usp.ime.jdx.entity.relationship.dependency.t2t.ClazzInheritanceDependency;
import br.usp.ime.jdx.entity.relationship.dependency.t2t.TypeToTypeDependency;
import br.usp.ime.jdx.filter.JavaAPIMatcher;

public class TypeDependencyExtractorTest {

	private String currentDir =
			Paths.get("").toAbsolutePath().toString().replaceAll("\\\\", "/"); 
	
	private String testPath = "src/test/java";
	
	private String packagePath = "br/usp/ime/jdx/processor/extractor/typedep";
	
	private String rootDir = currentDir + "/" + testPath + "/" + packagePath;
		
	@Test
	public void shouldFindNoCallsFromOneTypeToAnother(){
		
		String innerPackage = "/inheritance/clazz";
		String rootDir = this.rootDir + innerPackage;
		
		String packagePath = 
				(this.packagePath + innerPackage).replaceAll("/", ".");
		
		JDX jdx = new JDX();
		
		DependencyReport depReport = jdx.calculateDepsFrom(rootDir, false, 
				"*.java", new JavaAPIMatcher(), true);
		
		Collection<TypeToTypeDependency> typeDependencies = 
				depReport.getTypeToTypeDependencies();
		
		assertEquals(1, typeDependencies.size());
		
		Collection<ClazzInheritanceDependency> clazzInheritanceDeps = 
				depReport.getClazzInheritanceDependencies();

		assertEquals(1, clazzInheritanceDeps.size());
		
		ClazzInheritanceDependency clazzInheritanceDep = 
				clazzInheritanceDeps.iterator().next();
		
		assertEquals(packagePath + ".A", clazzInheritanceDep.getClient().getFQN());
		assertEquals(packagePath + ".B", clazzInheritanceDep.getSupplier().getFQN());
		assertEquals("extends", clazzInheritanceDep.getLabel());
		assertEquals(new Integer(1), clazzInheritanceDep.getStrength());
	}
	
	
}
