package frontend;

import backend.Player;

import javax.swing.JFrame;
import java.awt.BorderLayout;

/**
 * The GameFrame represents the main window of the game.
 *
 * It initializes and arranges all major UI components including:
 * the map display, player information panel, and action panel.
 *
 * It also creates the GameController and starts the game setup process.
 */
public class GameFrame extends JFrame {

    /**
     * Controller responsible for managing game logic and state.
     */
    private final GameController controller;

    /**
     * Panel displaying the game map and routes.
     */
    private final MapPanel mapPanel;

    /**
     * Panel displaying player information such as points and cards.
     */
    private final PlayerInfoPanel playerInfoPanel;

    /**
     * Panel containing player action buttons and messages.
     */
    private final ActionPanel actionPanel;

    /**
     * Constructs the main game window and initializes all components.
     *
     * @param playerOne the first player
     * @param playerTwo the second player
     */
    public GameFrame(Player playerOne, Player playerTwo) {
        setTitle("Train To Ride");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        controller = new GameController(playerOne, playerTwo);

        mapPanel = new MapPanel(controller);
        playerInfoPanel = new PlayerInfoPanel(controller);
        actionPanel = new ActionPanel(controller, mapPanel, playerInfoPanel);

        // Layout configuration
        add(mapPanel, BorderLayout.CENTER);
        add(playerInfoPanel, BorderLayout.EAST);
        add(actionPanel, BorderLayout.SOUTH);

        setVisible(true);

        // Initial game setup and UI refresh
        controller.setup();
        playerInfoPanel.refresh();
        mapPanel.repaint();
        actionPanel.refresh();
    }
}