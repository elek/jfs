package net.anzix.jfs.gdocs;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.anzix.jfs.gdocs.model.Entry;

public class GDocsPath implements Path {
    private Entry entry;

    private List<GDocsPath> children = new ArrayList<GDocsPath>();

    private GDocsFileSystem fileSystem;
    private GDocsPath parent;

    public GDocsPath(GDocsFileSystem fileSystem, Entry entry) {
        this.fileSystem = fileSystem;
        this.entry = entry;
    }

    public void addChild(GDocsPath child) {
        children.add(child);
        child.setParent(this);
    }

    private void setParent(GDocsPath path) {
        this.parent = path;
    }

    @Override
    public FileSystem getFileSystem() {
        return fileSystem;
    }

    @Override
    public boolean isAbsolute() {
        return false;
    }

    @Override
    public Path getRoot() {
        return fileSystem.getRoot();
    }

    @Override
    public Path getFileName() {
        return this;
    }

    @Override
    public Path getParent() {
        return parent;
    }

    @Override
    public int getNameCount() {
        return 0;
    }

    @Override
    public Path getName(int index) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Path subpath(int beginIndex, int endIndex) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean startsWith(Path other) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean startsWith(String other) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean endsWith(Path other) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean endsWith(String other) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Path normalize() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Path resolve(Path other) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Path resolve(String other) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Path resolveSibling(Path other) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Path resolveSibling(String other) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Path relativize(Path other) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public URI toUri() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Path toAbsolutePath() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Path toRealPath(LinkOption... options) throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public File toFile() {
        throw new UnsupportedOperationException();
    }

    @Override
    public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public WatchKey register(WatchService watcher, WatchEvent.Kind<?>... events) throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Iterator<Path> iterator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int compareTo(Path other) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<GDocsPath> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        if (entry == null) {
            return "/";
        } else {
            return entry.title;
        }

    }
}
