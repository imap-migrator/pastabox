package model;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.MessagingException;
import javax.mail.Store;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * Created by Oliver on 4/12/2017.
 */
public class Server {
    private String server;
    private String port;
    private String username;
    private String password;
    private boolean secure;

    private Store store;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public Store getStore() {
        return store;
    }

    /**
     * Initiates a connection to the mailserver and sets the variable store accordingly.
     * @throws MessagingException General connection error
     * @throws GeneralSecurityException Secure connectioon error
     */
    public void init() throws MessagingException, GeneralSecurityException {
        Properties props = System.getProperties();
        String imapProtocol = isSecure() ? "imaps" : "imap";

        // trust unsigned certs
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.imap.ssl.trust", "*");
        props.put("mail.imaps.ssl.trust", "*");
        props.put("mail.imap.ssl.socketFactory", sf);
        props.setProperty("mail.imap.connectionpooltimeout", "3000");
        props.setProperty("mail.imap.connectiontimeout", "3000");
        props.setProperty("mail.imap.timeout", "3000");
        props.setProperty("mail.imaps.connectionpooltimeout", "3000");
        props.setProperty("mail.imaps.connectiontimeout", "3000");
        props.setProperty("mail.imaps.timeout", "3000");

        // Build imap connection data.
        props.put("mail.store.protocol", imapProtocol);
        props.put("mail." + imapProtocol + ".port", port);
        props.put("mail." + imapProtocol + ".host", server);

        javax.mail.Session mailSession = javax.mail.Session.getInstance(props, null);
        Store store = mailSession.getStore();
        store.connect(server, Integer.parseInt(port), username, password);

        this.store = store;
/*
        Folder[] folders = store.getDefaultFolder().list("*");
        for(Folder folder:folders) {
            System.out.println(folder.getFullName());
        }*/
    }
}
