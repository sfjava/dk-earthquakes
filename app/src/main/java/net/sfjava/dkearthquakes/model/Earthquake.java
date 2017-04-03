package net.sfjava.dkearthquakes.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Data model object for a (single) Earthquake event.
 */
public class Earthquake {

    private String earthquakeId;
    private String datetimeStr;

    private float depth;
    private float magnitude;
    private float latitude;
    private float longitude;

    private String sourceCountryCode;

    public Earthquake() {
    }

    public String getEarthquakeId() {
        return earthquakeId;
    }

    public long getDatetime() {
        // return datetimeStr;
        return System.currentTimeMillis(); // FIXME
    }

    public float getDepth() {
        return depth;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
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
            e.earthquakeId = earthquakeJSON.getString("eqid");

            // TODO: finish impl for all other JSON fields
            //
        } catch (JSONException ex) {
            return null;
        }
        return e;
    }

}
