package com.example.groupandgoeventplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.groupandgoeventplanner.ui.showinvitations2.ShowInvitations2Fragment;

public class ShowInvitations_2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_invitations_2_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ShowInvitations2Fragment.newInstance())
                    .commitNow();
        }
    }
}
