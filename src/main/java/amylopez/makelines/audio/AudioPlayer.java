package amylopez.makelines.audio;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Class that handles playing audio
 */
public class AudioPlayer implements MusicPlayer {

    private static Logger log = LoggerFactory.getLogger(AudioPlayer.class);

    @Inject
    private Configuration config;

    /**
     * The media player that handles playing the music
     */
    private MediaPlayer mediaPlayer;

    /**
     * Initialize this class from config
     */
    public void init() {
        // hack to initialize the JFX Toolkit in order to play audio
        JFXPanel initializeJFX = new JFXPanel();

        String configPath = "audio/music/";

        String defaultSongConfig = config.getString(configPath + "@default");
        String musicPath = config.getString(configPath + "@path");
        String songFileName = config.getString(
                configPath + "song[@title='" + defaultSongConfig + "']/@filename");

        setMusicFile(musicPath + songFileName);
    }

    /**
     * Set the mp3 file to play as music
     * @param filename
     */
    private void setMusicFile(String filename) {
        log.debug("Setting music to file: {}", filename);

        Media music = new Media(getClass().getResource(filename).toString());
        mediaPlayer = new MediaPlayer(music);
    }

    /**
     * Start the music
     */
    @Override
    public void play() {
        mediaPlayer.play();
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    /**
     * Stop the music
     */
    @Override
    public void stop() {
        mediaPlayer.stop();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }
}
