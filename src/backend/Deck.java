package backend;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Abstract base class for all decks in the game.
 *
 * @param <T> the type of card held in this deck
 */
public abstract class Deck<T> {

    /** The ordered list of cards remaining in the draw pile. Index 0 is the top. */
    protected ArrayList<T> cards;

    /** Constructs an empty deck. */
    protected Deck() {
        cards = new ArrayList<>();
    }

    /** Shuffles the deck into a random order. */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Deals starting cards to a player at the beginning of the game.
     *
     * @param player the player to deal to
     */
    public abstract void deal(Player player);

    /**
     * Places a card at the bottom of the draw pile.
     *
     * @param card the card to move to the bottom
     */
    public abstract void toDeck(T card);

    /**
     * Draws the top card from the deck and removes it from the pile.
     *
     * @return the top card, or null if the deck is empty
     */
    public abstract T draw();
}