package com.example.mystorebusiness.account.ui.stocks;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.anychart.charts.Stock;
import com.example.mystorebusiness.R;
import com.example.mystorebusiness.account.ui.employees.Employee;
import com.example.mystorebusiness.data.Db_Employee;
import com.example.mystorebusiness.data.Db_Stocks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Objects;


public class UpdateStock extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Db_Stocks db;
    private EditText nameEdit,priceEdit,quantityEdit,description,cod,addition,final_price,data_expiration;
    private ImageView imageView;
    private int id_product;
    private Boolean imageChange=false;
    private String phatImage;
    private Uri actualUri;
    private static final int PICK_IMAGE_REQUEST = 0;
    Boolean infoItemHasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_updata);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        id_product = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("product_id")));
        Toast.makeText(this,String.valueOf(id_product),Toast.LENGTH_LONG).show();
        FloatingActionButton fab1 = findViewById(R.id.fab1_update);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addItemToDb())
                    onBackPressed();


            }
        });


        addition=findViewById(R.id.addition_update);
        nameEdit =  findViewById(R.id.product_name_update);
        cod =  findViewById(R.id.cod_update);
        data_expiration =  findViewById(R.id.product_data_update);
        priceEdit =  findViewById(R.id.price_update);;
        final_price=findViewById(R.id.price_final_update);
        quantityEdit =  findViewById(R.id.quantity_update);
        description =  findViewById(R.id.description_update);
        ImageButton decreaseQuantity = findViewById(R.id.decrease_quantity);
        ImageButton increaseQuantity = findViewById(R.id.increase_quantity);
        imageView =  findViewById(R.id.image_update);

        db = new Db_Stocks(this);

        Cursor result = db.getProducts(id_product);
        if (result.getCount() != 0) {
            while (result.moveToNext()) {
                cod.setText(result.getString(2));
                nameEdit.setText(result.getString(3));
                priceEdit.setText(result.getString(4));
                addition.setText(result.getString(5));
                final_price.setText(result.getString(6));
                quantityEdit.setText(result.getString(7));
                data_expiration.setText(result.getString(8));
                imageView.setImageURI(Uri.parse(result.getString(11)));
                phatImage=result.getString(11);
                description.setText(result.getString(10));
            }
        }

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
        if (!isAllOk) {
            return false;
        }


        Calendar calendar = Calendar.getInstance();
        String date=String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))+"."+String.valueOf(calendar.get(Calendar.MONTH)+1)+"."+String.valueOf(calendar.get(Calendar.YEAR));

        StockItem item = new StockItem(0,null,null,null,null,null,null,null,null,null,null);
        item.setId(id_product);
        item.setProductName(nameEdit.getText().toString());
        item.setCod(cod.getText().toString());

        if (imageChange)
            item.setImage(actualUri.toString());
        else
            item.setImage(phatImage);

        item.setPrice( priceEdit.getText().toString());
        item.setAddition(addition.getText().toString());
        item.setFinal_price(final_price.getText().toString());
        item.setQuantity( quantityEdit.getText().toString());
        item.setData_add(date);
        item.setData_expiration( data_expiration.getText().toString());
        item.setDescription(description.getText().toString());

        db.updateItem(item);
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
                                db = new Db_Stocks(getApplicationContext());
                                db.deleteItem(id_product);
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
        builder.setMessage(R.string.delete_product);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_employee, menu);
        return true;
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
                imageChange=true;
                imageView.setImageURI(actualUri);
                imageView.invalidate();
            }
        }
    }
}

