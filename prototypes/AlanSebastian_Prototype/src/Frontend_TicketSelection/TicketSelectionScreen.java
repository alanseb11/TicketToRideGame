package Frontend_TicketSelection;

import Backend_TicketSelection.DestinationTicket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class TicketSelectionScreen extends JPanel {

    private final JLabel resultLabel;

    public TicketSelectionScreen() {
        setLayout(new BorderLayout());
        setBackground(new Color(244, 239, 229));

        JLabel titleLabel = new JLabel("Ticket to Ride - Destination Ticket Selection Prototype", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(70, 40, 20));
        titleLabel.setBorder(new EmptyBorder(20, 10, 20, 10));
        add(titleLabel, BorderLayout.NORTH);

        DestinationTicket ticket1 = new DestinationTicket("Melbourne", "Sydney", 4);
        DestinationTicket ticket2 = new DestinationTicket("Brisbane", "Perth", 10);
        DestinationTicket ticket3 = new DestinationTicket("Adelaide", "Canberra", 5);

        JCheckBox ticketBox1 = new JCheckBox(ticket1.toString());
        JCheckBox ticketBox2 = new JCheckBox(ticket2.toString());
        JCheckBox ticketBox3 = new JCheckBox(ticket3.toString());

        ticketBox1.setOpaque(false);
        ticketBox2.setOpaque(false);
        ticketBox3.setOpaque(false);

        JLabel instructionLabel = new JLabel("Select destination tickets to keep. You must keep at least 2.");
        instructionLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));

        JButton confirmButton = new JButton("Confirm Selection");
        confirmButton.setFocusPainted(false);
        confirmButton.setPreferredSize(new Dimension(180, 45));
        confirmButton.setBackground(new Color(170, 55, 40));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(new Font("SansSerif", Font.BOLD, 15));

        JButton resetButton = new JButton("Reset");
        resetButton.setFocusPainted(false);
        resetButton.setPreferredSize(new Dimension(120, 45));
        resetButton.setFont(new Font("SansSerif", Font.BOLD, 15));

        resultLabel = new JLabel("<html><b>Waiting for selection...</b></html>");
        resultLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(255, 250, 242));
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(170, 140, 110), 2),
                new EmptyBorder(30, 40, 30, 40)
        ));

        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketBox1.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketBox2.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketBox3.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(confirmButton);
        buttonPanel.add(resetButton);

        centerPanel.add(instructionLabel);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(ticketBox1);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(ticketBox2);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(ticketBox3);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(buttonPanel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(resultLabel);

        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(centerPanel);

        add(wrapperPanel, BorderLayout.CENTER);

        confirmButton.addActionListener(e -> {
            ArrayList<String> selectedTickets = new ArrayList<>();

            if (ticketBox1.isSelected()) {
                selectedTickets.add(ticket1.toString());
            }
            if (ticketBox2.isSelected()) {
                selectedTickets.add(ticket2.toString());
            }
            if (ticketBox3.isSelected()) {
                selectedTickets.add(ticket3.toString());
            }

            if (selectedTickets.size() < 2) {
                resultLabel.setText("<html><font color='red'><b>Invalid selection:</b> You must keep at least 2 destination tickets.</font></html>");
                return;
            }

            StringBuilder keptTickets = new StringBuilder();
            for (int i = 0; i < selectedTickets.size(); i++) {
                keptTickets.append(selectedTickets.get(i));
                if (i < selectedTickets.size() - 1) {
                    keptTickets.append(", ");
                }
            }

            resultLabel.setText("<html><font color='green'><b>Tickets kept:</b> " + keptTickets + "</font></html>");
            System.out.println("Tickets kept: " + keptTickets);
        });

        resetButton.addActionListener(e -> {
            ticketBox1.setSelected(false);
            ticketBox2.setSelected(false);
            ticketBox3.setSelected(false);
            resultLabel.setText("<html><b>Waiting for selection...</b></html>");
        });
    }
}