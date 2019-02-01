package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * Test the RouteManager
 */
class RouteManagerTest {

    @BeforeEach
    void setup() {
        RouteManager.getInstance().clearRoutes();
    }

    @Test
    void testBasic() {
        Route r43 = new Route("43");
        Route r = RouteManager.getInstance().getRouteWithNumber("43");
        assertEquals(r43, r);
    }

    // TODO: design more tests

    @Test
    void testGetRouteWithNumber() {
        Route s9999 = new Route("9999");
        Route r = RouteManager.getInstance().getRouteWithNumber("9999", "My place");
        Route o = RouteManager.getInstance().getRouteWithNumber("9999", "My place");
        Route m = RouteManager.getInstance().getRouteWithNumber("6666", "My House");
        assertEquals(s9999, r);
        assertEquals(o, r);
        assertNotEquals(s9999, m);
        r.setName("My Home");
        assertEquals("My Home", r.getName());
        assertEquals("9999", r.getNumber());
        assertEquals("My House", m.getName());
        assertEquals("6666", m.getNumber());
    }
}
