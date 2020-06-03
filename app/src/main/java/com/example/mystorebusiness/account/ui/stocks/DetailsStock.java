package com.example.mystorebusiness.account.ui.stocks;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.anychart.charts.Stock;
import com.example.mystorebusiness.R;
import com.example.mystorebusiness.account.ui.employees.Employee;
import com.example.mystorebusiness.data.Db_Stocks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Objects;


public class DetailsStock extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Db_Stocks db;
    private EditText nameEdit,priceEdit,quantityEdit,description,cod,addition,final_price,data_expiration;
    private ImageView imageView;
    private int id_user;
    private Uri actualUri;
    private static final int PICK_IMAGE_REQUEST = 0;
    Boolean infoItemHasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        id_user=this.getIntent().getIntExtra("user_id", 0);
        FloatingActionButton fab1 = findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (addItemToDb())
                        onBackPressed();


            }
        });

        ImageView scan = findViewById(R.id.product_scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), ScanCodeActivity.class);
                it.putExtra("user_id", id_user);
                it.putExtra("scope", 0);
                startActivity(it);
                finish();
            }
        });

        addition=findViewById(R.id.addition_edit);
        final_price=findViewById(R.id.price_final_edit);
        nameEdit =  findViewById(R.id.product_name_edit);
        cod =  findViewById(R.id.product_cod_edit);
        data_expiration =  findViewById(R.id.product_data_edit);
        priceEdit =  findViewById(R.id.price_edit);
        quantityEdit =  findViewById(R.id.quantity_edit);
        description =  findViewById(R.id.description);
        imageView =  findViewById(R.id.image_view);

                nameEdit.setText(getIntent().getStringExtra("nameScan"));
                cod.setText(getIntent().getStringExtra("codScan"));
                data_expiration.setText(getIntent().getStringExtra("expiration_dateScan"));
                priceEdit.setText(getIntent().getStringExtra("priceScan"));
                quantityEdit.setText(getIntent().getStringExtra("quantityScan"));
                description.setText(getIntent().getStringExtra("descriptionScan"));


        ImageButton decreaseQuantity = findViewById(R.id.decrease_quantity);
        ImageButton increaseQuantity = findViewById(R.id.increase_quantity);

        db = new Db_Stocks(this);

        addition.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(!(addition.getText().toString().equals("") || priceEdit.getText().toString().equals("")))
                    final_price.setText(String.valueOf(Integer.parseInt(addition.getText().toString())+Integer.parseInt(priceEdit.getText().toString())));
                else
                    final_price.setText("");
            }
        });

        priceEdit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(!(addition.getText().toString().equals("") || priceEdit.getText().toString().equals("")))
                    final_price.setText(String.valueOf(Integer.parseInt(addition.getText().toString())+Integer.parseInt(priceEdit.getText().toString())));
                else
                    final_price.setText("");
            }
        });

        decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subtractOneToQuantity();
                infoItemHasChanged = true;
            }
        });

        increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sumOneToQuantity();
                infoItemHasChanged = true;
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToOpenImageSelector();
                infoItemHasChanged = true;
            }
        });
    }



    private void subtractOneToQuantity() {
        String previousValueString = quantityEdit.getText().toString();
        int previousValue;
        if (previousValueString.isEmpty()) {
            return;
        } else if (previousValueString.equals("0")) {
            return ;
        } else {
            previousValue = Integer.parseInt(previousValueString);
            quantityEdit.setText(String.valueOf(previousValue - 1));
        }
    }

    private void sumOneToQuantity() {
        String previousValueString = quantityEdit.getText().toString();
        int previousValue;
        if (previousValueString.isEmpty()) {
            previousValue = 0;
        } else {
            previousValue = Integer.parseInt(previousValueString);
        }
        quantityEdit.setText(String.valueOf(previousValue + 1));
    }



    private boolean addItemToDb() {
        boolean isAllOk = true;
        if (checkIfValueSet(nameEdit, "name")) {
            isAllOk = false;
        }
        if (checkIfValueSet(cod, "cod")) {
            isAllOk = false;
        }
        if (checkIfValueSet(addition, "addition")) {
            isAllOk = false;
        }
        if (checkIfValueSet(final_price, "final price")) {
            isAllOk = false;
        }
        if (checkIfValueSet(data_expiration, "data expiration")) {
            isAllOk = false;
        }
        if (checkIfValueSet(priceEdit, "price")) {
            isAllOk = false;
        }
        if (checkIfValueSet(quantityEdit, "quantity")) {
            isAllOk = false;
        }
        if (checkIfValueSet(description, "description")) {
            isAllOk = false;
        }
        if (actualUri == null) {
            isAllOk = false;
            Toast.makeText(this,"Missing image",Toast.LENGTH_LONG).show();
        }
        if (!isAllOk) {
            return false;
        }


        Calendar calendar = Calendar.getInstance();
        String date=String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))+"."+String.valueOf(calendar.get(Calendar.MONTH)+1)+"."+String.valueOf(calendar.get(Calendar.YEAR));

            StockItem item = new StockItem(
                    id_user,
                    cod.getText().toString().trim(),
                    nameEdit.getText().toString().trim(),
                    priceEdit.getText().toString().trim(),
                    addition.getText().toString().trim(),
                    final_price.getText().toString().trim(),
                    quantityEdit.getText().toString().trim(),
                    date,
                    data_expiration.getText().toString().trim(),
                    description.getText().toString().trim(),
                    actualUri.toString());

        boolean rez = db.checkCod(getIntent().getStringExtra("codScan"));
        if (!rez) {
            String result = db.addItem(item);
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "The product is already in stock!", Toast.LENGTH_LONG).show();
            nameEdit.setText("");
            cod.setText("");
            data_expiration.setText("");
            priceEdit.setText("");
            addition.setText("");
            quantityEdit.setText("");
            description.setText("");
            return false;
        }


        return true;
    }

    private boolean checkIfValueSet(EditText text, String description) {
        if (TextUtils.isEmpty(text.getText())) {
            text.setError("Missing product " + description);
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
                imageView.setImageURI(actualUri);
                imageView.invalidate();
            }
        }
    }
}

