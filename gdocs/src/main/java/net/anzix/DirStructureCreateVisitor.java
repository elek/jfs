package net.anzix;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class DirStructureCreateVisitor extends SimpleFileVisitor<Path> {

	Path orig;
	Path dest;

	public DirStructureCreateVisitor(Path orig, Path dest) {
		super();
		this.orig = orig;
		this.dest = dest;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		Path targetPath = dest.resolve(orig.relativize(dir).toString());
		System.out.println("visit dir: " + targetPath);
		if (!Files.exists(targetPath)) {
			Files.createDirectories(targetPath);
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes arg1)
			throws IOException {	
		return FileVisitResult.CONTINUE;
	}

}
