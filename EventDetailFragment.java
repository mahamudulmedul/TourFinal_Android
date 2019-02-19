package bd.org.bitm.mad.batch33.tourmate.fragment.Event;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import bd.org.bitm.mad.batch33.tourmate.LoginActivity;
import bd.org.bitm.mad.batch33.tourmate.R;
import bd.org.bitm.mad.batch33.tourmate.Utils.Constant;
import bd.org.bitm.mad.batch33.tourmate.Utils.TourUtility;
import bd.org.bitm.mad.batch33.tourmate.adapter.ExpandableListViewAdapter;
import bd.org.bitm.mad.batch33.tourmate.adapter.ExpenseAdapter;
import bd.org.bitm.mad.batch33.tourmate.db.TourMateDB;
import bd.org.bitm.mad.batch33.tourmate.fragment.Moment.ImageGalleryFragment;
import bd.org.bitm.mad.batch33.tourmate.fragment.Moment.MomentsFragment;
import bd.org.bitm.mad.batch33.tourmate.fragment.Moment.ViewGalleryFragment;
import bd.org.bitm.mad.batch33.tourmate.model.Event;
import bd.org.bitm.mad.batch33.tourmate.model.Expense;
import bd.org.bitm.mad.batch33.tourmate.model.Moment;
import bd.org.bitm.mad.batch33.tourmate.model.NetworkConnectivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailFragment extends Fragment {

    private ExpandableListView listView;
    private TextView noExpenseMessagetTv, eventNameTv, budgetStatusTv;
    private RecyclerView lv;
    private List<String> listHeader;
    private HashMap<String, List<String>> hashMapList;
    private ExpandableListAdapter adapter;

    private ExpenseAdapter expenseAdapter;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private String photoPath = null;

    private Event event;
    private List<Expense> expenseList = new ArrayList<>();

    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference eventRef;
    private DatabaseReference expenseRef;
    private ProgressBar budgetStatusProgressBar;
    private TextView progressBarTV;
    private double totalBudget=0.0;
    private String fileName = null;
    private String fileFormate = ".jpg";
    private double totalExpense = 0.0;



    public EventDetailFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        //getting current user
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //getting event from bundle
        Bundle bundle = getArguments();
        event = (Event) bundle.getSerializable("event");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        listView = view.findViewById(R.id.expLV);
        eventNameTv = view.findViewById(R.id.eventName);
        budgetStatusTv = view.findViewById(R.id.buggetStautas);
        budgetStatusProgressBar = view.findViewById(R.id.budgetStatusProgressBar);
        progressBarTV = view.findViewById(R.id.progressBarTV);
        initData();

        adapter = new ExpandableListViewAdapter(getContext(), listHeader, hashMapList);
        listView.setAdapter(adapter);

        getExpenseList();
        getProgressBarStatus();

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(final ExpandableListView expandableListView, View view, int i, int i1, long l) {

                String selected = (String) adapter.getChild(i, i1);
                switch (selected) {
                    case "Add New Expense":

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = LayoutInflater.from(getActivity());
                        View v = inflater.inflate(R.layout.add_expense_layout, null);
                        Button cancelBtn, expenseSaveBtn;
                        cancelBtn = v.findViewById(R.id.cancelButton);
                        expenseSaveBtn = v.findViewById(R.id.expenseSaveButton);
                        final EditText amountEt = v.findViewById(R.id.amountEt);
                        final EditText commentEt = v.findViewById(R.id.commentEt);

                        // Log.d("email", loginEmail);
                        final AlertDialog dialog = builder.create();
                        dialog.setTitle("Add New Expense");
                        dialog.setView(v);

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.dismiss();
                                Toast.makeText(getActivity(), "cancel", Toast.LENGTH_SHORT).show();
                            }
                        });

                        expenseSaveBtn.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onClick(View view) {
                                double amount = Double.parseDouble(amountEt.getText().toString());
                                String comment = commentEt.getText().toString();

                                double totalBudget = Double.parseDouble(event.getEstimateBudget());
                                double totalExpensesAmount = TourUtility.sumOfExpenses(expenseList);
                                double currentTotalExpenseAmount = totalExpensesAmount + amount;

                                if (currentTotalExpenseAmount > totalBudget) {
                                    Toast.makeText(getActivity(), "Amount is exceed your total budget!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Expense expense = new Expense();
                                    expense.setExpenseAmount(amount);
                                    expense.setComment(comment);
                                    expense.setExpenseDate(new Date().getTime());

                                    String eventId = event.getEventId();

                                    TourMateDB tourMateDB = new TourMateDB(getContext(), user);
                                    tourMateDB.addExpense(expense, eventId);
                                    getProgressBarStatus();
                                    dialog.dismiss();
                                }


                            }
                        });
                        dialog.show();
                        break;

                    case "View All Expense":
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        LayoutInflater layoutInflater = getLayoutInflater();
                        View convertView = (View) layoutInflater.inflate(R.layout.expense_list_alert_layout, null);
                        alertDialog.setView(convertView);
                        alertDialog.setTitle("List");
                        lv = (RecyclerView) convertView.findViewById(R.id.expenseLv);
                        noExpenseMessagetTv = (TextView) convertView.findViewById(R.id.noExpenseMessage);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        lv.setLayoutManager(layoutManager);

                        //setting the list to adapter
                        expenseAdapter = new ExpenseAdapter(expenseList);
                        lv.setAdapter(expenseAdapter);

                        Log.d("expense list size:", expenseList.size() + "");

                        alertDialog.setCancelable(true);
                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        final AlertDialog exDialog = alertDialog.create();
                        exDialog.setTitle("All Expenses");
                        exDialog.show();
                        break;
                    case "Add More Budget":
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater1 = LayoutInflater.from(getActivity());
                        View view1 = inflater1.inflate(R.layout.update_event_budget_layout, null);
                        Button cancelBtn1, budgetUpdateBtn;
                        cancelBtn = view1.findViewById(R.id.cancelButton);
                        expenseSaveBtn = view1.findViewById(R.id.updateBudgetBtn);
                        final EditText budgetEt = view1.findViewById(R.id.updateBudgetEt);

                        // Log.d("email", loginEmail);
                        final AlertDialog dialog1 = builder1.create();
                        dialog1.setTitle("Add New Expense");
                        dialog1.setView(view1);

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog1.dismiss();
                                Toast.makeText(getActivity(), "cancel", Toast.LENGTH_SHORT).show();
                            }
                        });

                        expenseSaveBtn.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onClick(View view) {

                                double newlyBudget = Double.parseDouble(budgetEt.getText().toString());
                                double previousBudget = Double.parseDouble(event.getEstimateBudget());
                                event.setEstimateBudget(String.valueOf(previousBudget + newlyBudget));

                                TourMateDB tourMateDB = new TourMateDB(getContext(), user);
                                tourMateDB.updateEvent(event);

                                budgetStatusTv.setText("Budget Status (" + TourUtility.sumOfExpenses(expenseList) + "/" +
                                        Double.parseDouble(event.getEstimateBudget()) + ")");

                                getProgressBarStatus();

                                Toast.makeText(getContext(), "Budget added successfully!", Toast.LENGTH_SHORT).show();
                                dialog1.dismiss();
                            }
                        });
                        dialog1.show();
                        break;
                    case "Take a Photo":

                        takePhoto();

                        break;
                    case "View All Moments":
                            viewMoment();
                    

                        break;
                    case "View Gallery":

                        viewGallery();

                        break;
                    case "Edit Event":

                        eventEdit();

                        break;
                    case "Delete Event":
                        AlertDialog.Builder delete = new AlertDialog.Builder(getActivity());
                        delete.setMessage("Are You Sure Delete This Event")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        deleteEvent();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                    }
                                });
                        // Create the AlertDialog object and return it

                         delete.create();
                        delete.show();

                        break;
                }

                Toast.makeText(getContext(), selected, Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        return view;
    }

    private void eventEdit() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("event", event);
        UpdateEventFragment updateEventFragment = new UpdateEventFragment();
        updateEventFragment.setArguments(bundle1);
        fragmentTransaction.replace(R.id.fragment_container, updateEventFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void deleteEvent() {
        String eventId = event.getEventId();
        TourMateDB tourMateDB = new TourMateDB(getContext(), user);
        tourMateDB.deleteEvent(eventId);
    }

    private void viewGallery() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
       FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("event", event);
        ImageGalleryFragment imageGalleryFragment = new ImageGalleryFragment();
        imageGalleryFragment.setArguments(bundle1);
        fragmentTransaction.replace(R.id.fragment_container, imageGalleryFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

    private void viewMoment() {
       FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
        Bundle b = new Bundle();
        b.putSerializable("event", event);
        MomentsFragment momentsFragment = new MomentsFragment();
        momentsFragment.setArguments(b);
        fragmentTransaction1.replace(R.id.fragment_container, momentsFragment);
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getProgressBarStatus() {
        int progressValue = TourUtility.getProgressPercentage(totalBudget, totalExpense);

        Log.d("Progress value:", progressValue + "");
        Log.d("total B value:", totalBudget + "");
        Log.d("total e value:", totalExpense + "");
            //budgetStatusProgressBar.setMin(0);
        budgetStatusProgressBar.setMax(100);
        budgetStatusProgressBar.setProgress(progressValue);
        progressBarTV.setText(progressValue + "%");
    }

    private void initData() {

        listHeader = new ArrayList<>();
        hashMapList = new HashMap<>();

        listHeader.add("Expenditure");
        listHeader.add("Moments");
        listHeader.add("More On Event");

        List<String> expense = new ArrayList<>();
        expense.add("Add New Expense");
        expense.add("View All Expense");
        expense.add("Add More Budget");

        List<String> moments = new ArrayList<>();
        moments.add("Take a Photo");
        moments.add("View All Moments");
        moments.add("View Gallery");

        List<String> moreOnEvent = new ArrayList<>();
        moreOnEvent.add("Edit Event");
        moreOnEvent.add("Delete Event");

        hashMapList.put(listHeader.get(0), expense);
        hashMapList.put(listHeader.get(1), moments);
        hashMapList.put(listHeader.get(2), moreOnEvent);
    }


    public void getExpenseList() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //initialize rootRef
        rootRef = FirebaseDatabase.getInstance().getReference();


        if (user != null) {
            userRef = rootRef.child("users").child(user.getUid());
            eventRef = userRef.child("Event");
            expenseRef = eventRef.child(event.getEventId()).child("Expense");
        }


        // getting event list from firebase
        expenseRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // clearing the event list for repeating data issue
                expenseList.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Expense expense = d.getValue(Expense.class);
                    expenseList.add(expense);
                }

                eventNameTv.setText(event.getEventName());
                budgetStatusTv.setText("Budget Status (" + TourUtility.sumOfExpenses(expenseList) + "/" +
                        Double.parseDouble(event.getEstimateBudget()) + ")");

                getProgressBarStatus();
                //displaying proper message for empty event
//                    if (expenseList.size() == 0) {
//                        noExpenseMessagetTv.setVisibility(View.VISIBLE);
//                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            File file = null;
            try {
                file = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file != null) {
                Uri photoUri = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        file);
                Toast.makeText(getActivity(), photoPath, Toast.LENGTH_SHORT).show();

                TourMateDB tourMateDB = new TourMateDB(getActivity(), user);

                Moment moment =new Moment();
                moment.setDate(new Date().getTime());
                moment.setFileName(fileName);
                moment.setPhotoPath(photoPath);
                moment.setFormatName(fileFormate);

                tourMateDB.addMoment(moment, event.getEventId());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, 111);

            }


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 111 && resultCode == getActivity().RESULT_OK){

        }
    }

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "JPEG_"+timeStamp;
        File storageDirectory = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageName,".jpg", storageDirectory);
        photoPath = imageFile.getAbsolutePath();

        return imageFile;
    }
}
