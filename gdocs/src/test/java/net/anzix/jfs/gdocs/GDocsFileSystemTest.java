package net.anzix.jfs.gdocs;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import junit.framework.Assert;
import net.anzix.jfs.common.DefaultPath;

import org.junit.Test;

public class GDocsFileSystemTest {

	@Test
	public void getPath(){
		GDocsFileSystem s = new GDocsFileSystem();
		
		Path p = s.getPath("/test");
		Assert.assertEquals("/test", p.toString());
		Assert.assertTrue(p.isAbsolute());
		
		Assert.assertEquals("/tmp/asd", s.getPath("/","tmp", "asd").toString());
		Assert.assertEquals("/tmp/asd", s.getPath("/tmp", "/asd").toString());
		Assert.assertEquals("/tmp/asd", s.getPath("/tmp", "asd").toString());
		Assert.assertEquals("/tmp/asd", s.getPath("/tmp", "asd/").toString());

		
		
	}
	
	@Test
	public void getTest(){
		GDocsFileSystem s = new GDocsFileSystem();
		Assert.assertEquals("application/pdf", s.getType(new DefaultPath((FileSystem)null,"ad.pdf")));
	}
	
}
