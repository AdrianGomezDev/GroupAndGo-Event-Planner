package com.example.groupandgoeventplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ShowInvitations_3_Activity extends AppCompatActivity implements Adapter.ItemClickListener{
    Adapter group_names_recycler_view;
    private CollectionReference invitees = FirebaseFirestore.getInstance().collection("/Invitees");
    String m_current_user_name;
    private RecyclerView data;
    final private ArrayList<String> groups =  new ArrayList<String>();
    final String TAG = "Here come groups names";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_invitations_3_);
        data = findViewById(R.id.group_names);
        m_current_user_name = getCurrentUserName();
        //groups = new ArrayList<String>();
        final List<String> temp_list = new ArrayList<String>();

        invitees.document(m_current_user_name).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                //the parameter documentSnapshot is a map key->value
                if (documentSnapshot.exists()){
                    //documentSnapshot.getData

                    Map<String, Object> data = documentSnapshot.getData();
                    ArrayList<String> temp = (ArrayList)data.get("groups");

                    for (int i = 0; i < temp.size(); i++) {
                        temp_list.add(temp.get(i));
                    }

                    Log.d(TAG, "DocumentSnapshot data dsfkbd: " + temp_list);

                }


            }
        });
        ArrayList<String> a = new ArrayList<String>();
        a.add("DFD");
        a.add("DFNDF");
        a.add("JNLJKN");
        Log.d("OUT OF THE LOOP", "OUT sdafas" + temp_list);

        data.setLayoutManager(new LinearLayoutManager(this));
        group_names_recycler_view = new Adapter(this, Arrays.asList("g1", "g2", "g3"));
        group_names_recycler_view.setClickListener(this);
        data.setAdapter(group_names_recycler_view);
        group_names_recycler_view.notifyDataSetChanged();







    }

    public String getCurrentUserName() {
        return "cheetos";
    }
    public void onItemClick(View view, int position){

    }
}
