package backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Singleton deck for Transportation Cards.
 *
 * <p>Card breakdown:
 * <ul>
 *   <li>6 × Old Car (GREEN)
 *   <li>6 × Milk Truck (BLUE)
 *   <li>6 × Submarine (YELLOW)
 *   <li>6 × Sports Car (ORANGE)
 *   <li>6 × Rocket Car (PINK)
 *   <li>6 × Cab (BLACK)
 *   <li>8 × Double Decker Bus (MULTI)
 * </ul>
 */
public class TransportationDeck extends Deck<TransportCard> {

    /** The five cards currently visible to both players. */
    private ArrayList<TransportCard> faceUpCards;

    // ── Singleton ─────────────────────────────────────────────────────────────────

    private static TransportationDeck instance;
    private ArrayList<TransportCard> discardPile;

    private TransportationDeck() {
        super();
        faceUpCards = new ArrayList<>();
        discardPile = new ArrayList<>();
        buildDeck();
        shuffle();
    }

    /**
     * Returns the single shared instance, creating it on first call.
     *
     * @return the singleton TransportationDeck
     */
    public static TransportationDeck getInstance() {
        if (instance == null) {
            instance = new TransportationDeck();
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    // ── Deck building ─────────────────────────────────────────────────────────────

    /** Populates the draw pile with all 44 transportation cards. */
    private void buildDeck() {
        addCards(Colour.GREEN,  "Old Car",           6);
        addCards(Colour.BLUE,   "Milk Truck",        6);
        addCards(Colour.YELLOW, "Submarine",         6);
        addCards(Colour.ORANGE, "Sports Car",        6);
        addCards(Colour.PINK,   "Rocket Car",        6);
        addCards(Colour.BLACK,  "Cab",               6);
        addCards(Colour.MULTI,  "Double Decker Bus", 8);
    }

    /**
     * Adds multiple copies of the same card to the draw pile.
     *
     * @param colour card colour
     * @param name card display name
     * @param count number of copies
     */
    private void addCards(Colour colour, String name, int count) {
        for (int i = 0; i < count; i++) {
            cards.add(new TransportCard(colour, name));
        }
    }

    // ── Deck interface ────────────────────────────────────────────────────────────

    /**
     * Deals 4 Transportation Cards to a player and then turns the top five cards
     * face-up. If three or more of those face-up cards are Buses, discards them and
     * replaces with the next five. Repeats until the face-up row is valid.
     *
     * @param player the player to deal to
     */
    @Override
    public void deal(Player player) {
        for (int i = 0; i < 4; i++) {
            TransportCard card = draw();
            if (card != null) {
                player.addTransportCard(card);
            }
        }
    }

    /**
     * Places a card at the bottom of the draw pile.
     *
     * @param card the card to move to the bottom
     */
    @Override
    public void toDeck(TransportCard card) {
        discardPile.add(card);
    }

    private String cardNameFor(Colour colour) {
        return switch (colour) {
            case GREEN -> "Old Car";
            case BLUE -> "Milk Truck";
            case YELLOW -> "Submarine";
            case ORANGE -> "Sports Car";
            case PINK -> "Rocket Car";
            case BLACK -> "Cab";
            case MULTI -> "Double Decker Bus";
            default -> colour.name();
        };
    }

    public void discard(Colour colour, int count) {
        String name = cardNameFor(colour);
        for (int i = 0; i < count; i++) {
            discardPile.add(new TransportCard(colour, name));
        }
    }

    /**
     * Draws and returns the top card from the draw pile, removing it.
     *
     * @return the top card, or null if the pile is empty
     */
    @Override
    public TransportCard draw() {
        if (cards.isEmpty()) {
            if (discardPile.isEmpty()) {
                return null;
            }
            // Reshuffle discard pile into draw pile
            cards.addAll(discardPile);
            discardPile.clear();
            Collections.shuffle(cards);
        }
        return cards.removeFirst();
    }

    // ── Face-up management ────────────────────────────────────────────────────────

    /**
     * Adds the top five cards from the draw pile to faceUpCards. If three or more
     * of the five are Buses, discards all five to the bottom of the pile and refills
     * with the next five. Repeats until the face-up row has fewer than three Buses.
     */
    public void turnFaceUp() {
        faceUpCards.clear();
        for (int i = 0; i < 5; i++) {
            TransportCard card = draw();
            if (card != null) {
                faceUpCards.add(card);
            }
        }
        while (checkThreeBuses() && !(cards.isEmpty() && discardPile.isEmpty())) {
            for (TransportCard card : faceUpCards) {
                toDeck(card);
            }
            faceUpCards.clear();
            for (int i = 0; i < 5; i++) {
                TransportCard card = draw();
                if (card != null) {
                    faceUpCards.add(card);
                }
            }
        }
    }

    /**
     * Handles a player's card draw action for their turn. The player draws exactly
     * two cards total. On each draw they choose a face-up slot (0–4) or -1 to draw
     * from the top of the pile.
     *
     * <p>Rules enforced:
     * <ul>
     *   <li>If the player picks a face-up Bus, they cannot draw a second card.
     *   <li>After a face-up card is taken, its slot is refilled from the top of the
     *       pile. If the replacement is a Bus, {@code canDraw} is set to false on
     *       that card.
     *   <li>Drawing from the pile always succeeds (no canDraw restriction).
     * </ul>
     *
     * <p>The caller (UI layer) is responsible for providing valid {@code choices}
     * values in response to player input.
     *
     * @param player the player drawing cards
     * @param choices an array of exactly two ints: each is a face-up index (0–4)
     *     or -1 to draw from the pile. If the first choice is a face-up Bus, only
     *     {@code choices[0]} is used.
     */
    public void draw(Player player, int[] choices) {
        int drawsRemaining = 2;
        int choiceIndex = 0;

        while (drawsRemaining > 0 && choiceIndex < choices.length) {
            int choice = choices[choiceIndex++];

            if (choice == -1) {
                // Draw from top of pile
                TransportCard card = draw();
                if (card != null) {
                    player.addTransportCard(card);
                }
                drawsRemaining--;

            } else {
                if (choice < 0 || choice >= faceUpCards.size()) {
                    continue;
                }
                // Draw from face-up slot
                TransportCard chosen = faceUpCards.get(choice);

                if (!chosen.canDraw()) {
                    // Locked card — caller should not have offered this choice, skip it
                    continue;
                }

                boolean isBus = chosen.getColour() == Colour.MULTI;
                faceUpCards.remove(choice);
                player.addTransportCard(chosen);

                // Refill the slot from the top of the pile
                TransportCard replacement = draw();
                if (replacement != null) {
                    if (replacement.getColour() == Colour.MULTI) {
                        replacement.setCanDraw(false);
                    }
                    faceUpCards.add(choice, replacement);
                }
                if (checkThreeBuses()) {
                    for (TransportCard card : faceUpCards) {
                        toDeck(card);
                    }
                    faceUpCards.clear();
                    for (int i = 0; i < 5; i++) {
                        TransportCard card = draw();
                        if (card != null) {
                            faceUpCards.add(card);
                        }
                    }

                    while (checkThreeBuses() && !(cards.isEmpty() && discardPile.isEmpty())) {
                        for (TransportCard card : faceUpCards) {
                            toDeck(card);
                        }
                        faceUpCards.clear();
                        for (int i = 0; i < 5; i++) {
                            TransportCard card = draw();
                            if (card != null) {
                                faceUpCards.add(card);
                            }
                        }
                    }
                }

                if (isBus) {
                    // Drawing a face-up Bus counts as both draws
                    drawsRemaining = 0;
                } else {
                    drawsRemaining--;
                }
            }
        }

        // Unlock any locked face-up Buses for the next player's turn
        for (TransportCard card : faceUpCards) {
            card.setCanDraw(true);
        }
    }

    /**
     * Checks whether three or more of the face-up cards are Buses.
     *
     * @return true if the three-bus rule is triggered
     */
    public boolean checkThreeBuses() {
        int busCount = 0;
        for (TransportCard card : faceUpCards) {
            if (card.getColour() == Colour.MULTI) {
                busCount++;
            }
        }
        return busCount >= 3;
    }

    // ── Getter ────────────────────────────────────────────────────────────────────

    /**
     * Returns the current face-up cards as an unmodifiable view.
     *
     * @return the face-up cards
     */
    public List<TransportCard> getFaceUpCards() {
        return Collections.unmodifiableList(faceUpCards);
    }

    public int drawPileSize() {
        return cards.size();
    }

    public int discardPileSize() {
        return discardPile.size();
    }
}