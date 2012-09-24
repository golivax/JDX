package br.usp.ime.jdx;

import br.usp.ime.jdx.app.JDX;
import br.usp.ime.jdx.entity.DependencyReport;
import br.usp.ime.jdx.filter.JavaFileFilter;
import br.usp.ime.jdx.filter.JavaNativeClassFilter;
import java.util.Date;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {

        System.out.println(new Date(System.currentTimeMillis()));

        String rootDir = "C:/Files/arquigrafia";
        JDX jdx = new JDX();
        DependencyReport depReport = jdx.calculateDepsFrom(
                rootDir, new JavaFileFilter(), new JavaNativeClassFilter());

        System.out.println(depReport.getDependencies().size());
        System.out.println(depReport.getDependencies());
        System.out.println(new Date(System.currentTimeMillis()));

    }
}
