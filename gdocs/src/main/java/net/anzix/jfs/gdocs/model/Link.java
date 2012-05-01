package net.anzix.jfs.gdocs.model;

import java.util.List;

import com.google.api.client.util.Key;

public class Link {

	private static final String ID_PREFIX = "https://docs.google.com/feeds/default/private/full/";

	@Key("@href")
	public String href;

	@Key("@rel")
	public String rel;

	@Key("@title")
	public String title;

	public Link() {

	}

	public Link(String string, String string2, String string3) {
		this.rel = string;
		this.href = string2;
		this.title = string3;
	}

	public static String find(List<Link> links, String rel) {
		if (links != null) {
			for (Link link : links) {
				if (rel.equals(link.rel)) {
					return link.href;
				}
			}
		}
		return null;
	}

	public String toId() {
		if (!href.startsWith(ID_PREFIX)) {
			throw new IllegalArgumentException("Href doesn't start with a "
					+ ID_PREFIX);
		}
		return href.substring(ID_PREFIX.length()).replace("%3A", ":");
	}
}
