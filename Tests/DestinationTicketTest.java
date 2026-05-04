import backend.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link DestinationTicket}, focusing on the BFS path-checking logic.
 */
@DisplayName("DestinationTicket")
class DestinationTicketTest {

    private Player player;

    @BeforeEach
    void setUp() {
        TransportationDeck.reset();
        player = new Player("Alice", "red");
    }

    // ── Constructor & getters ────────────────────────────────────────────────────

    @Test
    @DisplayName("Constructor stores cityA, cityB and points correctly")
    void constructorStoresFields() {
        DestinationTicket ticket = new DestinationTicket(City.BAKER_STREET, City.BIG_BEN, 4);
        assertEquals(City.BAKER_STREET, ticket.getCityA());
        assertEquals(City.BIG_BEN, ticket.getCityB());
        assertEquals(4, ticket.getPoints());
    }

    // ── checkPlayerConnect – negative cases ──────────────────────────────────────

    @Test
    @DisplayName("checkPlayerConnect returns false when player has claimed no routes")
    void notConnectedWithNoRoutes() {
        DestinationTicket ticket = new DestinationTicket(City.BAKER_STREET, City.BIG_BEN, 4);
        assertFalse(ticket.checkPlayerConnect(player));
    }

    @Test
    @DisplayName("checkPlayerConnect returns false when claimed route does not connect the ticket cities")
    void notConnectedWithUnrelatedRoute() {
        // Give player a route between two other cities
        Route unrelated = new Route(Colour.GREEN, City.WATERLOO, City.TRAFALGAR_SQUARE, 2, null);
        HashMap<Colour, Integer> cards = new HashMap<>();
        cards.put(Colour.GREEN, 2);
        // Manually give player the cards needed
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.claimRoute(unrelated, cards);

        DestinationTicket ticket = new DestinationTicket(City.BAKER_STREET, City.BIG_BEN, 4);
        assertFalse(ticket.checkPlayerConnect(player));
    }

    // ── checkPlayerConnect – positive cases ──────────────────────────────────────

    @Test
    @DisplayName("checkPlayerConnect returns true for a direct single-route connection")
    void connectedByDirectRoute() {
        // Create a direct route between the ticket cities
        Route direct = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 2, null);
        HashMap<Colour, Integer> cards = new HashMap<>();
        cards.put(Colour.GREEN, 2);
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.claimRoute(direct, cards);

        DestinationTicket ticket = new DestinationTicket(City.BAKER_STREET, City.BIG_BEN, 4);
        assertTrue(ticket.checkPlayerConnect(player));
    }

    @Test
    @DisplayName("checkPlayerConnect returns true for a two-hop path between ticket cities")
    void connectedByTwoHops() {
        // Route: BAKER_STREET -> BRITISH_MUSEUM -> ST_PAULS
        Route hop1 = new Route(Colour.GREEN, City.BAKER_STREET, City.BRITISH_MUSEUM, 1, null);
        Route hop2 = new Route(Colour.BLUE,  City.BRITISH_MUSEUM, City.ST_PAULS, 1, null);

        HashMap<Colour, Integer> cards1 = new HashMap<>();
        cards1.put(Colour.GREEN, 1);
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.claimRoute(hop1, cards1);

        HashMap<Colour, Integer> cards2 = new HashMap<>();
        cards2.put(Colour.BLUE, 1);
        player.addTransportCard(new TransportCard(Colour.BLUE, "Milk Truck"));
        player.claimRoute(hop2, cards2);

        DestinationTicket ticket = new DestinationTicket(City.BAKER_STREET, City.ST_PAULS, 3);
        assertTrue(ticket.checkPlayerConnect(player));
    }

    @Test
    @DisplayName("checkPlayerConnect returns true regardless of which city is cityA vs cityB")
    void connectionIsSymmetric() {
        Route direct = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 2, null);
        HashMap<Colour, Integer> cards = new HashMap<>();
        cards.put(Colour.GREEN, 2);
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.claimRoute(direct, cards);

        // Reversed ticket
        DestinationTicket reversed = new DestinationTicket(City.BIG_BEN, City.BAKER_STREET, 4);
        assertTrue(reversed.checkPlayerConnect(player));
    }

    @Test
    @DisplayName("checkPlayerConnect returns false when path is broken (only one of two hops claimed)")
    void notConnectedWithBrokenPath() {
        // Only claim the first hop
        Route hop1 = new Route(Colour.GREEN, City.BAKER_STREET, City.BRITISH_MUSEUM, 1, null);
        HashMap<Colour, Integer> cards1 = new HashMap<>();
        cards1.put(Colour.GREEN, 1);
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.claimRoute(hop1, cards1);

        // Ticket requires reaching ST_PAULS (two hops away)
        DestinationTicket ticket = new DestinationTicket(City.BAKER_STREET, City.ST_PAULS, 3);
        assertFalse(ticket.checkPlayerConnect(player));
    }
}
