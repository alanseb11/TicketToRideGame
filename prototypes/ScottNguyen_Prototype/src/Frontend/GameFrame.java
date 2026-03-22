package Frontend;

import javax.swing.*;

public class GameFrame{
    private JFrame frame;

    public GameFrame() {
        frame = new JFrame();
    }

    public void startUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setTitle("Ticket To Ride");
    }
}