package net.sfjava.dkearthquakes.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.sfjava.dkearthquakes.R;
import net.sfjava.dkearthquakes.model.Earthquake;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mProgressLayout;
    private TextView mMessageTV;

    public static final String EARTHQUAKE_DATA_URL
            = "http://api.geonames.org/earthquakesJSON?formatted=true&north=44.1&south=-9.9&east=-22.4&west=55.2&username=mkoppelman";
    private FetchEarthquakeDataTask mFetchEarthquakeDataTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressLayout = (LinearLayout) findViewById(R.id.progress_ll);
        mMessageTV =  (TextView) findViewById(R.id.message_tv);
    }

    @Override
    public void onResume() {
        super.onResume();

        updateEarthquakeData(EARTHQUAKE_DATA_URL);
    }

    protected void updateEarthquakeData(final String earthquakeDataURL) {
        if(mFetchEarthquakeDataTask != null) {
            return; // skip if we're already running and update task...
        }
        mFetchEarthquakeDataTask = new FetchEarthquakeDataTask(earthquakeDataURL);

        // kick off a background task to update our earthquake data
        mFetchEarthquakeDataTask = new FetchEarthquakeDataTask(earthquakeDataURL);
        mFetchEarthquakeDataTask.execute((Void) null);
    }

    private void showProgress(final boolean show) {
        mProgressLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * An asynchronous task used to retrieve the latest Earthquake data from the service endpoint.
     */
    public class FetchEarthquakeDataTask extends AsyncTask<Void, Void, ArrayList<Earthquake>> {

        private final String mEndpointURL;

        FetchEarthquakeDataTask(String endpointURL) {
            mEndpointURL = endpointURL;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mMessageTV.setText("Fetching Earthquake Data...");
            showProgress(true); // show a progress spinner...
        }

        @Override
        protected ArrayList<Earthquake> doInBackground(Void... params) {

            // fetch JSON earthquake data from the endpoint URL...
            StringBuilder resultJSON = new StringBuilder();
            try {
                URL url = new URL(mEndpointURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                Thread.sleep(3000); // FIXME: remove this ARTIFICIAL TEST DELAY once finished testing progress-spinner UI...
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        resultJSON.append(line);
                    }
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                ; // TODO: handle exceptions properly
            }

            // now parse the JSON list of all Earthquakes retrieved
            ArrayList<Earthquake> earthquakesList = new ArrayList<>();
            if (resultJSON.length() > 0) {
                try {
                    JSONObject earthquakesJSON = new JSONObject(resultJSON.toString());
                    JSONArray earthquakesJSONArray = earthquakesJSON.getJSONArray("earthquakes");

                    earthquakesList.clear(); // clear existing items
                    earthquakesList.addAll(Earthquake.fromJSON(earthquakesJSONArray)); // add new items

                } catch (Exception e) {
                ; // TODO: handle exceptions properly
            }
            }
            return earthquakesList;
        }

        @Override
        protected void onPostExecute(final ArrayList<Earthquake> earthquakesList) {
            super.onPostExecute(earthquakesList);

            mFetchEarthquakeDataTask = null;
            showProgress(false);

            if (earthquakesList != null) {
                // OK finally, update UI with the (updated) earthquakes list we received...
                //
                // FIXME: impl using RecyclerView; i.e. update it's Adapter instead
                StringBuilder sb = new StringBuilder();
                for(Earthquake earthquake : earthquakesList) {
                    sb.append(earthquake.getEarthquakeId() + "\n");
                }
                mMessageTV.setText(sb.toString());

            } else {
               // TODO: show error message in UI since we couldn't fetch (new) data
            }
        }

        @Override
        protected void onCancelled() {
            mFetchEarthquakeDataTask = null;
            showProgress(false);
        }
    }
}
