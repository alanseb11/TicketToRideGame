package frontend;

import backend.Colour;
import backend.Player;
import backend.DestinationTicket;

import javax.swing.*;
import java.awt.*;

public class PlayerInfoPanel extends JPanel {

    private final GameController controller;
    private final JTextArea infoArea;
    private final JButton showTicketsButton;

    public PlayerInfoPanel(GameController controller) {
        this.controller = controller;

        setPreferredSize(new Dimension(260, 600));
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Player Info", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        showTicketsButton = new JButton("Show Destination Tickets");
        showTicketsButton.addActionListener(e -> showDestinationTicketsPopup());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(showTicketsButton, BorderLayout.SOUTH);

        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(infoArea), BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        StringBuilder text = new StringBuilder();

        text.append("Current Turn:\n");
        text.append(controller.getCurrentPlayer().getName()).append("\n\n");

        appendPlayer(text, controller.getPlayerOne());
        text.append("\n");
        appendPlayer(text, controller.getPlayerTwo());

        infoArea.setText(text.toString());
    }

    private void appendPlayer(StringBuilder text, Player player) {
        text.append(player.getName()).append("\n");
        text.append("Colour: ").append(player.getColour()).append("\n");
        text.append("Score: ").append(player.getPoints()).append("\n");
        text.append("Buses: ").append(player.getBuses()).append("\n");

        text.append("Cards:\n");
        for (Colour colour : player.getTransportCards().keySet()) {
            text.append("  ")
                    .append(colour)
                    .append(": ")
                    .append(player.getTransportCards().get(colour))
                    .append("\n");
        }

        text.append("Destination Tickets: Hidden\n");
    }

    private void showDestinationTicketsPopup() {
        Player currentPlayer = controller.getCurrentPlayer();

        StringBuilder message = new StringBuilder();
        message.append(currentPlayer.getName()).append("'s Destination Tickets:\n\n");

        if (currentPlayer.getDestTicketCards().isEmpty()) {
            message.append("None");
        } else {
            for (DestinationTicket ticket : currentPlayer.getDestTicketCards()) {
                message.append(ticket.getCityA())
                        .append(" -> ")
                        .append(ticket.getCityB())
                        .append(" (")
                        .append(ticket.getPoints())
                        .append(" pts)")
                        .append("\n");
            }
        }

        JOptionPane.showMessageDialog(
                this,
                message.toString(),
                "Destination Tickets",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}