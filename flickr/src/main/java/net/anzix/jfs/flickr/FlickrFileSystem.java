package net.anzix.jfs.flickr;

import com.aetrion.flickr.*;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.AuthInterface;
import com.aetrion.flickr.auth.AuthUtilities;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.uploader.UploadMetaData;
import com.aetrion.flickr.uploader.Uploader;
import net.anzix.jfs.common.*;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.spi.FileSystemProvider;
import java.util.*;

public class FlickrFileSystem extends BaseFileSystem {
    private Flickr f;
    private String token;
    private PathHierarchy hierarchy;
    /**
     * caching directory creating as a set could be created only with at least
     * on valid file.
     */
    private Set<Path> dirsToCreate = new HashSet<Path>();

    public FlickrFileSystem(FileSystemProvider provider, URI uri) {
        super(provider);

        Properties p = new Properties();
        File fp = new File(System.getenv("user.home"), "jfs.oauth");
        if (fp.exists()) {
            try (FileReader fr = new FileReader(fp)) {
                p.load(fr);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }
        try {
            f = new Flickr("35c1f76f44831da16371329162eae7b4", "70a10e2cd57e534f", new REST());
            String frob = "";

            Flickr.debugStream = true;
            Flickr.debugRequest = true;
            RequestContext requestContext = RequestContext.getRequestContext();
            AuthInterface authInterface = f.getAuthInterface();

            Auth auth = null;
            token = "";
            //todo fix  this
            if (token == null) {
                URL url = f.getAuthInterface().buildAuthenticationUrl(
                        Permission.WRITE, frob);
                System.out.println(url.toString());
                Thread.sleep(20000);

                auth = authInterface.getToken(frob);
                token = auth.getToken();

            } else {
                auth = new Auth();
                auth.setToken(token);
            }
            auth = authInterface.checkToken(token);
            requestContext.setAuth(auth);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public void checkAccess(Path dir, AccessMode... modes) throws IOException {
        DefaultPath dp = (DefaultPath) dir;
        if (getHierarchy().getAttr(dp) != null
                && getHierarchy().isCollection(dp)) {
            // it's a directory or a set
            return;
        } else {
            if (getHierarchy().getAttr(dp) != null) {
                FlickrDirAttributes attr = getHierarchy().getAttr(dp);
                String id = attr.getElement().getAttribute("id");
                readSet(dp, id);
                if (getHierarchy().getChild(dp) != null) {
                    return;
                }

            }

        }
        throw new FileNotFoundException();
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(Path dir,
                                                    Filter<? super Path> filter) throws IOException {
        DefaultPath dp = (DefaultPath) dir;
        if (getHierarchy().getChild(dp) != null
                || getHierarchy().isCollection(dp)) {
            // it's a directory or a set
            return new CollectionDirectoryStream(getHierarchy().getChild(dp));
        } else {
            if (getHierarchy().getAttr(dp) != null) {
                FlickrDirAttributes attr = getHierarchy().getAttr(dp);
                String id = attr.getElement().getAttribute("id");
                readSet(dp, id);
                if (getHierarchy().getChild(dp) != null) {
                    return new CollectionDirectoryStream(getHierarchy()
                            .getChild(dp));
                }

            }

        }
        throw new FileNotFoundException(dir.toString());

    }

    private void readSet(DefaultPath parent, String id) {
        try {
            List<Parameter> parameters = new ArrayList<Parameter>();
            Map<String, String> st = new TreeMap<String, String>();
            parameters
                    .add(new Parameter("method", "flickr.photosets.getPhotos"));
            parameters.add(new Parameter("api_key", f.getApiKey()));
            parameters.add(new Parameter("photoset_id", id));
            parameters.add(new Parameter("api_sig", AuthUtilities.getSignature(
                    f.getSharedSecret(), parameters)));

            Response post = f.getTransport().post(f.getTransport().getPath(),
                    parameters);

            getHierarchy().parseSetList(parent, post.getPayload());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public PathHierarchy getHierarchy() {
        if (hierarchy == null) {
            try {
                List<Parameter> parameters = new ArrayList<Parameter>();
                Map<String, String> st = new TreeMap<String, String>();
                parameters.add(new Parameter("method",
                        "flickr.collections.getTree"));
                parameters.add(new Parameter("api_key", f.getApiKey()));
                parameters.add(new Parameter("api_sig", AuthUtilities
                        .getSignature(f.getSharedSecret(), parameters)));

                Response post = f.getTransport().post(
                        f.getTransport().getPath(), parameters);

                Element payload = post.getPayload();

                hierarchy = new PathHierarchy((DefaultPath) getRoot(), payload);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return hierarchy;
    }

    @Override
    public SeekableByteChannel newByteChannel(final Path file,
                                              Set<? extends OpenOption> options, FileAttribute<?>... attrs)
            throws IOException {
        if (options.contains(StandardOpenOption.READ)) {
            return new BufferedReadableByteChannel() {

                @Override
                protected void preloadContent(FileChannel ch) {
                    try {
                        ch.write(ByteBuffer.wrap("test".getBytes()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }

            };
        } else if (options.contains(StandardOpenOption.WRITE)) {
            return new BufferedWritableByteChannel() {

                @Override
                protected void writeContent(FileChannel ch) {
                    try {

                        Uploader uploader = f.getUploader();
                        UploadMetaData umd = new UploadMetaData();
                        umd.setPublicFlag(false);
                        umd.setFriendFlag(false);
                        umd.setFamilyFlag(false);
                        String name = file.getFileName().toString();
                        name = name.substring(0, name.lastIndexOf('.'));
                        umd.setTitle(name);
                        String id = uploader.upload(
                                Channels.newInputStream(ch), umd);
                        if (!file.getParent().equals(file.getRoot())) {
                            String parentSetId = checkParent(
                                    (DefaultPath) file.getParent(), id);
                            if (parentSetId != null) {
                                f.getPhotosetsInterface().addPhoto(parentSetId,
                                        id);
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
            };

        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Return with the parentSetId of a file.
     * <p/>
     * Will create collection when needed.
     *
     * @return
     * @throws FlickrException
     * @throws SAXException
     * @throws IOException
     */

    public String checkParent(DefaultPath path, String firstPhotoId)
            throws Exception {
        if (path.equals(path.getRoot())) {
            return null;
        }
        if (!getHierarchy().exists(path) && dirsToCreate.contains(path)) {
            String parentId = checkParent(path.getParent(), null);
            if (firstPhotoId != null) {
                // create set
                String newSetId = f
                        .getPhotosetsInterface()
                        .create(path.getFileName().toString(), "", firstPhotoId)
                        .getId();
                return null;
            } else {
                throw new IllegalArgumentException(
                        "Creating collections is unsupported");
                // create collection
            }
        } else if (!getHierarchy().exists(path)) {
            throw new IllegalArgumentException("Parent doesn't exist:" + path);
        } else {
            return getHierarchy().getAttr(path).getId();
        }
    }

    @Override
    public void createDirectory(Path dir, FileAttribute<?>... attrs)
            throws IOException {
        dirsToCreate.add(dir);

    }

}
