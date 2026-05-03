package frontend;

import backend.Player;

import javax.swing.JFrame;
import java.awt.BorderLayout;

public class GameFrame extends JFrame {

    private final GameController controller;
    private final MapPanel mapPanel;
    private final PlayerInfoPanel playerInfoPanel;
    private final ActionPanel actionPanel;

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

        add(mapPanel, BorderLayout.CENTER);
        add(playerInfoPanel, BorderLayout.EAST);
        add(actionPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}