package backend;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton deck for Destination Ticket Cards.
 *
 * <p>Contains all 22 destination tickets for the London map. Uses {@link
 * #getTickets(int, Player)} as the single shared implementation for both
 * dealing and drawing, conforming to DRY: the only difference between the two
 * actions is how many tickets the player must keep.
 */
public class DestinationTicketCardDeck extends Deck<DestinationTicket> {

    private List<DestinationTicket> pendingDraw;

    // ── Singleton ─────────────────────────────────────────────────────────────────

    private static DestinationTicketCardDeck instance;

    private DestinationTicketCardDeck() {
        super();
        buildDeck();
        shuffle();
    }

    /**
     * Returns the single shared instance, creating it on first call.
     *
     * @return the singleton DestinationTicketCardDeck
     */
    public static DestinationTicketCardDeck getInstance() {
        if (instance == null) {
            instance = new DestinationTicketCardDeck();
        }
        return instance;
    }
    public static void reset() {
        instance = null;
    }

    // ── Deck building ─────────────────────────────────────────────────────────────

    /**
     * Populates the deck with all 22 destination tickets for the London map. Point
     * values reflect route distance and strategic difficulty.
     */
    private void buildDeck() {
        // Short connections (2–3 pts)
        cards.add(new DestinationTicket(City.BAKER_STREET,      City.REGENTS_PARK,       2));
        cards.add(new DestinationTicket(City.HYDE_PARK,         City.BUCKINGHAM_PALACE,  2));
        cards.add(new DestinationTicket(City.COVENT_GARDEN,     City.TRAFALGAR_SQUARE,   2));
        cards.add(new DestinationTicket(City.GLOBE_THEATRE,     City.ELEPHANT_CASTLE,    2));
        cards.add(new DestinationTicket(City.ST_PAULS,          City.BRITISH_MUSEUM,     3));
        cards.add(new DestinationTicket(City.KINGS_CROSS,       City.THE_CHARTERHOUSE,   3));

        // Medium connections (4–5 pts)
        cards.add(new DestinationTicket(City.BAKER_STREET,      City.BIG_BEN,            4));
        cards.add(new DestinationTicket(City.TOWER_OF_LONDON,   City.BRITISH_MUSEUM,     4));
        cards.add(new DestinationTicket(City.WATERLOO,          City.PICCADILLY_CIRCUS,  4));
        cards.add(new DestinationTicket(City.BRICK_LANE,        City.GLOBE_THEATRE,      4));
        cards.add(new DestinationTicket(City.TRAFALGAR_SQUARE,  City.KINGS_CROSS,        5));
        cards.add(new DestinationTicket(City.REGENTS_PARK,      City.COVENT_GARDEN,      5));
        cards.add(new DestinationTicket(City.BUCKINGHAM_PALACE, City.WATERLOO,           5));
        cards.add(new DestinationTicket(City.ELEPHANT_CASTLE,   City.ST_PAULS,           5));

        // Long connections (7–10 pts)
        cards.add(new DestinationTicket(City.BAKER_STREET,      City.TOWER_OF_LONDON,    7));
        cards.add(new DestinationTicket(City.KINGS_CROSS,       City.BUCKINGHAM_PALACE,  7));
        cards.add(new DestinationTicket(City.BRICK_LANE,        City.HYDE_PARK,          7));
        cards.add(new DestinationTicket(City.REGENTS_PARK,      City.ELEPHANT_CASTLE,    8));
        cards.add(new DestinationTicket(City.TOWER_OF_LONDON,   City.HYDE_PARK,          8));
        cards.add(new DestinationTicket(City.WATERLOO,          City.KINGS_CROSS,        8));
        cards.add(new DestinationTicket(City.BAKER_STREET,      City.GLOBE_THEATRE,      9));
        cards.add(new DestinationTicket(City.PICCADILLY_CIRCUS, City.BRICK_LANE,        10));
    }

    // ── Deck interface ────────────────────────────────────────────────────────────

    /**
     * Deals starting destination tickets to a player. The player must keep at
     * least 2 of the 3 drawn.
     *
     * @param player the player to deal to
     */
    @Override
    public void deal(Player player) {
        getTickets(2, player);
    }

    /**
     * Places a destination ticket at the bottom of the deck. Called when a player
     * returns a ticket they chose not to keep.
     *
     * @param ticket the ticket to return
     */
    @Override
    public void toDeck(DestinationTicket ticket) {
        cards.add(ticket);
    }

    /**
     * Draws the top card from the deck and removes it from the pile.
     *
     * @return the top card, or null if the deck is empty
     */
    @Override
    public DestinationTicket draw() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.removeFirst();
    }

    // ── Mid-game draw ─────────────────────────────────────────────────────────────

    /**
     * Handles a player drawing destination tickets during their turn. The player
     * must keep at least 1 of the 3 drawn.
     *
     * @param player the player drawing tickets
     */
    public void draw(Player player) {
        getTickets(1, player);
    }

    // ── Core shared logic ─────────────────────────────────────────────────────────

    /**
     * Draws three destination tickets for a player and asks them which to keep.
     * The player must keep at least {@code min} tickets; they may return up to
     * {@code 3 - min}. Returned tickets go to the bottom of the deck via
     * {@link #toDeck}.
     *
     * <p>This method is the single implementation shared by {@link #deal} (min = 2)
     * and {@link #draw(Player)} (min = 1), conforming to DRY.
     *
     * <p>The caller provides {@code keptIndices} — the indices (0, 1, or 2) of the
     * drawn tickets the player wishes to keep. The UI layer is responsible for
     * collecting this input and validating that at least {@code min} indices are
     * selected before calling this method.
     *
     * @param min the minimum number of tickets the player must keep (1 or 2)
     * @param player the player selecting tickets
     * @param keptIndices indices (into the drawn list) of tickets to keep
     * @return the list of tickets drawn, so the UI can display them; tickets not in
     *     {@code keptIndices} are returned to the bottom of the deck
     */
    public List<DestinationTicket> getTickets(int min, Player player, List<Integer> keptIndices) {
        List<DestinationTicket> drawn = new ArrayList<>(pendingDraw);
        for (int i = 0; i < drawn.size(); i++) {
            if (keptIndices.contains(i)) {
                player.addDestinationTicket(drawn.get(i));
            } else {
                toDeck(drawn.get(i));
            }
        }

        pendingDraw.clear();
        return drawn;
    }

    /**
     * Overload used during initial setup when {@code keptIndices} are not yet
     * known — draws three tickets and returns them for the UI to display. The UI
     * must follow up with {@link #getTickets(int, Player, List)} once the player
     * has made their selection.
     *
     * <p>Also satisfies the design spec's {@code getTickets(min, player)} signature.
     *
     * @param min the minimum number of tickets the player must keep
     * @param player the player selecting tickets
     * @return the list of up to three tickets drawn
     */
    public List<DestinationTicket> getTickets(int min, Player player) {
        pendingDraw.clear();
        for (int i = 0; i < 3 && !cards.isEmpty(); i++) {
            pendingDraw.add(draw());
        }
        return new ArrayList<>(pendingDraw);
    }
    public int size() {
        return cards.size();
    }
    public List<DestinationTicket> getPendingDraw() {
        return new ArrayList<>(pendingDraw);
    }
}