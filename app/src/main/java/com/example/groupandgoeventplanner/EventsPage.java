package com.example.groupandgoeventplanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.groupandgoeventplanner.Interfaces.OnFragmentInteractionListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Objects;

public class EventsPage extends Fragment{

    private static final String ARG_EVENT_NAME = "Event Name";
    private static final String TAG = "Events";

    final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private OnFragmentInteractionListener mListener;

    private TextView name;
    private TextView date;
    private TextView locationName;
    private TextView complete;

    Intent intent;
    GeoPoint geo;

    private EventsModel eventsModel;

    public EventsPage() {
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
    public static EventsPage newInstance(String eventName) {
        EventsPage fragment = new EventsPage();
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

        View layout = inflater.inflate(R.layout.fragment_eventspage, container, false);

        if (getArguments() != null) {
            final String eventName = getArguments().getString(ARG_EVENT_NAME);
            readData(eventName, new FirestoreCallback() {
                @Override
                public void onCallBack(EventsModel eventsModel) {

                    if(eventsModel.isComplete()){
                        complete.setText("Complete");
                    }
                    else{
                        complete.setText("Active");
                    }
                    // Populate TextViews
                    name.setText(eventsModel.getName());
                    locationName.setText(eventsModel.getLocationName());




                }
            });

        }

        name = layout.findViewById(R.id.event_name_text_view);
        locationName = layout.findViewById(R.id.location_text_view);
        complete = layout.findViewById(R.id.complete_text_view);
        Button completeButton = layout.findViewById(R.id.completeButton);
        Button backButton = layout.findViewById(R.id.backButton);
        Button mapButton = layout.findViewById(R.id.mapButton);

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseFirestore.getInstance().collection("users")
                        .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                        .collection("Event Logs")
                        .document(name.getText().toString())
                        .delete()
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

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                intent = new Intent(EventsPage.this.getActivity(),MapViewerActivityPublic.class);
                //intent.putExtra("lat", geo.getLatitude());
                //intent.putExtra("long", geo.getLongitude());
                intent.putExtra("eventName",name.getText().toString());
                startActivity(intent);
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
        FirebaseFirestore.getInstance().collection("users")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .collection("Event Logs")
                .document(eventName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        eventsModel = documentSnapshot.toObject(EventsModel.class);
                        firestoreCallback.onCallBack(eventsModel);
                    }
                });
    }

    private interface FirestoreCallback {
        void onCallBack(EventsModel eventsModel);
    }
}
