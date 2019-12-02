package com.example.groupandgoeventplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.groupandgoeventplanner.Interfaces.OnFragmentInteractionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnFragmentInteractionListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*View headerView = navigationView.getHeaderView(0);
        Button signOut = headerView.findViewById(R.id.navSignOutButton);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(NavigationActivity.this, LoginActivity.class));
            }
        });*/

//        TextView profileEmail = headerView.findViewById(R.id.profileEmailTextView);
//        if (user != null) {
//            profileEmail.setText(user.getEmail());
//        }

        // Creates and displays the home fragment
        Fragment home = new Home();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, home);
        ft.commit();
        // Highlights the Home item in the nav drawer
        navigationView.setCheckedItem(R.id.nav_camera);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment newFragment = null;
        Intent intent;

        //initializing the fragment object which is selected
        switch (item.getItemId()) {
            case R.id.nav_camera:
                newFragment = new Home();
                break;
            case R.id.nav_gallery:
                newFragment = new Events();
                break;
            case R.id.nav_slideshow:
                intent = new Intent(this,CreateGroupActivity.class);
                startActivity(intent);
                //newFragment = new Diet();
                break;
            case R.id.nav_manage:
                intent = new Intent(this,MapViewerActivity.class);
                startActivity(intent);
                //newFragment = new ProgressPhotos();
                break;
            case R.id.nav_share:
                newFragment = new EventsPublic();
                break;

        }

        // Replace the current fragment with the selected fragment if it is different fragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (newFragment != null && !Objects.requireNonNull(currentFragment).getClass().equals(newFragment.getClass())) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, newFragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentMessage(String TAG, Object data) {
        if (TAG.equals("Events")){
            EventsPage newFragment = EventsPage.newInstance(data.toString());
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, newFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (TAG.equals("EventsPublic")){
            EventsPublicPage newFragment = EventsPublicPage.newInstance(data.toString());
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, newFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }

    public void sign_out_handler(View view) {
        //FirebaseAuth.getInstance().signOut();
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
