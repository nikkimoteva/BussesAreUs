package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Bus;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BusTest {
    private Route route;
    private Route route2;
    private double lat = 42.4;
    private double lon = -123.2;
    private Bus testBus;

    @BeforeEach
    void setup() {
        route = new Route("9999");
        route = new Route("9990");
        testBus = new Bus(route, lat, lon, "Home", "19:30");
    }

    @Test
    void testConstructor() {
        assertEquals("19:30", testBus.getTime());
        assertEquals("Home", testBus.getDestination());
        assertEquals(-123.2, testBus.getLatLon().getLongitude());
        assertEquals(42.4, testBus.getLatLon().getLatitude());
        assertEquals(route, testBus.getRoute());
    }
}
