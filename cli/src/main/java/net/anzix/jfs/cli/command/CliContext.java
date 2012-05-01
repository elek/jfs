package net.anzix.jfs.cli.command;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import jline.console.ConsoleReader;

public class CliContext {
	private ConsoleReader reader;
	private Path currentPath = FileSystems.getDefault().getRootDirectories().iterator().next();

	public CliContext(ConsoleReader reader) {
		super();
		this.reader = reader;
	}

	public ConsoleReader getReader() {
		return reader;
	}

	public Path getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(Path currentPath) {
		this.currentPath = currentPath;
		reader.setPrompt(currentPath.toString() + ">");
	}

}
