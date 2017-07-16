package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by Oliver on 4/12/2017.
 */
public class Server {
    private String server;
    private String port;
    private boolean secure;
    private boolean ignoreCertErrors;

    private ObservableList<Login> logins = FXCollections.observableArrayList();

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

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isIgnoreCertErrors() {
        return ignoreCertErrors;
    }

    public void setIgnoreCertErrors(boolean ignoreCertErrors) {
        this.ignoreCertErrors = ignoreCertErrors;
    }

    public ObservableList<Login> getLogins() {
        return logins;
    }

    public String getConnectionString() {
        return String.format("%s:%s,secure=%d,ignoreCertErrors=%d", getServer(), getPort(), isSecure() ? 1 : 0, isIgnoreCertErrors() ? 1 : 0);
    }
}
