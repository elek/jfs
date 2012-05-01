package net.anzix;

import java.io.IOException;
import java.io.OutputStream;

import com.google.api.client.http.AbstractHttpContent;

public class ByteArrayHttpContent extends AbstractHttpContent {

	private String type;
	private byte[] content;
	int len = 512 * 1024;

	public ByteArrayHttpContent(String type, byte[] content, int len) {
		super();
		this.type = type;
		this.content = content;
		this.len = len;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void writeTo(OutputStream out) throws IOException {
		System.out.println("write to");
		try {
			out.write(content, 0, len);
		} catch (Error ex) {
			ex.printStackTrace();
		}

	}

}
