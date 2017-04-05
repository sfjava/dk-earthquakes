package net.sfjava.dkearthquakes.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.sfjava.dkearthquakes.R;
import net.sfjava.dkearthquakes.model.Earthquake;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Adapter to display a list of Earthquakes as item rows in a RecyclerView.
 */

public class EarthquakesAdapter extends RecyclerView.Adapter<EarthquakesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Earthquake> earthquakesList;

    public EarthquakesAdapter(Context context, ArrayList<Earthquake> earthquakesList) {
        this.context = context;
        this.earthquakesList = earthquakesList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // note view-holder should contain a member variable for any view that will be set as you render a row
        public TextView earthquakeIdTV;
        public TextView magnitudeTV;
        public ProgressBar magnitudePB;
        public TextView depthTV;
        public TextView latitudeTV;
        public TextView longitudeTV;
        public TextView datetimeTV;

        // note view-holder constructor accepts the entire item row and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            super(itemView);

            earthquakeIdTV = (TextView) itemView.findViewById(R.id.earthquake_id_tv);
            magnitudeTV = (TextView) itemView.findViewById(R.id.magnitude_tv);
            magnitudePB = (ProgressBar) itemView.findViewById(R.id.magnitude_pb);
            depthTV = (TextView) itemView.findViewById(R.id.depth_tv);
            latitudeTV = (TextView) itemView.findViewById(R.id.latitude_tv);
            longitudeTV = (TextView) itemView.findViewById(R.id.longitude_tv);
            datetimeTV = (TextView) itemView.findViewById(R.id.datetime_tv);
        }
    }

    // inflate row-layout from XML and return the view-holder
    @Override
    public EarthquakesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.item_earthquake, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    // populate data from model iem into view via view-holder
    @Override
    public void onBindViewHolder(EarthquakesAdapter.ViewHolder vh, int position) {
        // get the data model obj based on position
        Earthquake quake = earthquakesList.get(position);

        // populate item views from data model obj
        vh.earthquakeIdTV.setText(quake.getEarthquakeId());
        vh.magnitudeTV.setText(Float.toString(quake.getMagnitude()));
        vh.magnitudePB.setProgress((int) ((quake.getMagnitude() / 10.0f) * 100)); // show magnitude bar as range 0-100

        // if magnitude of quake is greater or equal to 8.0, change color of magnitude-bar
        LayerDrawable layerDrawable = (LayerDrawable) vh.magnitudePB.getProgressDrawable();
        Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
        if(quake.getMagnitude() >= 8.0f) {
            // vh.magnitudePB.setProgressTintList(ColorStateList.valueOf(Color‌​.RED)); // only works for API >= 21
            progressDrawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        } else {
            progressDrawable.setColorFilter(Color.rgb(96, 192, 192), PorterDuff.Mode.SRC_IN);
        }

        vh.depthTV.setText(Float.toString(quake.getDepth()));
        vh.latitudeTV.setText(Double.toString(quake.getLatitude()));
        vh.longitudeTV.setText(Double.toString(quake.getLongitude()));

        String localDateTime = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(quake.getDatetime());
        vh.datetimeTV.setText(localDateTime);
    }

    // returns the total count of items in the list
    @Override
    public int getItemCount() {
        return earthquakesList.size();
    }
}
