package com.example.xu.group59.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.xu.group59.R;
import com.example.xu.group59.Utils.StringUtils;
import com.example.xu.group59.Utils.ToastUtils;
import com.example.xu.group59.models.HomelessPerson;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity that set up the login screen that any user will see when opening the application.  This
 * will also authenticate the user and add their information to the database
 */
public class LoginActivity extends AppCompatActivity {

    //Static Variables
    public static final String LOGGED_IN_USER_DATA = "registered_user_data";
    static final int RESULT_LOGIN_SUCCESS = 100;

    //Instance Variables
    private Button loginButton;
    private EditText emailEditText;
    private EditText passwordEditText;

    private final Collection<HomelessPerson> homelessPeople = new ArrayList<>();

    private final int MAX_LOGIN_ATTEPMTS = 3;
    private Map<String, Integer> loginAttempts;

    //Keeps track of if there is currently a login request
    private Boolean waitingForLoginResponse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Sets up the top bar
        setSupportActionBar((Toolbar) findViewById(R.id.app_toolbar));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Grabs each view from the layout
        loginButton = findViewById(R.id.login_button);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);

        loginAttempts = new HashMap<>();

        //sets an onclick listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //When waiting for login response of an attempt, don't let a new login request be made
                if (waitingForLoginResponse) {
                    return;
                }
                attemptLogin();
            }
        });
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

        authenticateUser(givenEmail, givenPassword);
    }

    //region [ Helper ] ================================= //

    /**
     * Authenticates user using username and password
     *
     * @param login given login
     * @param password given password
     */
    private void authenticateUser(String login, final String password) {


        if (loginAttempts.containsKey(login)) {
            if (loginAttempts.get(login) >= MAX_LOGIN_ATTEPMTS) {
                loginBlockedToast();
                return;
            }
        }

        waitingForLoginResponse = true;

        DatabaseReference userReference =
                FirebaseDatabase.getInstance().getReference(HomelessPerson.homelessPersonKey + "/" + login);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                waitingForLoginResponse = false;

                if (dataSnapshot.getValue() == null) {
                    loginFailedToast();
                } else {
                    HomelessPerson hp = new HomelessPerson(dataSnapshot);

                    if (hp.getUserStatus().contains(HomelessPerson.UserStatus.Blocked)) {
                        loginBlockedToast();
                        hp.logActivity("login_blocked");
                        return;
                    }

                    if (password.equals(hp.getPassword())) {
                        loginSuccessToast(hp);
                        loginAttempts.clear();
                        hp.logActivity("login_success");

                    } else {
                        if (loginAttempts.containsKey(hp.getUserLogin())) {
                            int curAttempts = loginAttempts.get(hp.getUserLogin());
                            loginAttempts.put(hp.getUserLogin(), curAttempts + 1);

                            if (loginAttempts.get(hp.getUserLogin()) >= MAX_LOGIN_ATTEPMTS) {
                                lockoutUser(hp);
                                hp.logActivity("login_failed");
                                hp.logActivity("login_blocked");
                                loginBlockedToast();
                                return;
                            }
                        } else {
                            loginAttempts.put(hp.getUserLogin(), 1);
                        }

                        hp.logActivity("login_failed");
                        loginFailedToast();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loginFailedToast();
            }
        });
    }

    private void lockoutUser(HomelessPerson hp) {
        DatabaseReference homelessPersonReference = FirebaseDatabase.getInstance()
                .getReference(HomelessPerson.homelessPersonKey)
                .child(hp.getUserLogin())
                .child("status");

        Map<String, Object> homelessPersonUpdates = new HashMap<>();

        homelessPersonUpdates.put("Blocked", true);
        homelessPersonUpdates.put("Active", null);

        homelessPersonReference.updateChildren(homelessPersonUpdates);
    }

    private void loginSuccessToast(Parcelable loggedInHomelessPerson) {

        ToastUtils.shortToastCenter(this, "Login Successful").show();

        Intent finishActivityIntent = new Intent();
        setResult(RESULT_LOGIN_SUCCESS,
                finishActivityIntent.putExtra(LOGGED_IN_USER_DATA, loggedInHomelessPerson));

        ToastUtils.shortToastCenter(this, "Login Successful").show();

        finish();
    }

    private void loginFailedToast() {
        ToastUtils.shortToastCenter(this, "Login Failed").show();
    }

    private void loginBlockedToast() {
        ToastUtils.shortToastCenter(this, String.format("Exceeded %d attempts, login blocked", MAX_LOGIN_ATTEPMTS)).show();
    }

}
