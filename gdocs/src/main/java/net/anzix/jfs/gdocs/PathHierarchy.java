package net.anzix.jfs.gdocs;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import net.anzix.jfs.common.DefaultFileAttributes;
import net.anzix.jfs.common.DefaultPath;
import net.anzix.jfs.gdocs.model.Category;
import net.anzix.jfs.gdocs.model.Entry;
import net.anzix.jfs.gdocs.model.Feed;

public class PathHierarchy {
	private Path root;

	/**
	 * resourceId -> original documentListEntry
	 */
	Map<String, Entry> entriesByResourceId = new HashMap<String, Entry>();

	/**
	 * Path => attributes (including original entry)
	 */
	Map<DefaultPath, GDocsFileAttributes> attribsByPath = new HashMap<DefaultPath, GDocsFileAttributes>();

	/**
	 * resourceId -> path
	 */
	Map<String, DefaultPath> pathesByResourceId = new HashMap<String, DefaultPath>();

	/**
	 * Hierarchy parent -> child
	 */
	Map<Path, List<DefaultPath>> hierarchy;

	public PathHierarchy(Path root) {
		super();
		this.root = root;
	}

	public void addElement(Entry e) {
		if (e.getFirstParent() != null) {
			DefaultPath parentPath = getPathObject(e.getFirstParent().toId());
			DefaultPath p = getPathObject(e.resourceId);
			addChildToHierarchy(parentPath, p);

		} else {
			addChildToHierarchy((DefaultPath) root, getPathObject(e.resourceId));
		}

	}

	private DefaultPath getPathObject(String resourceId) {
		DefaultPath path = pathesByResourceId.get(resourceId);
		if (path == null) {
			// Do we have an unparsed entry for this?
			Entry current = entriesByResourceId.get(resourceId);
			if (current == null) {
				throw new IllegalArgumentException("Resource doens't exist: "
						+ resourceId);
			}

			String parentResourceId = current.getFirstParent() != null ? current
					.getFirstParent().toId() : "folder:root";

			path = new DefaultPath(getPathObject(parentResourceId),
					current.title);

			attribsByPath.put(path, new GDocsFileAttributes(current));
			pathesByResourceId.put(resourceId, path);

		}
		return path;
	}

	private void addChildToHierarchy(DefaultPath root, DefaultPath child) {
		List<DefaultPath> elements = hierarchy.get(root);
		if (elements == null) {
			elements = new ArrayList<DefaultPath>();
			hierarchy.put(root, elements);
		}
		elements.add(child);
	}

	void cleanHierarchy() {
		hierarchy = new HashMap<Path, List<DefaultPath>>();

		// Pseudo entry for the root collection
		Entry e = new Entry();
		e.categories.add(new Category("http://schemas.google.com/g/2005#kind",
				"folder"));
		e.resourceId = "root";
		DefaultPath rp = new DefaultPath(root.getFileSystem(), "/");
		entriesByResourceId.put("folder:root", e);
		pathesByResourceId.put("folder:root", rp);
		hierarchy.put(root, new ArrayList<DefaultPath>());
		attribsByPath.put(rp, new GDocsFileAttributes(e));

	}

	protected void addEntry(Entry e) {
		entriesByResourceId.put(e.resourceId, e);
		addElement(e);
	}

	protected void parseFeed(Feed feed) {
		
		for (Entry e : feed.docs) {
			entriesByResourceId.put(e.resourceId, e);
		}
		for (Entry e : feed.docs) {
			addElement(e);
		}
	}

	public List<DefaultPath> get(Path dir) {
		return hierarchy.get(dir);
	}

	public GDocsFileAttributes getAttribute(DefaultPath path) {
		return attribsByPath.get(path);
	}
}
