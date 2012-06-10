package net.anzix.jfs.common;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class BaseFileSystem extends FileSystem {

	protected final FileSystemProvider provider;

	public BaseFileSystem(FileSystemProvider provider) {
		super();
		this.provider = provider;
	}

	@Override
	public FileSystemProvider provider() {
		return provider;
	}

	@Override
	public Iterable<Path> getRootDirectories() {
		List<Path> result = new ArrayList<Path>();
		result.add(getRoot());
		return result;
	}

	@Override
	public Iterable<FileStore> getFileStores() {
		return null;

	}

	@Override
	public Set<String> supportedFileAttributeViews() {
		return new HashSet<String>();
	}

	@Override
	public Path getPath(String first, String... more) {
		StringBuilder b = new StringBuilder();
		b.append(first);
		for (String e : more) {
			if (e.charAt(0) != '/' && !b.toString().equals("/")) {
				e = "/" + e;
			}
			if (e.charAt(e.length() - 1) == '/') {
				e = e.substring(0, e.length() - 1);
			}
			b.append(e.trim());
		}
		return new DefaultPath(this, b.toString());
	}

	@Override
	public PathMatcher getPathMatcher(String syntaxAndPattern) {
		return null;

	}

	@Override
	public UserPrincipalLookupService getUserPrincipalLookupService() {
		return null;

	}

	@Override
	public WatchService newWatchService() throws IOException {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	public Path getRoot() {
		DefaultPath p = new DefaultPath(this, "/");
		return p;
	}

	@Override
	public String getSeparator() {
		return "/";
	}

	public abstract SeekableByteChannel newByteChannel(Path dir,
			Set<? extends OpenOption> options, FileAttribute<?>... attrs)
			throws IOException;

	public abstract void checkAccess(Path path, AccessMode... modes)
			throws IOException;

	public abstract DirectoryStream<Path> newDirectoryStream(Path dir,
			Filter<? super Path> filter) throws IOException;

	public abstract void createDirectory(Path dir, FileAttribute<?>... attrs)
			throws IOException;

}
