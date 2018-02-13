package com.example.xu.group59;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TempApplication extends AppCompatActivity {

    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_application);

        //Sets up the top bar
        setSupportActionBar((Toolbar) findViewById(R.id.app_toolbar));

        logoutButton = (Button) findViewById(R.id.temp_logout_button);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogout();
            }
        });

    }

    private void attemptLogout() {
        Toast successToast = Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT);
        successToast.setGravity(Gravity.CENTER, 0, 0);
        successToast.show();

        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);

        finish();
    }
}
