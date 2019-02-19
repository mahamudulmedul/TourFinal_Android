package bd.org.bitm.mad.batch33.tourmate.fragment.Event;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bd.org.bitm.mad.batch33.tourmate.R;
import bd.org.bitm.mad.batch33.tourmate.db.TourMateDB;
import bd.org.bitm.mad.batch33.tourmate.model.Event;
import bd.org.bitm.mad.batch33.tourmate.model.NetworkConnectivity;

public class CreateEventFragment extends Fragment {


    private EditText eventET, departureET, startET, buggedET, startingEt, destinationEt;
    private Button registrationBT;
    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference eventRef;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private List<Event> events = new ArrayList<>();

    public CreateEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_create, container, false);
        eventET = v.findViewById(R.id.eventNameET);
        departureET = v.findViewById(R.id.departureET);
        buggedET = v.findViewById(R.id.budgetET);
        startingEt = v.findViewById(R.id.startDateET);
        destinationEt = v.findViewById(R.id.destrinationET);

        registrationBT = v.findViewById(R.id.registrationBT);

        departureET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                departureET.setText(day+"/"+month+"/"+year);
                            }
                        },2018, 5, 23);
                datePickerDialog.show();
            }
        });


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        /*//initialize rootRef
        rootRef = FirebaseDatabase.getInstance().getReference();

        Toast.makeText(getActivity(), user.getUid()+"", Toast.LENGTH_SHORT).show();

        if (user!=null) {
            eventRef = rootRef.child("users").child(user.getUid()).child("Event");
            //eventRef.setValue("Hello");
        }*/

        registrationBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = eventET.getText().toString();
                String deparuture = departureET.getText().toString();
                String startingLocation = startingEt.getText().toString();
                String destination = destinationEt.getText().toString();
                String budget = buggedET.getText().toString();

                TourMateDB tourMateDB = new TourMateDB(getActivity(), user);

                Event s = new Event();
                s.setCreationDate(NetworkConnectivity.getDate(new Date()));
                s.setEstimateBudget(budget);
                s.setDepartureDate(deparuture);
                s.setDestination(destination);
                s.setStartingLocation(startingLocation);
                s.setEventName(name);

                tourMateDB.addEvent(s);
                Toast.makeText(getActivity(), "Event created successfully!", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();

            }
        });
        return v;
    }


}
