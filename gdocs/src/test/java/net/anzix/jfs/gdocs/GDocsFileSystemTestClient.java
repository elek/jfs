package net.anzix.jfs.gdocs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.anzix.jfs.gdocs.model.Category;
import net.anzix.jfs.gdocs.model.Entry;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.MethodOverride;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.xml.atom.AtomContent;
import com.google.api.client.http.xml.atom.AtomParser;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;

public class GDocsFileSystemTestClient {

	static final XmlNamespaceDictionary DICTIONARY = new XmlNamespaceDictionary()
			.set("", "http://www.w3.org/2005/Atom")
			.set("app", "http://www.w3.org/2007/app")
			.set("batch", "http://schemas.google.com/gdata/batch")
			.set("docs", "http://schemas.google.com/docs/2007")
			.set("gAcl", "http://schemas.google.com/acl/2007")
			.set("gd", "http://schemas.google.com/g/2005")
			.set("openSearch", "http://a9.com/-/spec/opensearch/1.1/")
			.set("xml", "http://www.w3.org/XML/1998/namespace");

	/** Global instance of the HTTP transport. */
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	private static final List<String> SCOPES = Arrays
			.asList("https://docs.google.com/feeds/");

	public static void main(String[] args) throws Exception {
		new GDocsFileSystemTestClient().test();
	}

	public Credential getOauthCredential() throws Exception {
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

			GoogleTokenResponse resp = flow.newTokenRequest(code)
					.setRedirectUri("http://localhost").execute();
			c = flow.createAndStoreCredential(resp, "elekma");
			// set up global Oauth2 instance
		}
		return c;
	}

	public void oauth() throws Exception {

		Oauth2 oauth2 = Oauth2.builder(HTTP_TRANSPORT, JSON_FACTORY)
				.setApplicationName("jfs/1.0")
				.setHttpRequestInitializer(getOauthCredential()).build();

		// run commands
		Tokeninfo info = oauth2.tokeninfo()
				.setAccessToken(getOauthCredential().getAccessToken())
				.execute();
		System.out.println(info.toPrettyString());

	}
	
	public void mkdir() throws Exception {
		

		Logger.getLogger(HttpTransport.class.getName()).setLevel(Level.ALL);
		ConsoleHandler h = new ConsoleHandler();
		h.setLevel(Level.ALL);
		Logger.getLogger(HttpTransport.class.getName()).addHandler(h);

		HttpRequestFactory requestFactory = HTTP_TRANSPORT
				.createRequestFactory(getOauthCredential());
		GenericUrl urlu = new GenericUrl(
				"https://docs.google.com/feeds/default/private/full/folder%3Aroot/contents");
		
		Entry e = new Entry();
		e.title = "testfolder" + Math.random();
		
		Category c = new Category();
		c.scheme = "http://schemas.google.com/g/2005#kind";
		c.term = "http://schemas.google.com/docs/2007#folder";
		e.categories.add(c);
		
		AtomContent content = AtomContent.forEntry(DICTIONARY, e);
		HttpRequest upreq = requestFactory.buildPostRequest(urlu, content);
		upreq.getHeaders().setUserAgent("jfs/1.0");
		upreq.getHeaders().put("GData-Version", 3);
		MethodOverride override = new MethodOverride();
		override = new MethodOverride();
		override.intercept(upreq);

		HttpResponse fr = upreq.execute();


	}


	public void upload() throws Exception {
		File f = new File("/storage/IRATOK/INBOX/Report_000997.pdf");

		Logger.getLogger(HttpTransport.class.getName()).setLevel(Level.ALL);
		ConsoleHandler h = new ConsoleHandler();
		h.setLevel(Level.ALL);
		Logger.getLogger(HttpTransport.class.getName()).addHandler(h);

		HttpRequestFactory requestFactory = HTTP_TRANSPORT
				.createRequestFactory(getOauthCredential());
		GenericUrl urlu = new GenericUrl(
				"https://docs.google.com/feeds/upload/create-session/default/private/full");
		Entry e = new Entry();
		e.title = "testupload" + Math.random();
		AtomContent content = AtomContent.forEntry(DICTIONARY, e);
		content.writeTo(System.out);
		HttpRequest upreq = requestFactory.buildPostRequest(urlu, content);
		upreq.getHeaders().setUserAgent("jfs/1.0");
		upreq.getHeaders().put("GData-Version", 3);
		MethodOverride override = new MethodOverride();
		override = new MethodOverride();
		override.intercept(upreq);
		upreq.getHeaders().put("X-Upload-Content-Type", "application/pdf");
		upreq.getHeaders().put("X-Upload-Content-Length", f.length());

		HttpResponse fr = upreq.execute();

		String loc = fr.getHeaders().getLocation();
		System.out.println(fr.getStatusCode());
		System.out.println(fr.getStatusMessage());

		int pos = 0;
		byte[] buffer = new byte[512 * 1024];
		FileInputStream input = new FileInputStream(f);
		String uploadUrl = loc;
		while (pos < f.length()) {
			int len = input.read(buffer);
			if (len == -1)
				break;
			ByteArrayContent c = new ByteArrayContent("application/pdf",
					buffer, 0, len);
			c.writeTo(new FileOutputStream(new File("/tmp/qwe2")));
			HttpRequest req = requestFactory.buildPostRequest(new GenericUrl(
					uploadUrl), c);
			req.getHeaders().put("Content-Range",
					"bytes " + pos + "-" + (pos + len - 1) + "/" + f.length());
			req.getHeaders().setUserAgent("jfs/1.0");
			req.getHeaders().put("GData-Version", 3);
			MethodOverride o = new MethodOverride();
			o.intercept(upreq);
			try {
				HttpResponse resp = req.execute();
				System.out.println(resp.getStatusCode());
				System.out.println(resp.getStatusMessage());
				System.out.println(new Scanner(resp.getContent()).useDelimiter(
						"\\Z").next());
				uploadUrl = resp.getHeaders().getLocation();
				pos += len;
			} catch (HttpResponseException ex) {
				System.out.println(ex.getResponse().parseAsString());
			}
			break;

		}
		input.close();
		//
		// HttpResponse r = downreq.execute();
		//
		// InputStream s = r.getContent();
		// OutputStream o = new FileOutputStream(new File("/tmp/test.csv"));
		// byte[] buffer = new byte[1024];
		// while (s.read(buffer) != -1) {
		// o.write(buffer);
		//
		// }
		// o.close();
		// s.close();
	}

	public void download() throws Exception {
		HttpRequestFactory requestFactory = HTTP_TRANSPORT
				.createRequestFactory(getOauthCredential());
		GenericUrl urld = new GenericUrl(
				"https://docs.google.com/feeds/download/spreadsheets/Export?key=0AgUm7A7D5FgWdHB6ZVl0d05Fd3JPRk03OGdBbEwwRkE");
		HttpRequest downreq = requestFactory.buildGetRequest(urld);
		downreq.getHeaders().setUserAgent("jfs/1.0");
		downreq.getHeaders().put("GData-Version", 3);
		MethodOverride override = new MethodOverride();
		override = new MethodOverride();
		override.intercept(downreq);

		HttpResponse r = downreq.execute();

		InputStream s = r.getContent();
		OutputStream o = new FileOutputStream(new File("/tmp/test.csv"));
		byte[] buffer = new byte[1024];
		while (s.read(buffer) != -1) {
			o.write(buffer);

		}
		o.close();
		s.close();
	}

	// @Test
	public void test() throws Exception {
		Logger.getLogger(HttpTransport.class.getName()).setLevel(Level.ALL);
		ConsoleHandler h = new ConsoleHandler();
		h.setLevel(Level.ALL);
		Logger.getLogger(HttpTransport.class.getName()).addHandler(h);

		GenericUrl url = new GenericUrl(
				"https://docs.google.com/feeds/default/private/full/-/mine?showfolders=true&showroot=true&max-results=1000");

		HttpRequestFactory requestFactory = HTTP_TRANSPORT
				.createRequestFactory(getOauthCredential());
		HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setUserAgent("jfs/1.0");
		request.getHeaders().put("GData-Version", 3);
		request.addParser(new AtomParser(DICTIONARY));

		MethodOverride override = new MethodOverride();
		override.intercept(request);

		HttpResponse response = request.execute();
		FileWriter w = new FileWriter(new File("/tmp/test"));

		w.write(response.parseAsString());
		w.close();

	}
}
