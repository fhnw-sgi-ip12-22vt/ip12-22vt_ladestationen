package ch.ladestation.connectncharge.pui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * plays sounds when user Interacts
 */
public final class Sounder {
    private static final Media DEACTIVATE_MEDIA;
    private static final Media ACTIVATE_MEDIA;

    static {
        String resourcePath = Sounder.class.getResource("/deactivate.mp3").toString();
        DEACTIVATE_MEDIA = new Media(resourcePath);

        resourcePath = Sounder.class.getResource("/activate.mp3").toString();
        ACTIVATE_MEDIA = new Media(resourcePath);
    }

    private Sounder() {
    }

    public static void playActivate() {
        var med = new MediaPlayer(ACTIVATE_MEDIA);
        med.setOnEndOfMedia(med::dispose);
        med.play();
    }

    public static void playDeactivate() {
        var med = new MediaPlayer(DEACTIVATE_MEDIA);
        med.setOnEndOfMedia(med::dispose);
        med.play();
    }
}
