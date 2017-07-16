package model;

import com.sun.mail.util.MailSSLSocketFactory;
import javax.mail.MessagingException;
import javax.mail.Store;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * Created by Oliver on 4/13/2017.
 */
public class Login {
    private Server server;
    private String username;
    private String password;
    private Store store;
    private Migration migration;

    public Login(Server server) {
        this.server = server;
        this.migration = new Migration();
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

    public Server getServer() { return server; }

    public Store getStore() throws MessagingException, GeneralSecurityException {
        if(store == null) {
            Properties props = System.getProperties();
            String imapProtocol = server.isSecure() ? "imaps" : "imap";

            // trust unsigned certs
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            if (server.isIgnoreCertErrors()) {
                sf.setTrustAllHosts(true);
                props.put("mail.imap.ssl.trust", "*");
                props.put("mail.imaps.ssl.trust", "*");
            }
            props.put("mail.imap.ssl.socketFactory", sf);
            props.setProperty("mail.imap.connectionpooltimeout", "3000");
            props.setProperty("mail.imap.connectiontimeout", "3000");
            props.setProperty("mail.imap.timeout", "3000");
            props.setProperty("mail.imaps.connectionpooltimeout", "3000");
            props.setProperty("mail.imaps.connectiontimeout", "3000");
            props.setProperty("mail.imaps.timeout", "3000");

            // Build imap connection data.
            props.put("mail.store.protocol", imapProtocol);
            props.put("mail." + imapProtocol + ".port", server.getPort());
            props.put("mail." + imapProtocol + ".host", server.getServer());

            javax.mail.Session mailSession = javax.mail.Session.getInstance(props, null);

            Store store = mailSession.getStore();
            store.connect(server.getServer(), Integer.parseInt(server.getPort()), getUsername(), getPassword());
            setStore(store);
        }
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Migration getMigration() {
        return migration;
    }

    @Override
    public String toString() {
        return username;
    }
}
