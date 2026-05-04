package frontend;

import backend.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The GameController manages the overall game flow and logic.
 *
 * It coordinates player turns, route selection, card drawing,
 * ticket selection, and determines when the game ends.
 *
 * It acts as the bridge between the frontend UI and backend game logic.
 */
public class GameController {

    /**
     * Array of players in the game.
     */
    private Player[] players;

    /**
     * Index of the current player in the players array.
     */
    private int currentPlayerIndex;

    /**
     * The currently selected route on the map.
     */
    private Route selectedRoute;

    /**
     * Message displayed to the UI for feedback.
     */
    private String message;

    /**
     * Backend game logic handler.
     */
    private Game game;

    /**
     * Indicates if the final round has started.
     */
    private boolean finalRoundStarted;

    /**
     * Remaining turns once final round begins.
     */
    private int finalTurnsRemaining;

    /**
     * Deck for transport cards.
     */
    private final TransportationDeck transportDeck;

    /**
     * Deck for destination ticket cards.
     */
    private final DestinationTicketCardDeck ticketDeck;

    /**
     * Constructs the GameController with two players.
     *
     * @param playerOne first player
     * @param playerTwo second player
     */
    public GameController(Player playerOne, Player playerTwo) {
        this.players = new Player[] {playerOne, playerTwo};
        this.currentPlayerIndex = 0;
        this.selectedRoute = null;
        this.message = playerOne.getName() + "'s turn";
        this.game = new Game(playerOne, playerTwo);
        this.finalRoundStarted = false;
        this.finalTurnsRemaining = -1;

        TransportationDeck.reset();
        DestinationTicketCardDeck.reset();

        transportDeck = TransportationDeck.getInstance();
        ticketDeck    = DestinationTicketCardDeck.getInstance();
    }

    /**
     * Sets up the game by dealing initial cards and
     * allowing players to choose their starting destination tickets.
     */
    public void setup() {
        for (Player player : players) {
            transportDeck.deal(player);
        }

        transportDeck.turnFaceUp();

        // Each player must keep at least 2 tickets
        for (Player player : players) {
            List<DestinationTicket> drawn = ticketDeck.getTickets(2, player);
            List<Integer> kept = chooseTickets(player, drawn, 2);
            ticketDeck.getTickets(2, player, kept);
        }
    }

    /**
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    /**
     * @return player one
     */
    public Player getPlayerOne() {
        return players[0];
    }

    /**
     * @return player two
     */
    public Player getPlayerTwo() {
        return players[1];
    }

    /**
     * @return the currently selected route
     */
    public Route getSelectedRoute() {
        return selectedRoute;
    }

    /**
     * @return current game message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the selected route and updates the message.
     *
     * @param route the route selected by the player
     */
    public void selectRoute(Route route) {
        selectedRoute = route;

        if (route == null) {
            message = "No route selected";
        } else {
            message = "Selected route: "
                    + formatCityName(route.getCityA().name())
                    + " to "
                    + formatCityName(route.getCityB().name());
        }
    }

    /**
     * Attempts to claim the currently selected route.
     * Validates ownership and required resources before claiming.
     */
    public void claimSelectedRoute() {
        Player currentPlayer = getCurrentPlayer();

        if (selectedRoute == null) {
            message = "Select a route first.";
            return;
        }

        if (selectedRoute.getOwner() != null) {
            message = "This route has already been claimed.";
            return;
        }

        HashMap<Colour, Integer> cardsToUse = selectedRoute.canPlayerClaim(currentPlayer);

        if (cardsToUse == null) {
            message = currentPlayer.getName() + " does not have enough cards or buses.";
            return;
        }

        selectedRoute.setOwner(currentPlayer);
        currentPlayer.claimRoute(selectedRoute, cardsToUse);

        message = currentPlayer.getName() + " claimed "
                + formatCityName(selectedRoute.getCityA().name())
                + " to "
                + formatCityName(selectedRoute.getCityB().name());

        selectedRoute = null;
        endTurn();
    }

    /**
     * Allows the player to draw transport cards via UI selection.
     * Handles special rule where a wildcard (MULTI) card ends the draw.
     */
    public void drawTransportCard() {
        Player current = getCurrentPlayer();
        List<TransportCard> faceUp = transportDeck.getFaceUpCards();

        String[] options = new String[faceUp.size() + 1];

        for (int i = 0; i < faceUp.size(); i++) {
            options[i] = (i + 1) + ": " + faceUp.get(i).getName()
                    + " (" + faceUp.get(i).getColour() + ")";
        }

        options[faceUp.size()] = "Draw from pile";

        int pick1 = JOptionPane.showOptionDialog(
                null, "Pick first card:", "Draw Transport Cards",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[options.length - 1]);

        if (pick1 < 0 || pick1 >= faceUp.size()) pick1 = faceUp.size();
        int choice1 = (pick1 == faceUp.size()) ? -1 : pick1;

        boolean firstWasBus = (choice1 >= 0)
                && faceUp.get(choice1).getColour() == Colour.MULTI;

        int choice2 = -1;

        if (!firstWasBus) {
            String[] options2 = options.clone();

            if (choice1 >= 0 && choice1 < faceUp.size()) {
                options2[choice1] = (choice1 + 1) + ": ? (replacement card)";
            }

            int pick2 = JOptionPane.showOptionDialog(
                    null, "Pick second card:", "Draw Transport Cards",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options2, options2[options2.length - 1]);

            if (pick2 < 0 || pick2 >= faceUp.size()) pick2 = faceUp.size();
            choice2 = (pick2 == faceUp.size()) ? -1 : pick2;
        }

        transportDeck.draw(current, new int[]{choice1, choice2});
        message = current.getName() + " drew transport cards.";
        endTurn();
    }

    /**
     * Allows the player to draw and choose destination tickets.
     */
    public void drawDestinationTicket() {
        Player current = getCurrentPlayer();
        List<DestinationTicket> drawn = ticketDeck.getTickets(1, current);

        if (drawn.isEmpty()) {
            message = "No destination tickets remaining.";
            return;
        }

        List<Integer> kept = chooseTickets(current, drawn, 1);
        ticketDeck.getTickets(1, current, kept);

        message = current.getName() + " drew destination tickets.";
        endTurn();
    }

    /**
     * Displays a UI for selecting which destination tickets to keep.
     *
     * @param player the player choosing tickets
     * @param tickets the tickets drawn
     * @param min minimum number of tickets to keep
     * @return list of indices representing kept tickets
     */
    private List<Integer> chooseTickets(Player player,
                                        List<DestinationTicket> tickets, int min) {

        JCheckBox[] boxes = new JCheckBox[tickets.size()];
        JPanel panel = new JPanel(new GridLayout(tickets.size() + 1, 1));

        panel.add(new JLabel(player.getName() + " — keep at least " + min + ":"));

        for (int i = 0; i < tickets.size(); i++) {
            DestinationTicket t = tickets.get(i);

            String label = formatCityName(t.getCityA().name())
                    + "  →  " + formatCityName(t.getCityB().name())
                    + "  (" + t.getPoints() + " pts)";

            boxes[i] = new JCheckBox(label, true);
            panel.add(boxes[i]);
        }

        while (true) {
            JOptionPane.showMessageDialog(
                    null, panel, "Destination Tickets", JOptionPane.PLAIN_MESSAGE);

            List<Integer> kept = new ArrayList<>();

            for (int i = 0; i < boxes.length; i++) {
                if (boxes[i].isSelected()) kept.add(i);
            }

            if (kept.size() >= min) {
                return kept;
            }

            JOptionPane.showMessageDialog(
                    null,
                    "You must keep at least " + min + " ticket(s).",
                    "Invalid Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Ends the current player's turn and switches to the next player.
     * Also handles final round logic and game termination.
     */
    public void endTurn() {
        Player playerWhoJustPlayed = getCurrentPlayer();

        if (!finalRoundStarted && playerWhoJustPlayed.getBuses() <= 2) {
            finalRoundStarted = true;
            finalTurnsRemaining = players.length;
            message += " Final round started!";
        }

        if (finalRoundStarted) {
            finalTurnsRemaining--;

            if (finalTurnsRemaining <= 0) {
                endGame();
                return;
            }
        }

        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
        message += " Next turn: " + getCurrentPlayer().getName();
    }

    /**
     * Formats enum-style city names into readable strings.
     *
     * @param cityName raw enum name
     * @return formatted city name
     */
    private String formatCityName(String cityName) {
        return cityName.replace("_", " ");
    }

    /**
     * Handles end-of-game scoring and displays results.
     */
    private void endGame() {
        for (Player player : players) {
            player.calcLongestPath();
        }

        List<Player> winners = game.endGame();

        StringBuilder result = new StringBuilder();

        if (winners.size() == 1) {
            result.append("Winner: ")
                    .append(winners.get(0).getName())
                    .append("\n\n");
        } else {
            result.append("Draw between:\n");
            for (Player winner : winners) {
                result.append("- ").append(winner.getName()).append("\n");
            }
            result.append("\n");
        }

        result.append("Final Scores:\n");
        for (Player player : players) {
            result.append(player.getName())
                    .append(": ")
                    .append(player.getPoints())
                    .append(" points\n");
        }

        JOptionPane.showMessageDialog(
                null,
                result.toString(),
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE
        );

        message = "Game over.";
    }
}