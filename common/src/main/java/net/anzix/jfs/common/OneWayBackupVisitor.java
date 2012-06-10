package net.anzix.jfs.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Properties;
import java.util.Scanner;

public class OneWayBackupVisitor extends SimpleFileVisitor<Path> {
	Path orig;
	Path dest;

	public OneWayBackupVisitor(Path orig, Path dest) {
		super();
		this.orig = orig;
		this.dest = dest;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		String id = getId(dir);
		if (id == null) {
			System.out.println("Creating directory " + dir);
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes arg1)
			throws IOException {
		String id = getId(file);
		if (id == null) {
			System.out.println("uploading file " + file);
		}
		return FileVisitResult.CONTINUE;
	}

	private String getId(Path path) {
		try {
			if (Files.isDirectory(path)) {
				File f = new File(".flickr.collection");
				if (f.exists()) {
					return new Scanner(new FileInputStream(f)).useDelimiter(
							"\\Z").next();
				}
				f = new File(".flickr.set");
				if (f.exists()) {
					return new Scanner(new FileInputStream(f)).useDelimiter(
							"\\Z").next();
				}
				return null;
			} else {
				File f = new File(".flickr.photos");
				if (f.exists()) {
					Properties p = new Properties();
					p.load(new FileInputStream(f));
					return p.getProperty(path.getFileName().toString());
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return null;

	}

	private void saveId(Path path, String id) {

	}
}
