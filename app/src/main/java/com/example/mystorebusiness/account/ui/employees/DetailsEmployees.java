package com.example.mystorebusiness.account.ui.employees;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mystorebusiness.R;
import com.example.mystorebusiness.account.ui.stocks.ScanCodeActivity;
import com.example.mystorebusiness.data.Db_Employee;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class DetailsEmployees extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Db_Employee db;
    private Boolean imageChange=false;
    private String phatImage;
    private int id_employee;
    private EditText phone, mail,name,address,birth,CNP,series,description,salary;
    private ImageView imageView,call;
    private  Uri actualUri;
    private static final int PICK_IMAGE_REQUEST = 0;
    Boolean infoItemHasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees_details);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab1 = findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean invalid = false;
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (!mail.getText().toString().matches(emailPattern))
                {
                    invalid=true;
                    Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
                }

                if (!invalid) {
                    if (Objects.requireNonNull(getIntent().getStringExtra("employee_id")).equals("add")) {
                        if (addItemToDb())
                            onBackPressed();
                    } else {
                        Employee item = new Employee();
                        item.setId(Integer.parseInt(getIntent().getStringExtra("employee_id")));
                        item.setName(name.getText().toString());
                        item.setAddress(address.getText().toString());

                        if (imageChange)
                            item.setImage(actualUri.toString());
                        else
                            item.setImage(phatImage);

                        item.setPhone(phone.getText().toString());
                        item.setMail(mail.getText().toString());
                        item.setSalary(Integer.parseInt(salary.getText().toString()));
                        item.setCNP(CNP.getText().toString());
                        item.setSeries(series.getText().toString());
                        item.setBirth(birth.getText().toString());
                        item.setDescription(description.getText().toString());

                        db.updateEmployee(item);
                        onBackPressed();
                    }
                }

            }
        });

        db = new Db_Employee(this);

        call=findViewById(R.id.call);
        name =  findViewById(R.id.employee_name_edit);
        address =  findViewById(R.id.employee_address_edit);
        phone =  findViewById(R.id.employee_phone_edit);
        description =  findViewById(R.id.description);
        mail = findViewById(R.id.employee_email_edit);
        salary =  findViewById(R.id.employee_salary_edit);
        CNP =  findViewById(R.id.employee_CNP_edit);
        series =  findViewById(R.id.employee_series_edit);
        birth =  findViewById(R.id.employee_birth_edit);
        imageView =  findViewById(R.id.employee_image_view);

        if (!Objects.requireNonNull(getIntent().getStringExtra("employee_id")).equals("add")) {
             id_employee = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("employee_id")));
            Cursor result = db.getEmployee(id_employee);
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    name.setText(result.getString(2));
                    address.setText(result.getString(3));
                    phone.setText(result.getString(4));
                    mail.setText(result.getString(5));
                    salary.setText(result.getString(6));
                    CNP.setText(result.getString(7));
                    series.setText(result.getString(8));
                    birth.setText(result.getString(9));
                    imageView.setImageURI(Uri.parse(result.getString(10)));
                    phatImage=result.getString(10);
                    description.setText(result.getString(11));
                }
            }
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+phone.getText().toString()));
                    startActivity(intent);

                }
            });

        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToOpenImageSelector();
                infoItemHasChanged = true;
            }
        });
    }


    private boolean addItemToDb() {
        boolean isAllOk = true;
        if (checkIfValueSet(name, "name")) {
            isAllOk = false;
        }
        if (checkIfValueSet(address, "address")) {
            isAllOk = false;
        }
        if (checkIfValueSet(phone, "phone")) {
            isAllOk = false;
        }
        if (checkIfValueSet(mail, "email")) {
            isAllOk = false;
        }
        if (checkIfValueSet(salary, "salary")) {
            isAllOk = false;
        }
        if (checkIfValueSet(CNP, "CNP")) {
            isAllOk = false;
        }
        if (checkIfValueSet(series, "series")) {
            isAllOk = false;
        }
        if (checkIfValueSet(birth, "date of birth")) {
            isAllOk = false;
        }
        if (actualUri == null) {
            isAllOk = false;
            Toast.makeText(this,"Missing image",Toast.LENGTH_LONG).show();
        }
        if (!isAllOk) {
            return false;
        }

        Employee item = new Employee();
        item.setId_user(this.getIntent().getIntExtra("user_id", 0));
        item.setName(name.getText().toString().trim());
        item.setAddress(address.getText().toString().trim());
        item.setImage(actualUri.toString());
        item.setPhone(phone.getText().toString().trim());
        item.setMail(mail.getText().toString().trim());
        item.setSalary(Integer.parseInt(salary.getText().toString().trim()));
        item.setCNP(CNP.getText().toString().trim());
        item.setSeries(series.getText().toString().trim());
        item.setBirth(birth.getText().toString().trim());
        item.setDescription(description.getText().toString().trim());

        String result=db.addEmployee(item);
        Toast.makeText(this,result,Toast.LENGTH_LONG).show();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_delete:
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db = new Db_Employee(getApplicationContext());
                                db.deleteEmployee(id_employee);
                                onBackPressed();
                            }
                        };
                // Show dialog if you do not want to exit
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_employee);
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

    private boolean checkIfValueSet(EditText text, String description) {
        if (TextUtils.isEmpty(text.getText())) {
            text.setError("Missing employee " + description);
            return true;
        } else {
            text.setError(null);
            return false;
        }
    }



    public void tryToOpenImageSelector() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            return;
        }
        openImageSelector();
    }

    private void openImageSelector() {
        Intent intent;
        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageSelector();
                // permission was granted
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_employee, menu);
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code READ_REQUEST_CODE.
        // If the request code seen here doesn't match, it's the response to some other intent,
        // and the below code shouldn't run at all.

        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.  Pull that uri using "resultData.getData()"

            if (resultData != null) {
                actualUri = resultData.getData();
                imageChange=true;
                imageView.setImageURI(actualUri);
                imageView.invalidate();
            }
        }
    }
}

