package Frontend_TicketSelection;

import Backend_TicketSelection.DestinationTicket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

public class TicketSelectionScreen extends JPanel {

    private JLabel resultLabel;
    private Image background;

    public TicketSelectionScreen() {
        setLayout(new BorderLayout());

        URL imageLocation = getClass().getResource("/Frontend_TicketSelection/images/train_background.jpg");
        if (imageLocation != null) {
            background = new ImageIcon(imageLocation).getImage();
        }

        JLabel titleLabel = new JLabel("Ticket to Ride - Destination Ticket Selection", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(Color.BLACK);
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

        ticketBox1.setForeground(Color.BLACK);
        ticketBox2.setForeground(Color.BLACK);
        ticketBox3.setForeground(Color.BLACK);

        ticketBox1.setFont(new Font("SansSerif", Font.PLAIN, 18));
        ticketBox2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        ticketBox3.setFont(new Font("SansSerif", Font.PLAIN, 18));

        JLabel instructionLabel = new JLabel("Select destination tickets to keep. You must keep at least 2.");
        instructionLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        instructionLabel.setForeground(Color.BLACK);

        JButton confirmButton = new JButton("Confirm Selection");
        JButton resetButton = new JButton("Reset");

        resultLabel = new JLabel("Waiting for selection...");
        resultLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        resultLabel.setForeground(Color.BLACK);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(255, 255, 255, 220));
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                new EmptyBorder(30, 40, 30, 40)
        ));

        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketBox1.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketBox2.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketBox3.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
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
                resultLabel.setText("Invalid selection: You must keep at least 2 destination tickets.");
                resultLabel.setForeground(Color.RED);
                return;
            }

            StringBuilder keptTickets = new StringBuilder();
            for (int i = 0; i < selectedTickets.size(); i++) {
                keptTickets.append(selectedTickets.get(i));
                if (i < selectedTickets.size() - 1) {
                    keptTickets.append(", ");
                }
            }

            resultLabel.setText("Tickets kept: " + keptTickets);
            resultLabel.setForeground(new Color(0, 120, 0));

            System.out.println("Tickets kept: " + keptTickets);
        });

        resetButton.addActionListener(e -> {
            ticketBox1.setSelected(false);
            ticketBox2.setSelected(false);
            ticketBox3.setSelected(false);
            resultLabel.setText("Waiting for selection...");
            resultLabel.setForeground(Color.BLACK);
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}