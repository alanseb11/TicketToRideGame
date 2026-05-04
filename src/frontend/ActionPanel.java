package frontend;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;

public class ActionPanel extends JPanel {

    private final GameController controller;
    private final MapPanel mapPanel;
    private final PlayerInfoPanel playerInfoPanel;
    private final JLabel messageLabel;

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

        drawCardButton.addActionListener(e -> {
            controller.drawTransportCard();
            refresh();
        });

        claimRouteButton.addActionListener(e -> {
            controller.claimSelectedRoute();
            refresh();
        });

        drawTicketButton.addActionListener(e -> {
            controller.drawDestinationTicket();
            refresh();
        });

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

    public void refresh() {
        messageLabel.setText(controller.getMessage());
        mapPanel.repaint();
        playerInfoPanel.refresh();
    }
}