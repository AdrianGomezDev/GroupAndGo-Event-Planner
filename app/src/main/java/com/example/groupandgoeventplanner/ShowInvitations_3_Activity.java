package com.example.groupandgoeventplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ShowInvitations_3_Activity extends AppCompatActivity implements Adapter.ItemClickListener{
    Adapter group_names_recycler_view;
    private CollectionReference invitees = FirebaseFirestore.getInstance().collection("/Invitees");
    private CollectionReference users = FirebaseFirestore.getInstance().collection("/users");
    private String user_id;
    String m_current_user_name;
    private RecyclerView data;
    private ArrayList<String> groups = new ArrayList<>();
    final String TAG = "Here come groups names";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_invitations_3_);
        data = findViewById(R.id.group_names);
        user_id = (String)FirebaseAuth.getInstance().getCurrentUser().getUid();

        m_current_user_name = "cheetos";
        getCurrentUserName(new helperUsername() {
            public void onCallback(String name) {
                m_current_user_name = name;
                Log.d(TAG, "Inside use name" + m_current_user_name);
                getInvitations(new helperInvitation() {
                    public void onCallback(ArrayList<String> list) {
                        groups = list;
                        data.setLayoutManager(new LinearLayoutManager(ShowInvitations_3_Activity.this));
                        group_names_recycler_view = new Adapter(ShowInvitations_3_Activity.this, groups);
                        group_names_recycler_view.setClickListener(ShowInvitations_3_Activity.this);
                        data.setAdapter(group_names_recycler_view);
                        group_names_recycler_view.notifyDataSetChanged();
                    }
                });
            }
        }, user_id);
/**
        getInvitations(new helperInvitation() {
            public void onCallback(ArrayList<String> list) {
                groups = list;
                data.setLayoutManager(new LinearLayoutManager(ShowInvitations_3_Activity.this));
                group_names_recycler_view = new Adapter(ShowInvitations_3_Activity.this, groups);
                group_names_recycler_view.setClickListener(ShowInvitations_3_Activity.this);
                data.setAdapter(group_names_recycler_view);
                group_names_recycler_view.notifyDataSetChanged();
            }
        });
 **/
        /**
        getInvitations(new MyCallback() {
            public void onCallback(ArrayList<String> value) {
                groups = value;
                //Log.d(TAG, "Interface" + value);
                data.setLayoutManager(new LinearLayoutManager(ShowInvitations_3_Activity.this));
                group_names_recycler_view = new Adapter(ShowInvitations_3_Activity.this, groups);
                group_names_recycler_view.setClickListener(ShowInvitations_3_Activity.this);
                data.setAdapter(group_names_recycler_view);
                group_names_recycler_view.notifyDataSetChanged();
            }
        });
         **/
        //Log.d(TAG, "Interface outside" + groups);


/**
        data.setLayoutManager(new LinearLayoutManager(this));
        group_names_recycler_view = new Adapter(this, groups);
        group_names_recycler_view.setClickListener(this);
        data.setAdapter(group_names_recycler_view);
        group_names_recycler_view.notifyDataSetChanged();

 **/






    }


    private void getInvitations(final helperInvitation myCallback) {
        final ArrayList<String> result = new ArrayList<String>();
        invitees.document(m_current_user_name).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                //the parameter documentSnapshot is a map key->value
                if (documentSnapshot.exists()){
                    //documentSnapshot.getData

                    Map<String, Object> data = documentSnapshot.getData();
                    ArrayList<String> temp = (ArrayList)data.get("groups");

                    for (int i = 0; i < temp.size(); i++) {
                        result.add(temp.get(i));
                    }
                    //return groups;
                    myCallback.onCallback(result);

                    //Log.d(TAG, "DocumentSnapshot data dsfkbd: " + groups);

                }



            }
        });

    }
    private interface helperInvitation{
        void onCallback(ArrayList<String> list);
        //void onCallbackUserName(String current_user);
    }
    private interface helperUsername{
        void onCallback(String name);
        //void onCallbackUserName(String current_user);
    }

    private void getCurrentUserName(final helperUsername myCallback, String user_id) {
        final String result;
        users.document(user_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    Map<String, Object> data = documentSnapshot.getData();
                    myCallback.onCallback((String)data.get("username"));

                }
            }
        });
        //return "cheetos";
    }
    public void onItemClick(View view, int position){

    }
}
