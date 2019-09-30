package com.example.groupandgoeventplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class CreateGroupActivity extends AppCompatActivity implements Adapter.ItemClickListener{
    Adapter adapter;
    private DocumentReference users_emails = FirebaseFirestore.getInstance().document("/users/users_emails");
    private DocumentReference groups = FirebaseFirestore.getInstance().document("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
    }
    public void onItemClick(View view, int position) {
        //When user click on we should add in
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }


    public void search_for_email_handler(View view) {
        //THis function is responsible for taking all the email address that matches with the address
        //user put in
        EditText email_field = findViewById(R.id.search_field);
        final String email_to_search_for = email_field.toString();
        final ArrayList<String> result = new ArrayList<String>();
        //First try to print out all the emails in the database
        users_emails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //the parameter documentSnapshot is a map key->value
                if (documentSnapshot.exists()){
                    Map<String, Object> data = documentSnapshot.getData();
                    for (Object email: data.values()){
                        if (email.toString().contains(email_to_search_for)) {
                            result.add(email.toString());
                        }
                    }
                    /**
                    for (String name: data.keySet()) {
                        Toast.makeText(getApplicationContext(),name, Toast.LENGTH_SHORT).show();
                    }
                    for (Object email: data.values()){
                        Toast.makeText(getApplicationContext(), email.toString() ,Toast.LENGTH_SHORT).show();
                    }
                     **/
                }
            }
        });



    }
}
