package backend;

import java.util.HashMap;

public class Route{

    private Colour colour;

    private City cityA;

    private City cityB;

    private int length;

    private Player owner = null;

    private Route doubleRoute;

    private boolean ferry;

    private int requiredLocomotives;

    public Route(Colour colour, City cityA, City cityB, int length, Route doubleRoute) {
        this(colour, cityA, cityB, length, doubleRoute, false, 0);
    }

    public Route(Colour colour, City cityA, City cityB, int length, Route doubleRoute,
                 boolean ferry, int requiredLocomotives) {
        this.colour = colour;
        this.cityA = cityA;
        this.cityB = cityB;
        this.length = length;
        this.doubleRoute = doubleRoute;
        this.ferry = ferry;
        this.requiredLocomotives = requiredLocomotives;
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
        } else if (this.length == 5) {
            return 10;
        } else if (this.length == 6) {
            return 15;
        } else {
                return 0;
        }
    };

    /**
     * Checks if Player can claim this route based on Transport Cards they are holding
     *
     * @param player - the player trying to claim the route
     * @return a HashMap<Colour,Interger>, with the cards that will be used to claim the route
     */
    public HashMap<Colour, Integer> canPlayerClaim(Player player) {
        // Get Player's transportation cards
        HashMap<Colour, Integer> transportCards = player.getTransportCards();

        // Initialise the cards Player can use to claim this route
        HashMap<Colour, Integer> cardsToUse = new HashMap<>();

        if (ferry) {
            return canPlayerClaimFerry(player);
        }

        // Player may only claim if they have enough trains to place along the route
        if (player.getBuses() >= length) {
            // If the route has a twin, and it has been claimed. Cannot claim this one.
            if (doubleRoute != null) {
                if (doubleRoute.owner != null) {
                    return  null;
                }
            } else {
                // If the path is grey (any colour set can claim), use first set with enough
                // cards to claim the route
                if (colour == Colour.MULTI) {
                    for (Colour cardColour : transportCards.keySet()) {
                        if (transportCards.get(cardColour) >= length) {
                            cardsToUse.put(cardColour, length);
                            return cardsToUse;
                        }
                    }

                    // loop through to check if a mixed set of one colour + multi colour can be used
                    for (Colour cardColour : transportCards.keySet()) {
                        int numCards = transportCards.get(cardColour);
                        int diff = length - numCards;

                        if (transportCards.get(Colour.MULTI) >= diff) {
                            // In case route claimable with just multi-coloured cards
                            cardsToUse.put(colour, numCards);
                            cardsToUse.put(Colour.MULTI, diff);
                            return cardsToUse;
                        }
                    }

                    } else {
                    int numSameColourCards = transportCards.get(colour);
                    // If there are enough same colour cards, use that to claim
                    if (numSameColourCards >= length) {
                        cardsToUse.put(colour, length);
                        return cardsToUse;
                    } else {
                        // Else if not enough, try to make up the rest with multi-coloured bus cards
                        int diff =  length - numSameColourCards;
                        if (transportCards.get(Colour.MULTI) >= diff) {
                            // In case route claimable with just multi-coloured cards
                            if (numSameColourCards != 0) {
                                cardsToUse.put(colour, numSameColourCards);
                            }
                            cardsToUse.put(Colour.MULTI, diff);
                            return cardsToUse;

                        } else {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }

    private HashMap<Colour, Integer> canPlayerClaimFerry(Player player) {
        HashMap<Colour, Integer> transportCards = player.getTransportCards();
        HashMap<Colour, Integer> cardsToUse = new HashMap<>();

        if (player.getBuses() < length) {
            return null;
        }

        if (doubleRoute != null && doubleRoute.owner != null) {
            return null;
        }

        int normalCardsRequired = length - requiredLocomotives;

        int locomotiveCards = transportCards.get(Colour.MULTI);
        int sameColourCards = transportCards.get(colour);

        if (locomotiveCards < requiredLocomotives) {
            return null;
        }

        cardsToUse.put(Colour.MULTI, requiredLocomotives);

        if (sameColourCards >= normalCardsRequired) {
            if (normalCardsRequired > 0) {
                cardsToUse.put(colour, normalCardsRequired);
            }

            return cardsToUse;
        }

        int colourShortage = normalCardsRequired - sameColourCards;
        int remainingLocomotives = locomotiveCards - requiredLocomotives;

        if (remainingLocomotives >= colourShortage) {
            if (sameColourCards > 0) {
                cardsToUse.put(colour, sameColourCards);
            }

            cardsToUse.put(Colour.MULTI, requiredLocomotives + colourShortage);
            return cardsToUse;
        }

        return null;
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

    public boolean isFerry() {
        return ferry;
    }

    public int getRequiredLocomotives() {
        return requiredLocomotives;
    }
}