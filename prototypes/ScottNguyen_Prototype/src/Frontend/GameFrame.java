package Frontend;

import javax.swing.*;

/**
 * Class that represents the main game window to be used for Ticket To Ride
 */
public class GameFrame{
    private JFrame frame;

    /**
     * Constructor for GameFrame class. Creates a new JFrame.
     */
    public GameFrame() {
        frame = new JFrame();
    }

    /**
     * Starts the UI, sets GameFrame attributes. Adds GameScreenManage to the frame.
     */
    public void startUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setTitle("Ticket To Ride");

        frame.add(new GameScreenManager());
    }
}