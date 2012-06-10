package net.anzix.jfs.common;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Arrays;
import java.util.Iterator;

public class DefaultPath implements Path {

	private FileSystem fileSystem;

	private String[] pathSegments;

	private boolean absolute = true;

	public DefaultPath(FileSystem fileSystem, String path) {
		String s = path;
		absolute = false;
		if (path.charAt(0) == '/') {
			s = path.substring(1);
			absolute = true;
		}
		this.fileSystem = fileSystem;
		if (s.length() == 0) {
			this.pathSegments = new String[0];
		} else {
			this.pathSegments = s.split("/");
		}

	}

	public DefaultPath(FileSystem fileSystem, boolean absolute,
			String[] pathSegments) {
		this.fileSystem = fileSystem;
		this.pathSegments = pathSegments;
		this.absolute = absolute;
	}

	public DefaultPath(DefaultPath parentPath, String plainText) {
		this.fileSystem = parentPath.getFileSystem();
		this.pathSegments = Arrays.copyOf(parentPath.pathSegments,
				parentPath.pathSegments.length + 1);
		this.pathSegments[this.pathSegments.length - 1] = plainText;
	}

	@Override
	public FileSystem getFileSystem() {
		return fileSystem;
	}

	@Override
	public boolean isAbsolute() {
		return absolute;
	}

	@Override
	public Path getRoot() {
		return new DefaultPath(fileSystem, "/");
	}

	@Override
	public Path getFileName() {
		if (pathSegments.length > 0) {
			return new DefaultPath(fileSystem, false,
					new String[] { pathSegments[pathSegments.length - 1] });
		} else {
			return this;
		}

	}

	@Override
	public DefaultPath getParent() {
		return new DefaultPath(fileSystem, absolute, Arrays.copyOf(
				pathSegments, pathSegments.length - 1));
	}

	@Override
	public int getNameCount() {
		return pathSegments.length;
	}

	@Override
	public Path getName(int index) {
		return null;
	}

	@Override
	public Path subpath(int beginIndex, int endIndex) {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public boolean startsWith(Path other) {
		return false; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public boolean startsWith(String other) {
		return false; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public boolean endsWith(Path other) {
		return false; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public boolean endsWith(String other) {
		return false; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public Path normalize() {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public Path resolve(Path other) {
		return resolve(((DefaultPath) other).pathSegments);
	}

	public Path resolve(String[] segments) {

		if (segments.length == 1
				&& (segments[0].equals(".") || segments[0].equals(""))) {
			return new DefaultPath(getFileSystem(), absolute, pathSegments);
		}

		int start = 0;
		while (segments.length > start && segments[start].equals("..")) {
			start++;
		}

		String[] np = new String[pathSegments.length - start + segments.length
				- start];
		for (int i = 0; i < pathSegments.length - start; i++) {
			np[i] = pathSegments[i];
		}
		for (int i = pathSegments.length - start; i < np.length; i++) {
			np[i] = segments[i - pathSegments.length + start + start];
		}
		return new DefaultPath(getFileSystem(), absolute, np);

	}

	@Override
	public Path resolve(String other) {
		return resolve(other.split("/"));
	}

	@Override
	public Path resolveSibling(Path other) {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public Path resolveSibling(String other) {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public Path relativize(Path other) {
		if (pathSegments.length == 0 && absolute) {
			return new DefaultPath(fileSystem, false,
					((DefaultPath) other).pathSegments);
		}
		int c = Math.min(getNameCount(), other.getNameCount());
		DefaultPath o = new DefaultPath(getFileSystem(), other.toString());
		int e = 0;
		while (e < c) {
			if (!o.pathSegments[e].equals(pathSegments[e])) {
				break;
			}
			e++;
		}
		int size = (this.pathSegments.length - e) + (o.pathSegments.length - e);
		String[] p = new String[size];
		for (int i = 0; i < this.pathSegments.length - e; i++) {
			p[i] = "..";
		}
		for (int i = 0; i < o.pathSegments.length - e; i++) {
			p[this.pathSegments.length - e + i] = o.pathSegments[i + e];
		}
		return new DefaultPath(getFileSystem(), false, p);
	}

	@Override
	public URI toUri() {
		try {
			return new URI(fileSystem.provider().getScheme() + "://"
					+ getFileName());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Path toAbsolutePath() {
		return this;
	}

	@Override
	public Path toRealPath(LinkOption... options) throws IOException {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public File toFile() {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events,
			WatchEvent.Modifier... modifiers) throws IOException {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public WatchKey register(WatchService watcher, WatchEvent.Kind<?>... events)
			throws IOException {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public Iterator<Path> iterator() {
		return new Iterator<Path>() {
			int idx = 0;

			@Override
			public boolean hasNext() {
				return pathSegments.length > idx;
			}

			@Override
			public Path next() {
				return new DefaultPath(fileSystem, false,
						new String[] { pathSegments[idx++] });
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();

			}

		};
	}

	@Override
	public int compareTo(Path other) {
		return -1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (absolute ? 1231 : 1237);
		result = prime * result + Arrays.hashCode(pathSegments);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultPath other = (DefaultPath) obj;
		if (absolute != other.absolute)
			return false;
		if (!Arrays.equals(pathSegments, other.pathSegments))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String s = "";
		for (String path : pathSegments) {
			if (s.length() == 0 && !absolute) {
				s += path;
			} else {
				s += "/" + path;
			}
		}
		if (s.length() == 0) {
			if (absolute) {
				return "/";
			} else {
				return ".";
			}
		} else
			return s;
	}

}
