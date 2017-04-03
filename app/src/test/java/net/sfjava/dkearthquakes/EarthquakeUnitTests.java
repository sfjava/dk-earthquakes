package net.sfjava.dkearthquakes;

import net.sfjava.dkearthquakes.model.Earthquake;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the Earthquake model object.
 *
 */
public class EarthquakeUnitTests {
    @Test
    public void deserialize() throws Exception {
        String testEarthquakeJSON = "{ "
                + "\"datetime\": \"2011-03-11 04:46:23\","
                + "\"depth\": 24.4,"
                + "\"lng\": 142.369,"
                + "\"src\": \"us\","
                + "\"eqid\": \"c0001xgp\","
                + "\"magnitude\": 8.8,"
                + "\"lat\": 38.322"
                + "}";

        Earthquake testEarthquake = Earthquake.fromJSON(new JSONObject(testEarthquakeJSON));
        assertEquals("c0001xgp", testEarthquake.getEarthquakeId());
        // TODO: test all other object fields...
    }
}
