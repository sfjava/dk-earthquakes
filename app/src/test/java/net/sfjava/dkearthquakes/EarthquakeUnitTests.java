package net.sfjava.dkearthquakes;

import net.sfjava.dkearthquakes.model.Earthquake;

import org.json.JSONObject;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

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

        Calendar calendar = Calendar.getInstance();
        calendar.clear(); // CAVEAT: we *must* clear the millisecs field in order to match date-time in example
        calendar.set(2011, 02, 11, 04, 46, 23); // CAVEAT: note MM field is *zero* based (i.e. 0 = Jan)
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals(calendar.getTime().getTime(), testEarthquake.getDatetime().getTime());

        assertEquals(8.8f, testEarthquake.getMagnitude(), 0.001);
        assertEquals(24.4f, testEarthquake.getDepth(), 0.001);
        assertEquals(38.322d, testEarthquake.getLatitude(), 0.0001);
        assertEquals(142.369d, testEarthquake.getLongitude(), 0.0001);
        assertEquals("us", testEarthquake.getSourceCountryCode());
    }
}
