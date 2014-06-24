package br.usp.ime.jdx.util.filesystem;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
 

public class PathFinder extends SimpleFileVisitor<Path> {

	private List<String> foundPaths;
    private final PathMatcher matcher;

    public PathFinder(String pattern) {
    	
    	foundPaths = new ArrayList<String>();
        matcher = FileSystems.getDefault()
                .getPathMatcher("glob:" + pattern);
    }

    // Compares the glob pattern against the file or directory name.
    private void find(Path file) {
        Path name = file.getFileName();
        if (name != null && matcher.matches(name)) {
        	foundPaths.add(file.toString());
        }
    }

    // Invoke the pattern matching method on each file.
    @Override
	public FileVisitResult visitFile(Path file,
            BasicFileAttributes attrs) {
        find(file);
        return CONTINUE;
    }

    // Invoke the pattern matching method on each directory.
    @Override
    public FileVisitResult preVisitDirectory(Path dir,
            BasicFileAttributes attrs) {
        find(dir);
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file,
            IOException exc) {
        System.err.println(exc);
        return CONTINUE;
    }
    
    public List<String> getResults(){
    	return foundPaths;
    }
    
}
