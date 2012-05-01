package net.anzix;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Test;

public class RegularNioTest {
	@Test
	public void test() throws IOException{
		FileSystem fs = FileSystems.getDefault();
		Assert.assertEquals("/tmp/asd", fs.getPath("/","tmp", "asd").toString());
		Assert.assertEquals("/tmp/asd", fs.getPath("/tmp", "/asd").toString());
		Assert.assertEquals("/tmp/asd", fs.getPath("/tmp", "asd").toString());
		Assert.assertEquals("/tmp/asd", fs.getPath("/tmp", "asd/").toString());
		
		Assert.assertEquals("/tmp/asd", fs.getPath("/tmp").resolve("asd").toString());
		Path tmp = fs.getPath("/tmp");
		
		Assert.assertEquals("tmp", fs.getPath("/tmp").getFileName().toString());
		Iterator<Path> p = fs.getPath("/tmp/test").iterator();
		p.next();
		Assert.assertEquals("test", p.next().toString());
		Assert.assertEquals(false, fs.getPath("/").iterator().hasNext());
		
	
		
	}
}	
