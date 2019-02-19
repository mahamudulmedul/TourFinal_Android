package bd.org.bitm.mad.batch33.tourmate.fragment.Event;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import bd.org.bitm.mad.batch33.tourmate.R;
import bd.org.bitm.mad.batch33.tourmate.Utils.Constant;
import bd.org.bitm.mad.batch33.tourmate.adapter.EventAdapter;
import bd.org.bitm.mad.batch33.tourmate.model.Event;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends Fragment {
    private DatabaseReference eventRef;
    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView emailName;
    private RecyclerView recyclerView;
    private List<Event> events = new ArrayList<>();

    public EventListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        emailName = view.findViewById(R.id.emailNameTV);
        TextView textView = view.findViewById(R.id.events);
        recyclerView = view.findViewById(R.id.recyclerView);



        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                CreateEventFragment fragment = new CreateEventFragment();
                fragmentTransaction.add(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                EventDetailFragment ft = new EventDetailFragment();
                fragmentTransaction.add(R.id.fragment_container, ft);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        emailName.setText(Constant.UserData.email);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        if (user != null && user.getUid().equals(Constant.UserData.uid)) {
            userRef = rootRef.child("users").child(user.getUid());
            eventRef = userRef.child("Event");

        }
        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events.clear();
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Event event = d.getValue(Event.class);
                    events.add(event);
                }
                if(events.size() > 0){
                    EventAdapter eventAdapter = new EventAdapter(getContext(),events);
                    LinearLayoutManager l = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(l);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(eventAdapter);
                }else {
                    ((TextView)view.findViewById(R.id.emptyListMsg)).setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

}
