package net.anzix.jfs.common;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class DefaultFileAttributes implements BasicFileAttributes {
    private long size;
    private boolean directory;
    
    

	public DefaultFileAttributes(boolean directory) {
		super();
		this.directory = directory;
	}

	public DefaultFileAttributes(long size, boolean directory) {
		super();
		this.size = size;
		this.directory = directory;
	}

	@Override
    public FileTime lastModifiedTime() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public FileTime lastAccessTime() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public FileTime creationTime() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isRegularFile() {
        return !directory;
    }

    @Override
    public boolean isDirectory() {
        return directory;
    }

    @Override
    public boolean isSymbolicLink() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isOther() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public Object fileKey() {
        return null; 
    }
}
