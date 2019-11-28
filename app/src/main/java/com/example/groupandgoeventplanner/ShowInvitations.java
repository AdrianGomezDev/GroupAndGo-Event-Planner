package com.example.groupandgoeventplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
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
    private CollectionReference Invitees = FirebaseFirestore.getInstance().collection("/Invitees");
    private String m_current_user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setUpRecyclerView();
        //setContentView(R.layout.activity_show_invitations);

        //Go to invitees and grab the data
       // m_current_user_name = getCurrentUserName();
        //Toast.makeText(getApplicationContext(), m_current_user_name, Toast.LENGTH_SHORT).show();
        //data = findViewById(R.id.group_names);
        /**
        Invitees.document(m_current_user_name).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
         **/
        //RecyclerView recycler_view_search_results = findViewById(R.id.search_results);
        /**
        data.setLayoutManager(new LinearLayoutManager(this));
        group_names = new Adapter(this, groups);
        group_names.setClickListener(this);
        data.setAdapter(group_names);
        group_names.notifyDataSetChanged();
         **/

    }
    public String getCurrentUserName() {
        return "cheetos";
    }
    public void onItemClick(View view, int position){

    }
}
