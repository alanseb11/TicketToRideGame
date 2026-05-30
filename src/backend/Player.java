package backend;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A Player in the game
 */
public class Player {


    private String name;

    private String colour;

    private int buses = 17;

    private int points = 0;

    private int longestPath = 0;

    private ArrayList<DestinationTicket> destTicketCards;

    private HashMap<Colour, Integer> transportCards;

    private HashMap<City, ArrayList<Route>> routesClaimed;

    private int numDestTicketCompleted;

    public Player(String name, String colour) {
        this.name = name;
        this.colour = colour;
        transportCards = new HashMap<>();
        routesClaimed = new HashMap<>();
        destTicketCards = new ArrayList<>();


        City[] cities = {City.BAKER_STREET, City.COVENT_GARDEN, City.PICCADILLY_CIRCUS, City.BIG_BEN,
        City.BRICK_LANE, City.BRITISH_MUSEUM, City.BUCKINGHAM_PALACE, City.ELEPHANT_CASTLE,
        City.KINGS_CROSS, City.GLOBE_THEATRE, City.HYDE_PARK, City.THE_CHARTERHOUSE,
        City.TRAFALGAR_SQUARE, City.TOWER_OF_LONDON, City.REGENTS_PARK, City.ST_PAULS,
        City.WATERLOO};

        Colour[] colours = {Colour.GREEN, Colour.YELLOW, Colour.ORANGE, Colour.PINK, Colour.BLACK,
        Colour.MULTI, Colour.BLUE};


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
     * @param cardsToUse - cards player uses to claim the route
     */
    public void claimRoute(Route route, HashMap<Colour, Integer> cardsToUse) {

        // Takes away the cards to be used when claiming from player
        for (Colour cardColour: cardsToUse.keySet()) {
            int numCardsUsed = cardsToUse.get(cardColour);
            int numCardsHeld = transportCards.get(cardColour);
            transportCards.put(cardColour, numCardsHeld - numCardsUsed);
            TransportationDeck.getInstance().discard(cardColour, numCardsUsed);
        }

        // Decrement number of buses by the route length
        this.buses -= route.getLength();

        // Increase player points according to route length
        this.points += route.calculatePoints();

        // Add the route to Player's claimed routes adjacency list
        routesClaimed.get(route.getCityA()).add(route);
        routesClaimed.get(route.getCityB()).add(route);
    }


    /**
     * Method that goes through each Destination Ticket card Player is holding
     * and adds/decreases points based on if they have connected the cities or not
     */
    public void calcDestTickets() {
        for (DestinationTicket destTicket: destTicketCards) {
            int destTickerPts = destTicket.getPoints();
            if (destTicket.checkPlayerConnect(this)) {
                numDestTicketCompleted++;
                points += destTickerPts;
            } else {
                points -= destTickerPts;
            }
        }
    }
    /**
     * Method that calculates the longest continuous path from Player's
     * claimed routes. Calls dfs method on all cities to cover all possible start points.
     */
    public void calcLongestPath() {
        for (City city: routesClaimed.keySet()) {
            dfs(city, new ArrayList<>(), 0);
        }
    }


    /**
     * Method to calculate longest continous path from a City. Uses DFS + Backtracking
     * for an exhaustive search of the graph formed by Player's claimed routes.
     *
     * @param city - the city to calculate the path from
     * @param visitedRoutes - stores routes visited in calculations, to prevent repeating it
     * @param currentPathLen - the length of the calculated path
     *
     * GenAI Declaration:
     * GenAI was used to assist with the structure of the algorithm. I used ChatGPT-5 to
     * get an algorithm to find the weight of the longest path in an undirected graph.
     * I then used its output and adapted it to the way Routes are stored.
     */
    public void dfs(City city, List<Route> visitedRoutes, int currentPathLen) {

        longestPath = Math.max(longestPath, currentPathLen);

        for (Route route : routesClaimed.get(city)) {

            //Check if route has been visited
            if  (visitedRoutes.contains(route)) {
                //If it has, then skip
                continue;
            }

            City nextCity;

            // Next city to visit is the other city in the route
            if (route.getCityA().equals(city)) {
                nextCity = route.getCityB();
            } else {
                nextCity = route.getCityA();
            }

            // Get route length
            int length = route.getLength();

            //Mark current route as visited
            visitedRoutes.add(route);

            //Recursive call on city
            dfs(nextCity, visitedRoutes, currentPathLen + length);

            // Remove the route from visited to allow for backtracking
            visitedRoutes.remove(route);
        }
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

    /**
     * Getter method to get Player buses
     *
     * @return number of buses player is holding
     */
    public int getBuses() {
        return buses;
    }

    /**
     * Getter method to get Player's longest path
     *
     * @return the longest path of player
     */
    public int getLongestPath() {
        return longestPath;
    }

    /**
     * Method to add points to player
     *
     * @param points - points to add
     */
    public void addPoints(int points) {
        this.points += points;
    }

    /**
     * Method to add points to player
     *
     * @param points - points to take away
     */
    public void takePoints(int points) {
        this.points -= points;
    }

    /**
     * Method to get points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Getter metho to get number of completed destination tickets
     */
    public int getCompletedDestTickets() {
        return numDestTicketCompleted;
    }

    public String getName() {
        return name;
    }

    public String getColour() {
        return colour;
    }

    public void setBuses(int buses) {
        this.buses = buses;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setTransportCardCount(Colour colour, int amount) {
        transportCards.put(colour, amount);
    }

    public ArrayList<DestinationTicket> getDestTicketCards() {
        return destTicketCards;
    }

    /**
     * Adds a TransportCard to this player's hand.
     *
     * @param card the card received
     */
    public void addTransportCard(TransportCard card) {
        Colour c = card.getColour();
        transportCards.put(c, transportCards.get(c) + 1);
    }

    /**
     * Adds a DestinationTicket to this player's hand.
     *
     * @param ticket the destination ticket to add
     */
    public void addDestinationTicket(DestinationTicket ticket) {
        destTicketCards.add(ticket);
    }
}
