package net.sfjava.dkearthquakes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private View mProgressView;
    private TextView mMessageTV;

    public static final String EARTHQUAKE_DATA_URL
            = "http://api.geonames.org/earthquakesJSON?formatted=true&north=44.1&south=-9.9&east=-22.4&west=55.2&username=mkoppelman";
    private FetchEarthquakeDataTask mFetchEarthquakeDataTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressView = findViewById(R.id.progress_view);
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
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * An asynchronous task used to retrieve the latest Earthquake data from the service endpoint.
     */
    public class FetchEarthquakeDataTask extends AsyncTask<Void, Void, String> {

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
        protected String doInBackground(Void... params) {

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
            return resultJSON.toString();
        }

        @Override
        protected void onPostExecute(final String resultJSON) {
            super.onPostExecute(resultJSON);

            mFetchEarthquakeDataTask = null;
            showProgress(false);

            if (resultJSON.length() > 0) {
                // TODO: populate data-model with (JSON) results retrieved from endpoint URL
                //
                mMessageTV.setText(resultJSON); // FIXME: show result JSON in main layout

                // TODO: note that any JSON parsing operation could/should also be done as a background task
                //
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
