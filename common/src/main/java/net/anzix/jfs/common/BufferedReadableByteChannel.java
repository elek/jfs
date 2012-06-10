package net.anzix.jfs.common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public abstract class BufferedReadableByteChannel implements
		SeekableByteChannel {

	boolean open = true;

	FileChannel ch;

	@Override
	public boolean isOpen() {
		return open;
	}

	@Override
	public void close() throws IOException {
		open = false;
		ch.close();

	}

	@Override
	public int read(ByteBuffer dst) throws IOException {
		if (!open) {
			throw new IllegalStateException();
		}
		if (ch == null) {
			initialize();
		}
		return ch.read(dst);
	}

	private void initialize() {
		try {
			Path tmpFile = Files.createTempFile("jfs", "buffer");
			ch = FileChannel.open(tmpFile, StandardOpenOption.WRITE);
			preloadContent(ch);
			ch.close();
			ch = FileChannel.open(tmpFile, StandardOpenOption.READ,
					StandardOpenOption.DELETE_ON_CLOSE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	protected abstract void preloadContent(FileChannel ch);

	@Override
	public int write(ByteBuffer src) throws IOException {
		if (!open) {
			throw new IllegalStateException();
		}
		if (ch == null) {
			initialize();
		}
		return ch.write(src);

	}

	@Override
	public long position() throws IOException {
		return ch.position();
	}

	@Override
	public SeekableByteChannel position(long newPosition) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public long size() throws IOException {
		return ch.size();
	}

	@Override
	public SeekableByteChannel truncate(long size) throws IOException {
		throw new UnsupportedOperationException();
	}

}
