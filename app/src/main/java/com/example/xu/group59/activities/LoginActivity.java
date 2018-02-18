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
import com.example.xu.group59.models.User;

public class LoginActivity extends AppCompatActivity {

    //Instance Variables
    Button loginButton;
    EditText usernameEditText, passwordEditText;

    //Hardcoded username and password values that are correct
    User defaultUser = new User("user", "pass", "default_user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Sets up the top bar
        setSupportActionBar((Toolbar) findViewById(R.id.app_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Grabs each view from the layout
        loginButton = (Button) findViewById(R.id.login_button);
        usernameEditText = (EditText) findViewById(R.id.username_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);

        //sets an onclick listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        String givenUsername = usernameEditText.getText().toString();
        String givenPassword = passwordEditText.getText().toString();

        if (givenUsername.equals(defaultUser.getUserID()) && givenPassword.equals(defaultUser.getPassword())) {
            Toast successToast = Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT);
            successToast.setGravity(Gravity.CENTER, 0, 0);
            successToast.show();

            launchTempApplication();
        } else {
            Toast failToast = Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT);
            failToast.setGravity(Gravity.CENTER, 0, 0);
            failToast.show();
        }
    }

    private void launchTempApplication() {
        Intent intent = new Intent(this, TempApplication.class);
        startActivity(intent);
    }

}
