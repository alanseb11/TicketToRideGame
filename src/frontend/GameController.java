package frontend;

import backend.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GameController {

    private Player[] players;
    private int currentPlayerIndex;
    private Route selectedRoute;
    private String message;

    private Game game;
    private boolean finalRoundStarted;
    private int finalTurnsRemaining;

    private final TransportationDeck transportDeck;
    private final DestinationTicketCardDeck ticketDeck;

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
     * Deals starting hands and runs initial destination-ticket selection dialogs.
     * Called once from GameFrame after the window is visible.
     */
    public void setup() {
        for (Player player : players) {
            transportDeck.deal(player);
        }
        transportDeck.turnFaceUp();

        // Each player must keep at least 2 of their 3 drawn starting tickets
        for (Player player : players) {
            List<DestinationTicket> drawn = ticketDeck.getTickets(2, player);
            List<Integer> kept = chooseTickets(player, drawn, 2);
            ticketDeck.getTickets(2, player, kept);
        }

    }

    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    public Player getPlayerOne() {
        return players[0];
    }

    public Player getPlayerTwo() {
        return players[1];
    }

    public Route getSelectedRoute() {
        return selectedRoute;
    }

    public String getMessage() {
        return message;
    }

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

        if (!selectedRoute.canPlayerClaim(currentPlayer)) {
            message = currentPlayer.getName() + " does not have enough cards or buses.";
            return;
        }

        selectedRoute.setOwner(currentPlayer);
        currentPlayer.claimRoute(selectedRoute);

        message = currentPlayer.getName() + " claimed "
                + formatCityName(selectedRoute.getCityA().name())
                + " to "
                + formatCityName(selectedRoute.getCityB().name());

        selectedRoute = null;
        endTurn();
    }

    public void drawTransportCard() {
        Player current = getCurrentPlayer();
        List<TransportCard> faceUp = transportDeck.getFaceUpCards();

        // Build option labels: one per face-up card, plus a blind-draw option
        String[] options = new String[faceUp.size() + 1];
        for (int i = 0; i < faceUp.size(); i++) {
            options[i] = (i + 1) + ": " + faceUp.get(i).getName()
                    + " (" + faceUp.get(i).getColour() + ")";
        }
        options[faceUp.size()] = "Draw from pile";

        // First pick
        int pick1 = JOptionPane.showOptionDialog(
                null, "Pick first card:", "Draw Transport Cards",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[options.length - 1]);
        if (pick1 < 0 || pick1 >= faceUp.size()) pick1 = faceUp.size();
        int choice1 = (pick1 == faceUp.size()) ? -1 : pick1;

        // A face-up Bus counts as both draws — skip second pick
        boolean firstWasBus = (choice1 >= 0)
                && faceUp.get(choice1).getColour() == Colour.MULTI;

        int choice2 = -1; // default: blind (ignored by deck if first was a bus)
        if (!firstWasBus) {
            int pick2 = JOptionPane.showOptionDialog(
                    null, "Pick second card:", "Draw Transport Cards",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[options.length - 1]);
            if (pick2 < 0 || pick2 >= faceUp.size()) pick2 = faceUp.size();
            choice2 = (pick2 == faceUp.size()) ? -1 : pick2;
        }

        transportDeck.draw(current, new int[]{choice1, choice2});
        message = current.getName() + " drew transport cards.";
        endTurn();
    }

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


    private void dealStartingCards() {
        for (Player player : players) {
            for (int i = 0; i < 4; i++) {
                giveRandomTransportCard(player);
            }
        }
    }

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

    private void giveRandomTransportCard(Player player) {
        Colour[] drawableColours = {
                Colour.GREEN,
                Colour.YELLOW,
                Colour.ORANGE,
                Colour.PINK,
                Colour.BLACK,
                Colour.MULTI
        };

        int randomIndex = (int) (Math.random() * drawableColours.length);
        Colour drawnColour = drawableColours[randomIndex];

        int currentAmount = player.getTransportCards().getOrDefault(drawnColour, 0);
        player.getTransportCards().put(drawnColour, currentAmount + 1);
    }

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

    private String formatCityName(String cityName) {
        return cityName.replace("_", " ");
    }

    private void endGame() {
        for (Player player : players) {
            player.calcLongestPath();
        }

        java.util.List<Player> winners = game.endGame();

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

        javax.swing.JOptionPane.showMessageDialog(
                null,
                result.toString(),
                "Game Over",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
        );

        message = "Game over.";
    }
}