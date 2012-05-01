package net.anzix.jfs.cli.command;

import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;

import org.kohsuke.args4j.Argument;

public class Open implements Command {

	@Argument
	private String uri;

	@Override
	public void execute(CliContext context, PrintWriter output) {
		if (uri == null) {
			context.setCurrentPath(FileSystems.getDefault()
					.getRootDirectories().iterator().next());
		} else {
			try {
				context.setCurrentPath(FileSystems.getFileSystem(new URI(uri)).getRootDirectories().iterator().next());
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}

	}

	@Override
	public String getName() {
		return "open";
	}

}
