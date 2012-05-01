package net.anzix.jfs.gdocs.model;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.util.Key;

public class Feed {

	@Key("openSearch:totalResults")
	public int totalResults;

	@Key("link")
	public List<Link> links;

	@Key("entry")
	public List<Entry> docs = new ArrayList<Entry>();

	public Link getLink(String type) {
		for (Link link : links) {
			if (type.equals(link.rel)) {
				return link;
			}
		}
		return null;
	}

}
