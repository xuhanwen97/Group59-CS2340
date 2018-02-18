package com.example.xu.group59.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xu.group59.R;

public class RegisterActivity extends AppCompatActivity {

    //Instance Variables
    Button registerButton;
    EditText usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Sets up the top bar
        setSupportActionBar((Toolbar) findViewById(R.id.app_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Grabs each view from the layout
        registerButton = (Button) findViewById(R.id.register_button);
        usernameEditText = (EditText) findViewById(R.id.username_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);

        //sets an onclick listener for the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        Toast successToast = Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT);
        successToast.setGravity(Gravity.CENTER, 0, 0);
        successToast.show();
    }

}
