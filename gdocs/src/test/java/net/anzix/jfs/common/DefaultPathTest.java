package net.anzix.jfs.common;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Test;

public class DefaultPathTest {

	@Test
	public void test() {

		DefaultPath root = new DefaultPath((FileSystem) null, "/");
		Assert.assertEquals("/", root.toString());
		DefaultPath test = new DefaultPath(root, "test");
		Assert.assertEquals("/test", test.toString());
		DefaultPath qqq = new DefaultPath(test, "qqq");
		Assert.assertEquals("/test/qqq", qqq.toString());

		Assert.assertEquals("/", test.getParent().toString());

		Iterator<Path> it = qqq.iterator();

		Assert.assertEquals("test", it.next().toString());
		Assert.assertEquals("qqq", it.next().toString());

		Assert.assertEquals("test/qqq", qqq.getRoot().relativize(qqq)
				.toString());
		it = qqq.getRoot().relativize(qqq).iterator();

		Assert.assertEquals("test", it.next().toString());
		Assert.assertEquals("qqq", it.next().toString());

	}

	@Test
	public void test2() {
		FileSystem fs = FileSystems.getDefault();
		Path child = fs.getPath("/tmp");
		for (Path name : fs.getPath("/tmp").relativize(
				fs.getPath("/tmp/test/qwe"))) {
			child = child.resolve(name);
		}
	}

	@Test
	public void resolve() {
		DefaultPath root = new DefaultPath((FileSystem) null, "/");
		DefaultPath test = new DefaultPath(root, "test");

		Assert.assertEquals(test, root.resolve("test"));
		
		Assert.assertEquals(root, test.resolve(".."));
		Assert.assertEquals("/asd", test.resolve("../asd").toString());

		Assert.assertEquals("/test/asd/qwe", test.resolve("asd/qwe").toString());
		Assert.assertEquals("/test/asd/qwe",
				test.resolve(new DefaultPath(root.getFileSystem(), "asd/qwe"))
						.toString());

		Assert.assertEquals(
				"/test",
				test.resolve(
						new DefaultPath(root.getFileSystem(), false,
								new String[] { "." })).toString());

		Assert.assertEquals("/test", test.resolve(new DefaultPath(test
				.getFileSystem(), false, new String[] { "." })).toString());
		
		Assert.assertEquals("/test", test.resolve("").toString());
		
		DefaultPath n = (DefaultPath) test.resolve("uuu/qwe");
		Assert.assertEquals("/test/uuu/qwe", n.toString());
		Assert.assertEquals(3, n.getNameCount());
		

	}

	@Test
	public void relativize() {
		DefaultPath root = new DefaultPath((FileSystem) null, "/");
		DefaultPath test = new DefaultPath(root, "test");
		DefaultPath asd = new DefaultPath(test, "asd");
		DefaultPath qwe = new DefaultPath(root, "qwe");
		Assert.assertEquals("/test", test.toString());
		Assert.assertEquals("/test/asd", asd.toString());

		Path rel = test.relativize(asd);
		Assert.assertFalse(rel.isAbsolute());
		Assert.assertEquals("asd", rel.toString());
		Assert.assertEquals("../../qwe", asd.relativize(qwe).toString());

		DefaultPath l = new DefaultPath((FileSystem) null, "/tmp/qwe");
		Assert.assertEquals(
				"ert",
				l.relativize(new DefaultPath((FileSystem) null, "/tmp/qwe/ert"))
						.toString());

		l = new DefaultPath((FileSystem) null, "/tmp/qwe/ert");
		Assert.assertEquals("../../..", l.relativize(root).toString());

	}
}
