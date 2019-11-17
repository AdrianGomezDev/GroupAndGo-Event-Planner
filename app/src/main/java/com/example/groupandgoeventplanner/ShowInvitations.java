package com.example.groupandgoeventplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class ShowInvitations extends AppCompatActivity implements Adapter.ItemClickListener {
    Adapter group_names;
    private DocumentReference pending_invites = FirebaseFirestore.getInstance().document("/Pending/pendingInvites");
    private RecyclerView data;
    private ArrayList<String> groups;
    private DocumentReference invitees = FirebaseFirestore.getInstance().document("/Pending/Invitees");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_invitations);
        //Go to pending and grab the data
        data = findViewById(R.id.group_names);
        pending_invites.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                //the parameter documentSnapshot is a map key->value
                if (documentSnapshot.exists()){
                    Map<String, Object> data = documentSnapshot.getData();
                    for (Object group_name: data.values()){
                        String name = group_name.toString();
                        groups.add(name);
                    }

                }


            }
        });

    }
    public void onItemClick(View view, int position){

    }
}
