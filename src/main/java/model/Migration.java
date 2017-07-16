package model;

import controller.MigrationController;
import helper.DataHelper;
import javafx.application.Platform;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Created by Oliver on 4/16/2017.
 */
public class Migration {
    private Login toLogin;
    private HashMap<Folder, Folder> folderMapping;
    private HashMap<Message, MessagingException> failedMessages;
    private HashMap<Folder, MessagingException> failedFolders;

    public Migration() {
        this.folderMapping = new HashMap<>();
        this.failedMessages = new HashMap<>();
        this.failedFolders = new HashMap<>();
    }

    public Login getToLogin() {
        return toLogin;
    }

    public void setToLogin(Login toLogin) {
        this.toLogin = toLogin;
    }

    public HashMap<Folder, Folder> getFolderMapping() {
        return folderMapping;
    }

    public void setFolderMapping(HashMap<Folder, Folder> folderMapping) {
        this.folderMapping = folderMapping;
    }

    public void execute(Login fromLogin) {
        DataHelper.getLogger(this.getClass())
                .log(Level.INFO, String.format("Started migration for account %s of server %s", fromLogin.getUsername(), fromLogin.getServer().getConnectionString()));

        for(Folder origin:folderMapping.keySet()) {
            MigrationController.instance.startTime = System.nanoTime();

            Folder target = folderMapping.get(origin);

            String curStatus = String.format("Migrating %s (%s) to %s (%s)", origin.getName(), fromLogin.getUsername(), target.getName(), toLogin.getUsername());
            DataHelper.getLogger(this.getClass())
                    .log(Level.INFO, curStatus);
            Platform.runLater(() -> MigrationController.instance.progressStatus.setText(curStatus));

            int total;

            try {
                origin.open(Folder.READ_ONLY);

                DataHelper.getLogger(this.getClass())
                        .log(Level.INFO, "Origin folder opened with mode READ_ONLY.");

                total = origin.getMessageCount();

                DataHelper.getLogger(this.getClass())
                        .log(Level.INFO, String.format("Total mails: %d", total));
            } catch (MessagingException e) {
                // Cannot access folder

                DataHelper.getLogger(this.getClass())
                        .log(Level.SEVERE, "Could not open folder. " + fromLogin.getServer().getConnectionString() + ",folder=" + origin.getFullName(), e);

                failedFolders.put(origin, e);

                continue;
            }

            try {
                target.open(Folder.READ_WRITE);

                DataHelper.getLogger(this.getClass())
                        .log(Level.INFO, "Target folder opened with mode READ_WRITE.");
            } catch (MessagingException e) {
                // Cannot access folder

                DataHelper.getLogger(this.getClass())
                        .log(Level.SEVERE, "Could not open folder. " + toLogin.getServer().getConnectionString() + ",folder=" + target.getFullName(), e);

                failedFolders.put(target, e);

                continue;
            }

            for(int i = 1; i <= total; ++i) {

                try {


                    Message[] originMessage = new Message[] { origin.getMessage(i) };

                    try {
                        target.appendMessages(originMessage);

                        DataHelper.getLogger(this.getClass())
                                .log(Level.INFO, String.format("Message %d copied to %s ", i, toLogin.getServer().getConnectionString()));

                    } catch (MessagingException e) {

                        DataHelper.getLogger(this.getClass())
                                .log(Level.SEVERE, String.format("Could not copy message %d to %s ", i, toLogin.getServer().getConnectionString()), e);

                        failedMessages.put(originMessage[0], e);
                    }

                } catch (MessagingException e) {
                    DataHelper.getLogger(this.getClass())
                            .log(Level.SEVERE, String.format("Could not retrieve message %d from %s ", i, fromLogin.getServer().getConnectionString()), e);
                }

                MigrationController.instance.updateProgress(i, total);
            }


            try {

                origin.close(false);

                DataHelper.getLogger(this.getClass())
                        .log(Level.INFO, "Origin folder closed.");

                target.close(false);

                DataHelper.getLogger(this.getClass())
                        .log(Level.INFO, "Target folder closed.");

            } catch (MessagingException e) {
                // It's not important whether the folders are closed or not though.

                DataHelper.getLogger(this.getClass())
                        .log(Level.WARNING, "Could not close folder.", e);
            }

        }
    }
}
