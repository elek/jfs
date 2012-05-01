package net.anzix;

import java.io.File;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 * 
 */
public class App {
	public void copy() throws Exception {

		createSample();

		final Path orig = FileSystems.getDefault().getPath("/tmp/jfstest");

		final Path dest = FileSystems.getDefault().getPath("/tmp/dest");

		Files.walkFileTree(orig, new RecursiveCopyVisitor(orig, dest));

	}

	public static void main(String[] args) throws Exception {
		createDir();
	}

	private static void download() throws Exception {
		Path p = FileSystems.getFileSystem(new URI("gdocs://docs.google.com"))
				.getPath("/archiv/auto/auto_fek_szamla.pdf");
		// Path p =
		// FileSystems.getDefault().getPath("/archiv/migros_cumulus_karya.pdf");
		Path to = FileSystems.getDefault().getPath("/tmp")
				.resolve(p.getRoot().relativize(p).toString());

		Files.createDirectories(to.getParent());
		Files.copy(p, to);

	}
	
	private static void upload() throws Exception {
		Path p = FileSystems.getFileSystem(new URI("gdocs://docs.google.com"))
				.getPath("/test.pdf");
		// Path p =
		// FileSystems.getDefault().getPath("/archiv/migros_cumulus_karya.pdf");
		Path from = FileSystems.getDefault().getPath("/storage/IRATOK/INBOX/Report_000997.pdf");
				
		//Files.createDirectories(to.getParent());
		Files.copy(from,p);

	}
	
	private static void createDir() throws Exception {
		
		Path p = FileSystems.getFileSystem(new URI("gdocs://docs.google.com"))
				.getPath("/proba");
		// Path p =
		// FileSystems.getDefault().getPath("/archiv/migros_cumulus_karya.pdf");
		//Path from = FileSystems.getDefault().getPath("/storage/IRATOK/INBOX/Report_000997.pdf");
				
		//Files.createDirectories(to.getParent());
		Files.createDirectories(p);

	}



	public static void main2(String[] args) throws Exception {
		// Path p = FileSystems.getDefault().getPath("/");
		// Path p = FileSystems.getFileSystem(new
		// URI("dummy://test/")).getPath("/");
		Path p = FileSystems.getFileSystem(new URI("gdocs://docs.google.com"))
				.getPath("/archiv");
		DirectoryStream<Path> paths = Files.newDirectoryStream(p);
		for (Path pp : paths) {
			System.out.println(pp.toString());
			System.out.println(pp.getFileName().toAbsolutePath());
		}
	}

	public static void downloadRecursive() throws Exception {

		Path orig = FileSystems.getFileSystem(
				new URI("gdocs://docs.google.com")).getPath("/Esküvő");

		Path dest = FileSystems.getDefault().getPath("/tmp/dest2");

		Files.walkFileTree(orig, new RecursiveCopyVisitor(orig, dest));

	}

	private static void createSample() throws Exception {
		Path p = FileSystems.getDefault().getPath("/tmp/jfstest");
		if (!Files.exists(p)) {
			Files.createDirectory(p);
		}
		List<String> s = new ArrayList<String>();
		s.add("test");
		s.add("test2");
		Files.write(p.resolve("test.txt"), s, Charset.defaultCharset());
		Files.write(p.resolve("test2.txt"),
				Arrays.asList(new String[] { "asd" }), Charset.defaultCharset());
	}
}