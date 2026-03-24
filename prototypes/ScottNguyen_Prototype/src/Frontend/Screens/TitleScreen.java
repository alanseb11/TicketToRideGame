package Frontend.Screens;

import Frontend.GameScreenManager;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class TitleScreen extends JPanel{

    private JButton startButton;

    private Image background;

    public TitleScreen(GameScreenManager gsm) {

        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(100, 50));
        startButton.setFocusable(false);
        startButton.addActionListener(e -> {
            gsm.showScreen("COLOURSELECT");
        });
        URL imageLocation = TitleScreen.class.getResource("/Frontend/Images/TitleScreenImg.jpg");
        this.background = new ImageIcon(imageLocation).getImage();

        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 400));

        add(startButton);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }

}
