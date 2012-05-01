package net.anzix.jfs.cli;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jline.console.ConsoleReader;
import net.anzix.jfs.cli.command.Cd;
import net.anzix.jfs.cli.command.CliContext;
import net.anzix.jfs.cli.command.Command;
import net.anzix.jfs.cli.command.Ls;
import net.anzix.jfs.cli.command.Open;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class Cli {
	Path currentPath;

	private Map<String, Command> commands = new HashMap<String, Command>();

	public Cli() {
		addCommand(Open.class);
		addCommand(Cd.class);
		addCommand(Ls.class);
	}

	public static void main(String[] args) throws Exception {
		new Cli().start();

	}

	private void addCommand(Class<? extends Command> commandClass) {
		try {
			Command c = commandClass.newInstance();
			commands.put(c.getName(), c);
		} catch (Exception e) {
			throw new AssertionError(e);
		}

	}

	private void start() throws Exception {
		ConsoleReader r = new ConsoleReader();
		r.setPrompt("jfs>");
		PrintWriter out = new PrintWriter(r.getOutput());
		String line;
		CliContext context = new CliContext(r);
		while ((line = r.readLine()) != null) {
			try {
				String input = line.trim();
				String cmdName = input.indexOf(' ') > 0 ? input.substring(0,
						input.indexOf(' ')) : input;
				String args[] = cmdName.length() == input.length() ? new String[0]
						: (input.substring(cmdName.length() + 1).trim()
								.split(" "));
				Command c = commands.get(cmdName);
				if (cmdName.equals("exit")) {
					break;
				}
				if (c != null) {
					CmdLineParser p = new CmdLineParser(c);
					p.parseArgument(args);
					c.execute(context, out);
				} else {
					out.println("Bad command: " + cmdName);
				}
			} catch (CmdLineException ex) {
				ex.printStackTrace(out);
			}
		}

	}
}
