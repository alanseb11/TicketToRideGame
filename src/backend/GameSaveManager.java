package backend;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GameSaveManager {

    public static void saveGame(Player[] players, int currentPlayerIndex, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("CURRENT_PLAYER=" + currentPlayerIndex);

            for (int i = 0; i < players.length; i++) {
                Player player = players[i];

                writer.println("PLAYER=" + i);
                writer.println("NAME=" + player.getName());
                writer.println("COLOUR=" + player.getColour());
                writer.println("POINTS=" + player.getPoints());
                writer.println("BUSES=" + player.getBuses());

                writer.println("CARDS_START");
                for (Colour colour : player.getTransportCards().keySet()) {
                    writer.println(colour + "=" + player.getTransportCards().get(colour));
                }
                writer.println("CARDS_END");
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not save game.", e);
        }
    }
}