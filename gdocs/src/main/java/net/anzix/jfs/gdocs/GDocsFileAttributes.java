package net.anzix.jfs.gdocs;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import net.anzix.jfs.gdocs.model.Entry;

public class GDocsFileAttributes implements BasicFileAttributes {

	private Entry e;

	public GDocsFileAttributes(Entry e) {
		super();
		this.e = e;
	}

	@Override
	public FileTime lastModifiedTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileTime lastAccessTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileTime creationTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRegularFile() {
		return !getType().equals("folder");
	}

	private String getType() {
		return e.getType();
	}

	@Override
	public boolean isDirectory() {
		return getType().equals("folder");
	}

	@Override
	public boolean isSymbolicLink() {
		return false;
	}

	@Override
	public boolean isOther() {
		return false;
	}

	@Override
	public long size() {
		return 0;
	}

	@Override
	public Object fileKey() {
		return null;
	}

	public Entry getEntry() {
		return e;
	}

	public String getResourceId() {
		return e.resourceId;
	}

}
