package backend;

public class TransportCard {

    private Colour colour;
    private boolean canDraw;
    private String name;

    public TransportCard(Colour colour, String name) {
        this.colour = colour;
        this.name = name;
        this.canDraw = true;
    }

    public Colour getColour() {
        return colour;
    }

    /**
     * Getter for card display name.
     *
     * @return the name of this card (e.g. "Double Decker Bus")
     */
    public String getName() {
        return name;
    }

    /**
     * Returns whether this card can currently be drawn from the face-up row.
     * Always true for non-MULTI cards and for MULTI cards at the top of the draw pile.
     *
     * @return true if the player is allowed to draw this card
     */
    public boolean canDraw() {
        return canDraw;
    }

    /**
     * Sets whether this card can be drawn.
     * Called by TransportationDeck when a Bus replacement in the face-up row
     * must be locked to prevent drawing.
     *
     * @param canDraw false to lock this card, true to unlock
     */
    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    @Override
    public String toString() {
        return name + " (" + colour + ")";
    }
}
