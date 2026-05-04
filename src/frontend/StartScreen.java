package frontend;

import backend.Player;

import javax.swing.*;
import java.awt.*;

/**
 * The StartScreen represents the initial screen of the game.
 *
 * It allows players to enter their names and choose their colours
 * before starting the game.
 */
public class StartScreen extends JFrame {

    /**
     * Text field for Player 1's name.
     */
    private JTextField playerOneNameField;

    /**
     * Text field for Player 2's name.
     */
    private JTextField playerTwoNameField;

    /**
     * Dropdown for Player 1's colour selection.
     */
    private JComboBox<String> playerOneColourBox;

    /**
     * Dropdown for Player 2's colour selection.
     */
    private JComboBox<String> playerTwoColourBox;

    /**
     * Constructs the start screen and initializes the UI.
     */
    public StartScreen() {
        setTitle("Train To Ride - Start Game");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setupUI();

        setVisible(true);
    }

    /**
     * Sets up all UI components for the start screen.
     */
    private void setupUI() {
        JPanel mainPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel("Start Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        playerOneNameField = new JTextField("Player 1");
        playerTwoNameField = new JTextField("Player 2");

        String[] colours = {"blue", "red", "green", "yellow", "black", "orange", "pink"};

        playerOneColourBox = new JComboBox<>(colours);
        playerTwoColourBox = new JComboBox<>(colours);
        playerTwoColourBox.setSelectedItem("red");

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> startGame());

        add(titleLabel, BorderLayout.NORTH);

        mainPanel.add(new JLabel("Player 1 Name:"));
        mainPanel.add(playerOneNameField);

        mainPanel.add(new JLabel("Player 1 Colour:"));
        mainPanel.add(playerOneColourBox);

        mainPanel.add(new JLabel("Player 2 Name:"));
        mainPanel.add(playerTwoNameField);

        mainPanel.add(new JLabel("Player 2 Colour:"));
        mainPanel.add(playerTwoColourBox);

        mainPanel.add(new JLabel(""));
        mainPanel.add(startButton);

        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Validates input and starts the game.
     *
     * Ensures both players have names and different colours,
     * then creates Player objects and launches the GameFrame.
     */
    private void startGame() {
        String playerOneName = playerOneNameField.getText().trim();
        String playerTwoName = playerTwoNameField.getText().trim();

        String playerOneColour = (String) playerOneColourBox.getSelectedItem();
        String playerTwoColour = (String) playerTwoColourBox.getSelectedItem();

        // Validation: names must not be empty
        if (playerOneName.isEmpty() || playerTwoName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both players need a name.");
            return;
        }

        // Validation: colours must be different
        if (playerOneColour.equals(playerTwoColour)) {
            JOptionPane.showMessageDialog(this, "Players must choose different colours.");
            return;
        }

        Player playerOne = new Player(playerOneName, playerOneColour);
        Player playerTwo = new Player(playerTwoName, playerTwoColour);

        // Close start screen and launch game
        dispose();
        new GameFrame(playerOne, playerTwo);
    }
}