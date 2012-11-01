package net.anzix.jfs.cli;

import net.anzix.RecursiveCopyVisitor;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class Sync {

	@Option(name = "--delete", usage = "delete extraneous files from dest dirs")
	public boolean delete;

	@Argument(required = true, index = 2, metaVar = "destinationRoot")
	public String toRoot;

	@Argument(required = true, index = 3, metaVar = "destinationPath")
	public String toPath;

	@Argument(required = true, index = 1, metaVar = "fromPath")
	public String fromPath;

	@Argument(required = true, index = 0, metaVar = "fromRoot")
	public String fromRoot;

	public static void main(String args[]) throws Exception {
		Sync s = new Sync();
		CmdLineParser parser = new CmdLineParser(s);
		try {
			parser.parseArgument(args);
			s.start();
		} catch (CmdLineException e) {
            parser.printUsage(System.out);
			e.printStackTrace();
		}
	}

	private void start() throws Exception {
		Path f = getPathFromURI(fromRoot, fromPath);
		Path t = getPathFromURI(toRoot, toPath);
		Files.walkFileTree(f, new RecursiveCopyVisitor(f, t));

	}

	private Path getPathFromURI(String root, String path) {
		try {

			return FileSystems.getFileSystem(new URI(root)).getPath(path);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}
