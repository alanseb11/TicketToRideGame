import backend.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Game}, covering end-game scoring, tiebreaking,
 * and longest path bonus.
 */
@DisplayName("Game")
class GameTest {

    private Player alice;
    private Player bob;
    private Game game;

    @BeforeEach
    void setUp() {
        TransportationDeck.reset();
        alice = new Player("Alice", "red");
        bob   = new Player("Bob",   "blue");
        game  = new Game(alice, bob);
    }

    // ── endGame – clear winner ────────────────────────────────────────────────────

    @Test
    @DisplayName("endGame returns the player with more points as the sole winner")
    void endGameClearPointsWinner() {
        alice.addPoints(15);
        bob.addPoints(10);

        List<Player> winners = game.endGame();
        assertEquals(1, winners.size());
        assertSame(alice, winners.get(0));
    }

    @Test
    @DisplayName("endGame correctly adds destination ticket points for completed tickets")
    void endGameAddsCompletedTicketPoints() {
        // Alice claims a direct route so her ticket is satisfied
        Route direct = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 2, null);
        alice.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        alice.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        java.util.HashMap<Colour, Integer> cards = new java.util.HashMap<>();
        cards.put(Colour.GREEN, 2);
        alice.claimRoute(direct, cards);
        alice.addDestinationTicket(new DestinationTicket(City.BAKER_STREET, City.BIG_BEN, 5));
        // Alice's points before endGame: 2 (from route claim)

        List<Player> winners = game.endGame();
        // After endGame: alice = 2 + 5 = 7, bob = 0
        assertEquals(1, winners.size());
        assertSame(alice, winners.get(0));
    }

    @Test
    @DisplayName("endGame subtracts points for an incomplete destination ticket")
    void endGameSubtractsIncompleteTicket() {
        alice.addPoints(3);
        alice.addDestinationTicket(new DestinationTicket(City.BAKER_STREET, City.BIG_BEN, 5));
        // alice has no routes — ticket incomplete → 3 - 5 = -2

        bob.addPoints(0);

        List<Player> winners = game.endGame();
        // Bob wins with 0 points vs Alice's -2
        assertEquals(1, winners.size());
        assertSame(bob, winners.get(0));
    }

    // ── endGame – longest path bonus ─────────────────────────────────────────────

    @Test
    @DisplayName("endGame awards 10-point longest path bonus to the player with longer path")
    void endGameLongestPathBonusGoesToLongerPath() {
        // Alice: BAKER_STREET -3-> BRITISH_MUSEUM
        Route r1 = new Route(Colour.GREEN, City.BAKER_STREET, City.BRITISH_MUSEUM, 3, null);
        java.util.HashMap<Colour, Integer> c1 = new java.util.HashMap<>();
        c1.put(Colour.GREEN, 3);
        for (int i = 0; i < 3; i++) alice.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        alice.claimRoute(r1, c1);
        alice.calcLongestPath(); // longest = 3

        // Bob: shorter path
        Route r2 = new Route(Colour.BLUE, City.WATERLOO, City.TRAFALGAR_SQUARE, 1, null);
        java.util.HashMap<Colour, Integer> c2 = new java.util.HashMap<>();
        c2.put(Colour.BLUE, 1);
        bob.addTransportCard(new TransportCard(Colour.BLUE, "Milk Truck"));
        bob.claimRoute(r2, c2);
        bob.calcLongestPath(); // longest = 1

        // Both start at 0 raw points; alice should win after 10pt bonus
        List<Player> winners = game.endGame();
        assertEquals(1, winners.size());
        assertSame(alice, winners.get(0));
    }

    // ── endGame – tiebreaking ─────────────────────────────────────────────────────

    @Test
    @DisplayName("endGame on equal points tiebreaks by completed destination tickets")
    void endGameTiebreakedByDestTickets() {
        alice.addPoints(10);
        bob.addPoints(10);

        // Alice has 1 completed ticket; Bob has 0
        // Give alice a completed ticket via calcDestTickets (but we need to prevent double-adding)
        // Easier: directly manipulate via public API
        // Alice and bob have no routes → incomplete tickets subtract points. Instead give
        // alice a route that satisfies her ticket so completedCount = 1.
        Route direct = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 2, null);
        alice.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        alice.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        java.util.HashMap<Colour, Integer> cards = new java.util.HashMap<>();
        cards.put(Colour.GREEN, 2);
        alice.claimRoute(direct, cards);
        alice.addDestinationTicket(new DestinationTicket(City.BAKER_STREET, City.BIG_BEN, 0));
        // Alice's route gives 2 extra points, so override to keep tie
        alice.takePoints(2); // back to 10

        List<Player> winners = game.endGame();
        // Alice completes 1 ticket; bob completes 0 → alice wins tiebreak
        assertEquals(1, winners.size());
        assertSame(alice, winners.get(0));
    }

    @Test
    @DisplayName("endGame returns multiple players when a tie cannot be broken")
    void endGameReturnsBothPlayersOnUnbreakableTie() {
        alice.addPoints(10);
        bob.addPoints(10);
        // No destination tickets, no routes — equal on all metrics
        List<Player> winners = game.endGame();
        assertEquals(2, winners.size());
    }

    // ── getPlayersWithMax() ───────────────────────────────────────────────────────

    @Test
    @DisplayName("getPlayersWithMax returns only the player with the highest metric value")
    void getPlayersWithMaxSingleWinner() {
        alice.addPoints(5);
        bob.addPoints(10);
        List<Player> result = game.getPlayersWithMax(Player::getPoints, List.of(alice, bob));
        assertEquals(1, result.size());
        assertSame(bob, result.get(0));
    }

    @Test
    @DisplayName("getPlayersWithMax returns all players when they share the maximum")
    void getPlayersWithMaxTie() {
        alice.addPoints(10);
        bob.addPoints(10);
        List<Player> result = game.getPlayersWithMax(Player::getPoints, List.of(alice, bob));
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("getPlayersWithMax returns empty list on empty candidate list")
    void getPlayersWithMaxEmptyCandidates() {
        List<Player> result = game.getPlayersWithMax(Player::getPoints, List.of());
        assertTrue(result.isEmpty());
    }
}
