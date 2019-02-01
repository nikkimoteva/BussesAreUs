package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.model.exception.StopException;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test the StopManager
 */
class StopManagerTest {

    @BeforeEach
    void setup() {
        StopManager.getInstance().clearStops();
    }

    @Test
    void testBasic() {
        Stop s9999 = new Stop(9999, "My house", new LatLon(-49.2, 123.2));
        Stop r = StopManager.getInstance().getStopWithNumber(9999);
        assertEquals(s9999, r);
    }

    // TODO: design more tests

    @Test
    void testGetStopWithNumber() {
        Stop s9999 = new Stop(9999, "My place", new LatLon(-50.2, 123.4));
        Stop r = StopManager.getInstance().getStopWithNumber(9999, "My place", new LatLon(-50.2, 123.4));
        Stop m = StopManager.getInstance().getStopWithNumber(6666, "My House", new LatLon(-5.2, 13.4));
        assertEquals(s9999, r);
        assertNotEquals(s9999, m);
        r.setName("My Home");
        assertEquals("My Home", r.getName());
        assertEquals(9999, r.getNumber());
        assertEquals(-5.2, m.getLocn().getLatitude());
        assertEquals(13.4, m.getLocn().getLongitude());
        m.setLocn(new LatLon(-62, 90));
        assertEquals(-62, m.getLocn().getLatitude());
        assertEquals(90, m.getLocn().getLongitude());
        assertEquals("My House", m.getName());
        assertEquals(6666, m.getNumber());
    }

    @Test
    void testSetSelected() {
        Stop s9999 = new Stop(9999, "My house", new LatLon(-49.2, 123.2));
        Stop y = new Stop(9999, "My house", new LatLon(-49.2, 123.2));
        Stop u = StopManager.getInstance().getStopWithNumber(9876, "My place", new LatLon(-50.2, 123.4));
        Stop v = StopManager.getInstance().getStopWithNumber(9876, "My place", new LatLon(-50.2, 123.4));
        Stop r = StopManager.getInstance().getStopWithNumber(9999, "My place", new LatLon(-50.2, 123.4));
        try {
            StopManager.getInstance().setSelected(y);
        } catch (StopException e) {
            fail("Unexpected Exception!");
        }
        assertEquals(StopManager.getInstance().getSelected(), y);
        assertEquals(StopManager.getInstance().getSelected(), s9999);
        assertEquals(StopManager.getInstance().getSelected(), r);
        assertEquals(u, v);
        assertEquals(2, StopManager.getInstance().getNumStops());
        StopManager.getInstance().clearStops();
        assertEquals(0, StopManager.getInstance().getNumStops());

        Stop m = new Stop(6666, "My House", new LatLon(-5.2, 13.4));
        try {
            StopManager.getInstance().setSelected(m);
            fail("It does not contain the stop!");
        } catch (StopException e) {
            //expected
        }
        StopManager.getInstance().clearSelectedStop();
        assertNotEquals(StopManager.getInstance().getSelected(), y);
        assertNotEquals(StopManager.getInstance().getSelected(), s9999);
        assertNotEquals(StopManager.getInstance().getSelected(), r);
    }

    @Test
    void testClearStopsAndGetNumStops() {
        Stop s9999 = new Stop(9999, "My house", new LatLon(-49.2, 123.2));
        Stop r = StopManager.getInstance().getStopWithNumber(9999, "My place", new LatLon(-50.2, 123.4));
        assertEquals(1, StopManager.getInstance().getNumStops());
        StopManager.getInstance().clearStops();
        assertEquals(0, StopManager.getInstance().getNumStops());
    }

    @Test
    void testFindNearestTo() {
        Stop s9999 = new Stop(9999, "My house", new LatLon(-49.2, 123.2));
        Stop y = new Stop(908, "My house", new LatLon(-50.2, 123.4));
        Stop u = StopManager.getInstance().getStopWithNumber(9876, "My place", new LatLon(-30.0,
                                                                                                        140.2));
        Stop r = StopManager.getInstance().getStopWithNumber(9999, "My place", new LatLon(-10.9,
                                                                                                        80.5));
        assertEquals(r, StopManager.getInstance().findNearestTo(new LatLon(-10.91, 80.52)));
        assertNotEquals(u, StopManager.getInstance().findNearestTo(r.getLocn()));
        assertNull(StopManager.getInstance().findNearestTo(new LatLon(-9.82, 20)));
        assertNull(StopManager.getInstance().findNearestTo(new LatLon(-10.8, 80)));
        assertEquals(r, StopManager.getInstance().findNearestTo(new LatLon(-10.89, 80.49)));
    }

}
