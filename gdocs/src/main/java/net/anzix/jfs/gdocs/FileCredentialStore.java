package net.anzix.jfs.gdocs;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialStore;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

public class FileCredentialStore implements CredentialStore {

    private static final String REFRESH_TOKEN = "refreshToken";
    private static final String ACCESS_TOKEN = "accessToken";
    private File file = new File(System.getProperty("user.home"), ".jfs.oauth");

    @Override
    public boolean load(String userId, Credential credential) {
        try {
            if (!file.exists()) {
                return false;
            }
            FileReader r = new FileReader(file);
            Properties p = new Properties();
            p.load(r);
            r.close();
            if (p.getProperty(userId + "." + ACCESS_TOKEN) == null) {
                return false;
            }
            credential.setAccessToken(p.getProperty(userId + "." + ACCESS_TOKEN));
            credential.setRefreshToken(p.getProperty(userId + "." + REFRESH_TOKEN));
            return true;
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Error during loading credential from code", ex);
        }

    }

    @Override
    public void store(String userId, Credential credential) {
        try {
            Properties p = new Properties();
            if (file.exists()) {
                FileReader r = new FileReader(file);
                p.load(r);
                r.close();
            }
            p.setProperty(userId + "." + ACCESS_TOKEN, credential.getAccessToken());
            p.setProperty(userId + "." + REFRESH_TOKEN, credential.getRefreshToken());
            FileWriter w = new FileWriter(file);
            p.store(w, "");
            w.close();

        } catch (Exception ex) {
            throw new RuntimeException(
                    "Error during writing credential from code", ex);
        }

    }

    @Override
    public void delete(String userId, Credential credential) {
        // TODO Auto-generated method stub

    }

}
