package net.sfjava.dkearthquakes.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Data model object for a (single) Earthquake event.
 */
public class Earthquake {

    private String earthquakeId;
    private Date datetime;

    private float depth;
    private float magnitude;
    private double latitude;
    private double longitude;

    private String sourceCountryCode;

    public Earthquake() {
    }

    public String getEarthquakeId() {
        return earthquakeId;
    }

    public Date getDatetime() {
        return datetime;
    }

    public float getDepth() {
        return depth;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getSourceCountryCode() {
        return sourceCountryCode;
    }

    /*
     * Returns a new instance of an Earthquake object, populated from the provided JSON string param.
     *
     * Note that the JSON object for an Earthquake (from the service endpoint we are using)
     * has the following structure:
     *
     *   {
     *      "datetime": "2011-03-11 04:46:23",
     *      "depth": 24.4,
     *      "lng": 142.369,
     *      "src": "us",
     *      "eqid": "c0001xgp",
     *      "magnitude": 8.8,
     *      "lat": 38.322
     *  }
     *
     */
    public static Earthquake fromJSON(JSONObject earthquakeJSON) {

        Earthquake e = new Earthquake();

        // deserialize JSON into object fields...
        try {
            String datetimeStr = earthquakeJSON.getString("datetime");
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = sdf.parse(datetimeStr);
                e.datetime = date;

            } catch (ParseException e1) {
                // TODO: handle exception when parsing date/time field
            }

            e.earthquakeId = earthquakeJSON.getString("eqid");
            e.magnitude = (float) earthquakeJSON.getDouble("magnitude");
            e.depth = (float) earthquakeJSON.getDouble("depth");
            e.latitude = earthquakeJSON.getDouble("lat");
            e.longitude = earthquakeJSON.getDouble("lng");
            e.sourceCountryCode = earthquakeJSON.getString("src");

        } catch (JSONException ex) {
            return null;
        }
        return e;
    }

    /*
     * Returns a List of Earthquake objects, populated from the provided JSONArray object.
     *
     * Note that the JSONArray object for a *list* of Earthquakes (from the service endpoint we are using)
     * has the following structure:
     *
     * { "earthquakes": [
     *   {
     *      "datetime": "2011-03-11 04:46:23",
     *      "depth": 24.4,
     *      "lng": 142.369,
     *      "src": "us",
     *      "eqid": "c0001xgp",
     *      "magnitude": 8.8,
     *      "lat": 38.322
     *   },
     *   {
     *      "datetime": "2012-04-11 06:38:37",
     *      "depth": 22.9,
     *      "lng": 93.0632,
     *      "src": "us",
     *      "eqid": "c000905e",
     *      "magnitude": 8.6,
     *      "lat": 2.311
     *   },
     *   ...
     * ]}
     *
     */
    public static ArrayList<Earthquake> fromJSON(JSONArray earthquakesJSONArray) {

        ArrayList<Earthquake> earthquakesList = new ArrayList<Earthquake>(earthquakesJSONArray.length());
        for (int i = 0; i < earthquakesJSONArray.length(); i++) {
            try {
                JSONObject earthquakeJSON = earthquakesJSONArray.getJSONObject(i);
                Earthquake earthquake = Earthquake.fromJSON(earthquakeJSON);
                if (earthquake != null) {
                    earthquakesList.add(earthquake);
                }
            } catch (Exception e) {
                // TODO: handle this exception appropriately
                continue;
            }
        }
        return earthquakesList;
    }
}
