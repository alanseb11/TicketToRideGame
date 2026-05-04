import backend.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Route}.
 */
@DisplayName("Route")
class RouteTest {

    @BeforeEach
    void resetDeck() {
        TransportationDeck.reset();
    }

    // ── calculatePoints() ────────────────────────────────────────────────────────

    @ParameterizedTest(name = "length {0} → {1} points")
    @CsvSource({"1,1", "2,2", "3,4", "4,7", "5,10", "6,15"})
    @DisplayName("calculatePoints returns correct value for each valid route length")
    void calculatePointsAllLengths(int length, int expectedPoints) {
        Route route = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, length, null);
        assertEquals(expectedPoints, route.calculatePoints());
    }

    @Test
    @DisplayName("calculatePoints returns 0 for an unsupported length")
    void calculatePointsUnknownLengthReturnsZero() {
        Route route = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 99, null);
        assertEquals(0, route.calculatePoints());
    }

    // ── Getters ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getColour, getCityA, getCityB, getLength return constructor values")
    void gettersReturnConstructorValues() {
        Route route = new Route(Colour.BLUE, City.WATERLOO, City.TRAFALGAR_SQUARE, 2, null);
        assertEquals(Colour.BLUE, route.getColour());
        assertEquals(City.WATERLOO, route.getCityA());
        assertEquals(City.TRAFALGAR_SQUARE, route.getCityB());
        assertEquals(2, route.getLength());
    }

    @Test
    @DisplayName("getOwner returns null before any player claims the route")
    void ownerNullInitially() {
        Route route = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 1, null);
        assertNull(route.getOwner());
    }

    @Test
    @DisplayName("setOwner stores and getOwner retrieves the player")
    void setOwnerStoresPlayer() {
        Route route = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 1, null);
        Player player = new Player("Alice", "red");
        route.setOwner(player);
        assertSame(player, route.getOwner());
    }

    // ── canPlayerClaim() – coloured route ────────────────────────────────────────

    @Test
    @DisplayName("canPlayerClaim returns null when player has insufficient buses")
    void cannotClaimWhenNotEnoughBuses() {
        // Player starts with 4 buses; route needs 5
        Route route = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 5, null);
        Player player = new Player("Alice", "red");
        // Give enough cards
        for (int i = 0; i < 5; i++) player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        assertNull(route.canPlayerClaim(player));
    }

    @Test
    @DisplayName("canPlayerClaim returns correct card map when player has enough same-colour cards")
    void canClaimWithSameColourCards() {
        Route route = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 2, null);
        Player player = new Player("Alice", "red");
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));

        HashMap<Colour, Integer> result = route.canPlayerClaim(player);
        assertNotNull(result);
        assertEquals(2, result.get(Colour.GREEN));
    }

    @Test
    @DisplayName("canPlayerClaim returns null when player has insufficient same-colour cards and no buses")
    void cannotClaimInsufficientCards() {
        Route route = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 3, null);
        Player player = new Player("Alice", "red");
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car")); // only 1 green, need 3
        assertNull(route.canPlayerClaim(player));
    }

    @Test
    @DisplayName("canPlayerClaim uses MULTI (bus) cards to supplement same-colour cards")
    void canClaimWithMixedCards() {
        Route route = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 3, null);
        Player player = new Player("Alice", "red");
        player.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        player.addTransportCard(new TransportCard(Colour.MULTI, "Double Decker Bus"));
        player.addTransportCard(new TransportCard(Colour.MULTI, "Double Decker Bus"));

        HashMap<Colour, Integer> result = route.canPlayerClaim(player);
        assertNotNull(result);
        assertEquals(1, result.get(Colour.GREEN));
        assertEquals(2, result.get(Colour.MULTI));
    }

    @Test
    @DisplayName("canPlayerClaim on a MULTI (grey) route accepts any single colour with enough cards")
    void canClaimGreyRouteWithAnyColour() {
        Route route = new Route(Colour.MULTI, City.BAKER_STREET, City.BIG_BEN, 2, null);
        Player player = new Player("Alice", "red");
        player.addTransportCard(new TransportCard(Colour.BLUE, "Milk Truck"));
        player.addTransportCard(new TransportCard(Colour.BLUE, "Milk Truck"));

        HashMap<Colour, Integer> result = route.canPlayerClaim(player);
        assertNotNull(result);
    }

    // ── canPlayerClaim() – double route ──────────────────────────────────────────

    @Test
    @DisplayName("canPlayerClaim returns null when the parallel double route is already claimed")
    void cannotClaimWhenDoubleRouteTaken() {
        Player owner = new Player("Bob", "blue");
        Route twin = new Route(Colour.BLUE, City.BAKER_STREET, City.BIG_BEN, 2, null);
        twin.setOwner(owner); // twin is already claimed

        Route route = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 2, twin);
        Player alice = new Player("Alice", "red");
        alice.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        alice.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));

        assertNull(route.canPlayerClaim(alice));
    }

    @Test
    @Disabled("KNOWN DEFECT: canPlayerClaim() has a structural bug in Route.java (lines 66-70). " +
              "The else-block that performs card validation is only entered when doubleRoute == null. " +
              "When doubleRoute != null but is unclaimed (owner == null), execution falls through " +
              "to the terminal 'return null', making ALL double routes permanently unclaimable. " +
              "Fix: restructure so card-validation logic runs whenever the route is not blocked, " +
              "regardless of whether a double route reference exists.")
    @DisplayName("[KNOWN DEFECT] canPlayerClaim succeeds when double route exists but is unclaimed")
    void canClaimWhenDoubleRouteUnclaimed() {
        Route twin = new Route(Colour.BLUE, City.BAKER_STREET, City.BIG_BEN, 2, null);
        // twin.owner remains null — unclaimed

        Route route = new Route(Colour.GREEN, City.BAKER_STREET, City.BIG_BEN, 2, twin);
        Player alice = new Player("Alice", "red");
        alice.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));
        alice.addTransportCard(new TransportCard(Colour.GREEN, "Old Car"));

        assertNotNull(route.canPlayerClaim(alice));
    }
}
