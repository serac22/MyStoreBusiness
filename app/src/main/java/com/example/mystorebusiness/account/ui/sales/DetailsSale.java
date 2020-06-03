package com.example.mystorebusiness.account.ui.sales;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mystorebusiness.R;
import com.example.mystorebusiness.data.Db_Sales;
import com.example.mystorebusiness.data.Db_Stocks;

import java.util.Calendar;

public class DetailsSale extends AppCompatActivity {

    private Db_Sales db;
    private Db_Stocks db_s;
    private EditText cod,name,quantity,price,total;
    private int id_user,stock_quantity;
    private String code;
    private Button sale, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_details);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = new Db_Sales(this);
        db_s = new Db_Stocks(this);

        id_user=this.getIntent().getIntExtra("user_id", 0);
        code=this.getIntent().getStringExtra("codScan");

        cod = findViewById(R.id.cod);
        sale = findViewById(R.id.saleButton);
        cancel = findViewById(R.id.cancelButton);
        name = findViewById(R.id.name);
        quantity=findViewById(R.id.quantity);
        price=findViewById(R.id.price_edit);
        total=findViewById(R.id.total);

        Cursor result = db_s.getProduct(code,id_user);
        if (result.getCount() != 0) {
            while (result.moveToNext()) {
                name.setText(result.getString(3));
                price.setText(result.getString(6));
                stock_quantity=Integer.parseInt(result.getString(7));
                cod.setText(code);
            }
        }else
            Toast.makeText(this,"The product is not in stock",Toast.LENGTH_LONG).show();


        quantity.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(!quantity.getText().toString().equals(""))
                total.setText(String.valueOf(Integer.parseInt(quantity.getText().toString())*Integer.parseInt(price.getText().toString())));
                else
                    total.setText("");
            }
        });

        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addItemToDb())
                    onBackPressed();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    onBackPressed();

            }
        });


    }



    private boolean addItemToDb() {
        boolean isAllOk = true;
        if (checkIfValueSet(name, "name")) {
            isAllOk = false;
        }
        if (checkIfValueSet(cod, "cod")) {
            isAllOk = false;
        }
        if (checkIfValueSet(total, "total")) {
            isAllOk = false;
        }
        if (checkIfValueSet(price, "price")) {
            isAllOk = false;
        }
        if (checkIfValueSet(quantity, "quantity")) {
            isAllOk = false;
        }

        if (!isAllOk) {
            return false;
        }


        SaleItem item = new SaleItem();
        item.setId_user(id_user);
        item.setCod(cod.getText().toString().trim());
        item.setName(name.getText().toString().trim());
        item.setQuantity(Integer.parseInt(quantity.getText().toString().trim()));
        item.setTotal(Integer.parseInt(total.getText().toString().trim()));

        Calendar calendar = Calendar.getInstance();
        String date=String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))+"."+String.valueOf(calendar.get(Calendar.MONTH)+1)+"."+String.valueOf(calendar.get(Calendar.YEAR));
        item.setData(date);

        String result=db.addItem(item);
        db_s.updateQuantity(stock_quantity-Integer.parseInt(quantity.getText().toString()),cod.getText().toString(),id_user);
        Toast.makeText(this,result,Toast.LENGTH_LONG).show();

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



}
