package net.anzix.jfs.flickr;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import net.anzix.jfs.common.RecursiveCopyVisitor;

public class FlickrIntegration {

	public static void main(String[] args) throws Exception {
		backup();
	}

	public static void lsDir() throws Exception {
		FileSystem fs = FileSystems.getFileSystem(new URI("flickr://einstand"));
		for (Path p : fs.provider().newDirectoryStream(fs.getPath("/2011/01"), null)) {
			System.out.println(p.toString());
		}
	}
	
	public static void upload() throws Exception {
		FileSystem fs = FileSystems.getFileSystem(new URI("flickr://einstand"));
		Path from = FileSystems.getDefault().getPath("/tmp/test.jpg");
		Files.copy(from, fs.getPath("/asd.jpg"));
	}
	
	public static void uploadToSet() throws Exception {
		FileSystem fs = FileSystems.getFileSystem(new URI("flickr://einstand"));
		Path from = FileSystems.getDefault().getPath("/tmp/test.jpg");
		Files.copy(from, fs.getPath("/2012/04/asd.jpg"));
	}
	
	public static void backup() throws Exception {
		FileSystem fs = FileSystems.getFileSystem(new URI("flickr://einstand"));
		Path from = FileSystems.getDefault().getPath("/tmp/imgtest");
		Files.walkFileTree(from, new RecursiveCopyVisitor(from, fs.getPath("/")));
	}
}
