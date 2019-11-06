package com.example.groupandgoeventplanner;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateGroupActivity extends AppCompatActivity implements Adapter.ItemClickListener{
    Adapter adapter;
    private DocumentReference users_emails = FirebaseFirestore.getInstance().document("/users/users_emails");
    private CollectionReference groups = FirebaseFirestore.getInstance().collection("/groups");
    private FirebaseUser user;
    private String current_user_email;
    private List<String> group_members_emails;
    private String group_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        user = FirebaseAuth.getInstance().getCurrentUser();
        current_user_email = user.getEmail();
    }


    public void onItemClick(View view, int position) {
        //get group name
        //String group_name = findViewById(R.id.group_name).toString();
        /**
         * Users have to come back to the main screen and click on the create group button to create a new group
         *
         */
        if (group_members_emails.contains(adapter.getItem(position))) {
            //Do not add
            return;
        }
        else {
            group_members_emails.add(adapter.getItem(position));
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
        List<String> sample = new ArrayList<String>();
        sample.add("Hello");
        sample.add("Items");
        sample.add("DFL");
        RecyclerView recyclerView = findViewById(R.id.search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, result);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

        if (group_name.isEmpty()) {
            Toast.makeText(this, "Group name required", Toast.LENGTH_LONG).show();
        }


    }
}
