package com.example.mystorebusiness.account.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mystorebusiness.R;
import com.example.mystorebusiness.account.ContActivity;
import com.example.mystorebusiness.data.Db_Users;
import com.example.mystorebusiness.data.UserAccount;

public class ContEdit extends AppCompatActivity {

    private int id_user;
    private EditText email,username,name,mobile,address,birth;
    private Db_Users db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cont_details);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        id_user=this.getIntent().getIntExtra("user_id", 0);
        name= findViewById(R.id.name_details);
        username= findViewById(R.id.username_details);
        mobile=findViewById(R.id.mobile_details);
        email= findViewById(R.id.email_details);
        address= findViewById(R.id.address_details);
        birth= findViewById(R.id.birth_details);
        Button save = findViewById(R.id.save_details);

        db=new Db_Users(this);
        Cursor result=db.getUsers(id_user);
        if(result.getCount()!=0) {
            while (result.moveToNext()) {
                username.setText(result.getString(1));
                email.setText(result.getString(2));
                mobile.setText(result.getString(4));
                name.setText(result.getString(5));
                birth.setText(result.getString(6));
                address.setText(result.getString(7));
            }
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean invalid = false;
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if (email.getText().toString().equals("")) {
                        invalid = true;
                        Toast.makeText(getApplicationContext(), "Email missing",
                                Toast.LENGTH_SHORT).show();
                    } else
                    if (!email.getText().toString().matches(emailPattern))
                    {
                        invalid=true;
                        Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
                    }else
                    if (username.getText().toString().equals("")) {
                        invalid = true;
                        Toast.makeText(getApplicationContext(),
                                "Enter your username", Toast.LENGTH_SHORT)
                                .show();
                    }


                if (!invalid) {
                    UserAccount user = new UserAccount();
                    user.setId(id_user);
                    user.setMail(email.getText().toString());
                    user.setUsername(username.getText().toString());
                    user.setName(name.getText().toString());
                    user.setPhone((mobile.getText().toString()));
                    user.setAddress(address.getText().toString());
                    user.setBirth(birth.getText().toString());
                    db.updateUser(user);
                    Intent it = new Intent(getApplicationContext(), ContActivity.class);
                    it.putExtra("user_id", String.valueOf(id_user));
                    startActivity(it);
                }
            }
        });

    }
}
