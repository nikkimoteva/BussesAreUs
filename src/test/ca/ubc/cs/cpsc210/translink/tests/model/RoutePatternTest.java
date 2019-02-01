package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class RoutePatternTest {
    private RoutePattern testPattern;
    private RoutePattern testPattern2;
    private RoutePattern testPattern3;
    private Route testingRoute;
    private Route route;

    @BeforeEach
    void setup() {
        route = new Route("9999");
        testingRoute = new Route("9990");
        testPattern = new RoutePattern("name", "Home", "West", route);
    }

    @Test
    void testRoutePattern() {
        assertEquals("name", testPattern.getName());
        assertEquals("Home", testPattern.getDestination());
        assertEquals("West", testPattern.getDirection());
        assertEquals(0, testPattern.getPath().size());

        List<LatLon> testPath = new ArrayList<>();
        testPattern.setDirection("East");
        testPattern.setDestination("");
        testPattern.setPath(testPath);
        testPath.add(new LatLon(42.3, -123.2));
        testPath.add(new LatLon(43.2, -122.3));
        assertEquals("name", testPattern.getName());
        assertEquals("", testPattern.getDestination());
        assertEquals("East", testPattern.getDirection());
        assertEquals(2, testPattern.getPath().size());
        assertEquals(43.2, testPath.get(1).getLatitude());
        assertEquals(42.3, testPath.get(0).getLatitude());
        assertEquals(-123.2, testPath.get(0).getLongitude());
        assertEquals(-122.3, testPath.get(1).getLongitude());

        testPattern2 = new RoutePattern("name", "School", "West", route);
        testPattern3 = new RoutePattern("not", "the", "same", testingRoute);
        assertNotEquals(testPattern, testPattern3);
        assertFalse(testPattern.equals(testPattern3));
        assertEquals(testPattern, testPattern2);
        assertEquals(testPattern.hashCode(), testPattern2.hashCode());

    }

    @Test
    void testEquals() {
        assertNotEquals(testingRoute, route);
        assertFalse(testingRoute.equals(route));
    }
}
