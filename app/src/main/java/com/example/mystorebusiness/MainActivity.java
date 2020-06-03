package com.example.mystorebusiness;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mystorebusiness.account.ContActivity;
import com.example.mystorebusiness.data.Db_Users;

@SuppressWarnings("rawtypes")
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton btRegister;
    private TextView tvLogin;
    private Button btLogin;
    private EditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btRegister  = findViewById(R.id.btRegister);
        tvLogin     = findViewById(R.id.tvLogin);
        btLogin     = findViewById(R.id.btLogin);
        username    = findViewById(R.id.username);
        password    = findViewById(R.id.password);
        btLogin.setOnClickListener(this);
        btRegister.setOnClickListener(this);
        username.setText("");
        password.setText("");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

        if (v==btRegister){

            Intent intent   = new Intent(MainActivity.this,RegisterActivity.class);
            Pair[] pairs    = new Pair[1];
            pairs[0] = new Pair<View,String>(tvLogin,"tvLogin");
            //noinspection unchecked
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
            startActivity(intent,activityOptions.toBundle());

        }else
            if (v==btLogin){
                Db_Users db=new Db_Users(this);
                 boolean rez=db.checkUser(username.getText().toString(),password.getText().toString());
                 if(rez) {

                     Cursor result=db.getUserID(username.getText().toString());
                     if(result.getCount()!=0){

                         StringBuilder buffer=new StringBuilder();
                         while(result.moveToNext()) {
                            buffer.append(result.getString(0));
                        }

                        Intent it = new Intent(getApplicationContext(), ContActivity.class);
                        it.putExtra("user_id", buffer.toString());
                        startActivity(it);
                     }
                }else {
                     Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
                     username.setText("");
                     password.setText("");
                 }
             }

    }

    @Override
    public void onBackPressed() {

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        System.exit(0);
                    }
                };
        // Show dialog if you do not want to exit
        showUnsavedChangesDialog(discardButtonClickListener);

    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.exit_application);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}



