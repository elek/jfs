package net.anzix.jfs.flickr;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.anzix.jfs.common.DefaultPath;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PathHierarchy {

	/**
	 * Path => attributes (including original entry)
	 */
	Map<DefaultPath, FlickrDirAttributes> attribsByPath = new HashMap<DefaultPath, FlickrDirAttributes>();

	/**
	 * resourceId -> path
	 */
	Map<String, DefaultPath> pathesById = new HashMap<String, DefaultPath>();

	/**
	 * Hierarchy parent -> child
	 */
	Map<Path, List<DefaultPath>> hierarchy = new HashMap<Path, List<DefaultPath>>();

	public PathHierarchy(DefaultPath root, Element payload) {
		for (int i = 0; i < payload.getChildNodes().getLength(); i++) {
			Node n = payload.getChildNodes().item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE)
				parse(root, (Element) n);
		}

	}

	public void parseSetList(DefaultPath path, Element payload) {
		for (int i = 0; i < payload.getChildNodes().getLength(); i++) {
			Node n = payload.getChildNodes().item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE)
				parsePhotoNode(path, (Element) n);
		}

	}

	private void parsePhotoNode(DefaultPath parent, Element n) {
		String title = n.getAttribute("title");
		String id = n.getAttribute("id");
		String secret = n.getAttribute("secret");

		DefaultPath path = (DefaultPath) parent.resolve(title);
		pathesById.put(id, path);
		addChild(parent, path);
		attribsByPath.put(path, new FlickrDirAttributes(n));

	}

	private void parse(DefaultPath parent, Element e) {

		String title = e.getAttribute("title");
		String id = e.getAttribute("id");

		DefaultPath path = (DefaultPath) parent.resolve(title);
		pathesById.put(id, path);
		addChild(parent, path);
		attribsByPath.put(path, new FlickrDirAttributes(e));
		for (int j = 0; j < e.getChildNodes().getLength(); j++) {
			if (e.getChildNodes().item(j).getNodeType() == Node.ELEMENT_NODE) {
				parse(path, (Element) e.getChildNodes().item(j));
			}

		}

	}

	private void addChild(DefaultPath parent, DefaultPath path) {
		List<DefaultPath> child = hierarchy.get(parent);
		if (child == null) {
			child = new ArrayList<DefaultPath>();
			hierarchy.put(parent, child);
		}
		child.add(path);

	}

	public List<DefaultPath> getChild(DefaultPath path) {
		return hierarchy.get(path);
	}

	public FlickrDirAttributes getAttr(DefaultPath path) {
		return attribsByPath.get(path);
	}

	public boolean exists(DefaultPath path) {
		return attribsByPath.containsKey(path);
	}

	public boolean isCollection(DefaultPath dp) throws FileNotFoundException {
		if (dp.equals(dp.getRoot())){
			return true;
		}
		if (getAttr(dp)==null){
			throw new FileNotFoundException(dp.toString());
		}
		return getAttr(dp).getType().equals("collection");
	}

}
