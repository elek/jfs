package net.anzix.jfs.dummy;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class DummyFileAttributes implements BasicFileAttributes {
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
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isDirectory() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
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
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object fileKey() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
