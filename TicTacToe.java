import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.sound.sampled.*;

public class TicTacToe implements ActionListener {
    private JFrame frame;
    private JPanel panel;
    private JButton[] buttons;
    private JButton newGameButton;
    private JButton resetButton;
    private JButton exitButton;
    private JLabel scoreLabel;
    private int playerXScore;
    private int playerOScore;
    private boolean playerX;
    private Clip winSound;
    private Clip drawSound;

    public TicTacToe() {
        frame = new JFrame();
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Tic Tac Toe");

        panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3));

        buttons = new JButton[9];
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
            buttons[i].setBackground(Color.WHITE); // Set button background color
            buttons[i].setForeground(Color.BLACK); // Set button text color
            buttons[i].addActionListener(this);
            panel.add(buttons[i]);
        }

        newGameButton = new JButton("New Game");
        resetButton = new JButton("Reset");
        exitButton = new JButton("Exit");

        newGameButton.addActionListener(this);
        resetButton.addActionListener(this);
        exitButton.addActionListener(this);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(newGameButton);
        controlPanel.add(resetButton);
        controlPanel.add(exitButton);

        scoreLabel = new JLabel("Score - Player X: " + playerXScore + " | Player O: " + playerOScore);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));

        frame.add(panel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.add(scoreLabel, BorderLayout.NORTH);
        frame.setVisible(true);

        playerX = true;
        playerXScore = 0;
        playerOScore = 0;

        // Load win sound
        try {
            File winSoundFile = new File("C:\\Users\\Nanni\\Downloads\\iron_nee_vanchala_enti.wav");
            AudioInputStream winAudioInputStream = AudioSystem.getAudioInputStream(winSoundFile);
            winSound = AudioSystem.getClip();
            winSound.open(winAudioInputStream);

            File drawSoundFile = new File("C:\\Users\\Nanni\\Downloads\\oh_no_no.wav");
            AudioInputStream drawAudioInputStream = AudioSystem.getAudioInputStream(drawSoundFile);
            drawSound = AudioSystem.getClip();
            drawSound.open(drawAudioInputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newGameButton) {
            resetGame();
            resetScore(); // Reset score when starting a new game
        } else if (e.getSource() == resetButton) {
            resetGame();
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        } else {
            JButton clickedButton = (JButton) e.getSource();
            if (clickedButton.getText().equals("")) {
                if (playerX) {
                    clickedButton.setText("X");
                    clickedButton.setForeground(Color.RED); // Set X color
                } else {
                    clickedButton.setText("O");
                    clickedButton.setForeground(Color.BLUE); // Set O color
                }
                playerX = !playerX;
                if (checkForWin()) {
                    playWinSound();
                    if (playerX) {
                        playerOScore++;
                        JOptionPane.showMessageDialog(frame, "Player O wins!");
                    } else {
                        playerXScore++;
                        JOptionPane.showMessageDialog(frame, "Player X wins!");
                    }
                    updateScore();
                    resetGame();
                } else if (isBoardFull()) {
                    playDrawSound(); // Play draw sound
                    JOptionPane.showMessageDialog(frame, "It's a draw!");
                    resetGame();
                }
            }
        }
    }

    private boolean checkForWin() {
        String[] board = new String[9];
        for (int i = 0; i < 9; i++) {
            board[i] = buttons[i].getText();
        }
        for (int i = 0; i < 3; i++) {
            if (board[i*3].equals(board[i*3+1]) && board[i*3].equals(board[i*3+2]) && !board[i*3].equals("")) {
                return true;
            }
            if (board[i].equals(board[i+3]) && board[i].equals(board[i+6]) && !board[i].equals("")) {
                return true;
            }
        }
        if (board[0].equals(board[4]) && board[0].equals(board[8]) && !board[0].equals("")) {
            return true;
        }
        return board[2].equals(board[4]) && board[2].equals(board[6]) && !board[2].equals("");
    }

    private boolean isBoardFull() {
        for (JButton button : buttons) {
            if (button.getText().equals("")) {
                return false;
            }
        }
        return true;
    }

    private void resetGame() {
        for (JButton button : buttons) {
            button.setText("");
            button.setBackground(Color.WHITE); // Reset button background color
            button.setForeground(Color.BLACK); // Reset button text color
        }
        playerX = true;
    }

    private void playWinSound() {
        try {
            winSound.setFramePosition(0); // Rewind to the beginning
            winSound.start(); // Start playing the sound
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void playDrawSound() {
        try {
            drawSound.setFramePosition(0); // Rewind to the beginning
            drawSound.start(); // Start playing the sound
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateScore() {
        scoreLabel.setText("Score - Player X: " + playerXScore + " | Player O: " + playerOScore);
    }

    private void resetScore() {
        playerXScore = 0;
        playerOScore = 0;
        updateScore();
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}

