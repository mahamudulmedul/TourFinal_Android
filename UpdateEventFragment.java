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

import java.util.Calendar;

import bd.org.bitm.mad.batch33.tourmate.R;
import bd.org.bitm.mad.batch33.tourmate.db.TourMateDB;
import bd.org.bitm.mad.batch33.tourmate.model.Event;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateEventFragment extends Fragment {

    private EditText eventET, departureET, startET, budgetET, startingEt, destinationEt;
    private Button updateEventBt;
    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference eventRef;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private Event event;


    public UpdateEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_update_event, container, false);
        eventET = v.findViewById(R.id.eventNameEt);
        departureET = v.findViewById(R.id.departureEt);
        budgetET = v.findViewById(R.id.budgetEt);
        startingEt = v.findViewById(R.id.startingEt);
        destinationEt = v.findViewById(R.id.destinationEt);
        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        updateEventBt = v.findViewById(R.id.updateEventButton);

        departureET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                departureET.setText(day + "/" + (month+1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });


        //getting event from bundle
        Bundle bundle = getArguments();
        event = (Event) bundle.getSerializable("event");

        //setting the event value into edit text
        eventET.setText(event.getEventName());
        departureET.setText(event.getDepartureDate());
        destinationEt.setText(event.getDestination());
        startingEt.setText(event.getStartingLocation());
        budgetET.setText(event.getEstimateBudget());


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        updateEvent();

        return v;
    }


    public void updateEvent() {
        updateEventBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                event.setEventName(eventET.getText().toString());
                event.setDepartureDate(departureET.getText().toString());
                event.setStartingLocation(startingEt.getText().toString());
                event.setDestination(destinationEt.getText().toString());
                event.setEstimateBudget(budgetET.getText().toString());
                //String keyId = event.getEventId();

                TourMateDB tourMateDB = new TourMateDB(getActivity(), user);
                tourMateDB.updateEvent(event);

                Toast.makeText(getActivity(), "Updated Successfully!", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();

            }
        });
    }
}
