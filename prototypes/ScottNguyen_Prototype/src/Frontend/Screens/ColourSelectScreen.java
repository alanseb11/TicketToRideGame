package Frontend.Screens;

import Backend.Player;
import Frontend.GameScreenManager;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ColourSelectScreen extends JPanel {

    private Image background;

    public ColourSelectScreen(GameScreenManager gsm, Player player1, Player player2) {
        URL imageLocation = TitleScreen.class.getResource("/Frontend/Images/TitleScreenImg.jpg");
        this.background = new ImageIcon(imageLocation).getImage();

        String[] colours = {"Blue", "Red", "Green", "Yellow", "Black"};

        JComboBox<String> colourSelector1 = new JComboBox<>(colours);
        JComboBox<String> colourSelector2 = new JComboBox<>(colours);

        JLabel colourLabel1 = new JLabel("Player 1 Colour:");
        colourLabel1.setBackground(Color.WHITE);
        colourLabel1.setOpaque(true);
        JLabel colourLabel2 = new JLabel("Player 2 Colour:");
        colourLabel2.setBackground(Color.WHITE);
        colourLabel2.setOpaque(true);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setPreferredSize(new Dimension(100, 50));
        confirmButton.setFocusable(false);
        confirmButton.addActionListener(e -> {
            player1.setColour(colourSelector1.getSelectedItem().toString());
            player2.setColour(colourSelector2.getSelectedItem().toString());
            System.out.println(player1.getColour());
            System.out.println(player2.getColour());
        });
        setLayout(new GridBagLayout()); // set layout of whole page to border to position things

        // create panel to store all colour options
        JPanel colourPanel = new JPanel();
        colourPanel.setLayout(new GridLayout(2,2,10,10));
        colourPanel.add(colourLabel1);
        colourPanel.add(colourSelector1);
        colourPanel.add(colourLabel2);
        colourPanel.add(colourSelector2);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(confirmButton);

        colourPanel.setOpaque(false);
        buttonPanel.setOpaque(false);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(colourPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(buttonPanel);
        contentPanel.setOpaque(false);

        add(contentPanel);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}
