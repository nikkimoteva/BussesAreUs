package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.*;
import ca.ubc.cs.cpsc210.translink.parsers.exception.StopDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * A parser for the data returned by Translink stops query
 */
public class StopParser {
    private String filename;

    public StopParser(String filename) {
        this.filename = filename;
    }

    /**
     * Parse stop data from the file and add all stops to stop manager.
     *
     */
    public void parse() throws IOException, StopDataMissingException, JSONException {
        DataProvider dataProvider = new FileDataProvider(filename);

        parseStops(dataProvider.dataSourceToString());
    }

    /**
     * Parse stop information from JSON response produced by Translink.
     * Stores all stops and routes found in the StopManager and RouteManager.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException when:
     * <ul>
     *     <li>JSON data does not have expected format (JSON syntax problem)</li>
     *     <li>JSON data is not an array</li>
     * </ul>
     * @throws StopDataMissingException when
     * <ul>
     *     <li> JSON data is missing Name, StopNo, Routes or location (Latitude or Longitude) elements for any stop</li>
     * </ul>
     * If a StopDataMissingException is thrown, all stops for which all required data is available
     * are first added to the stop manager.
     */
    public void parseStops(String jsonResponse)
            throws JSONException, StopDataMissingException {
        // TODO: Task 4: Implement this method
        JSONArray stopArray = new JSONArray(jsonResponse);
        boolean thrownUp = false;
        for (int i = 0; i < stopArray.length(); i++) {
            JSONObject stopObject = stopArray.getJSONObject(i);
            try {
                parseStop(stopObject);
            } catch (StopDataMissingException e) {
                thrownUp = true;
            }
        }
        if (thrownUp) {
            throw new StopDataMissingException();
        }
    }

    private void parseStop(JSONObject stopObject) throws StopDataMissingException {
        try {
            String stopName = stopObject.getString("Name");
            int stopNo = stopObject.getInt("StopNo");
            Double stopLat = stopObject.getDouble("Latitude");
            Double stopLon = stopObject.getDouble("Longitude");
            LatLon locn = new LatLon(stopLat, stopLon);

            String routes = stopObject.getString("Routes");
            String[] routeSplit = routes.split(", ");
            Stop stopParse = StopManager.getInstance().getStopWithNumber(stopNo, stopName, locn);
            storeRoute(stopParse, routeSplit);

        } catch (JSONException e) {
            throw new StopDataMissingException();
        }
    }

    private void storeRoute(Stop stopParse, String[] routeSplit) {
        for (int j = 0; j < routeSplit.length; j++) {
            String routeString = routeSplit[j];
            Route routing = RouteManager.getInstance().getRouteWithNumber(routeString);
            stopParse.addRoute(routing);
        }
    }
}
