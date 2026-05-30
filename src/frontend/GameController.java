package frontend;

import backend.*;

import javax.swing.*;
import java.util.*;
import java.util.List;

public class GameController {

    private Player[] players;
    private int currentPlayerIndex;
    private Route selectedRoute;
    private String message;
    private MapPanel mapPanel;

    private Game game;
    private boolean finalRoundStarted;
    private int finalTurnsRemaining;

    private final TransportationDeck transportDeck;
    private final DestinationTicketCardDeck ticketDeck;
    private JLabel statusLabel;
    private final GameDialogService dialogService;

    public GameController(Player playerOne, Player playerTwo) {
        this.players = new Player[] {playerOne, playerTwo};
        this.currentPlayerIndex = 0;
        this.selectedRoute = null;
        this.message = playerOne.getName() + "'s turn";
        this.game = new Game(playerOne, playerTwo);
        this.finalRoundStarted = false;
        this.finalTurnsRemaining = -1;
        dialogService = new GameDialogService();

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
            List<Integer> kept = dialogService.chooseTickets(player, drawn, 2);
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
        updateStatus();
        return message;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void prepareLoadedGame() {
        transportDeck.turnFaceUp();
    }

    public void setMapPanel(MapPanel mapPanel) {
        this.mapPanel = mapPanel;
    }

    public void setStatusLabel(JLabel statusLabel) {
        this.statusLabel = statusLabel;
    }

    private void updateStatus() {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
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

        // Must select a route
        if (selectedRoute == null) {
            message = "Select a route first.";
            return;
        }

        // If selected route is already claimed, cannot claim
        if (selectedRoute.getOwner() != null) {
            message = "This route has already been claimed.";
            return;
        }

        // Get cards player may use to claim the route with
        HashMap<Colour, Integer> cardsToUse = selectedRoute.canPlayerClaim(currentPlayer);

        // If no cards able to be used, cannot claim
        if (cardsToUse == null) {
            message = selectedRoute.getClaimFailureReason(currentPlayer);
            return;
        }

        // When claiming, set as owner
        selectedRoute.setOwner(currentPlayer);
        // Call claim route logic for points etc.
        currentPlayer.claimRoute(selectedRoute, cardsToUse);

        if (selectedRoute.isLandmarkRoute()) {
            applyLandmarkEffect(selectedRoute, currentPlayer);
        } else {
            message = currentPlayer.getName() + " claimed "
                    + formatCityName(selectedRoute.getCityA().name())
                    + " to "
                    + formatCityName(selectedRoute.getCityB().name());
        }

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
        transportDeck.draw(current, new int[]{choice1});
        if (!firstWasBus) {
            List<TransportCard> faceUpAfterFirst = transportDeck.getFaceUpCards(); // live state
            String[] options2 = new String[faceUpAfterFirst.size() + 1];
            for (int i = 0; i < faceUpAfterFirst.size(); i++) {
                TransportCard card = faceUpAfterFirst.get(i);
                // shows the real card name/colour, or flags locked buses
                options2[i] = !card.canDraw()
                        ? (i + 1) + ": " + card.getName() + " (" + card.getColour() + ") [Bus – locked]"
                        : (i + 1) + ": " + card.getName() + " (" + card.getColour() + ")";
            }
            options2[faceUpAfterFirst.size()] = "Draw from pile";

            int pick2 = JOptionPane.showOptionDialog(
                    null, "Pick second card:", "Draw Transport Cards",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options2, options2[options2.length - 1]);
            if (pick2 < 0 || pick2 >= faceUp.size()) pick2 = faceUp.size();
            choice2 = (pick2 == faceUp.size()) ? -1 : pick2;
        }
        transportDeck.draw(current, new int[]{choice2});
        for (TransportCard card : transportDeck.getFaceUpCards()) {
            card.setCanDraw(true);
        }

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

        List<Integer> kept = dialogService.chooseTickets(current, drawn, 1);
        ticketDeck.getTickets(1, current, kept);
        message = current.getName() + " drew destination tickets.";
        endTurn();
    }


    public void endTurn() {
        Player playerWhoJustPlayed = getCurrentPlayer();

        if (!finalRoundStarted && playerWhoJustPlayed.getBuses() <= 2) {
            finalRoundStarted = true;

            // Every OTHER player gets one final turn
            finalTurnsRemaining = players.length;

            message += " Final round started!";
        } else if (finalRoundStarted) {
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

    public void saveGame() {
        GameSaveManager.saveGame(players, currentPlayerIndex, "saved_game.txt");
        message = "Game saved successfully to saved_game.txt.";
    }

    public void loadGame() {
        currentPlayerIndex =
                GameSaveManager.loadGame(players, mapPanel, "saved_game.txt");

        selectedRoute = null;

        message =
                "Game loaded successfully. Current turn: "
                        + getCurrentPlayer().getName();
    }

    private void applyLandmarkEffect(Route route, Player currentPlayer) {

        switch (route.getLandmarkEffect()) {

            case POINTS_OR_CARDS:

                int choice = JOptionPane.showOptionDialog(
                        null,
                        "Choose your landmark reward.",
                        "Landmark Route",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"+2 Points", "Draw 2 Cards"},
                        null
                );

                if (choice == 0) {
                    currentPlayer.addPoints(2);

                    message = currentPlayer.getName()
                            + " activated a landmark route and gained +2 bonus points.";
                } else {

                    drawBonusTransportCard(currentPlayer);
                    drawBonusTransportCard(currentPlayer);

                    message = currentPlayer.getName()
                            + " activated a landmark route and drew 2 bonus transport cards.";
                }

                break;

            case STEAL_CARD:

                stealRandomCard(currentPlayer);

                break;

            case DETECTIVE_CHOICE:

                int detectiveChoice = JOptionPane.showOptionDialog(
                        null,
                        "Baker Street Detective Bonus: choose your reward.",
                        "Landmark Route",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Draw 1 Card", "+1 Point"},
                        null
                );

                if (detectiveChoice == 0) {
                    drawBonusTransportCard(currentPlayer);

                    message = currentPlayer.getName()
                            + " used the Baker Street Detective Bonus and drew 1 transport card.";
                } else {
                    currentPlayer.addPoints(1);

                    message = currentPlayer.getName()
                            + " used the Baker Street Detective Bonus and gained +1 point.";
                }

                break;

            default:

                message = currentPlayer.getName()
                        + " claimed a landmark route.";

                break;
        }
    }

    private void drawBonusTransportCard(Player player) {
        transportDeck.draw(player, new int[]{-1});
    }

    private void stealRandomCard(Player thief) {
        Player victim = players[0] == thief ? players[1] : players[0];

        ArrayList<Colour> availableColours = new ArrayList<>();

        for (Colour colour : victim.getTransportCards().keySet()) {
            int count = victim.getTransportCards().get(colour);

            for (int i = 0; i < count; i++) {
                availableColours.add(colour);
            }
        }

        if (availableColours.isEmpty()) {
            message = thief.getName() + " activated a landmark route, but "
                    + victim.getName() + " had no cards to steal.";
            return;
        }

        int randomIndex = (int) (Math.random() * availableColours.size());
        Colour stolenColour = availableColours.get(randomIndex);

        victim.setTransportCardCount(
                stolenColour,
                victim.getTransportCards().get(stolenColour) - 1
        );

        thief.setTransportCardCount(
                stolenColour,
                thief.getTransportCards().get(stolenColour) + 1
        );

        message = thief.getName() + " activated a landmark route and stole 1 "
                + stolenColour + " card from " + victim.getName() + ".";
    }

}