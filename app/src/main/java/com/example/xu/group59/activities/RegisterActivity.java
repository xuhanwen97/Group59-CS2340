package com.example.xu.group59.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.xu.group59.R;
import com.example.xu.group59.Utils.StringUtils;
import com.example.xu.group59.Utils.ToastUtils;
import com.example.xu.group59.models.Admin;
import com.example.xu.group59.models.HomelessPerson;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //Static Variables
    public final static String REGISTERED_USER_DATA = "registered_user_data";
    static final int RESULT_REGISTER_SUCCESS = 100;

    //Instance Variables
    Button registerButton;
    CheckBox adminCheckbox;
    EditText emailEditText, passwordEditText, nameEditText;

    //Keeps track of if there is currently a login request
    private Boolean waitingForLoginResponse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Sets up the top bar
        setSupportActionBar((Toolbar) findViewById(R.id.app_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Grabs each view from the layout
        registerButton = (Button) findViewById(R.id.register_button);
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        adminCheckbox = (CheckBox) findViewById(R.id.admin_check_box);

        //sets an onclick listener for the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Waiting result of a previous register, deactivate the register button
                if (waitingForLoginResponse) {
                    return;
                }
                attemptRegister();
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

    private void attemptRegister() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String name = nameEditText.getText().toString();

        if (StringUtils.isNullOrEmpty(email)) {
            ToastUtils.shortToastCenter(this, "Email cannot be empty").show();
            return;
        }

        if (StringUtils.isNullOrEmpty(password)) {
            ToastUtils.shortToastCenter(this, "Password cannot be empty").show();
            return;
        }

        if (StringUtils.isNullOrEmpty(name)) {
            ToastUtils.shortToastCenter(this, "Name cannot be empty").show();
            return;
        }

        //Check if user already exists, show toast if already exists
        waitingForLoginResponse = true;
        DatabaseReference checkUserReference =
                FirebaseDatabase.getInstance().getReference(HomelessPerson.homelessPersonKey + "/" + email);

        checkUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                waitingForLoginResponse = false;

                if (dataSnapshot.getValue() != null) {
                    registerUserExistsToast();
                } else {
                    registerUser();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                registerFailedToast();
            }
        });
    }

    //Make the calls to firebase that create the user in there
    private void registerUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String name = nameEditText.getText().toString();
        Boolean isAdmin = adminCheckbox.isChecked();

        //Email is the key to the HomelessPerson/user/admin created
        DatabaseReference createHomelessReference =
                FirebaseDatabase.getInstance().getReference(HomelessPerson.homelessPersonKey + "/" + email);

        HashMap newHomelessData = new HashMap();
        newHomelessData.put(HomelessPerson.currentShelterKey, "");
        newHomelessData.put(HomelessPerson.nameKey, name);
        newHomelessData.put(HomelessPerson.passwordKey, password);
        newHomelessData.put(HomelessPerson.statusKey,
                isAdmin ? HomelessPerson.defaultAdminStatus() : HomelessPerson.defaultHomelessStatus());

        createHomelessReference.setValue(newHomelessData);

        registerSuccessToast();
    }


    private void registerSuccessToast() {
        ToastUtils.shortToastCenter(this, "Register Successful").show();

        finish();

    }

    private void registerUserExistsToast() {
        ToastUtils.shortToastCenter(this, "Homeless Person with email already exists").show();
    }

    private void registerFailedToast() {
        ToastUtils.shortToastCenter(this, "Creating user failed").show();
    }

}
