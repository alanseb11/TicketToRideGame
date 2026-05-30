package frontend;

import backend.DestinationTicket;
import backend.Player;

import javax.swing.*;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class GameDialogService {

    public List<Integer> chooseTickets(Player player, List<DestinationTicket> tickets, int min) {
        JCheckBox[] boxes = new JCheckBox[tickets.size()];
        JPanel panel = new JPanel(new GridLayout(tickets.size() + 1, 1));

        panel.add(new JLabel(player.getName() + " — keep at least " + min + ":"));

        for (int i = 0; i < tickets.size(); i++) {
            DestinationTicket ticket = tickets.get(i);

            String label = ticket.getCityA().name().replace("_", " ")
                    + "  →  "
                    + ticket.getCityB().name().replace("_", " ")
                    + "  ("
                    + ticket.getPoints()
                    + " pts)";

            boxes[i] = new JCheckBox(label, true);
            panel.add(boxes[i]);
        }

        while (true) {
            JOptionPane.showMessageDialog(
                    null,
                    panel,
                    "Destination Tickets",
                    JOptionPane.PLAIN_MESSAGE
            );

            List<Integer> kept = new ArrayList<>();

            for (int i = 0; i < boxes.length; i++) {
                if (boxes[i].isSelected()) {
                    kept.add(i);
                }
            }

            if (kept.size() >= min) {
                return kept;
            }

            JOptionPane.showMessageDialog(
                    null,
                    "You must keep at least " + min + " ticket(s).",
                    "Invalid Selection",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }
}