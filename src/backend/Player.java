package backend;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Player in the game
 */
public class Player {


    private String name;

    private String colour;

    private int buses = 17;

    private int points = 0;

    private DestinationTicketCard[] destTicketCards;

    private HashMap<Colour, Integer> transportCards;

    private HashMap<City, ArrayList<Route>> routesClaimed;

    private int numDestTicketCompleted;

    public Player(String name, String colour) {
        this.name = name;
        this.colour = colour;
        transportCards = new HashMap<>();
        routesClaimed = new HashMap<>();

        // TEMPORARY CITIES - TODO: NEED TO MOVE INTO GAME CLASS WHEN MADE
        City[] cities = {City.BAKER_STREET, City.COVENT_GARDEN, City.PICCADILLY_CIRCUS, City.BIG_BEN,
        City.BRICK_LANE, City.BRITISH_MUSEUM, City.BUCKINGHAM_PALACE, City.ELEPHANT_CASTLE,
        City.KINGS_CROSS, City.GLOBE_THEATRE, City.HYDE_PARK, City.THE_CHARTERHOUSE,
        City.TRAFALGAR_SQUARE, City.TOWER_OF_LONDON, City.REGENTS_PARK, City.ST_PAULS,
        City.WATERLOO};

        Colour[] colours = {Colour.GREEN, Colour.YELLOW, Colour.ORANGE, Colour.PINK, Colour.BLACK,
        Colour.MULTI};


        // initialises adjacency list for the routes Player will claim
        for (City city : cities) {
            routesClaimed.put(city, new ArrayList<>());
        }

        //initialises dictionary to store how many of each transport card Player is holding
        for (Colour cardColour: colours) {
            transportCards.put(cardColour, 0);
        }


    }

    /**
     * Method that handles logic when Player claims a Route
     *
     * @param route - route to be claimed
     */
    public void claimRoute(Route route) {
        // Get route colour
        Colour routeColour = route.getColour();

        // Decrement number of cards of that colour in Player's hand by the route length
        transportCards.put(routeColour, transportCards.get(routeColour) - route.getLength());

        // Decrement number of buses by the route length
        this.buses -= route.getLength();

        // Increase player points according to route length
        this.points += route.calculatePoints();
    }

    /**
     * Getter method for Player transportation cards held
     *
     * @return the transportation cards Player has
     */
    public HashMap<Colour, Integer> getTransportCards() {
        return transportCards;
    }

    /**
     * Getter method for Player routes claimed
     *
     * @return the routes claimed by Player in the form of an adjacency list
     */
    public HashMap<City, ArrayList<Route>> getPlayerRoutes() {
        return routesClaimed;
    }
}
