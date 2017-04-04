package net.sfjava.dkearthquakes.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private TextView mProgressMessageTV;
    private RecyclerView mEarthquakesListRV;
    private EarthquakesAdapter mEarthquakesAdapter;

    public static final String EARTHQUAKE_DATA_URL
            = "http://api.geonames.org/earthquakesJSON?formatted=true&north=44.1&south=-9.9&east=-22.4&west=55.2&username=mkoppelman";
    private FetchEarthquakesDataTask mFetchEarthquakesDataTask = null;

    // the current list of earthquakes displayed in the list-view
    private ArrayList<Earthquake> mEarthquakesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressLayout = (LinearLayout) findViewById(R.id.progress_ll);
        mProgressMessageTV = (TextView) findViewById(R.id.progress_message_tv);

        mEarthquakesListRV = (RecyclerView) findViewById(R.id.earthquakes_list_rv);
        mEarthquakesAdapter = new EarthquakesAdapter(getApplicationContext(), mEarthquakesList);
        mEarthquakesListRV.setAdapter(mEarthquakesAdapter);
        mEarthquakesListRV.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL); // add hairline dividers between rows
        mEarthquakesListRV.addItemDecoration(itemDecoration);
    }

    @Override
    public void onResume() {
        super.onResume();

        updateEarthquakeData(EARTHQUAKE_DATA_URL);
    }

    protected void updateEarthquakeData(final String earthquakeDataURL) {
        if(mFetchEarthquakesDataTask != null) {
            return; // skip if we're already running and update task...
        }
        mFetchEarthquakesDataTask = new FetchEarthquakesDataTask(earthquakeDataURL);

        // kick off a background task to update our earthquake data
        mFetchEarthquakesDataTask = new FetchEarthquakesDataTask(earthquakeDataURL);
        mFetchEarthquakesDataTask.execute((Void) null);
    }

    private void showProgress(final boolean show) {
        // show the progress-spinner animation along with an informational message
        mProgressMessageTV.setText("Fetching Earthquake Data...");
        mProgressLayout.setVisibility(show ? View.VISIBLE : View.GONE);

        // hide the list-view while we're updating...
        mEarthquakesListRV.setVisibility(!show ? View.VISIBLE : View.GONE);
    }

    /**
     * An asynchronous task used to retrieve the latest Earthquake data from the service endpoint.
     */
    public class FetchEarthquakesDataTask extends AsyncTask<Void, Void, ArrayList<Earthquake>> {

        private final String mEndpointURL;

        FetchEarthquakesDataTask(String endpointURL) {
            mEndpointURL = endpointURL;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
            ArrayList<Earthquake> earthquakesList = null; // new ArrayList<>();
            if (resultJSON.length() > 0) {
                try {
                    JSONObject earthquakesJSON = new JSONObject(resultJSON.toString());
                    JSONArray earthquakesJSONArray = earthquakesJSON.getJSONArray("earthquakes");

                    earthquakesList = Earthquake.fromJSON(earthquakesJSONArray);

                } catch (Exception e) {
                ; // TODO: handle exceptions properly
            }
            }
            return earthquakesList;
        }

        @Override
        protected void onPostExecute(final ArrayList<Earthquake> earthquakesList) {
            super.onPostExecute(earthquakesList);

            mFetchEarthquakesDataTask = null;
            showProgress(false);

            if (earthquakesList != null) {
                // OK finally, update UI with the (updated) earthquakes list we received...
                //
                mEarthquakesList.clear();
                mEarthquakesList.addAll(earthquakesList);
                mEarthquakesAdapter.notifyDataSetChanged();

            } else {
               // TODO: show error message in UI since we couldn't fetch (new) data
            }
        }

        @Override
        protected void onCancelled() {
            mFetchEarthquakesDataTask = null;
            showProgress(false);
        }
    }
}
