package net.anzix.jfs.cli.command;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Path;

public class Ls implements Command {

	@Override
	public void execute(CliContext context, PrintWriter output) {
		Path p = context.getCurrentPath();
		try {
			DirectoryStream<Path> dir = p.getFileSystem().provider()
					.newDirectoryStream(p, new Filter<Path>() {

						@Override
						public boolean accept(Path entry) throws IOException {
							return true;
						}

					});
			for (Path item : dir) {
				output.write(item.getFileName() + "\n");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);

		}

	}

	@Override
	public String getName() {
		return "ls";
	}

}
