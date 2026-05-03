package frontend;

import backend.*;

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

        Colour[] drawableColours = {
                Colour.GREEN,
                Colour.BLUE,
                Colour.YELLOW,
                Colour.ORANGE,
                Colour.PINK,
                Colour.BLACK,
                Colour.MULTI
        };

        int randomIndex = (int) (Math.random() * drawableColours.length);
        Colour drawnColour = drawableColours[randomIndex];

        int currentAmount = currentPlayer.getTransportCards().getOrDefault(drawnColour, 0);

        currentPlayer.getTransportCards().put(drawnColour, currentAmount + 1);

        message = currentPlayer.getName() + " drew a " + drawnColour + " transport card.";

        endTurn();
    }

    public void drawDestinationTicket() {
        Player currentPlayer = getCurrentPlayer();

        // Simple hardcoded ticket pool (you can expand later)
        DestinationTicket[] tickets = {
                new DestinationTicket(City.HYDE_PARK, City.BAKER_STREET, 5),
                new DestinationTicket(City.KINGS_CROSS, City.BRICK_LANE, 4),
                new DestinationTicket(City.WATERLOO, City.ELEPHANT_CASTLE, 3),
                new DestinationTicket(City.BIG_BEN, City.BUCKINGHAM_PALACE, 2),
                new DestinationTicket(City.ST_PAULS, City.TOWER_OF_LONDON, 4),
                new DestinationTicket(City.TRAFALGAR_SQUARE, City.COVENT_GARDEN, 2)
        };

        int randomIndex = (int) (Math.random() * tickets.length);
        DestinationTicket drawnTicket = tickets[randomIndex];

        // add to player
        currentPlayer.getDestTicketCards().add(drawnTicket);

        message = currentPlayer.getName() + " drew a destination ticket: "
                + drawnTicket.getCityA() + " → " + drawnTicket.getCityB();

        endTurn();
    }

    private void dealStartingCards() {
        for (Player player : players) {
            for (int i = 0; i < 4; i++) {
                giveRandomTransportCard(player);
            }
        }
    }

    private void giveRandomTransportCard(Player player) {
        Colour[] drawableColours = {
                Colour.GREEN,
                Colour.BLUE,
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
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
        message += " Next turn: " + getCurrentPlayer().getName();
    }

    private String formatCityName(String cityName) {
        return cityName.replace("_", " ");
    }
}