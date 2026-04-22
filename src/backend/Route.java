package backend;

public class Route{

    private String colour;

    private String cityA;

    private String cityB;

    private int length;

    private Player owner = null;

    public Route(String colour, String cityA, String cityB, int length) {
        this.colour = colour;
        this.cityA = cityA;
        this.cityB = cityB;
        this.length = length;
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
        int cardCount = 0;
        TransportCard[] transportCards = player.getTransportCards();
        for (int i = 0; i <= transportCards.length; i++) {
            if (transportCards[i].getColour() == this.colour) {
                cardCount++;
            }
        }

        if (cardCount >= this.length) {
            return true;
        } else {
            return false;
        }
    }


}