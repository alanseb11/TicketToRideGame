package backend;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

public class Game {

    private List<Player> players;

    //TODO: ADD DECKS

    public Game(Player player1, Player player2) {
        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
    }


    /**
     * Handles end of game. Returns list of Players who
     * win the game.
     */
    public List<Player> endGame() {
        // Add or subtract points depending on if Player completed their Destination Tickets
        for  (Player player : players) {
            player.calcDestTickets();
        }

        // Get all Players with the Longest Path
        List<Player> ownsLongestPath = getPlayersWithMax(Player::getLongestPath, players);

        // Gift all players with the longest path 10 points
        for (Player player : ownsLongestPath) {
            player.addPoints(10);
        }

        // Get all Players with the highest point score
        List<Player> hasMostPoints = getPlayersWithMax(Player::getPoints, players);


        // If only one with most points, they are the winner
        if (hasMostPoints.size() == 1) {
            return hasMostPoints;
        } else {
            // Otherwise, compare Destination Tickets completed. Ties may occur.
            return getPlayersWithMax(Player::getCompletedDestTickets, hasMostPoints);

        }

    }


    /**
     * Helper function. Finds all Players with the highest possible stat for a metric.
     * E.g. if you want to find which Players have the most Destination Ticket Cards completed
     *
     * @param metric - the thing to find the max of (points, destination tickets completed, longest route)
     * @param candidates - the list of players we want to look through
     * @return the list of players who have the max stat
     */
    public List<Player> getPlayersWithMax(ToIntFunction<Player> metric, List<Player> candidates) {
        int max = 0;
        List<Player> playersWithMax = new ArrayList<>();

        // Get the max possible stat
        for (Player player : candidates) {
            max = Math.max(max, metric.applyAsInt(player));
        }

        // Get all Players with the same max scores
        for (Player player : candidates) {
            if (metric.applyAsInt(player) == max) {
                playersWithMax.add(player);
            }
        }

        return playersWithMax;

    }
}
