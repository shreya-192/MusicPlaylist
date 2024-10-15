import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayer {
    private JFrame frame;
    private JButton playPauseButton, nextButton, prevButton;
    private JSlider volumeSlider;
    private JLabel songLabel;

    private ArrayList<String> songs;
    private int currentSongIndex;
    private Clip audioClip;
    private boolean isPlaying;

    public MusicPlayer() {
        songs = new ArrayList<>();
        // Add paths to your WAV songs
        songs.add("audio-1.wav");
        songs.add("audio-2.wav");
        songs.add("audio-3.wav");
        songs.add("audio-4.wav");
        currentSongIndex = 0;

        createUI();
    }

    private void createUI() {
        frame = new JFrame("Music Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new FlowLayout());

        songLabel = new JLabel("Now Playing: " + songs.get(currentSongIndex));
        frame.add(songLabel);

        playPauseButton = new JButton("Play");
        playPauseButton.addActionListener(new PlayPauseAction());
        frame.add(playPauseButton);

        nextButton = new JButton("Next");
        nextButton.addActionListener(new NextAction());
        frame.add(nextButton);

        prevButton = new JButton("Previous");
        prevButton.addActionListener(new PrevAction());
        frame.add(prevButton);

        volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setPaintTicks(true);
        volumeSlider.addChangeListener(e -> {
            // Volume adjustment (not implemented in this basic version)
        });
        frame.add(volumeSlider);

        frame.setVisible(true);
    }

    private void playSong(String path) {
        try {
            if (audioClip != null && isPlaying) {
                audioClip.stop();
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path));
            audioClip = AudioSystem.getClip();
            audioClip.open(audioInputStream);
            audioClip.start();
            isPlaying = true;
            playPauseButton.setText("Pause");
            songLabel.setText("Now Playing: " + new File(path).getName());
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void pauseSong() {
        if (audioClip != null && isPlaying) {
            audioClip.stop();
            isPlaying = false;
            playPauseButton.setText("Play");
        }
    }

    private void nextSong() {
        currentSongIndex = (currentSongIndex + 1) % songs.size();
        if (isPlaying) {
            audioClip.close();
            playSong(songs.get(currentSongIndex));
        }
        songLabel.setText("Now Playing: " + songs.get(currentSongIndex));
    }

    private void prevSong() {
        currentSongIndex = (currentSongIndex - 1 + songs.size()) % songs.size();
        if (isPlaying) {
            audioClip.close();
            playSong(songs.get(currentSongIndex));
        }
        songLabel.setText("Now Playing: " + songs.get(currentSongIndex));
    }

    private class PlayPauseAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isPlaying) {
                pauseSong();
            } else {
                playSong(songs.get(currentSongIndex));
            }
        }
    }

    private class NextAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            nextSong();
        }
    }

    private class PrevAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            prevSong();
        }
    }

    public static void main(String[] args) {
        new MusicPlayer();
    }
}