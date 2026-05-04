package frontend;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;

/**
 * The ActionPanel represents the set of controls available to the player during their turn.
 *
 * It provides buttons for core gameplay actions such as:
 * drawing transport cards, claiming routes, drawing destination tickets,
 * and ending the turn.
 *
 * It also displays feedback messages from the GameController.
 */
public class ActionPanel extends JPanel {

    /**
     * The main game controller handling game logic.
     */
    private final GameController controller;

    /**
     * Reference to the map panel for repainting updates.
     */
    private final MapPanel mapPanel;

    /**
     * Reference to the player info panel for refreshing player data.
     */
    private final PlayerInfoPanel playerInfoPanel;

    /**
     * Label used to display messages to the player.
     */
    private final JLabel messageLabel;

    /**
     * Constructs an ActionPanel with buttons for player actions and a message display.
     *
     * @param controller the game controller managing game logic
     * @param mapPanel the panel displaying the game map
     * @param playerInfoPanel the panel displaying player information
     */
    public ActionPanel(GameController controller, MapPanel mapPanel, PlayerInfoPanel playerInfoPanel) {
        this.controller = controller;
        this.mapPanel = mapPanel;
        this.playerInfoPanel = playerInfoPanel;

        setLayout(new FlowLayout());

        JButton drawCardButton = new JButton("Draw Transport Card");
        JButton claimRouteButton = new JButton("Claim Selected Route");
        JButton drawTicketButton = new JButton("Draw Destination Ticket");
        JButton endTurnButton = new JButton("End Turn");

        messageLabel = new JLabel(controller.getMessage());

        // Action: Draw a transport card
        drawCardButton.addActionListener(e -> {
            controller.drawTransportCard();
            refresh();
        });

        // Action: Claim the currently selected route
        claimRouteButton.addActionListener(e -> {
            controller.claimSelectedRoute();
            refresh();
        });

        // Action: Draw a destination ticket
        drawTicketButton.addActionListener(e -> {
            controller.drawDestinationTicket();
            refresh();
        });

        // Action: End the current player's turn
        endTurnButton.addActionListener(e -> {
            controller.endTurn();
            refresh();
        });

        add(drawCardButton);
        add(claimRouteButton);
        add(drawTicketButton);
        add(endTurnButton);
        add(messageLabel);
    }

    /**
     * Refreshes the UI components of the panel.
     *
     * Updates the message label, repaints the map,
     * and refreshes player information.
     */
    public void refresh() {
        messageLabel.setText(controller.getMessage());
        mapPanel.repaint();
        playerInfoPanel.refresh();
    }
}