package com.example.xu.group59.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.xu.group59.R;
import com.example.xu.group59.models.User;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {

    //Static Variables
    static final int ACTIVITY_REGISTER = 0;
    static final int ACTIVITY_LOGIN = 1;
    static final String EXTRA_USER_LIST = "extra_user_list";

    User defaultUser = new User("user", "pass", "default_user");

    Button loginButton, registerButton;
    ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Sets up the top bar
        setSupportActionBar((Toolbar) findViewById(R.id.app_toolbar));

        //Grabs each view from the layout
        loginButton = (Button) findViewById(R.id.welcome_login_button);
        registerButton = (Button) findViewById(R.id.welcome_register_button);

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

        users = new ArrayList<>();
        users.add(defaultUser);
    }

    //region [ Helpers ] ================================= //

    //region [ Activity Helpers ] ================================= //

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);

        intent.putParcelableArrayListExtra(EXTRA_USER_LIST, users);
        startActivityForResult(intent, ACTIVITY_LOGIN);
    }

    private void launchRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);

        intent.putParcelableArrayListExtra(EXTRA_USER_LIST, users);
        startActivityForResult(intent, ACTIVITY_REGISTER);
    }

    private void launchTempApplication() {
        Intent intent = new Intent(this, HomelessHomeActivity.class);
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
                if (resultCode == RegisterActivity.RESULT_REGISTER_SUCCESS) {
                    users.add((User) data.getParcelableExtra(RegisterActivity.REGISTERED_USER_DATA));
                }
                break;
            case ACTIVITY_LOGIN:
                if (resultCode == LoginActivity.RESULT_LOGIN_SUCCESS) {
                    //user that's logged in
                    User loggedInUser = data.getParcelableExtra(LoginActivity.LOGGED_IN_USER_DATA);

                    launchTempApplication();
                }
                break;
        }
    }

    //endregion
}
