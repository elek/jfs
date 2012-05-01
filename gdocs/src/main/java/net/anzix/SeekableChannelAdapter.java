package net.anzix;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;

public class SeekableChannelAdapter implements SeekableByteChannel {
	private Channel delegate;
	private long pos;

	public SeekableChannelAdapter(Channel delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public boolean isOpen() {
		return delegate.isOpen();
	}

	@Override
	public void close() throws IOException {
		delegate.close();

	}

	@Override
	public int read(ByteBuffer dst) throws IOException {
		if (delegate instanceof ByteChannel) {
			int r = ((ByteChannel) delegate).read(dst);
			pos += r;
			return r;
		} else if (delegate instanceof ReadableByteChannel) {
			int r = ((ReadableByteChannel) delegate).read(dst);
			pos += r;
			return r;
		} else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public int write(ByteBuffer src) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public long position() throws IOException {
		return pos;
	}

	@Override
	public SeekableByteChannel position(long newPosition) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public long size() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public SeekableByteChannel truncate(long size) throws IOException {
		throw new UnsupportedOperationException();
	}

}
