package com.example.mystorebusiness;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mystorebusiness.account.ContActivity;
import com.example.mystorebusiness.data.Db_Users;
import com.example.mystorebusiness.data.UserAccount;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText password, password_retype, mail, username;
    private Button btSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RelativeLayout relative_layout = findViewById(R.id.relative_layout);
        btSignUp        = findViewById(R.id.sign_up);
        mail            =  findViewById(R.id.email);
        username        =  findViewById(R.id.username_sign);
        password        = findViewById(R.id.password_sign);
        password_retype =findViewById(R.id.password_retype_sign);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.uptodowndiagonal);
        relative_layout.setAnimation(animation);

        btSignUp.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        boolean invalid = false;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (v == btSignUp) {
            if (mail.getText().toString().equals("")) {
                invalid = true;
                Toast.makeText(getApplicationContext(), "Enter your email",
                        Toast.LENGTH_SHORT).show();
            } else if (!mail.getText().toString().matches(emailPattern)) {
                invalid = true;
                Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            } else if (username.getText().toString().equals("")) {
                invalid = true;
                Toast.makeText(getApplicationContext(),
                        "Enter your username", Toast.LENGTH_SHORT)
                        .show();
            } else if (password.getText().toString().equals("")) {
                invalid = true;
                Toast.makeText(getApplicationContext(), "Enter your password",
                        Toast.LENGTH_SHORT).show();
            } else if (password_retype.getText().toString().equals("")) {
                invalid = true;
                Toast.makeText(getApplicationContext(), "Enter the password_retype",
                        Toast.LENGTH_SHORT).show();
            } else if (!password_retype.getText().toString().equals(password.getText().toString())) {
                invalid = true;
                Toast.makeText(getApplicationContext(), "Failed password verification",
                        Toast.LENGTH_SHORT).show();
            }

            if (!invalid) {

                Db_Users db = new Db_Users(this);
                boolean rez1 = db.checkUser(mail.getText().toString());
                if (!rez1) {

                    UserAccount user = new UserAccount(username, password, mail);
                    String rez = db.addUser(user);
                    Toast.makeText(this, rez, Toast.LENGTH_LONG).show();
                    mail.setText("");
                    username.setText("");
                    password.setText("");
                    password_retype.setText("");
                    ;

                } else {
                    Toast.makeText(this, "There is a user with this e-mail address!", Toast.LENGTH_LONG).show();
                    password.setText("");
                    password_retype.setText("");
                }
            }

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
