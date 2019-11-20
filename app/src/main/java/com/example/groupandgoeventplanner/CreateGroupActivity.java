package com.example.groupandgoeventplanner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateGroupActivity extends AppCompatActivity implements Adapter.ItemClickListener{
    Adapter search_members_adapter;
    Adapter group_members_adapter;

    private DocumentReference users_emails = FirebaseFirestore.getInstance().document("/users/users_emails");
    private CollectionReference groups = FirebaseFirestore.getInstance().collection("/groups");
    private DocumentReference pending_invites = FirebaseFirestore.getInstance().document("/Pending/pendingInvites");
    private CollectionReference m_invitees = FirebaseFirestore.getInstance().collection("/Invitees");


    private FirebaseUser user;
    private String current_user_email;
    //List of invitees
    private List<String> group_members_emails;
    private String m_group_name;
    RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String current_user_name;
    private final String TAG = "LNDF";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group_members_emails = new ArrayList<>();
        setContentView(R.layout.activity_create_group);
        recyclerView = findViewById(R.id.group_members);
        user = FirebaseAuth.getInstance().getCurrentUser();
        current_user_email = user.getEmail();
        String u_id = user.getUid();

        //final String current_user_name;
        /**
        DocumentReference docRef = db.collection("users").document(u_id);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //current_user_name = document.getData().get("username").toString();
                        current_user_name = document.getString("username");
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    }
                    else {
                        Log.d(TAG, "DocumentSnapshot data: ");
                    }

                }
            }
        });
         **/


        Toast.makeText(this, current_user_name, Toast.LENGTH_LONG).show();
        setUpRecyclerView();
    }


    public void onItemClick(View view, int position) {
        //get group name
        //String group_name = findViewById(R.id.group_name).toString();
        /**
         * Users have to come back to the main screen and click on the create group button to create a new group
         *
         */
        closeKeyBoard();
        if (group_members_emails.contains(search_members_adapter.getItem(position))) {
            //Do not add
            Toast.makeText(this, "You clicked on email address ",Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            group_members_emails.add(search_members_adapter.getItem(position));

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            group_members_adapter = new Adapter(this, group_members_emails);
            group_members_adapter.setClickListener(this);
            recyclerView.setAdapter(group_members_adapter);
            group_members_adapter.notifyDataSetChanged();

        }
        //group_members_emails.add(adapter.getItem(position));

        //When user click on we should add them into the group
        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void search_for_user_names_handler(View view) {
        closeKeyBoard();
        EditText email_field = (EditText) findViewById(R.id.search_field);
        final String user_name_to_search_for = email_field.getText().toString();
        Toast.makeText(getApplicationContext(), user_name_to_search_for, Toast.LENGTH_SHORT).show();
        final List<String> result = new ArrayList<String>();
        //First try to print out all the emails in the database
        users_emails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                //the parameter documentSnapshot is a map key->value
                if (documentSnapshot.exists()){
                    Map<String, Object> data = documentSnapshot.getData();
                    for (String key: data.keySet()) {
                        if (key.toLowerCase().startsWith(user_name_to_search_for.toLowerCase())) {
                            result.add(key);
                        }
                    }

                }


            }
        });
        RecyclerView recycler_view_search_results = findViewById(R.id.search_results);
        recycler_view_search_results.setLayoutManager(new LinearLayoutManager(this));
        search_members_adapter = new Adapter(this, result);
        search_members_adapter.setClickListener(this);
        recycler_view_search_results.setAdapter(search_members_adapter);
        search_members_adapter.notifyDataSetChanged();
    }

    public void search_for_email_handler(View view) {
        //THis function is responsible for taking all the email address that matches with the address
        //user put in
        //view.clearFocus();closeKeyBoard();

        closeKeyBoard();
        EditText email_field = (EditText) findViewById(R.id.search_field);
        final String email_to_search_for = email_field.getText().toString();
        Toast.makeText(getApplicationContext(), email_to_search_for, Toast.LENGTH_SHORT).show();
        final List<String> result = new ArrayList<String>();
        //First try to print out all the emails in the database
        users_emails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                //the parameter documentSnapshot is a map key->value
                if (documentSnapshot.exists()){
                    Map<String, Object> data = documentSnapshot.getData();
                    for (Object email: data.values()){
                        if (email.toString().toLowerCase().startsWith(email_to_search_for.toLowerCase())) {
                            result.add(email.toString());
                        }
                    }

                }


            }
        });
        //Has to click on the edit text field to show result, even hiding the soft keyboard right after clicking search
        //Toast.makeText(getApplicationContext(), result.size(), Toast.LENGTH_SHORT).show();
        //for (int i = 0; i < result.size(); i++) {
        //Toast.makeText(getApplicationContext(), result.get(i), Toast.LENGTH_SHORT).show();
        //}
        // set up the RecyclerView

        RecyclerView recycler_view_search_results = findViewById(R.id.search_results);
        recycler_view_search_results.setLayoutManager(new LinearLayoutManager(this));
        search_members_adapter = new Adapter(this, result);
        search_members_adapter.setClickListener(this);
        recycler_view_search_results.setAdapter(search_members_adapter);
        search_members_adapter.notifyDataSetChanged();
        //view.clearFocus();
        //Toast.makeText(this, "You clicked  on row number ",Toast.LENGTH_SHORT).show();
        //closeKeyBoard();
        //This is not really working as expected, in a sense that you have to click on the search field to make the
        //result appears, but let sleep on it for now

    }



    public void create_group_handler(View view) {
        //Once the user click the create group button, assuming these things have been filled out
        /**
         * Group name and all group members
         * maybe each group name should be unique too, so should check for its uniqueness before creating
         */
        m_group_name = ((EditText) findViewById(R.id.group_name)).getText().toString();
        Toast.makeText(this, "Button clicked", Toast.LENGTH_LONG).show();
        Toast.makeText(this, m_group_name, Toast.LENGTH_LONG).show();

        //return;

        if (m_group_name.isEmpty()) {
            Toast.makeText(this, "Group name required", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this, "Get here", Toast.LENGTH_LONG).show();

        //Create a host object and invitees objects
        Host host = new Host(current_user_email);
        ArrayList<Invitation> invitations = new ArrayList<>();
        ArrayList<Invitees> invitees = new ArrayList<>();
        //At first the status of accepting invitation is false for all invitees
        for (int i = 0; i < group_members_emails.size(); i++) {
            //invitees.add(new Invitees(group_members_emails.get(i), false))
            Invitation new_invitation = new Invitation(m_group_name, "host", false);
            Invitees current_invitee = new Invitees(group_members_emails.get(i), "host");
            current_invitee.invitedTo(m_group_name);
            invitations.add(new_invitation);
            invitees.add(current_invitee);
        }
        //host.create_group(m_group_name, invitees);


        //m_invitees.document("invitees.get(")
        for (int i = 0; i < invitations.size(); i++) {

            //m_invitees.update(invitees.get(i).getInviteeEmail(), "groups", FieldValue.arrayUnion("greater_virginia"));
            m_invitees.document(invitees.get(i).getInviteeEmail()).update("groups", FieldValue.arrayUnion(m_group_name));
            m_invitees.document(invitees.get(i).getInviteeEmail()).update("hosts", FieldValue.arrayUnion("host"));
        }




/**
        //First this will push the email of the invitees into Pending table in Firebase
        //the hash map will list groupname-> [member1, member2, ... member n, host]
        Map<String, Object> host_members = new HashMap<>();
        final List<String> temp = group_members_emails;
        temp.add(current_user_email);
        host_members.put(group_name, temp);
        pending_invites.update(host_members);

        //push data into invitees invitess->[group1, group2, .. , group n]
        invitees.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                //the parameter documentSnapshot is a map key->value
                if (documentSnapshot.exists()){
                    Map<String, Object> data = documentSnapshot.getData();
                    for (int i = 0; i < temp.size(); i++) {
                        String invited_person = temp.get(i);
                        if (data.containsKey(invited_person)) {
                            //Object a = data.get(invited_person);
                            List<Object> b = java.util.Arrays.asList(data.get(invited_person));
                            b.add(group_name);
                            data.put(invited_person, b);
                            //Update in firebase database
                            invitees.update(data);


                        }
                    }

                }



            }
        });

        Map<String, Object> invitess = new HashMap<>();
        for (int i = 0; i < temp.size(); i++) {
            invitess.put(temp.get(i), group_name);
        }
        invitees.update(invitess);


        

        //host_members.put("Host-members", group_members_emails);
 **/



    }
    private void setUpRecyclerView() {
        recyclerView.setAdapter(group_members_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(group_members_adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
