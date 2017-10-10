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


public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {

    private List<Event> eventsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ownerName, location, time;
        public ImageView icon, color, background;

        public MyViewHolder(View view) {
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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_row, parent, false);

        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        Event event = eventsList.get(position);
        //holder.ownerName.setText(event.getOwner().getfName());
        holder.location.setText(event.getLoc());
        holder.time.setText(event.getTime());
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}
