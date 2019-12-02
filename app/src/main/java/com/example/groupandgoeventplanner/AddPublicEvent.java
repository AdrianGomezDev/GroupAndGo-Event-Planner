package com.example.groupandgoeventplanner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.example.groupandgoeventplanner.Util.isEmpty;
import static com.example.groupandgoeventplanner.Util.setTimeToZero;

public class AddPublicEvent extends DialogFragment {
    private static final String EVENT_MODEL_KEY = "EventsPublicModel";
    private Date timestamp;
    private Date endTimeStamp;

    public static AddPublicEvent newInstance(EventsPublicModel eventPublic) {
        AddPublicEvent dialogFragment = new AddPublicEvent();
        Bundle args = new Bundle();
        args.putSerializable(EVENT_MODEL_KEY, eventPublic);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View layout = inflater.inflate(R.layout.dialog_fragment_add_event_public, null);

        final List<String> events = new ArrayList<>();


        final EventsPublicModel eventsPublicModel = (EventsPublicModel) Objects.requireNonNull(getArguments()).getSerializable(EVENT_MODEL_KEY);
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final EditText eventNameInput = layout.findViewById(R.id.eventNameEditText);



        // Get date of the event from user, default is the current date.
        final TextView dateInput = layout.findViewById(R.id.dateOfEventEditText);
        final TextView endDateInput = layout.findViewById(R.id.endDateEditText);
        final EditText addressInput = layout.findViewById(R.id.addressEditText);
        final Calendar myCalendar;
        //final GeoPoint eventGeo;




        myCalendar = Calendar.getInstance();
        myCalendar.setTime(new Date());
        setTimeToZero(myCalendar);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setTimeToZero(myCalendar);
                timestamp = new Date(myCalendar.getTimeInMillis());
                String myFormat = "MMM dd, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dateInput.setText(sdf.format(myCalendar.getTime()));
            }
        };

        final DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setTimeToZero(myCalendar);
                timestamp = new Date(myCalendar.getTimeInMillis());
                String myFormat = "MMM dd, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                endDateInput.setText(sdf.format(myCalendar.getTime()));
            }
        };


        timestamp = new Date(myCalendar.getTimeInMillis());
        dateInput.setText(DateFormat.getDateInstance().format(timestamp));
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Objects.requireNonNull(getContext()), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        dateInput.setText(DateFormat.getDateInstance().format(new Date()));
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Objects.requireNonNull(getContext()), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endTimeStamp = new Date(myCalendar.getTimeInMillis());
        endDateInput.setText(DateFormat.getDateInstance().format(endTimeStamp));
        endDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Objects.requireNonNull(getContext()), endDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDateInput.setText(DateFormat.getDateInstance().format(new Date()));
        endDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Objects.requireNonNull(getContext()), endDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        builder.setView(layout);
        builder.setTitle("Add a new public event ")
                .setPositiveButton("Add event", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(isEmpty(eventNameInput)){
                            eventNameInput.setError("Please type an event name");
                        }
                        else{



                            Map<String, Object> eventDoc = new HashMap<>();
                            Map<String, Object> nestedEventData = new HashMap<>();
                            Map<String, Object> date = new HashMap<>();
                            Map<String, Object> endDate = new HashMap<>();
                            date.put("date", timestamp);
                            endDate.put("endDate", endTimeStamp);

                            // Update FireStore Event collection with date
                            db.collection("Events")
                                    .document(eventNameInput.getText().toString())
                                    .set(date, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("FireStore", "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("FireStore", "Error writing document", e);
                                        }
                                    });

                            db.collection("Events")
                                    .document(eventNameInput.getText().toString())
                                    .set(endDate, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("FireStore", "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("FireStore", "Error writing document", e);
                                        }
                                    });


                            GeoPoint eventGeo = getLocationFromAddress(addressInput.getText().toString());


                            //goalDoc.put(Objects.requireNonNull(goalModel).getGoalName(), nestedgoalData);
                            eventDoc.put("name",eventNameInput.getText().toString());
                            eventDoc.put("locationName", addressInput.getText().toString());
                            eventDoc.put("complete", false);
                            eventDoc.put("latLong",eventGeo);





                            // Update FireStore database
                            db.collection("Events")
                                    .document(eventNameInput.getText().toString())
                                    .set(eventDoc, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("FIRESTORE", "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("FIRESTORE", "Error writing document", e);
                                        }
                                    });



                        }}

                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        AddPublicEvent.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public GeoPoint getLocationFromAddress(String addressStr){
        Geocoder coder = new Geocoder(this.getActivity());
        List<Address> address;
        GeoPoint point = null;

        try{
            address = coder.getFromLocationName(addressStr,5);
            if (address == null) {

                return null;
            }

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            point = new GeoPoint((double) (location.getLatitude()),
                    (double) (location.getLongitude()));


        }
        catch(IOException e) {

        }
        return point;
    }
}