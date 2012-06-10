package net.anzix.jfs.gdocs;

import net.anzix.jfs.common.DefaultFileSystemProvider;
import net.anzix.jfs.common.DefaultPath;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.util.Map;
import java.util.Set;

public class GDocsFileSystemProvider extends
		DefaultFileSystemProvider<DefaultPath, GDocsFileSystem> {

	public GDocsFileSystemProvider() {
		super(GDocsFileSystem.class);
	}

	@Override
	public String getScheme() {
		return "gdocs";
	}

	@Override
	public FileSystem newFileSystem(URI uri, Map<String, ?> env)
			throws IOException {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public FileSystem getFileSystem(URI uri) {
		return new GDocsFileSystem(this, uri);
	}

	@Override
	public Path getPath(URI uri) {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public SeekableByteChannel newByteChannel(Path path,
			Set<? extends OpenOption> options, FileAttribute<?>... attrs)
			throws IOException {
		return ((GDocsFileSystem) path.getFileSystem()).newByteChannel(path,
				options, attrs);
	}


	@Override
	public void delete(Path path) throws IOException {

	}

	@Override
	public void copy(Path source, Path target, CopyOption... options)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void move(Path source, Path target, CopyOption... options)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSameFile(Path path, Path path2) throws IOException {
		return (path.equals(path2));
	}

	@Override
	public boolean isHidden(Path path) throws IOException {
		return false;
	}

	@Override
	public FileStore getFileStore(Path path) throws IOException {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public void checkAccess(Path path, AccessMode... modes) throws IOException {
		if (path.getFileSystem() instanceof GDocsFileSystem) {
			((GDocsFileSystem) path.getFileSystem()).checkAccess(path, modes);
		}
	}

	@Override
	public <V extends FileAttributeView> V getFileAttributeView(Path path,
			Class<V> type, LinkOption... options) {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public OutputStream newOutputStream(Path path, OpenOption... options)
			throws IOException {
		if (path.getFileSystem() instanceof GDocsFileSystem) {
			return ((GDocsFileSystem) path.getFileSystem()).newOutputStream(
					path, options);
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public <A extends BasicFileAttributes> A readAttributes(Path path,
			Class<A> type, LinkOption... options) throws IOException {

		if (path.getFileSystem() instanceof GDocsFileSystem) {
			return ((GDocsFileSystem) path.getFileSystem()).readAttributes(
					path, type, options);
		} else {
			return null;
		}

	}

	@Override
	public Map<String, Object> readAttributes(Path path, String attributes,
			LinkOption... options) throws IOException {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public void setAttribute(Path path, String attribute, Object value,
			LinkOption... options) throws IOException {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

}
