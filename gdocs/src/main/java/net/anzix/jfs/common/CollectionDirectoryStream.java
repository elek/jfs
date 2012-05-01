package net.anzix.jfs.common;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;

public class CollectionDirectoryStream implements DirectoryStream<Path>{
    private Collection<? extends Path> elements;

    public CollectionDirectoryStream(Collection<? extends Path> elements) {
        this.elements = elements;
    }

    @Override
    public Iterator<Path> iterator() {
        return (Iterator<Path>) elements.iterator();
    }

    @Override
    public void close() throws IOException {

    }
}
