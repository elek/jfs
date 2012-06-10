package net.anzix.jfs.gdocs;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.MethodOverride;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.xml.atom.AtomContent;
import com.google.api.client.http.xml.atom.AtomParser;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.xml.XmlNamespaceDictionary;
import net.anzix.SeekableChannelAdapter;
import net.anzix.jfs.common.BaseFileSystem;
import net.anzix.jfs.common.CollectionDirectoryStream;
import net.anzix.jfs.common.DefaultPath;
import net.anzix.jfs.gdocs.model.Category;
import net.anzix.jfs.gdocs.model.Entry;
import net.anzix.jfs.gdocs.model.Feed;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GDocsFileSystem extends BaseFileSystem {
	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	private static final List<String> SCOPES = Arrays
			.asList("https://docs.google.com/feeds/");

	static final XmlNamespaceDictionary DICTIONARY = new XmlNamespaceDictionary()
			.set("", "http://www.w3.org/2005/Atom")
			.set("app", "http://www.w3.org/2007/app")
			.set("batch", "http://schemas.google.com/gdata/batch")
			.set("docs", "http://schemas.google.com/docs/2007")
			.set("gAcl", "http://schemas.google.com/acl/2007")
			.set("gd", "http://schemas.google.com/g/2005")
			.set("openSearch", "http://a9.com/-/spec/opensearch/1.1/")
			.set("xml", "http://www.w3.org/XML/1998/namespace");

	static final Map<String, String> extensionTypeMap = new HashMap<String, String>();

	/** Global instance of the HTTP transport. */
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	private GDocsPath root;

	private HttpRequestFactory requestFactory;

	private PathHierarchy pathHierarchy;

	/**
	 * Unit test only;
	 */
	public GDocsFileSystem() {
		super(null);
		init();

	}

	public GDocsFileSystem(GDocsFileSystemProvider provider, URI uri) {
		super(provider);
		requestFactory = HTTP_TRANSPORT.createRequestFactory(getOAuth2Auth());
		init();

	}

	public void init() {
		extensionTypeMap.put("pdf", "application/pdf");
		extensionTypeMap.put("txt", "text/plain");
		extensionTypeMap.put("jpeg", "image/jpeg");
		extensionTypeMap.put("jpg", "image/jpeg");
	}

	public HttpRequestInitializer getOAuth2Auth() {
		GoogleClientSecrets secrets = new GoogleClientSecrets();
		Details d = new Details();
		d.setClientId("108216468429.apps.googleusercontent.com");
		d.setClientSecret("3H2PWgLD6FAILq7fJgep9xZt");
		secrets.setInstalled(d);

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, secrets, SCOPES)
				.setCredentialStore(new FileCredentialStore()).build();

		Credential c = flow.loadCredential("elekma");

		if (c == null) {
			System.out.println(flow.newAuthorizationUrl()
					.setRedirectUri("http://localhost").build());
			String code = new Scanner(System.in).next();

			try {
				GoogleTokenResponse resp = flow.newTokenRequest(code)
						.setRedirectUri("http://localhost").execute();
				c = flow.createAndStoreCredential(resp, "elekma");
			} catch (Exception ex) {
				throw new RuntimeException("Error on executing token request",
						ex);
			}

			// set up global Oauth2 instance
		}
		return c;

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

	protected void loadHierarchy() {
		try {
			pathHierarchy.cleanHierarchy();
			Logger logger = Logger.getLogger("com.google.api.client");
			logger.setLevel(Level.FINE);

			GenericUrl url = new GenericUrl(
					"https://docs.google.com/feeds/default/private/full/-/mine?showfolders=true&showroot=true&max-results=1000");
			HttpRequest request = createRequest(url);
			request.addParser(new AtomParser(DICTIONARY));

			Feed feed = request.execute().parseAs(Feed.class);
			pathHierarchy.parseFeed(feed);

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private HttpRequest createRequest(GenericUrl url) throws IOException {
		return interceptRequest(requestFactory.buildGetRequest(url));

	}

	public SeekableByteChannel newByteChannel(Path path,
			Set<? extends OpenOption> options, FileAttribute<?>[] attrs) {
		try {
			Entry e = pathHierarchy.getAttribute((DefaultPath) path).getEntry();
			GenericUrl urld = new GenericUrl(e.content.src);
			HttpRequest downreq = createRequest(urld);
			return new SeekableChannelAdapter(Channels.newChannel(downreq
					.execute().getContent()));
		} catch (Exception ex) {
			throw new RuntimeException("Can't open path " + path.toString(), ex);
		}

	}

	public <A extends BasicFileAttributes> A readAttributes(Path path,
			Class<A> type, LinkOption... options) throws IOException {
		if (type.equals(BasicFileAttributes.class)) {
			GDocsFileAttributes attr = getPathHierarch().getAttribute(
					(DefaultPath) path);
			if (attr == null) {
				throw new NoSuchFileException(path.toString());
			}
			return (A) attr;
		} else {
			return null;
		}

	}

	private PathHierarchy getPathHierarch() {
		if (pathHierarchy == null) {
			pathHierarchy = new PathHierarchy(getRoot());
			loadHierarchy();
		}
		return pathHierarchy;
	}

	public void checkAccess(Path path, AccessMode[] modes)
			throws NoSuchFileException {
		if (getPathHierarch().getAttribute((DefaultPath) path) == null) {
			throw new NoSuchFileException(path.toString());
		}
	}

	public OutputStream newOutputStream(final Path path, OpenOption[] options) {
		if (getPathHierarch().getAttribute((DefaultPath) path) != null) {
			throw new UnsupportedOperationException(
					"Patching existing files is not yet supported");
		} else {
			OutputStream out = new ByteArrayOutputStream() {

				@Override
				public void close() throws IOException {
					super.close();
					System.out.println("Now I can upload all of the "
							+ toByteArray().length + " bytes");
					try {
						upload(path, toByteArray());
					} catch (Exception e) {
						throw new RuntimeException("Error during file upload "
								+ path.toString(), e);
					}
				}

			};
			return out;

		}

	}

	protected void upload(Path path, byte[] byteArray) throws Exception {
		GenericUrl uploadUrl;
		if (path.getParent().equals(getRoot())) {
			// todo get from the initial response
			uploadUrl = new GenericUrl(
					"https://docs.google.com/feeds/upload/create-session/default/private/full?convert=false");
		} else {
			uploadUrl = new GenericUrl(getPathHierarch()
					.getAttribute((DefaultPath) path.getParent()).getEntry()
					.getLink(GDocs.CREATE_MEDIA).href
					+ "?convert=false");
		}

		String type = getType(path);
		;
		String name = getName(path, type);
		Entry e = new Entry();
		e.title = path.getFileName().toString();
		AtomContent content = AtomContent.forEntry(DICTIONARY, e);

		HttpRequest createMetadataRequest = interceptRequest(requestFactory
				.buildPostRequest(uploadUrl, content));
		// createMetadataRequest.getHeaders().put("X-Upload-Content-Type",
		// type);
		createMetadataRequest.getHeaders().put("X-Upload-Content-Length",
				byteArray.length);

		String tUploadUrl = createMetadataRequest.execute().getHeaders()
				.getLocation();

		int pos = 0;

		while (pos < byteArray.length) {
			int len = Math.min(1024 * 512, byteArray.length - pos);

			HttpRequest req = interceptRequest(requestFactory.buildPostRequest(
					new GenericUrl(tUploadUrl), new ByteArrayContent(type,
							byteArray, pos, len)));
			req.getHeaders().put(
					"Content-Range",
					"bytes " + pos + "-" + (pos + len - 1) + "/"
							+ byteArray.length);

			req.setThrowExceptionOnExecuteError(false);
			HttpResponse resp = req.execute();
			if (!resp.isSuccessStatusCode() && resp.getStatusCode() != 308) {
				throw new RuntimeException("Error on uploading file chunk for "
						+ path + " response " + resp.getStatusCode() + " "
						+ resp.getStatusMessage());
			}
			pos += len;

		}

	}

	private String getName(Path path, String type) {
		String name = path.getFileName().toString();
		if (name.endsWith("pdf") && type.equals("application/pdf")) {
			name = name.substring(0, name.length() - 4);
		}
		return name;

	}

	protected String getType(Path path) {
		String name = path.getFileName().toString();
		int pos = name.lastIndexOf('.');
		if (pos != -1) {
			String ext = name.substring(pos + 1).toLowerCase();
			if (extensionTypeMap.get(ext) != null) {
				return extensionTypeMap.get(ext);
			}
		}
		return "application/octet-stream";
	}

	private HttpRequest interceptRequest(HttpRequest request) {
		request.getHeaders().setUserAgent("jfs/1.0");
		request.getHeaders().put("GData-Version", 3);
		MethodOverride override = new MethodOverride();
		override.intercept(request);
		return request;
	}

	public void createDirectory(Path dir, FileAttribute<?>[] attrs)
			throws IOException {
		try {
			if (getPathHierarch().getAttribute((DefaultPath) dir) != null) {
				throw new IllegalArgumentException("Path already exists: "
						+ dir.toString());
			}
			GDocsFileAttributes attribute = getPathHierarch().getAttribute(
					(DefaultPath) dir.getParent());
			if (attribute == null) {
				throw new IOException("Parent folder doesn't exist for "
						+ dir.toString());
			}
			GenericUrl uploadUrl = new GenericUrl(
					"https://docs.google.com/feeds/default/private/full/"
							+ URLEncoder.encode(attribute.getResourceId())
							+ "/contents");

			Entry e = new Entry();
			e.title = dir.getFileName().toString();
			Category c = new Category();
			c.scheme = "http://schemas.google.com/g/2005#kind";
			c.term = "http://schemas.google.com/docs/2007#folder";
			e.categories.add(c);

			HttpRequest request = interceptRequest(requestFactory
					.buildPostRequest(uploadUrl,
							AtomContent.forEntry(DICTIONARY, e)));
			request.addParser(new AtomParser(DICTIONARY));
			HttpResponse resp = request.execute();
			getPathHierarch().addEntry(resp.parseAs(Entry.class));
		} catch (Exception ex) {
			throw new IOException("Error during creating new dir: "
					+ dir.toString(), ex);
		}

	}

	@Override
	public DirectoryStream<Path> newDirectoryStream(Path dir,
			Filter<? super Path> filter) throws IOException {
		if (getPathHierarch().get(dir) == null) {
			return new CollectionDirectoryStream(new ArrayList<Path>());
		}
		return new CollectionDirectoryStream(pathHierarchy.get(dir));

	}
}
