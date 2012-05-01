package net.anzix;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

public class RecursiveCopyVisitor extends SimpleFileVisitor<Path> {

	Path orig;
	Path dest;

	public RecursiveCopyVisitor(Path orig, Path dest) {
		super();
		this.orig = orig;
		this.dest = dest;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		Path targetPath = dest.resolve(orig.relativize(dir).toString());
		System.out.println("visit dir: " + orig.relativize(dir));
		if (!Files.exists(targetPath)) {
			System.out.println("create directory " + targetPath);
			Files.createDirectories(targetPath);
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes arg1)
			throws IOException {
		Path destFile = dest.resolve(orig.relativize(file).toString());
		if (!Files.exists(destFile)) {
			System.out.println("copy " + file + " to " + destFile);
			Files.copy(file, destFile, StandardCopyOption.REPLACE_EXISTING);
		}
		return FileVisitResult.CONTINUE;
	}
}
