package frontend;

import javax.swing.SwingUtilities;

/**
 * Entry point of the application.
 *
 * This class launches the GUI by creating the StartScreen
 * on the Event Dispatch Thread (EDT), ensuring thread-safe
 * execution of Swing components.
 */
public class Main {

    /**
     * Main method that starts the program.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartScreen::new);
    }
}