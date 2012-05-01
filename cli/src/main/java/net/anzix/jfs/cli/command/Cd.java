package net.anzix.jfs.cli.command;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.Argument;

public class Cd implements Command {
	@Argument
	private String dir;

	@Override
	public void execute(CliContext context, PrintWriter output) {
		Path p = context.getCurrentPath();
		Path to = p.resolve(dir).normalize();

		if (!Files.isDirectory(to)) {
			output.println("Error: " + to + " is not a directory");
		} else {
			context.setCurrentPath(to);
		}

	}

	@Override
	public String getName() {
		return "cd";
	}

}
