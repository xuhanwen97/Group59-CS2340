package com.example.xu.group59.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xu.group59.R;
import com.example.xu.group59.Utils.StringUtils;
import com.example.xu.group59.Utils.ToastUtils;
import com.example.xu.group59.models.User;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    //Static Variables
    public final static String LOGGED_IN_USER_DATA = "registered_user_data";
    static final int RESULT_LOGIN_SUCCESS = 100;

    //Instance Variables
    Button loginButton;
    EditText emailEditText, passwordEditText;

    ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Sets up the top bar
        setSupportActionBar((Toolbar) findViewById(R.id.app_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Grabs each view from the layout
        loginButton = (Button) findViewById(R.id.login_button);
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);

        //sets an onclick listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        //Grab the user list from intent
        if (getIntent() != null && getIntent().hasExtra(WelcomeActivity.EXTRA_USER_LIST)) {
            users = getIntent().getParcelableArrayListExtra(WelcomeActivity.EXTRA_USER_LIST);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void attemptLogin() {
        String givenEmail = emailEditText.getText().toString();
        String givenPassword = passwordEditText.getText().toString();

        if (StringUtils.isNullOrEmpty(givenEmail)) {
            ToastUtils.shortToastCenter(this, "Email cannot be empty").show();
            return;
        }

        if (StringUtils.isNullOrEmpty(givenPassword)) {
            ToastUtils.shortToastCenter(this, "Password cannot be empty").show();
            return;
        }

        User loggedInUser = authenticateUser(givenEmail, givenPassword);

        if (loggedInUser != null) {
            ToastUtils.shortToastCenter(this, "Login Successful").show();

            Intent finishActivityIntent = new Intent();
            setResult(RESULT_LOGIN_SUCCESS,
                    finishActivityIntent.putExtra(LOGGED_IN_USER_DATA, loggedInUser));

            ToastUtils.shortToastCenter(this, "Login Successful").show();

            finish();
        } else {
            ToastUtils.shortToastCenter(this, "Login Failed").show();
        }
    }

    //region [ Helper ] ================================= //

    /**
     * Authenticates user using username and password
     *
     * @param email given email
     * @param password given password
     * @return the user if email and password match a registered user, null otherwise
     */
    private User authenticateUser(String email, String password) {

        for (User user : users) {
            if (user.getUserID().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }

    //endregion


}
