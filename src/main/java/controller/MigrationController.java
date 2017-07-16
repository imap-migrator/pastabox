package controller;

import annotation.Controller;
import com.jfoenix.controls.JFXProgressBar;
import helper.DataHelper;
import helper.WizardInterface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import model.Login;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Created by Oliver on 7/10/2017.
 */
@Controller(title="Status")
public class MigrationController implements WizardInterface {
    public JFXProgressBar progressBar;
    public Label progressStatus;
    public Label progressMailCount;
    public Label progressRemainingTime;

    public static MigrationController instance;

    public Long startTime;

    public int current = 0;
    public int total = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            TemplateController.instance.next.setVisible(false);
            TemplateController.instance.back.setText("Cancel");
        });

        instance = this;

        start();
    }

    private void start() {
        Thread migrationThread = new Thread(() -> {
            for (Login origin:TemplateController.originServer.getLogins()) {
                try {
                    origin.getMigration().execute(origin);
                } catch (Exception e) {
                    DataHelper.getLogger(this.getClass())
                            .log(Level.SEVERE, "Migration did not execute cleanly.", e);
                }
            }
        });
        // Exit thread if program exits.
        migrationThread.setDaemon(true);
        migrationThread.start();

        Thread progressThread = new Thread(() -> {
            while(migrationThread.isAlive()) {
                if(total == 0 && current == 0)
                    continue;

                Long elapsedTime = System.nanoTime() - startTime;
                Long allTimeForMigration = (elapsedTime * total / current);
                Long remainingTime = allTimeForMigration - elapsedTime;

                Platform.runLater(() -> progressRemainingTime.setText(
                        String.format("Est. remaining time: %d min, %d sec",
                                TimeUnit.NANOSECONDS.toMinutes(remainingTime),
                                TimeUnit.NANOSECONDS.toSeconds(remainingTime) - TimeUnit.NANOSECONDS.toMinutes(remainingTime) * 60
                        )
                ));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    DataHelper.getLogger(this.getClass())
                            .log(Level.WARNING, "Sleep has been interrupted", e);
                }
            }

            // Clean up
            Platform.runLater(() -> {
                progressStatus.setText("Finished");
                progressMailCount.setText("");
                progressRemainingTime.setText("");

                TemplateController.instance.switchView("finish");
            });
        });
        progressThread.setDaemon(true);
        progressThread.start();
    }

    public void updateProgress(int current, int total) {
        this.current = current;
        this.total = total;

        Platform.runLater(() -> {
            progressBar.setProgress(1.0 / (double) total * (double) current);
            progressMailCount.setText(String.format("Progress: %s/%s", current, total));
        });
    }

    @Override
    public void next(ActionEvent event) {

    }

    @Override
    public void back(ActionEvent event) {
        TemplateController.instance.next.setVisible(true);
        TemplateController.instance.back.setText("Back");
        TemplateController.instance.switchView("mapping");
    }
}
