package net.anzix.jfs.cli.command;

import java.io.PrintWriter;

public interface Command {
	public void execute(CliContext context, PrintWriter output);
	public String getName();
}
