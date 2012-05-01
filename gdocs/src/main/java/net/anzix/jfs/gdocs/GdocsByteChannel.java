package net.anzix.jfs.gdocs;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

public class GdocsByteChannel implements SeekableByteChannel{
	
	private byte[] buffer = new byte[1024];
	
	private InputStream is;
	
	int pos = -1;
	
	private long size;
	
	public GdocsByteChannel(InputStream is, long size) {
		super();
		this.is = is;
		this.size = size;
	}

	public GdocsByteChannel(InputStream is) {
		super();
		this.is = is;
	}

	@Override
	public boolean isOpen() {
		return true;
	}

	@Override
	public void close() throws IOException {
		is.close();
		
		
	}

	@Override
	public int read(ByteBuffer dst) throws IOException {
		int b = is.read(buffer);
		for (int i=0;i<b;i++){
			dst.put(buffer[i]);
		}
		pos += b;
		return b;
	}

	@Override
	public int write(ByteBuffer src) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long position() throws IOException {
		return pos;
	}

	@Override
	public SeekableByteChannel position(long newPosition) throws IOException {		
		return null;
	}

	@Override
	public long size() throws IOException {
		return size;
	}

	@Override
	public SeekableByteChannel truncate(long size) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
