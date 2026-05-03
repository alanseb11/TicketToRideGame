package backend;

import java.util.HashMap;

public class Route{

    private Colour colour;

    private City cityA;

    private City cityB;

    private int length;

    private Player owner = null;

    private Route doubleRoute;

    /**
     * Sets the owner of this route when a player claims it.
     *
     * @param owner the player who has claimed this route
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * Getter for the owner of this route.
     *
     * @return the player who claimed this route, or null if unclaimed
     */
    public Player getOwner() {
        return owner;
    }

    public Route(Colour colour, City cityA, City cityB, int length, Route doubleRoute) {
        this.colour = colour;
        this.cityA = cityA;
        this.cityB = cityB;
        this.length = length;
        this.doubleRoute = doubleRoute;
    }

    /**
     * Calculates the amount of points gained when this route is claimed
     *
     * @return the number of points gained if this is claimed
     */
    public int calculatePoints() {
        if (this.length == 1) {
            return 1;
        } else if (this.length == 2) {
            return 2;
        } else if (this.length == 3) {
            return 4;
        } else if (this.length == 4) {
            return 7;
        } else {
            return 0;
        }
    };

    /**
     * Checks if Player can claim this route based on Transport Cards they are holding
     *
     * @param player - the player trying to claim the route
     * @return a boolean determining if they can claim or not
     */
    public boolean canPlayerClaim(Player player) {
        // Get Player's transportation cards
        HashMap<Colour, Integer> transportCards = player.getTransportCards();

        // Get number of cards Player has matching the route's colour
        Integer numCards = transportCards.get(colour);

        // If not enough same colour cards, check if the multicolour cards can be used
        if (numCards < length) {
            numCards += transportCards.get(Colour.MULTI);
        }
        // If enough same colour cards and buses, then can claim
        if (this.owner != null) {
            // This route has already been claimed
            return false;
        }

        // If enough same colour cards and buses, then can claim
        if (doubleRoute != null) {
            // If a double route exists, can claim if the other route has not been claimed
            return numCards >= this.length && doubleRoute.owner == null &&
                    player.getBuses() >= this.length;
        } else {
            return numCards >= this.length && player.getBuses() >= this.length;
        }

    }

    /**
     * Getter method for Route colour
     *
     * @return the Route's colour
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Getter method for Route length
     *
     * @return the Route's length
     */
    public int getLength() {
        return length;
    }

    public City getCityA() {
        return cityA;
    }

    public City getCityB() {
        return cityB;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

}