package com.example.groupandgoeventplanner;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groupandgoeventplanner.Interfaces.OnFragmentInteractionListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class EventsPublic extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private FloatingActionButton newEvent;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirestoreRecyclerAdapter<EventsPublicModel, EventsViewHolder> adapter;


    public EventsPublic(){

    }

    public static EventsPublic newInstance(String param1, String param2) {
        EventsPublic fragment = new EventsPublic();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("Public Events");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eventspublic, container, false);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity());

        newEvent = view.findViewById(R.id.fab);


        recyclerView = view.findViewById(R.id.eventsPublicRecyclerView);



        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        Query query = rootRef.collection("Events")
                /*.orderBy("date", Query.Direction.ASCENDING)*/;

        final EventsPublicModel eventsModel = new EventsPublicModel("","",false);

        FirestoreRecyclerOptions<EventsPublicModel> options = new FirestoreRecyclerOptions.Builder<EventsPublicModel>()
                .setQuery(query, EventsPublicModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<EventsPublicModel, EventsViewHolder>(options) {


            @NonNull
            @Override
            public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.eventpublic_index, viewGroup, false);

                return new EventsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull EventsViewHolder holder, int position, @NonNull final EventsPublicModel eventsPublicModel) {
                final String eventName = eventsPublicModel.getName();
                holder.setEventName(eventName);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View view){
                        //Toast.makeText(getActivity(), "TEST", Toast.LENGTH_LONG).show();
                        onButtonPressed(eventName);
                    }
                });

            }
        };

        newEvent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view){
                //DialogFragment dialogFragment = AddEventLog.newInstance(eventsModel);
                //dialogFragment.show(getChildFragmentManager(), "Event Log");
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void onButtonPressed(String eventName) {
        if (mListener != null) {
            mListener.onFragmentMessage("EventsPublic", eventName);
            //Toast.makeText(this.getActivity(), eventName, Toast.LENGTH_LONG).show();

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

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }

    private class EventsViewHolder extends RecyclerView.ViewHolder {
        private View view;

        EventsViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        void setEventName(String eventName) {
            TextView textView = view.findViewById(R.id.eventpublic_index_text_view);
            textView.setText(eventName);
        }
    }
}
