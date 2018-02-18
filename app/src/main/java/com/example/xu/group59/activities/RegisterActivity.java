package com.example.xu.group59.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.xu.group59.R;
import com.example.xu.group59.Utils.StringUtils;
import com.example.xu.group59.Utils.ToastUtils;
import com.example.xu.group59.models.User;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    //Static Variables
    public final static String REGISTERED_USER_DATA = "registered_user_data";
    static final int RESULT_REGISTER_SUCCESS = 100;

    //Instance Variables
    Button registerButton;
    EditText emailEditText, passwordEditText, nameEditText;

    ArrayList<User> users = new ArrayList<>();

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

        //sets an onclick listener for the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
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
        for (User user : users) {
            if (user.getUserID().equals(email)) {
                ToastUtils.shortToastCenter(this, "User with email already exists").show();
                return;
            }
        }

        //Create user, if successful end the activity
        try {
            User user = new User(email, password, name);

            Intent finishActivityIntent = new Intent();
            setResult(RESULT_REGISTER_SUCCESS,
                    finishActivityIntent.putExtra(REGISTERED_USER_DATA, user));

            ToastUtils.shortToastCenter(this, "Register Successful").show();

            finish();
        } catch (Exception ex) {
            ToastUtils.shortToastCenter(this, "Creating user failed").show();
        }
    }

}
