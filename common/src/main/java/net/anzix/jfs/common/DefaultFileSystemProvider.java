package net.anzix.jfs.common;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;

public abstract class DefaultFileSystemProvider<PATH, FILESYSTEM extends BaseFileSystem>
		extends FileSystemProvider {

	Class<FILESYSTEM> fsClass;

	// @Override
	// public FileSystem newFileSystem(URI uri, Map<String, ?> env)
	// throws IOException {
	// // TODO Auto-generated method stub
	// return null;
	// }

	public DefaultFileSystemProvider(Class<FILESYSTEM> fsClass) {
		super();
		this.fsClass = fsClass;
	}

	@Override
	public FileSystem getFileSystem(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path getPath(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SeekableByteChannel newByteChannel(Path dir,
			Set<? extends OpenOption> options, FileAttribute<?>... attrs)
			throws IOException {
		if (fsClass.isAssignableFrom(dir.getFileSystem().getClass())) {
			return ((FILESYSTEM) dir.getFileSystem()).newByteChannel(dir,options, attrs);
		} else {
			throw new IllegalArgumentException("Invalid file system type: "
					+ dir.getFileSystem().getClass());
		}
	}

	@Override
	public DirectoryStream<Path> newDirectoryStream(Path dir,
			Filter<? super Path> filter) throws IOException {
		if (fsClass.isAssignableFrom(dir.getFileSystem().getClass())) {
			return ((FILESYSTEM) dir.getFileSystem()).newDirectoryStream(dir,filter);
		} else {
			throw new IllegalArgumentException("Invalid file system type: "
					+ dir.getFileSystem().getClass());
		}
	}

	
	@Override
	public void createDirectory(Path dir, FileAttribute<?>... attrs)
			throws IOException {
		if (fsClass.isAssignableFrom(dir.getFileSystem().getClass())) {
			((FILESYSTEM) dir.getFileSystem()).createDirectory(dir, attrs);
		} else {
			throw new IllegalArgumentException("Invalid file system type: "
					+ dir.getFileSystem().getClass());
		}

	}

	@Override
	public void delete(Path path) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void copy(Path source, Path target, CopyOption... options)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void move(Path source, Path target, CopyOption... options)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSameFile(Path path, Path path2) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHidden(Path path) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public FileStore getFileStore(Path path) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void checkAccess(Path path, AccessMode... modes) throws IOException {
		if (fsClass.isAssignableFrom(path.getFileSystem().getClass())) {
			((FILESYSTEM) path.getFileSystem()).checkAccess(path, modes);
		} else {
			throw new IllegalArgumentException("Invalid file system type: "
					+ path.getFileSystem().getClass());
		}

	}

	@Override
	public <V extends FileAttributeView> V getFileAttributeView(Path path,
			Class<V> type, LinkOption... options) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <A extends BasicFileAttributes> A readAttributes(Path path,
			Class<A> type, LinkOption... options) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> readAttributes(Path path, String attributes,
			LinkOption... options) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttribute(Path path, String attribute, Object value,
			LinkOption... options) throws IOException {
		// TODO Auto-generated method stub

	}

}
