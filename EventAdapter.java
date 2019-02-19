package bd.org.bitm.mad.batch33.tourmate.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bd.org.bitm.mad.batch33.tourmate.MainActivity;
import bd.org.bitm.mad.batch33.tourmate.R;
import bd.org.bitm.mad.batch33.tourmate.fragment.Event.EventDetailFragment;
import bd.org.bitm.mad.batch33.tourmate.model.DateConverter;
import bd.org.bitm.mad.batch33.tourmate.model.Event;
import bd.org.bitm.mad.batch33.tourmate.model.NetworkConnectivity;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private Context context;
    private List<Event> events;
    private MainActivity activity;
    private Date date = new Date();
    private DateConverter converter;

    public EventAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
        this.date = Calendar.getInstance().getTime();
        converter = new DateConverter();
        activity = (MainActivity) context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list, parent, false);

        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, final int position) {
        Event event = events.get(position);
        holder.eventNameTV.setText(event.getEventName());
        holder.departureTV.setText("Start On: "+event.getDepartureDate());
        holder.createTV.setText("Created On: "+event.getCreationDate());
        holder.leftDateTV.setText(NetworkConnectivity.daysDifference(new Date(), event.getDepartureDate())+" days left");



    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{
       private TextView eventNameTV, createTV, departureTV,leftDateTV;
       private Context context;
        public EventViewHolder(View itemView) {
            super(itemView);
            eventNameTV = itemView.findViewById(R.id.eventName);
            createTV = itemView.findViewById(R.id.createdDate);
            departureTV = itemView.findViewById(R.id.startDate);
            leftDateTV = itemView.findViewById(R.id.dayLeft);

            this.context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Clicked on "+getAdapterPosition(), Toast.LENGTH_LONG).show();
                    Event evented = events.get(getAdapterPosition());

                    AppCompatActivity activity = (AppCompatActivity) context;

                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    EventDetailFragment fragment = new EventDetailFragment();

                    //sending event to UpdateEventFragment using bundle
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("event",evented);
                    fragment.setArguments(bundle);

                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }
    }
}
