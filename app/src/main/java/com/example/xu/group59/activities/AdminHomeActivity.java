package com.example.xu.group59.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.Toast;

import com.example.xu.group59.R;
import com.example.xu.group59.fragments.HomelessListFragment;
import com.example.xu.group59.models.HomelessPerson;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {

    public static final String LOGGED_IN_USER_TAG = "logged_in_user";
    private List<HomelessPerson> currentHomelessList;
    private HomelessPerson loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeless_home);

        //Grab the signed in user
        if ((getIntent().getExtras() != null) && (getIntent().getExtras().get(LOGGED_IN_USER_TAG) != null)) {
            loggedInUser = (HomelessPerson) getIntent().getExtras().get(LOGGED_IN_USER_TAG);
        } else {
            loggedInUser = null;
        }

        //Sets up the top bar
        setSupportActionBar((Toolbar) findViewById(R.id.app_toolbar));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_activity_admin_home);
        }

        setupHomelessList();
    }

    @Override
    public boolean onSupportNavigateUp() {

        attemptLogout();
        return true;
    }

    private void attemptLogout() {
        loggedInUser = null;
        Toast successToast = Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT);
        successToast.setGravity(Gravity.CENTER, 0, 0);
        successToast.show();

        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);

        finish();
    }

    private void setupHomelessList() {

        Query homelessListQuery = FirebaseDatabase.getInstance()
                .getReference(HomelessPerson.homelessPersonKey)
                .orderByKey();

        homelessListQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<HomelessPerson> homelessList = new ArrayList<>(10);
                for (DataSnapshot homelessSnapshot : dataSnapshot.getChildren()) {
                    HomelessPerson tempHomeless = new HomelessPerson(homelessSnapshot);

                    // Only add non-admins
                    if (!tempHomeless.getUserStatus().contains(HomelessPerson.UserStatus.Admin)) {
                        homelessList.add(tempHomeless);
                    }
                }

                showHomelessList(homelessList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showHomelessList(List<HomelessPerson> homelessList) {
        HomelessListFragment homelessListFragment
                = (HomelessListFragment) getSupportFragmentManager().findFragmentByTag(HomelessListFragment.TAG);

        // Not sure if statement is necessary
        if (homelessListFragment != null) {
            //replace homeless list fragment, don't make new
            homelessListFragment = HomelessListFragment.newInstance(homelessList);

            currentHomelessList = homelessList;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.homeless_home_fragment_container, homelessListFragment, HomelessListFragment.TAG)
                    .commit();
        } else {
            homelessListFragment = HomelessListFragment.newInstance(homelessList);

            currentHomelessList = homelessList;
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.homeless_home_fragment_container, homelessListFragment, HomelessListFragment.TAG)
                    .addToBackStack(null)
                    .commit();
        }

    }
}
