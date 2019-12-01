package com.example.groupandgoeventplanner;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groupandgoeventplanner.Interfaces.OnFragmentInteractionListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EventsPublicPage extends Fragment{

    private static final String ARG_EVENT_NAME = "Event Name";
    private static final String TAG = "EventsPublic";

    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private OnFragmentInteractionListener mListener;

    private TextView name;
    private TextView date;
    private TextView locationName;
    private TextView complete;

    private EventsPublicModel eventsPublicModel;

    public EventsPublicPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param eventName Name of the exercise to query.
     * @return A new instance of fragment Exercise.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsPublicPage newInstance(String eventName) {
        EventsPublicPage fragment = new EventsPublicPage();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_NAME, eventName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_eventspublicpage, container, false);

        if (getArguments() != null) {
            final String eventName = getArguments().getString(ARG_EVENT_NAME);
            readData(eventName, new FirestoreCallback() {
                @Override
                public void onCallBack(EventsPublicModel eventsPublicModel) {

                    if(eventsPublicModel.isComplete()){
                        complete.setText("Complete");
                    }
                    else{
                        complete.setText("Active");
                    }
                    // Populate TextViews
                    name.setText(eventsPublicModel.getName());
                    locationName.setText(eventsPublicModel.getLocationName());




                }
            });

        }

        name = layout.findViewById(R.id.event_name_text_view);
        locationName = layout.findViewById(R.id.location_text_view);
        complete = layout.findViewById(R.id.muscle_text_view);
        Button completeButton = layout.findViewById(R.id.completeButton);
        Button backButton = layout.findViewById(R.id.backButton);

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> eventDoc = new HashMap<>();
                Map<String, Object> nestedEventData = new HashMap<>();



                eventDoc.put("name",name.getText().toString());
                eventDoc.put("locationName", locationName.getText().toString());
                //eventDoc.put("date", );
                eventDoc.put("complete", false);

                // Update FireStore database
                        /*db.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                                .collection("Goal Logs").document(goalNameInput.getText().toString())
                                .set(goalDoc, SetOptions.merge())
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
                                });*/

                db.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                        .collection("Event Logs").document(name.getText().toString())
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

                Toast.makeText(getActivity(), "Event Added", Toast.LENGTH_LONG).show();

            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });



        // Inflate the layout for this fragment
        return layout;
    }

    public void onButtonPressed(String videoID) {
        if (mListener != null) {
            mListener.onFragmentMessage(TAG, videoID);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void readData(String eventName, final FirestoreCallback firestoreCallback){
        // Fetch the event from Firestore
        FirebaseFirestore.getInstance().collection("Events")
                .document(eventName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        eventsPublicModel = documentSnapshot.toObject(EventsPublicModel.class);
                        firestoreCallback.onCallBack(eventsPublicModel);
                    }
                });
    }

    private interface FirestoreCallback {
        void onCallBack(EventsPublicModel eventsPublicModel);
    }
}
