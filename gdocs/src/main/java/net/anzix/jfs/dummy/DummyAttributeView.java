package net.anzix.jfs.dummy;

import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class DummyAttributeView implements BasicFileAttributeView {
    private String name;

    public DummyAttributeView(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public BasicFileAttributes readAttributes() throws IOException {
        return new BasicFileAttributes() {
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
                return name();
            }
        };
    }

    @Override
    public void setTimes(FileTime fileTime, FileTime fileTime1, FileTime fileTime2) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
