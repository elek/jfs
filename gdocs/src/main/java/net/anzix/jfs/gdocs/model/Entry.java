package net.anzix.jfs.gdocs.model;

import java.util.ArrayList;
import java.util.List;

import net.anzix.jfs.gdocs.GDocs;

import com.google.api.client.util.Key;

public class Entry {

	@Key("@gd:etag")
	public String etag;

	@Key("gd:resourceId")
	public String resourceId;

	@Key("link")
	public List<Link> links = new ArrayList<Link>();

	@Key("category")
	public List<Category> categories = new ArrayList<Category>();

	@Key
	public String summary;

	@Key
	public Content content;

	@Key
	public String title;

	@Key
	public String updated;

	public Link getLink(String type) {
		for (Link link : links) {
			if (type.equals(link.rel)) {
				return link;
			}
		}
		return null;
	}

	public Link getFirstParent() {
		return getLink(GDocs.PARENT);
	}

	public String getType() {
		for (Category category : categories) {
			if (GDocs.KIND.equals(category.scheme)) {
				return category.label;
			}
		}
		return "unknown";

	}
}
