package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.*;
import ca.ubc.cs.cpsc210.translink.parsers.exception.ArrivalsDataMissingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A parser for the data returned by the Translink arrivals at a stop query
 */
public class ArrivalsParser {

    /**
     * Parse arrivals from JSON response produced by TransLink query.  All parsed arrivals are
     * added to the given stop assuming that corresponding JSON object has a RouteNo and an
     * array of Schedules.  If RouteNo or array of Schedules is missing for a particular route,
     * then none of the arrivals for that route are added to the stop.
     *
     * Each schedule must have an ExpectedCountdown, ScheduleStatus, and Destination.  If
     * any of the aforementioned elements is missing, the arrival is not added to the stop.
     *
     * @param stop             stop to which parsed arrivals are to be added
     * @param jsonResponse    the JSON response produced by Translink
     * @throws JSONException  when:
     * <ul>
     *     <li>JSON response does not have expected format (JSON syntax problem)</li>
     *     <li>JSON response is not an array</li>
     * </ul>
     * @throws ArrivalsDataMissingException  when no arrivals are added to the stop
     */
    public static void parseArrivals(Stop stop, String jsonResponse)
            throws JSONException, ArrivalsDataMissingException {
        // TODO: Task 4: Implement this method
        JSONArray routeArray = new JSONArray(jsonResponse);
        boolean thrownArrivals = true; //no arrivals have been added yet, array has no newly added arrivals
        for (int i = 0; i < routeArray.length(); i++) {
            JSONObject arrivalObj = routeArray.getJSONObject(i);
            try {
                parseRouteArr(arrivalObj, stop, true);
                thrownArrivals = false;
            } catch (ArrivalsDataMissingException e) {
                // ignored
            }
        }
        if (thrownArrivals) {
            throw new ArrivalsDataMissingException();
        }
    }

    private static void parseRouteArr(JSONObject arrivalObj, Stop stop, boolean incorrectArrival)
            throws ArrivalsDataMissingException {
        try {
            String routeNo = arrivalObj.getString("RouteNo");
            JSONArray schedule = arrivalObj.getJSONArray("Schedules");
            Route route = RouteManager.getInstance().getRouteWithNumber(routeNo);
            for (int i = 0; i < schedule.length(); i++) {
                JSONObject arrivalObject = schedule.getJSONObject(i);
                try {
                    parseArrival(arrivalObject, route, stop);
                    incorrectArrival = false;
                } catch (ArrivalsDataMissingException e) {
                    // ignore
                }
            }
        } catch (JSONException e) {
            throw new ArrivalsDataMissingException();
        }
        if (incorrectArrival) {
            throw new ArrivalsDataMissingException();
        }
    }

    private static void parseArrival(JSONObject arrivalObject, Route route, Stop stop)
            throws ArrivalsDataMissingException {
        try {
            int expectedCountdown = arrivalObject.getInt("ExpectedCountdown");
            String destination = arrivalObject.getString("Destination");
            String scheduleStatus = arrivalObject.getString("ScheduleStatus");

            Arrival arrives = new Arrival(expectedCountdown, destination, route);
            stop.addArrival(arrives);

        } catch (JSONException e) {
            throw new ArrivalsDataMissingException();
        }

    }


}
