package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.regex.Pattern;

public class RouteTest {
    private List<Stop> stops;
    private Stop testStop;
    private Route testRoute;
    private Route testRoute2;
    private Route testRoute3;
    private RoutePattern testPattern;
    private RoutePattern testPattern2;
    private RoutePattern testPattern3;
    private List<RoutePattern> patterns;
    private String number;

    @BeforeEach
    void setup() {
        testRoute = new Route("9999");
        testRoute2 = new Route("9999");
        testRoute3 = new Route(null);
        testPattern = new RoutePattern("name", "dest", "North", testRoute);
        testPattern2 = new RoutePattern("name2", "dest2", "North2", testRoute2);
        testPattern3 = new RoutePattern("name", "dest", "North", testRoute);
        testStop = new Stop(1, "naming", new LatLon(42.3, -123.2));
    }

    @Test
    void testRoute() {
        testRoute.setName("name");
        testRoute.addPattern(testPattern);
        testRoute.addStop(testStop);
        assertEquals("9999", testRoute.getNumber());
        assertEquals("name", testRoute.getName());
        assertEquals(1, testRoute.getStops().size());
        assertTrue(testRoute.hasStop(testStop));
        testRoute.removeStop(testStop);
        assertEquals(0, testRoute.getStops().size());
        assertEquals(testRoute.hashCode(), testRoute2.hashCode());
        assertEquals("Route 9999", testRoute.toString());
        assertFalse(testRoute.equals(testRoute3));
    }

    @Test
    void testGetPattern() {
        testRoute.addPattern(testPattern);
        testRoute2.addPattern(testPattern2);
        assertEquals(testPattern2, testRoute2.getPattern("name2"));
        assertEquals(testPattern, testRoute2.getPattern("name", "dest", "North"));
        assertEquals(testPattern2, testRoute2.getPattern("name2", "dest2", "North2"));
    }
}
