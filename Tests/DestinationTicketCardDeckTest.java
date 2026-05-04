import backend.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link DestinationTicketCardDeck}.
 */
@DisplayName("DestinationTicketCardDeck")
class DestinationTicketCardDeckTest {

    @BeforeEach
    void resetSingleton() {
        DestinationTicketCardDeck.reset();
        TransportationDeck.reset();
    }

    // ── Singleton ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getInstance returns the same object on repeated calls")
    void singletonSameInstance() {
        DestinationTicketCardDeck a = DestinationTicketCardDeck.getInstance();
        DestinationTicketCardDeck b = DestinationTicketCardDeck.getInstance();
        assertSame(a, b);
    }

    @Test
    @DisplayName("reset() causes getInstance to produce a fresh deck")
    void resetProducesFreshDeck() {
        DestinationTicketCardDeck first = DestinationTicketCardDeck.getInstance();
        DestinationTicketCardDeck.reset();
        DestinationTicketCardDeck second = DestinationTicketCardDeck.getInstance();
        assertNotSame(first, second);
    }

    // ── Deck construction ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Fresh deck contains exactly 22 destination tickets")
    void freshDeckHas22Cards() {
        DestinationTicketCardDeck deck = DestinationTicketCardDeck.getInstance();
        assertEquals(22, deck.size());
    }

    // ── draw() ───────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("draw() removes and returns the top card")
    void drawRemovesTopCard() {
        DestinationTicketCardDeck deck = DestinationTicketCardDeck.getInstance();
        int before = deck.size();
        DestinationTicket ticket = deck.draw();
        assertNotNull(ticket);
        assertEquals(before - 1, deck.size());
    }

    @Test
    @DisplayName("draw() returns null on an empty deck")
    void drawReturnsNullWhenEmpty() {
        DestinationTicketCardDeck deck = DestinationTicketCardDeck.getInstance();
        for (int i = 0; i < 22; i++) deck.draw();
        assertNull(deck.draw());
    }

    // ── toDeck() ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("toDeck() adds a card to the bottom of the deck (deck grows by 1)")
    void toDeckGrowsDeck() {
        DestinationTicketCardDeck deck = DestinationTicketCardDeck.getInstance();
        DestinationTicket extra = new DestinationTicket(City.BAKER_STREET, City.BIG_BEN, 4);
        int before = deck.size();
        deck.toDeck(extra);
        assertEquals(before + 1, deck.size());
    }

    @Test
    @DisplayName("toDeck() places card at the bottom — original top is still drawn first")
    void toDeckGoesToBottom() {
        DestinationTicketCardDeck deck = DestinationTicketCardDeck.getInstance();
        DestinationTicket originalTop = deck.draw(); // remember the original top
        DestinationTicket sentinel = new DestinationTicket(City.WATERLOO, City.KINGS_CROSS, 8);
        deck.toDeck(sentinel);                        // add to bottom
        deck.toDeck(originalTop);                     // add the previously drawn card to bottom too

        // Draw all remaining original cards
        for (int i = 0; i < 21; i++) deck.draw();

        // sentinel should be near the bottom; the 22nd draw returns it
        DestinationTicket fromBottom = deck.draw();
        assertEquals(sentinel, fromBottom);
    }

    // ── deal() ───────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("deal() presents 3 tickets to the player for selection")
    void dealPresentThreeTickets() {
        DestinationTicketCardDeck deck = DestinationTicketCardDeck.getInstance();
        Player player = new Player("Alice", "red");
        List<DestinationTicket> presented = deck.getTickets(2, player);
        assertEquals(3, presented.size());
    }

    @Test
    @DisplayName("deal() reduces deck by 3 before player selection")
    void dealReducesDeckByThree() {
        DestinationTicketCardDeck deck = DestinationTicketCardDeck.getInstance();
        Player player = new Player("Alice", "red");
        deck.getTickets(2, player);
        // Deck should be reduced by 3 (pending draw holds them)
        assertEquals(19, deck.size());
    }

    @Test
    @DisplayName("Keeping 2 of 3 dealt tickets adds 2 to player hand and returns 1 to deck")
    void dealKeepTwoReturnOne() {
        DestinationTicketCardDeck deck = DestinationTicketCardDeck.getInstance();
        Player player = new Player("Alice", "red");
        deck.getTickets(2, player);          // draw phase
        deck.getTickets(2, player, List.of(0, 1)); // keep indices 0 and 1

        assertEquals(2, player.getDestTicketCards().size());
        // The returned ticket goes to the bottom; deck should now be 19 + 1 = 20
        assertEquals(20, deck.size());
    }

    @Test
    @DisplayName("Keeping all 3 dealt tickets adds 3 to player hand")
    void dealKeepAllThree() {
        DestinationTicketCardDeck deck = DestinationTicketCardDeck.getInstance();
        Player player = new Player("Alice", "red");
        deck.getTickets(2, player);
        deck.getTickets(2, player, List.of(0, 1, 2));

        assertEquals(3, player.getDestTicketCards().size());
        assertEquals(19, deck.size()); // nothing returned
    }

    // ── mid-game draw(Player) ────────────────────────────────────────────────────

    @Test
    @DisplayName("Mid-game draw presents 3 tickets; keeping 1 returns 2 to deck")
    void midGameDrawKeepOne() {
        DestinationTicketCardDeck deck = DestinationTicketCardDeck.getInstance();
        Player player = new Player("Bob", "blue");
        deck.draw(player);                           // presents 3 tickets
        deck.getTickets(1, player, List.of(0));      // keep only index 0

        assertEquals(1, player.getDestTicketCards().size());
        assertEquals(21, deck.size()); // 22 - 3 drawn + 2 returned
    }

    @Test
    @DisplayName("getPendingDraw reflects cards presented to the player")
    void getPendingDrawReflectsPresented() {
        DestinationTicketCardDeck deck = DestinationTicketCardDeck.getInstance();
        Player player = new Player("Alice", "red");
        List<DestinationTicket> presented = deck.getTickets(2, player);
        List<DestinationTicket> pending = deck.getPendingDraw();
        assertEquals(presented.size(), pending.size());
    }
}
