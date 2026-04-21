package Frontend_TicketSelection;

import javax.swing.JFrame;

public class GameFrame {
    private final JFrame frame;

    public GameFrame() {
        frame = new JFrame("Ticket to Ride Prototype");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }

    public void startUI() {
        frame.add(new TicketSelectionScreen());
        frame.setVisible(true);
    }
}