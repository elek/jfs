package net.anzix.jfs.gdocs.model;

import com.google.api.client.util.Key;

public class Content {
	@Key("@type")
	public String type;

	@Key("@src")
	public String src;
}
