package net.anzix.jfs.dummy;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.anzix.jfs.common.CollectionDirectoryStream;
import net.anzix.jfs.common.DefaultFileAttributes;
import net.anzix.jfs.common.DefaultPath;

public class DummyFileSystemProvider extends FileSystemProvider {
	public DummyFileSystemProvider() {

	}

	@Override
	public String getScheme() {
		return "dummy";
	}

	@Override
	public FileSystem newFileSystem(URI uri, Map<String, ?> stringMap)
			throws IOException {
		return new DummyFileSystem(this);
	}

	@Override
	public FileSystem getFileSystem(URI uri) {
		return new DummyFileSystem(this);
	}

	@Override
	public Path getPath(URI uri) {
		return null;
	}

	@Override
	public SeekableByteChannel newByteChannel(Path paths,
			Set<? extends OpenOption> openOptions,
			FileAttribute<?>... fileAttributes) throws IOException {
		return new SeekableByteChannel() {
			long pos = 0;

			@Override
			public boolean isOpen() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public void close() throws IOException {
				// TODO Auto-generated method stub

			}

			@Override
			public int write(ByteBuffer src) throws IOException {
				return 0;
			}

			@Override
			public SeekableByteChannel truncate(long size) throws IOException {
				return this;
			}

			@Override
			public long size() throws IOException {
				return 10;
			}

			@Override
			public int read(ByteBuffer dst) throws IOException {
				if (pos < size()) {
					dst.put((byte) 60);
					pos++;
					return 1;
				} else {
					return -1;
				}

			}

			@Override
			public SeekableByteChannel position(long newPosition)
					throws IOException {
				this.pos = newPosition;
				return this;
			}

			@Override
			public long position() throws IOException {

				return pos;
			}
		};
	}

	@Override
	public DirectoryStream<Path> newDirectoryStream(Path paths,
			DirectoryStream.Filter<? super Path> filter) throws IOException {
		if (paths.toString().equals("/")) {
			List<Path> p = new ArrayList<Path>();
			p.add(new DefaultPath(paths.getFileSystem(), "test1.txt"));
			p.add(new DefaultPath(paths.getFileSystem(), "test3.txt"));
			return new CollectionDirectoryStream(p);
		} else {
			return new CollectionDirectoryStream(new ArrayList<Path>());
		}
	}

	@Override
	public void createDirectory(Path paths, FileAttribute<?>... fileAttributes)
			throws IOException {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public void delete(Path paths) throws IOException {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public void copy(Path paths, Path paths1, CopyOption... copyOptions)
			throws IOException {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public void move(Path paths, Path paths1, CopyOption... copyOptions)
			throws IOException {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public boolean isSameFile(Path paths, Path paths1) throws IOException {
		return false; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public boolean isHidden(Path paths) throws IOException {
		return false; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public FileStore getFileStore(Path paths) throws IOException {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public void checkAccess(Path paths, AccessMode... accessModes)
			throws IOException {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public <V extends FileAttributeView> V getFileAttributeView(Path paths,
			Class<V> vClass, LinkOption... linkOptions) {
		if (paths.toString().equals("/") || paths.toString().endsWith("/")) {
			return (V) new DefaultFileAttributes(true);
		} else {
			return (V) new DefaultFileAttributes(false);
		}
	}

	@Override
	public <A extends BasicFileAttributes> A readAttributes(Path paths,
			Class<A> aClass, LinkOption... linkOptions) throws IOException {
		if (aClass.equals(BasicFileAttributes.class)) {
			if (paths.toString().equals("/") || paths.toString().endsWith("/")) {
				return (A) new DefaultFileAttributes(true);
			} else {
				return (A) new DefaultFileAttributes(false);
			}
		} else {
			return null;
		}
	}

	@Override
	public Map<String, Object> readAttributes(Path paths, String s,
			LinkOption... linkOptions) throws IOException {
		return new HashMap<String, Object>();
	}

	@Override
	public void setAttribute(Path paths, String s, Object o,
			LinkOption... linkOptions) throws IOException {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}
}
