package Frontend;

import javax.swing.JPanel;
import java.awt.*;

/**
 * Class that manages the content shown on the GameFrame. Uses a CardLayout to
 * switch between cards (screens) in the game.
 */
public class GameScreenManager extends JPanel {

    private CardLayout layout;

    /**
     * Constructor for the GameScreenManager(), which manages the screens of the game
     */
    public GameScreenManager() {
        layout = new CardLayout();

        setLayout(layout);

    }

    /**
     * Used to display a specified screen
     *
     * @param name the name of the screen to be shown on the frame
     */
    public void showScreen(String name) {
        layout.show(this, name);
    }
}
