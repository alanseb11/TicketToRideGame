import backend.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TransportCard}.
 */
@DisplayName("TransportCard")
class TransportCardTest {

    @Test
    @DisplayName("Constructor sets colour and name correctly")
    void constructorSetsFields() {
        TransportCard card = new TransportCard(Colour.GREEN, "Old Car");
        assertEquals(Colour.GREEN, card.getColour());
        assertEquals("Old Car", card.getName());
    }

    @Test
    @DisplayName("canDraw is true by default after construction")
    void canDrawDefaultTrue() {
        TransportCard card = new TransportCard(Colour.MULTI, "Double Decker Bus");
        assertTrue(card.canDraw());
    }

    @Test
    @DisplayName("setCanDraw(false) locks the card")
    void setCanDrawFalseLocks() {
        TransportCard card = new TransportCard(Colour.BLUE, "Milk Truck");
        card.setCanDraw(false);
        assertFalse(card.canDraw());
    }

    @Test
    @DisplayName("setCanDraw(true) unlocks a previously locked card")
    void setCanDrawTrueUnlocks() {
        TransportCard card = new TransportCard(Colour.BLUE, "Milk Truck");
        card.setCanDraw(false);
        card.setCanDraw(true);
        assertTrue(card.canDraw());
    }

    @Test
    @DisplayName("toString contains name and colour")
    void toStringContainsNameAndColour() {
        TransportCard card = new TransportCard(Colour.PINK, "Rocket Car");
        String s = card.toString();
        assertTrue(s.contains("Rocket Car"));
        assertTrue(s.contains("PINK"));
    }

    @Test
    @DisplayName("Each colour constant can be stored in a TransportCard")
    void allColoursSupported() {
        for (Colour c : Colour.values()) {
            TransportCard card = new TransportCard(c, c.name());
            assertEquals(c, card.getColour());
        }
    }
}
