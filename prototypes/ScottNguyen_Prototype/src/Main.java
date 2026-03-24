import Backend.Player;
import Frontend.GameFrame;

public class Main {

    public static void main(String[] args) {
        Player player1 = new Player();
        Player player2 = new Player();
        GameFrame gameFrame = new GameFrame(player1, player2);
        gameFrame.startUI(player1, player2);
    }
}