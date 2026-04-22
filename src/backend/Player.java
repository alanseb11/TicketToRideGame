package backend;


/**
 * A Player in the game
 */
public class Player {


    private String name;

    private String colour;

    private int buses = 17;

    private int points = 0;

    private DestinationTicketCard[] destTicketCards;

    private TransportCard[] transportCards;

    private Route[]  routesClaimed; //TODO: to be implemented as an adjacency list - Scott

    private int numDestTicketCompleted;

    public Player(String name, String colour) {
        this.name = name;
        this.colour = colour;
    }

    public TransportCard[] getTransportCards() {
        return transportCards;
    }
}
