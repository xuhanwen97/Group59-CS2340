package com.example.xu.group59.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.Toast;

import com.example.xu.group59.R;
import com.example.xu.group59.fragments.ShelterListFragment;
import com.example.xu.group59.models.Shelter;

public class HomelessHomeActivity extends AppCompatActivity
        implements ShelterListFragment.ShelterListFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeless_home);

        if (savedInstanceState == null) {
            ShelterListFragment shelterListFragment = ShelterListFragment.newInstance(this);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.homeless_home_fragment_container, shelterListFragment, ShelterListFragment.TAG)
                    .commit();
        }

        //Sets up the top bar
        setSupportActionBar((Toolbar) findViewById(R.id.app_toolbar));
    }

    private void attemptLogout() {
        Toast successToast = Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT);
        successToast.setGravity(Gravity.CENTER, 0, 0);
        successToast.show();

        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void onShelterClicked(Shelter shelter) {
    }
}
