package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Arrival;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test Arrivals
 */
class ArrivalsTest {
    private Route route;
    private Arrival arrival;
    private Arrival arrival2;
    private Arrival arrival3;

    @BeforeEach
    void setup() {
        route = RouteManager.getInstance().getRouteWithNumber("43");
        arrival = new Arrival(23, "Homes", route);
    }

    @Test
    void testConstructor() {
        assertEquals(23, arrival.getTimeToStopInMins());
        assertEquals(route, arrival.getRoute());
    }

    // TODO: design more tests

    @Test
    void testCompareTo() {
        arrival2 = new Arrival(13, "Home", route);
        arrival3 = new Arrival(33, "Home", route);
        assertEquals(-10, arrival2.compareTo(arrival));
        assertEquals(-20, arrival2.compareTo(arrival3));
        assertEquals(10, arrival.compareTo(arrival2));
        assertEquals(-10, arrival.compareTo(arrival3));
        assertEquals(10, arrival3.compareTo(arrival));
        assertEquals(20, arrival3.compareTo(arrival2));
        assertEquals(0, arrival3.compareTo(arrival3));
        assertEquals(0, arrival2.compareTo(arrival2));
        assertEquals(0, arrival.compareTo(arrival));
        assertEquals("Home", arrival2.getDestination());
    }


    @Test
    void testGetStatus() {
        arrival.setStatus("Early");
        assertEquals("Early", arrival.getStatus());
        arrival.setStatus("Late");
        assertEquals("Late", arrival.getStatus());
        arrival.setStatus("On Time");
        assertEquals("On Time", arrival.getStatus());
    }

}
