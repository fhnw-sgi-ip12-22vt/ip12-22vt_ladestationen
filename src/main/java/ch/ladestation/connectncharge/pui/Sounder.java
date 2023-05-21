package ch.ladestation.connectncharge.pui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * plays sounds when user Interacts
 */
public final class Sounder {
    private static final MediaPlayer DEACTIVATE_PLAYER;
    private static final MediaPlayer ACTIVATE_PLAYER;

    static {
        String resourcePath = Sounder.class.getResource("/deactivate.wav").toString();
        DEACTIVATE_PLAYER = new MediaPlayer(new Media(resourcePath));

        resourcePath = Sounder.class.getResource("/activate.wav").toString();
        ACTIVATE_PLAYER = new MediaPlayer(new Media(resourcePath));
    }

    private Sounder() {
    }

    public static void playActivate() {
        ACTIVATE_PLAYER.play();
    }

    public static void playDeactivate() {
        DEACTIVATE_PLAYER.play();
    }
}
