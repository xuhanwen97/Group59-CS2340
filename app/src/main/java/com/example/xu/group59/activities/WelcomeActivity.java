package com.example.xu.group59.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.xu.group59.R;
import com.example.xu.group59.models.HomelessPerson;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Activity that allows a user to chose to register with the app or login to the app.
 */
public class WelcomeActivity extends AppCompatActivity {

    //Static Variables
    private static final int ACTIVITY_REGISTER = 0;
    private static final int ACTIVITY_LOGIN = 1;

    HomelessPerson defaultHomelessPerson = new HomelessPerson("user", "pass", "default_user");

    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Sets up the top bar
        setSupportActionBar((Toolbar) findViewById(R.id.app_toolbar));

        //Grabs each view from the layout
        loginButton = findViewById(R.id.welcome_login_button);
        registerButton = findViewById(R.id.welcome_register_button);

        //sets an onclick listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchLoginActivity();
            }
        });

        //sets an onclick listener for the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRegisterActivity();
            }
        });
    }

    //region [ Helpers ] ================================= //

    //region [ Activity Helpers ] ================================= //

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);

        startActivityForResult(intent, ACTIVITY_LOGIN);
    }

    private void launchRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);

        startActivityForResult(intent, ACTIVITY_REGISTER);
    }

    private void launchHomelessHomeActivity(Parcelable loggedInUser) {
        Intent intent = new Intent(this, HomelessHomeActivity.class);
        intent.putExtra(HomelessHomeActivity.LOGGED_IN_USER_TAG, loggedInUser);
        startActivity(intent);
    }

    private void launchAdminHomeActivity(Parcelable loggedInUser) {
        Intent intent = new Intent(this, AdminHomeActivity.class);
        intent.putExtra(HomelessHomeActivity.LOGGED_IN_USER_TAG, loggedInUser);
        startActivity(intent);
    }

    //endregion

    //endregion

    //region [ Activity Lifecycle ] ================================= //

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ACTIVITY_REGISTER:
                break;
            case ACTIVITY_LOGIN:
                if (resultCode == LoginActivity.RESULT_LOGIN_SUCCESS) {
                    //user that's logged in
                    HomelessPerson loggedInHomelessPerson = data.getParcelableExtra(LoginActivity.LOGGED_IN_USER_DATA);

                    if (loggedInHomelessPerson.getUserStatus().contains(HomelessPerson.UserStatus.Admin)) {
                        //launch admin page
                        launchAdminHomeActivity(loggedInHomelessPerson);
                    } else {
                        launchHomelessHomeActivity(loggedInHomelessPerson);
                    }
                }
                break;
        }
    }

    //endregion
}
