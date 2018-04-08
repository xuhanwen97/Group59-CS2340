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
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    //Static Variables
    public final static String LOGGED_IN_USER_DATA = "registered_user_data";
    static final int RESULT_LOGIN_SUCCESS = 100;

    //Instance Variables
    Button loginButton;
    EditText emailEditText, passwordEditText;

    List<HomelessPerson> homelessPeople = new ArrayList<>();

    //Keeps track of if there is currently a login request
    private Boolean waitingForLoginResponse = false;

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

                    if (password.equals(hp.getPassword())) {
                        loginSuccessToast(hp);
                    } else {
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
//
//    public void addHomelessToList(HomelessPerson homelessPerson) {
//        if (homelessPerson != null) {
//            homelessPeople.add(homelessPerson);
//        }
//    }

    public void getUserList() {
        DatabaseReference userReference =
                FirebaseDatabase.getInstance().getReference(HomelessPerson.homelessPersonKey);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot homelessSnapshot : dataSnapshot.getChildren()) {

                    HomelessPerson hp = new HomelessPerson(homelessSnapshot);

                    homelessPeople.add(hp);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //endregion


}
