package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for routes stored in a compact format in a txt file
 */
public class RouteMapParser {
    private String fileName;

    public RouteMapParser(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Parse the route map txt file
     */
    public void parse() {
        DataProvider dataProvider = new FileDataProvider(fileName);
        try {
            String c = dataProvider.dataSourceToString();
            if (!c.equals("")) {
                int posn = 0;
                while (posn < c.length()) {
                    int endposn = c.indexOf('\n', posn);
                    String line = c.substring(posn, endposn);
                    parseOnePattern(line);
                    posn = endposn + 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse one route pattern, adding it to the route that is named within it
     * @param str  string representing one route pattern (single line of data from source file)
     *
     * Each line begins with a capital N, which is not part of the route number, followed by the
     * bus route number, a dash, the pattern name, a semicolon, and a series of 0 or more real
     * numbers corresponding to the latitude and longitude (in that order) of a point in the pattern,
     * separated by semicolons. The 'N' that marks the beginning of the line is not part of the bus
     * route number.
     */
    private void parseOnePattern(String str) {
        int indexOfFirstSemicolon = str.indexOf(";");
        int indexOfFirstDash = str.indexOf("-");
        String routeNumber = str.substring(1, indexOfFirstDash);
        String patternName = str.substring(indexOfFirstDash + 1, indexOfFirstSemicolon);
        String afterFirst = str.substring(indexOfFirstSemicolon + 1);
        if (!afterFirst.isEmpty() && afterFirst != null) {
            String[] latLoning = afterFirst.split(";");
            List<LatLon> path = getLatLons(latLoning);
            storeRouteMap(routeNumber, patternName, path);
        }
    }

    private List<LatLon> getLatLons(String[] latLoning) {
        double lat = 0.0;
        double lon = 0.0;
        List<LatLon> path = new ArrayList<>();
        for (int i = 0; i < latLoning.length; i++) {
            String data = latLoning[i];
            if (i % 2 != 0) {
                lon = Double.parseDouble(data);
                path.add(new LatLon(lat, lon));
            } else {
                lat = Double.parseDouble(data);
            }
        }
        return path;
    }

    /**
     * Store the parsed pattern into the named route
     * Your parser should call this method to insert each route pattern into the corresponding route object
     * There should be no need to change this method
     *
     * @param routeNumber       the number of the route
     * @param patternName       the name of the pattern
     * @param elements          the coordinate list of the pattern
     */
    private void storeRouteMap(String routeNumber, String patternName, List<LatLon> elements) {
        Route r = RouteManager.getInstance().getRouteWithNumber(routeNumber);
        RoutePattern rp = r.getPattern(patternName);
        rp.setPath(elements);
    }

//    // EFFECTS: returns number of ResourcePattern
//    private String parsePatternNum(String[] names) {
//        String namePattern = names[1];
////        for (int i = 2; i < names.length; i++) {
////            namePattern = namePattern + "-" + names[i];
////        }
//        return namePattern;
//    }
}
