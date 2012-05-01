package net.anzix.jfs.gdocs;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import net.anzix.RecursiveCopyVisitor;

import org.junit.Test;

public class GDocsIntegrationTestBench {

	// @Test
	public void mkdir() throws Exception {
		Path p = FileSystems.getFileSystem(new URI("gdocs://docs.google.com"))
				.getPath("/proba"); //
		Files.createDirectory(p);
		Files.createDirectory(p.resolve("qwe"));
		for (Path xp : p.relativize(p)) {

		}

	}

	// @Test
	public void mkdirs() throws Exception {
		Path p = FileSystems.getFileSystem(new URI("gdocs://docs.google.com"))
				.getPath("/proba/asd/qwe"); //
		Files.createDirectories(p);

	}

	// @Test
	public void download() throws Exception {
		Path from = FileSystems.getFileSystem(
				new URI("gdocs://docs.google.com")).getPath(
				"/archiv/auto/auto_fek_szamla.pdf");
		Path to = FileSystems.getDefault().getPath("/tmp")
				.resolve(from.getRoot().relativize(from).toString());
		Files.createDirectories(to.getParent());
		Files.copy(from, to);

	}

	// @Test
	public void upload() throws Exception {
		Path to = FileSystems.getFileSystem(new URI("gdocs://docs.google.com"))
				.getPath("/proba/test.pdf");

		Path from = FileSystems.getDefault().getPath(
				"/tmp/qwe/Report_000955.pdf");

		Files.copy(from, to);

	}

	@Test
	public void uploadRecursive() throws Exception {
		Path to = FileSystems.getFileSystem(new URI("gdocs://docs.google.com"))
				.getPath("/archiv2");

		Path from = FileSystems.getDefault().getPath("/storage/IRATOK/sanagate");

		Files.walkFileTree(from, new RecursiveCopyVisitor(from, to));

	}
}
