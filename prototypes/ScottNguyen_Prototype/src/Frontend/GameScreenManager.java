package Frontend;

import javax.swing.JPanel;
import java.awt.*;

public class GameScreenManager extends JPanel {

    private CardLayout layout;

    public GameScreenManager() {
        layout = new CardLayout();

        setLayout(layout);

    }

    public void showScreen(String name) {
        layout.show(this, name);
    }
}
