package frontend;

import backend.Player;
import backend.Route;

public class GameController {

    private Player[] players;
    private int currentPlayerIndex;
    private Route selectedRoute;
    private String message;

    public GameController(Player playerOne, Player playerTwo) {
        this.players = new Player[] {playerOne, playerTwo};
        this.currentPlayerIndex = 0;
        this.selectedRoute = null;
        this.message = playerOne.getName() + "'s turn";
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
        Player currentPlayer = getCurrentPlayer();

        // Temporary frontend behaviour until Deck is connected.
        // This gives the current player one wildcard card.
        currentPlayer.getTransportCards().put(
                backend.Colour.MULTI,
                currentPlayer.getTransportCards().get(backend.Colour.MULTI) + 1
        );

        message = currentPlayer.getName() + " drew a transport card.";
        endTurn();
    }

    public void drawDestinationTicket() {
        Player currentPlayer = getCurrentPlayer();

        // Temporary placeholder until Destination Ticket deck is connected.
        message = currentPlayer.getName() + " drew destination tickets.";
        endTurn();
    }

    public void endTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
        message += " Next turn: " + getCurrentPlayer().getName();
    }

    private String formatCityName(String cityName) {
        return cityName.replace("_", " ");
    }
}