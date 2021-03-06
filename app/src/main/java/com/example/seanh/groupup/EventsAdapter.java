package com.example.seanh.groupup;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private List<Event> eventsList;
    private User currentUser;
    private List<Bitmap> eventBitmapList;

    public class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView name, location, time;
        public ImageView color, subStar, ownStar, background;

        public EventViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.textEventRowTitle);
            location = view.findViewById(R.id.textEventRowLocation);
            time = view.findViewById(R.id.textEventRowTime);

            color = view.findViewById(R.id.imageEventRowColor);
            subStar = view.findViewById(R.id.imageEventRowSubHeart);
            ownStar = view.findViewById(R.id.imageEventRowOwnStar);
            background = view.findViewById(R.id.imageEventRowBackground);
        }
    }

    public EventsAdapter(List<Event> eventsList, User currentUser, List<Bitmap> bmL) {
        this.eventsList = eventsList;
        this.currentUser = currentUser;
        this.eventBitmapList = bmL;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_row, parent, false);

        return new EventViewHolder(itemView);
    }

    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = eventsList.get(position);
        Bitmap eventImage = eventBitmapList.get(position);
        holder.name.setText( event.getName() );
        holder.location.setText( event.getAddressStreet() );
        holder.time.setText( event.genStartTimeDateSimple() );
        holder.color.setColorFilter( event.getColor() );
        holder.background.setImageBitmap(eventImage);
        if(currentUser != null) {
            //holder.background.setImageDrawable(); //TODO set image
            if (currentUser.getSubscribedEventIds() != null) {
                if (currentUser.containsSubscribedEvent(event.getId())) {
                    holder.subStar.setVisibility(View.VISIBLE);
                } else if (currentUser.getId().equals(event.getOwnerId())) {
                    holder.ownStar.setVisibility(View.VISIBLE);
                } else {
                    holder.subStar.setVisibility(View.INVISIBLE);
                    holder.ownStar.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
