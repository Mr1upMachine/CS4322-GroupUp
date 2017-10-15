package com.example.seanh.groupup;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;


public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private List<Event> eventsList;

    public class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView ownerName, location, time;
        public ImageView icon, color, background;

        public EventViewHolder(View view) {
            super(view);
            //ownerName = (TextView) view.findViewById(R.id.textEventRowOwner);
            location = (TextView) view.findViewById(R.id.textEventRowLocation);
            time = (TextView) view.findViewById(R.id.textEventRowTime);

            icon = (ImageView) view.findViewById(R.id.imageEventRowIcon);
            color = (ImageView) view.findViewById(R.id.imageEventRowColor);
            background = (ImageView) view.findViewById(R.id.imageEventRowBackground);

            //sets the color of the triangle to a random color
            Random rnd = new Random();
            color.setColorFilter(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        }
    }

    public EventsAdapter(List<Event> eventsList) {
        this.eventsList = eventsList;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_row, parent, false);

        return new EventViewHolder(itemView);
    }

    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = eventsList.get(position);
        //holder.ownerName.setText(event.getOwner().getfName());
        holder.location.setText(event.generateLocString());
        holder.time.setText(event.getTime());
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        eventsList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Event> list) {
        eventsList.addAll(list);
        notifyDataSetChanged();
    }
}
