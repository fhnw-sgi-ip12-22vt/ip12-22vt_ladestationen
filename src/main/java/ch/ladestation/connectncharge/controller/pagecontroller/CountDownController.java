package ch.ladestation.connectncharge.controller.pagecontroller;

import ch.ladestation.connectncharge.controller.StageHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class CountDownController implements Initializable {

    private static final int COUNTDOWN_SECONDS = 4;
    @FXML
    private Label countDownText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startCountDown();
    }

    private void startCountDown() {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        final Runnable runnable = new Runnable() {
            private int countdownStarter = COUNTDOWN_SECONDS;

            public void run() {
                Platform.runLater(() -> {
                    countDownText.setText(String.valueOf(countdownStarter));
                });
                countdownStarter--;

                if (countdownStarter == 0) {
                    Platform.runLater(() -> {
                        countDownText.setText("Los ...");
                    });
                } else if (countdownStarter < 0) {
                    Platform.runLater(() -> {
                        countDownText.setText("Los ...");
                        try {
                            StageHandler.openStage("/ch/ladestation/connectncharge/gamepage.fxml", "/css/style.css",
                                (Stage) countDownText.getScene().getWindow());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    scheduler.shutdown();
                }
            }
        };
        scheduler.scheduleAtFixedRate(runnable, 0, 1, SECONDS);
    }
}
