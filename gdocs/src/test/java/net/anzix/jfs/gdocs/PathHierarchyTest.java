package net.anzix.jfs.gdocs;

import junit.framework.Assert;
import net.anzix.jfs.common.DefaultPath;
import net.anzix.jfs.gdocs.model.Category;
import net.anzix.jfs.gdocs.model.Entry;
import net.anzix.jfs.gdocs.model.Feed;
import net.anzix.jfs.gdocs.model.Link;

import org.junit.Test;

public class PathHierarchyTest {

	@Test
	public void loadHierarchy() {
		Feed f = new Feed();

		Entry e = new Entry();
		e.etag = "qweqwe123";
		e.resourceId = "file:14asdf79087897098709870987098";
		e.title = "file1";
		e.links.add(new Link(
				"http://schemas.google.com/docs/2007#parent",
				"https://docs.google.com/feeds/default/private/full/folder%3Aroot",
				"My Drive"));
		f.docs.add(e);

		e = new Entry();
		e.etag = "qweqwe1345";
		e.resourceId = "file:3948579879s8d7f98s7df987";
		e.title = "file2";
		e.links.add(new Link(
				"http://schemas.google.com/docs/2007#parent",
				"https://docs.google.com/feeds/default/private/full/folder%3Aroot",
				"My Drive"));
		f.docs.add(e);

		// file in the subfolder
		e = new Entry();
		e.etag = "werwe789";
		e.resourceId = "file:23947897sdfsfd";
		e.title = "file3";
		e.links.add(new Link(
				"http://schemas.google.com/docs/2007#parent",
				"https://docs.google.com/feeds/default/private/full/folder%3A87s98fjksdhfsd87f98s7",
				"folder1"));
		f.docs.add(e);

		// second folder
		e = new Entry();
		e.etag = "sdfjkh8798789";
		e.resourceId = "folder:87s98fjksdhfsd87f98s7";
		e.title = "folder1";
		e.categories.add(new Category("http://schemas.google.com/g/2005#kind",
				"folder"));
		e.links.add(new Link(
				"http://schemas.google.com/docs/2007#parent",
				"https://docs.google.com/feeds/default/private/full/folder%3Aroot",
				"My Drive"));
		f.docs.add(e);

		GDocsFileSystem fs = new GDocsFileSystem();
		
		PathHierarchy s = new PathHierarchy(fs.getRoot());
		s.cleanHierarchy();
		s.parseFeed(f);

		
		// only on folder
		Assert.assertEquals(2, s.hierarchy.size());
		Assert.assertNotNull(s.hierarchy.get(new DefaultPath(fs, "/")));
		Assert.assertNotNull("No child hierarchy for folder1",
				s.hierarchy.get(new DefaultPath(fs, "/folder1")));

		// 1 entry in folder1
		Assert.assertEquals(1, s.hierarchy.get(new DefaultPath(fs, "/folder1"))
				.size());

		// 3 entry in the root folder
		Assert.assertEquals(3, s.hierarchy.get(new DefaultPath(fs, "/")).size());
		

	}
}
