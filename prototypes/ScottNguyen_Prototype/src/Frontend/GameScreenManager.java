package Frontend;

import Backend.Player;
import Frontend.Screens.ColourSelectScreen;
import Frontend.Screens.TitleScreen;

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
    public GameScreenManager(Player player1, Player player2) {
        layout = new CardLayout();

        setLayout(layout);

        add(new TitleScreen(this), "TITLE"); // add the title screen
        add(new ColourSelectScreen(this, player1, player2), "COLOURSELECT");// add the colour select screen

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
