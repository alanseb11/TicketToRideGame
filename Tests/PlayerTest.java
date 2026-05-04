import backend.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Player}.
 */
@DisplayName("Player")
class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        TransportationDeck.reset();
        player = new Player("Alice", "red");
    }

    // ── Initial state ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Player starts with 4 buses")
    void initialBusCount() {
        assertEquals(4, player.getBuses());
    }

    @Test
    @DisplayName("Player starts with 0 points")
    void initialPoints() {
        assertEquals(0, player.getPoints());
    }

    @Test
    @DisplayName("Player starts with 0 of each card colour")
    void initialTransportCards() {
        player.getTransportCards().values().forEach(count -> assertEquals(0, count));
    }

    @Test
    @DisplayName("Player starts with an empty destination ticket hand")
    void initialDestinationTickets() {
        assertTrue(player.getDestTicketCards().isEmpty());
    }

    @Test
    @DisplayName("getName and getColour return constructor values")
    void gettersReturnConstructorValues() {
        assertEquals("Alice", player.getName());
        assertEquals("red", player.getColour());
    }

    // ── addTransportCard() ───────────────────────────────────────────────────────

    @Test
    @DisplayName("addTransportCard increments the count for that colour")
    void addTransportCardIncrementsCount() {
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        assertEquals(1, player.getTransportCards().get(Colour.GREEN));
    }

    @Test
    @DisplayName("addTransportCard accumulates multiple cards of the same colour")
    void addMultipleTransportCards() {
        player.addTransportCard(new TransportCard(Colour.BLUE, "Milk Truck"));
        player.addTransportCard(new TransportCard(Colour.BLUE, "Milk Truck"));
        player.addTransportCard(new TransportCard(Colour.BLUE, "Milk Truck"));
        assertEquals(3, player.getTransportCards().get(Colour.BLUE));
    }

    @Test
    @DisplayName("addTransportCard for MULTI increments the MULTI count only")
    void addBusCardIncrementsBusCount() {
        player.addTransportCard(new TransportCard(Colour.MULTI, "Double Decker Bus"));
        assertEquals(1, player.getTransportCards().get(Colour.MULTI));
        assertEquals(0, player.getTransportCards().get(Colour.GREEN));
    }

    // ── addDestinationTicket() ───────────────────────────────────────────────────

    @Test
    @DisplayName("addDestinationTicket appends the ticket to the player's hand")
    void addDestinationTicketAppendsTicket() {
        DestinationTicket ticket = new DestinationTicket(City.BAKER_STREET, City.BIG_BEN, 4);
        player.addDestinationTicket(ticket);
        assertEquals(1, player.getDestTicketCards().size());
        assertSame(ticket, player.getDestTicketCards().get(0));
    }

    // ── addPoints / takePoints ───────────────────────────────────────────────────

    @Test
    @DisplayName("addPoints increases the score")
    void addPointsIncreasesScore() {
        player.addPoints(7);
        assertEquals(7, player.getPoints());
    }

    @Test
    @DisplayName("takePoints decreases the score")
    void takePointsDecreasesScore() {
        player.addPoints(10);
        player.takePoints(3);
        assertEquals(7, player.getPoints());
    }

    // ── claimRoute() ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("claimRoute deducts the correct cards from the player's hand")
    void claimRouteDeductsCards() {
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));

        Route route = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 2, null);
        HashMap<Colour, Integer> cardsToUse = new HashMap<>();
        cardsToUse.put(Colour.GREEN, 2);
        player.claimRoute(route, cardsToUse);

        assertEquals(0, player.getTransportCards().get(Colour.GREEN));
    }

    @Test
    @DisplayName("claimRoute deducts buses equal to route length")
    void claimRouteDeductsBuses() {
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));

        Route route = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 2, null);
        HashMap<Colour, Integer> cardsToUse = new HashMap<>();
        cardsToUse.put(Colour.GREEN, 2);
        player.claimRoute(route, cardsToUse);

        assertEquals(2, player.getBuses()); // 4 - 2 = 2
    }

    @Test
    @DisplayName("claimRoute adds correct points for a length-2 route (2 pts)")
    void claimRouteAddsPoints() {
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));

        Route route = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 2, null);
        HashMap<Colour, Integer> cardsToUse = new HashMap<>();
        cardsToUse.put(Colour.GREEN, 2);
        player.claimRoute(route, cardsToUse);

        assertEquals(2, player.getPoints()); // length-2 route = 2 pts
    }

    @Test
    @DisplayName("claimRoute adds route to player's adjacency list for both endpoints")
    void claimRouteUpdatesAdjacencyList() {
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));

        Route route = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 2, null);
        HashMap<Colour, Integer> cardsToUse = new HashMap<>();
        cardsToUse.put(Colour.GREEN, 2);
        player.claimRoute(route, cardsToUse);

        assertTrue(player.getPlayerRoutes().get(City.BAKER_STREET).contains(route));
        assertTrue(player.getPlayerRoutes().get(City.BIG_BEN).contains(route));
    }

    // ── calcDestTickets() ────────────────────────────────────────────────────────

    @Test
    @DisplayName("calcDestTickets adds points for a completed ticket")
    void calcDestTicketsAddsForCompleted() {
        Route direct = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 2, null);
        HashMap<Colour, Integer> cards = new HashMap<>();
        cards.put(Colour.GREEN, 2);
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.claimRoute(direct, cards);

        player.addDestinationTicket(new DestinationTicket(City.BAKER_STREET, City.BIG_BEN, 4));
        int beforeCalc = player.getPoints(); // 2 pts from route
        player.calcDestTickets();
        assertEquals(beforeCalc + 4, player.getPoints());
    }

    @Test
    @DisplayName("calcDestTickets subtracts points for an incomplete ticket")
    void calcDestTicketsSubtractsForIncomplete() {
        // No routes claimed — ticket is not satisfied
        player.addDestinationTicket(new DestinationTicket(City.BAKER_STREET, City.BIG_BEN, 4));
        player.addPoints(10); // baseline
        player.calcDestTickets();
        assertEquals(6, player.getPoints()); // 10 - 4
    }

    @Test
    @DisplayName("calcDestTickets increments completed ticket counter for completed tickets")
    void calcDestTicketsIncrementsCompletedCounter() {
        Route direct = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 2, null);
        HashMap<Colour, Integer> cards = new HashMap<>();
        cards.put(Colour.GREEN, 2);
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.claimRoute(direct, cards);

        player.addDestinationTicket(new DestinationTicket(City.BAKER_STREET, City.BIG_BEN, 4));
        player.calcDestTickets();
        assertEquals(1, player.getCompletedDestTickets());
    }

    // ── calcLongestPath() / dfs() ────────────────────────────────────────────────

    @Test
    @DisplayName("calcLongestPath returns 0 when player has claimed no routes")
    void longestPathZeroWithNoRoutes() {
        player.calcLongestPath();
        assertEquals(0, player.getLongestPath());
    }

    @Test
    @DisplayName("calcLongestPath returns route length for a single claimed route")
    void longestPathSingleRoute() {
        Route route = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 3, null);
        HashMap<Colour, Integer> cards = new HashMap<>();
        cards.put(Colour.GREEN, 3);
        for (int i = 0; i < 3; i++) player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.claimRoute(route, cards);

        player.calcLongestPath();
        assertEquals(3, player.getLongestPath());
    }

    @Test
    @DisplayName("calcLongestPath sums lengths along a continuous chain of routes")
    void longestPathChainedRoutes() {
        // BAKER_STREET -2-> BRITISH_MUSEUM -1-> ST_PAULS  → total path = 3
        Route r1 = new Route(Colour.GREEN, City.BAKER_STREET, City.BRITISH_MUSEUM, 2, null);
        Route r2 = new Route(Colour.BLUE,  City.BRITISH_MUSEUM, City.ST_PAULS, 1, null);

        HashMap<Colour, Integer> c1 = new HashMap<>(); c1.put(Colour.GREEN, 2);
        HashMap<Colour, Integer> c2 = new HashMap<>(); c2.put(Colour.BLUE, 1);

        for (int i = 0; i < 2; i++) player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.addTransportCard(new TransportCard(Colour.BLUE, "Milk Truck"));
        player.claimRoute(r1, c1);
        player.claimRoute(r2, c2);

        player.calcLongestPath();
        assertEquals(3, player.getLongestPath());
    }

    @Test
    @DisplayName("calcLongestPath picks the longer of two branching paths")
    void longestPathChoosesLongerBranch() {
        // Branch A: BAKER_STREET -1-> BRITISH_MUSEUM  (length 1)
        // Branch B: BAKER_STREET -2-> BIG_BEN          (length 2)
        Route rA = new Route(Colour.GREEN, City.BAKER_STREET, City.BRITISH_MUSEUM, 1, null);
        Route rB = new Route(Colour.BLUE,  City.BAKER_STREET, City.BIG_BEN, 2, null);

        HashMap<Colour, Integer> cA = new HashMap<>(); cA.put(Colour.GREEN, 1);
        HashMap<Colour, Integer> cB = new HashMap<>(); cB.put(Colour.BLUE, 2);

        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.addTransportCard(new TransportCard(Colour.BLUE, "Milk Truck"));
        player.addTransportCard(new TransportCard(Colour.BLUE, "Milk Truck"));
        player.claimRoute(rA, cA);
        player.claimRoute(rB, cB);

        player.calcLongestPath();
        // The longest continuous path starting from BAKER_STREET goes through both branches = 3
        assertEquals(3, player.getLongestPath());
    }
}
