package bd.org.bitm.mad.batch33.tourmate.fragment.Moment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import bd.org.bitm.mad.batch33.tourmate.adapter.PhotoGalleryAdapter;
import bd.org.bitm.mad.batch33.tourmate.model.Event;
import bd.org.bitm.mad.batch33.tourmate.model.Moment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageGalleryFragment extends Fragment {


    private RecyclerView recyclerView;

    private FirebaseUser user;
    private FirebaseAuth auth;

    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference eventRef;
    private DatabaseReference momentRef;

    private Event event;

    private List<Moment> moments = new ArrayList<>();

    private String photoPath = "*";

    public ImageGalleryFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get current user
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //get event from bundle
        Bundle bundle = getArguments();
        event = (Event) bundle.getSerializable("event");



        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_gallery, container, false);
        recyclerView = view.findViewById(R.id.imageGalleryRv);




//        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
//        imageView.setImageBitmap(bitmap);
        getMomentList();

        return view;
    }

    public void getMomentList() {

        rootRef = FirebaseDatabase.getInstance().getReference();

        //Toast.makeText(getActivity(), user.getUid(), Toast.LENGTH_SHORT).show();

        if (user!=null) {
            Log.d("event id:", event.getEventId());
            userRef = rootRef.child("users").child(user.getUid());
            eventRef = userRef.child("Event");
            momentRef = eventRef.child(event.getEventId()).child("Moment");
        }

        // getting event list from firebase
        momentRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // clearing the event list for repeating data issue
                moments.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Moment moment = d.getValue(Moment.class);
                    moments.add(moment);
                }

                Log.d("moments size:", moments.size()+"");

                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);

                PhotoGalleryAdapter adapter = new PhotoGalleryAdapter(moments);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}

