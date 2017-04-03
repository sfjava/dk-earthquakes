package net.sfjava.dkearthquakes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private View mProgressView;

    public static final String EARTHQUAKE_DATA_URL = "STATE_CURRENT_PAGE";
    private FetchEarthquakeDataTask mFetchEarthquakeDataTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressView = findViewById(R.id.progress_view);
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

        // show a progress spinner and...
        showProgress(true);

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
    public class FetchEarthquakeDataTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEndpointURL;

        FetchEarthquakeDataTask(String endpointURL) {
            mEndpointURL = endpointURL;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            // TODO: fetch actual data from the endpoint URL...
            //
            try {
                // FIXME: for now, simulate network access...
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;   // report 'failed'
            }
            return true;        // report 'success'
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mFetchEarthquakeDataTask = null;
            showProgress(false);

            if (success) {
                // TODO: populate data-model with (JSON) results retrieved from endpoint URL
                //
                // TODO: note that this parsing operation could/should also be done as a background task
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
