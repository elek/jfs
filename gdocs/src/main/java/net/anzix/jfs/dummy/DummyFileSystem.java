package net.anzix.jfs.dummy;

import net.anzix.jfs.common.DefaultPath;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DummyFileSystem extends FileSystem {
	private DummyFileSystemProvider provider;

	public DummyFileSystem(DummyFileSystemProvider provider) {
		this.provider = provider;
	}

	@Override
	public FileSystemProvider provider() {
		return provider;
	}

	@Override
	public void close() throws IOException {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public boolean isOpen() {
		return true;
	}

	@Override
	public boolean isReadOnly() {
		return false; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public String getSeparator() {
		return "/";
	}

	@Override
	public Iterable<Path> getRootDirectories() {
		List<Path> roots = new ArrayList();
		roots.add(new DefaultPath(this, "/"));
		return roots;
	}

	@Override
	public Iterable<FileStore> getFileStores() {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public Set<String> supportedFileAttributeViews() {
		return new HashSet<String>();
	}

	@Override
	public Path getPath(String first, String... more) {
		if (first.equals("/"))
			return new DefaultPath(this, "/");
		String[] pathElements = new String[more.length + 1];
		pathElements[0] = first;
		for (int i = 0; i < more.length; i++) {
			pathElements[i + 1] = more[i];
		}
		return new DefaultPath(this, true, pathElements);
	}

	@Override
	public PathMatcher getPathMatcher(String s) {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public UserPrincipalLookupService getUserPrincipalLookupService() {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public WatchService newWatchService() throws IOException {
		throw new UnsupportedOperationException();
	}
}
