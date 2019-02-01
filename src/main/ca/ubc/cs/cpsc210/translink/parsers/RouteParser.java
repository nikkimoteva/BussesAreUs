package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.parsers.exception.RouteDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Parse route information in JSON format.
 */
public class RouteParser {
    private String filename;

    public RouteParser(String filename) {
        this.filename = filename;
    }

    /**
     * Parse route data from the file and add all route to the route manager.
     *
     */
    public void parse() throws IOException, RouteDataMissingException, JSONException {
        DataProvider dataProvider = new FileDataProvider(filename);

        parseRoutes(dataProvider.dataSourceToString());
    }

    /**
     * Parse route information from JSON response produced by Translink.
     * Stores all routes and route patterns found in the RouteManager.  A pattern that
     * is missing any one of PatternNo, Destination or Direction is silently ignored
     * and not added to the route.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException   when:
     * <ul>
     *     <li>JSON data does not have expected format (JSON syntax problem)
     *     <li>JSON data is not an array
     * </ul>
     * If a JSONException is thrown, no stops should be added to the stop manager
     *
     * @throws RouteDataMissingException when
     * <ul>
     *  <li>JSON data is missing RouteNo, Name, or Patterns element for any route</li>
     *  <li>The value of the Patterns element is not an array for any route</li>
     * </ul>
     *
     * If a RouteDataMissingException is thrown, all correct routes are first added to the route manager.
     */
    public void parseRoutes(String jsonResponse)
            throws JSONException, RouteDataMissingException {
        // TODO: Task 4: Implement this method
        JSONArray routesArray = new JSONArray(jsonResponse);
        boolean hasItEverBeenThrown = false;

        for (int i = 0; i < routesArray.length(); i++) {
            JSONObject routeObj = routesArray.getJSONObject(i);
            try {
                parseRoute(routeObj);
            } catch (RouteDataMissingException e) {
                hasItEverBeenThrown = true;
            }
        }
        if (hasItEverBeenThrown) {
            throw new RouteDataMissingException();
        }
    }

    private void parseRoute(JSONObject routeObj) throws RouteDataMissingException {
        try {
            String routeNo = routeObj.getString("RouteNo");
            String routeName = routeObj.getString("Name");
            JSONArray patternArray = routeObj.getJSONArray("Patterns");
            Route route = RouteManager.getInstance().getRouteWithNumber(routeNo, routeName);
            for (int i = 0; i < patternArray.length(); i++) {
                JSONObject patternObj = patternArray.getJSONObject(i);
                parsePattern(patternObj, route);
//                    route.addPattern(parsedPattern);
            }
        } catch (JSONException e) {
            throw new RouteDataMissingException();
        }
    }

    private void parsePattern(JSONObject pattenObj, Route route) {
        try {
            String patternNo = pattenObj.getString("PatternNo");
            String destination = pattenObj.getString("Destination");
            String direction = pattenObj.getString("Direction");
            route.getPattern(patternNo, destination, direction);
        } catch (JSONException e) {
            //ignore
        }

//        RoutePattern routingPat = new RoutePattern(patternNo, destination, direction, route);
//        return routingPat;
    }
}
