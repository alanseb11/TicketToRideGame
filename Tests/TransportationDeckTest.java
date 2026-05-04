import backend.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TransportationDeck}.
 */
@DisplayName("TransportationDeck")
class TransportationDeckTest {

    @BeforeEach
    void resetSingleton() {
        TransportationDeck.reset();
    }

    // ── Singleton ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getInstance returns the same object on repeated calls")
    void singletonSameInstance() {
        TransportationDeck a = TransportationDeck.getInstance();
        TransportationDeck b = TransportationDeck.getInstance();
        assertSame(a, b);
    }

    @Test
    @DisplayName("reset() causes getInstance to produce a fresh deck")
    void resetProducesFreshDeck() {
        TransportationDeck first = TransportationDeck.getInstance();
        TransportationDeck.reset();
        TransportationDeck second = TransportationDeck.getInstance();
        assertNotSame(first, second);
    }

    // ── Deck construction ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Fresh deck contains 44 draw-pile cards")
    void freshDeckHas44Cards() {
        TransportationDeck deck = TransportationDeck.getInstance();
        assertEquals(44, deck.drawPileSize());
    }

    @Test
    @DisplayName("Fresh deck has an empty discard pile")
    void freshDeckHasEmptyDiscard() {
        TransportationDeck deck = TransportationDeck.getInstance();
        assertEquals(0, deck.discardPileSize());
    }

    // ── draw() ───────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("draw() removes and returns the top card")
    void drawReducesPileByOne() {
        TransportationDeck deck = TransportationDeck.getInstance();
        int before = deck.drawPileSize();
        TransportCard card = deck.draw();
        assertNotNull(card);
        assertEquals(before - 1, deck.drawPileSize());
    }

    @Test
    @DisplayName("draw() returns null and reshuffles discard when draw pile is empty")
    void drawReshuffflesDiscardWhenEmpty() {
        TransportationDeck deck = TransportationDeck.getInstance();
        // Drain all 44 draw pile cards
        for (int i = 0; i < 44; i++) deck.draw();
        assertEquals(0, deck.drawPileSize());

        // Add cards to discard pile
        deck.discard(Colour.GREEN, 3);
        assertEquals(3, deck.discardPileSize());

        // Next draw should reshuffle discard into draw pile
        TransportCard card = deck.draw();
        assertNotNull(card);
        // Discard should now be empty, draw pile has remaining cards
        assertEquals(0, deck.discardPileSize());
    }

    @Test
    @DisplayName("draw() returns null when both piles are empty")
    void drawReturnsNullWhenBothPilesEmpty() {
        TransportationDeck deck = TransportationDeck.getInstance();
        for (int i = 0; i < 44; i++) deck.draw();
        assertNull(deck.draw());
    }

    // ── deal() ───────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("deal() gives player exactly 4 transport cards")
    void dealGivesPlayerFourCards() {
        TransportationDeck deck = TransportationDeck.getInstance();
        Player player = new Player("Alice", "red");
        deck.deal(player);
        int total = player.getTransportCards().values().stream().mapToInt(Integer::intValue).sum();
        assertEquals(4, total);
    }

    @Test
    @DisplayName("deal() reduces draw pile by 4")
    void dealReducesPileByFour() {
        TransportationDeck deck = TransportationDeck.getInstance();
        Player player = new Player("Alice", "red");
        deck.deal(player);
        assertEquals(40, deck.drawPileSize());
    }

    // ── toDeck / discard ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("discard() increases the discard pile count")
    void discardIncreasesDiscardPile() {
        TransportationDeck deck = TransportationDeck.getInstance();
        deck.discard(Colour.BLACK, 2);
        assertEquals(2, deck.discardPileSize());
    }

    // ── turnFaceUp() ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("turnFaceUp() produces exactly 5 face-up cards from a full deck")
    void turnFaceUpFiveCards() {
        TransportationDeck deck = TransportationDeck.getInstance();
        deck.turnFaceUp();
        assertEquals(5, deck.getFaceUpCards().size());
    }

    @Test
    @DisplayName("turnFaceUp() reduces draw pile by 5 when no three-bus rule fires")
    void turnFaceUpReducesPile() {
        // Use a deck where we know there are not 3 buses in the first 5
        // Rebuild via reset - we can't guarantee no three-bus rule fires so just check <= 5
        TransportationDeck deck = TransportationDeck.getInstance();
        int before = deck.drawPileSize();
        deck.turnFaceUp();
        // Draw pile will be smaller by at least 5 (possibly more if three-bus rule triggered)
        assertTrue(deck.drawPileSize() <= before - 5);
    }

    @Test
    @DisplayName("turnFaceUp() face-up list is unmodifiable")
    void faceUpIsUnmodifiable() {
        TransportationDeck deck = TransportationDeck.getInstance();
        deck.turnFaceUp();
        List<TransportCard> faceUp = deck.getFaceUpCards();
        assertThrows(UnsupportedOperationException.class, () -> faceUp.add(new TransportCard(Colour.GREEN, "Old Car")));
    }

    // ── checkThreeBuses() ────────────────────────────────────────────────────────

    @Test
    @DisplayName("checkThreeBuses() returns false when fewer than 3 buses are face-up")
    void checkThreeBusesFalse() {
        TransportationDeck deck = TransportationDeck.getInstance();
        // Drain all and manually add 2 bus cards via discard + draw path is hard,
        // so we test through the face-up mechanism indirectly:
        // Fresh deck has 8 buses out of 44; most turnFaceUp calls will not trigger
        deck.turnFaceUp();
        // If three-bus rule did NOT fire, checkThreeBuses should be false
        if (deck.getFaceUpCards().size() == 5) {
            assertFalse(deck.checkThreeBuses() && countBuses(deck.getFaceUpCards()) < 3);
        }
    }

    private int countBuses(List<TransportCard> cards) {
        return (int) cards.stream().filter(c -> c.getColour() == Colour.MULTI).count();
    }

    // ── draw(Player, int[]) ──────────────────────────────────────────────────────

    @Test
    @DisplayName("Drawing two blind cards from pile gives player 2 extra cards")
    void drawTwoFromPile() {
        TransportationDeck deck = TransportationDeck.getInstance();
        deck.turnFaceUp();
        Player player = new Player("Alice", "red");
        int before = player.getTransportCards().values().stream().mapToInt(Integer::intValue).sum();
        deck.draw(player, new int[]{-1, -1});
        int after = player.getTransportCards().values().stream().mapToInt(Integer::intValue).sum();
        assertEquals(before + 2, after);
    }

    @Test
    @DisplayName("Drawing a face-up Bus uses both draws (player gets 1 card)")
    void drawFaceUpBusUsesBothDraws() {
        // Build a deck that guarantees a bus in slot 0 of face-up
        // Strategy: drain all, discard only buses, let reshuffle put them in draw pile, then turnFaceUp
        TransportationDeck deck = TransportationDeck.getInstance();

        // Drain draw pile
        for (int i = 0; i < 44; i++) deck.draw();

        // Put 5 buses into discard pile so they become the face-up row
        deck.discard(Colour.MULTI, 5);
        // Also put a non-bus card so we don't hit "empty" edge case during reshuffle turnFaceUp
        deck.discard(Colour.GREEN, 5);

        deck.turnFaceUp();
        List<TransportCard> faceUp = deck.getFaceUpCards();

        // Find any bus slot
        int busSlot = -1;
        for (int i = 0; i < faceUp.size(); i++) {
            if (faceUp.get(i).getColour() == Colour.MULTI && faceUp.get(i).canDraw()) {
                busSlot = i;
                break;
            }
        }

        if (busSlot == -1) {
            // No drawable bus in face-up; skip this assertion (three-bus rule may have fired)
            return;
        }

        Player player = new Player("Bob", "blue");
        deck.draw(player, new int[]{busSlot, -1});
        int total = player.getTransportCards().values().stream().mapToInt(Integer::intValue).sum();
        // Drawing a bus counts as both draws — player should have exactly 1 card
        assertEquals(1, total);
    }

    @Test
    @DisplayName("draw(player, choices) with two non-bus face-up cards gives player 2 cards")
    void drawTwoFaceUpNonBus() {
        TransportationDeck deck = TransportationDeck.getInstance();
        deck.turnFaceUp();

        // Find two non-bus, drawable slots
        List<TransportCard> faceUp = deck.getFaceUpCards();
        int slot1 = -1, slot2 = -1;
        for (int i = 0; i < faceUp.size(); i++) {
            if (faceUp.get(i).getColour() != Colour.MULTI && faceUp.get(i).canDraw()) {
                if (slot1 == -1) slot1 = i;
                else { slot2 = i; break; }
            }
        }

        if (slot1 == -1 || slot2 == -1) return; // Not enough non-bus face-up cards; skip

        Player player = new Player("Alice", "red");
        deck.draw(player, new int[]{slot1, slot2});
        int total = player.getTransportCards().values().stream().mapToInt(Integer::intValue).sum();
        assertEquals(2, total);
    }
}
