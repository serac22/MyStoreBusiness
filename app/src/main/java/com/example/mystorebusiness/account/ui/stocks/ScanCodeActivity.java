package com.example.mystorebusiness.account.ui.stocks;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.example.mystorebusiness.account.ui.sales.DetailsSale;
import com.google.zxing.Result;

public class ScanCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    int MY_PERMISSIONS_REQUEST_CAMERA=0;
    private String text;
    private int id_user,scope;

    ZXingScannerView scannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        id_user=this.getIntent().getIntExtra("user_id", 0);
        scope=this.getIntent().getIntExtra("scope", 0);
    }

    @Override
    public void handleResult(Result result) {
        text=result.getText();
        String[] parts = text.split("-");
        String cod,name=null,price=null,quantity=null,description=null,expiration_date=null;
        if(parts.length==1) {
            cod = parts[0];
        }else {
             cod = parts[0];
             name = parts[1];
             price = parts[2];
             quantity = parts[3];
             expiration_date=parts[4];
             description = parts[5];
        }

        if(scope==1){
            Intent it = new Intent(ScanCodeActivity.this, DetailsSale.class);
            it.putExtra("codScan", cod);
            it.putExtra("user_id", id_user);
            startActivity(it);
        }else{

            Intent it = new Intent(ScanCodeActivity.this, DetailsStock.class);
            it.putExtra("codScan", cod);
            it.putExtra("nameScan", name);
            it.putExtra("priceScan", price);
            it.putExtra("quantityScan", quantity);
            it.putExtra("expiration_dateScan", expiration_date);
            it.putExtra("descriptionScan", description);
            it.putExtra("user_id", id_user);
            startActivity(it);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}
