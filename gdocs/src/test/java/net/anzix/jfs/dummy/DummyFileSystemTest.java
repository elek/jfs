package net.anzix.jfs.dummy;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;

import net.anzix.RecursiveCopyVisitor;

import org.junit.Test;

public class DummyFileSystemTest {
	
	@Test
	public void test() throws Exception{
		FileSystem from = new DummyFileSystemProvider().getFileSystem(null);
		Path root = from.getRootDirectories().iterator().next();
		for (Path p : from.provider().newDirectoryStream(root, null)){
			System.out.println(p);
		}				
	}
	
	
	
	@Test
	public void copy() throws Exception{
		Path orig = new DummyFileSystemProvider().getFileSystem(null).getPath("/");
		Path dest = FileSystems.getDefault().getPath("/tmp/asd");
		Files.walkFileTree(orig, new RecursiveCopyVisitor(orig, dest));
	}
}
