package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.*;
import ca.ubc.cs.cpsc210.translink.model.exception.RouteException;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class StopTest {
    private int num;
    private String name;
    private LatLon locn;
    private Route testRoute;
    private Route testRoute2;
    private Stop testStop;
    private Stop testStop2;
    private Stop testStop3;
    private HashSet<Route> routes;
    private RoutePattern testPattern;
    private RoutePattern testPattern2;
    private Arrival testArrival;
    private Arrival testArrival2;
    private Arrival testArrival3;
    private ArrayList<Arrival> arrival;
    private ArrayList<Bus> buses;
    private Bus testBus;
    private double lat = 42.4;
    private double lon = -123.2;

    @BeforeEach
    void setup() {
        testRoute = new Route("9999");
        testRoute2 = new Route("9999");
        testStop = new Stop(1, "naming", new LatLon(42.3, -123.2));
        testStop2 = new Stop(7, "namings", new LatLon(42.4, -123.3));
        testStop3 = new Stop(1, "namings", new LatLon(42.4, -123.3));

        testPattern = new RoutePattern("name", "dest", "North", testRoute);
        testArrival = new Arrival(10, "Home", testRoute);
        testArrival2 = new Arrival(20, "Home", testRoute);
        testArrival3 = new Arrival(30, "Home", testRoute);

        testBus = new Bus(testRoute, lat, lon, "Home", "19:30");

        testRoute.setName("name");
        testRoute.hasStop(testStop);
        testRoute.addPattern(testPattern);
    }

    @Test
    void testStop() {
        testStop.addRoute(testRoute);
        //assertFalse(testStop.onRoute(testRoute));
        testRoute.addStop(testStop);
        assertFalse(testStop.onRoute(testRoute2));
        assertEquals(1, testStop.getRoutes().size());
        testStop.removeRoute(testRoute);
        assertEquals(0, testStop.getRoutes().size());

        testStop2.addRoute(testRoute2);
        testRoute2.addStop(testStop2);
        assertTrue(testStop2.onRoute(testRoute2));
    }

    @Test
    void testStopArrival() {
        testStop.addArrival(testArrival);
        testStop.addArrival(testArrival2);
        testStop.addArrival(testArrival3);
        //assertEquals();
        testStop.clearArrivals();

        assertEquals(testStop, testStop3);
        assertEquals(testStop.hashCode(), testStop3.hashCode());
    }

//    @Test
//    void testBus() {
//        try {
//            testStop.addBus(testBus);
//        } catch (RouteException e) {
//            fail("This stop not on bus' route!!");
//        }
//    }
}
