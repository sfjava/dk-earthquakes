package net.sfjava.dkearthquakes.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sfjava.dkearthquakes.R;
import net.sfjava.dkearthquakes.model.Earthquake;

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

        // note view-holder constructor accepts the entire item row and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            super(itemView);

            earthquakeIdTV = (TextView) itemView.findViewById(R.id.earthquake_id_tv);
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
    public void onBindViewHolder(EarthquakesAdapter.ViewHolder viewHolder, int position) {
        // get the data model obj based on position
        Earthquake earthquake = earthquakesList.get(position);

        // populate item views from data model obj
        TextView textView = viewHolder.earthquakeIdTV;
        textView.setText(earthquake.getEarthquakeId());
    }

    // returns the total count of items in the list
    @Override
    public int getItemCount() {
        return earthquakesList.size();
    }
}
