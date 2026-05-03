package frontend;

import backend.Colour;
import backend.Player;
import backend.DestinationTicket;

import javax.swing.*;
import java.awt.*;

public class PlayerInfoPanel extends JPanel {

    private final GameController controller;
    private final JTextArea infoArea;

    public PlayerInfoPanel(GameController controller) {
        this.controller = controller;

        setPreferredSize(new Dimension(260, 600));
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Player Info", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        add(title, BorderLayout.NORTH);
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

        text.append("Destination Tickets:\n");

        if (player.getDestTicketCards().isEmpty()) {
            text.append("  None\n");
        } else {
            for (DestinationTicket ticket : player.getDestTicketCards()) {
                text.append("  ")
                        .append(ticket.getCityA())
                        .append(" -> ")
                        .append(ticket.getCityB())
                        .append(" (")
                        .append(ticket.getPoints())
                        .append(" pts)")
                        .append("\n");
            }
        }
    }
}