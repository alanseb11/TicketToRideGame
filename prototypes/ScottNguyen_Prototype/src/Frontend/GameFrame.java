package Frontend;

import Backend.Player;

import javax.swing.*;

/**
 * Class that represents the main game window to be used for Ticket To Ride
 */
public class GameFrame{
    private JFrame frame;

    /**
     * Constructor for GameFrame class. Creates a new JFrame.
     */
    public GameFrame(Player player1, Player player2) {
        frame = new JFrame();
    }

    /**
     * Starts the UI, sets GameFrame attributes. Adds GameScreenManage to the frame.
     */
    public void startUI(Player player1, Player player2) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Ticket To Ride");

        frame.add(new GameScreenManager(player1, player2));
        frame.setVisible(true);
    }
}