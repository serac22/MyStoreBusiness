package com.example.mystorebusiness.account.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mystorebusiness.R;
import com.example.mystorebusiness.data.Db_Users;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private int id_user;
    private Db_Users db;
    private ImageView profile,edit;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int PICK_IMAGE_REQUEST = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        id_user = Integer.parseInt(Objects.requireNonNull(requireActivity().getIntent().getStringExtra("user_id")));
        TextView name = root.findViewById(R.id.name_cont);
        TextView username = root.findViewById(R.id.username_cont);
        TextView mobile = root.findViewById((R.id.mobile_cont));
        TextView email = root.findViewById(R.id.email_cont);
        TextView address = root.findViewById(R.id.address_cont);
        TextView birth = root.findViewById(R.id.birth_cont);
        edit=root.findViewById(R.id.image_edit);
        profile = root.findViewById(R.id.image_cont);

        db = new Db_Users(getActivity());
        Cursor result = db.getUsers(id_user);
        if (result.getCount() != 0) {
            while (result.moveToNext()) {
                username.setText(result.getString(1));
                email.setText(result.getString(2));
                mobile.setText(result.getString(4));
                name.setText(result.getString(5));
                birth.setText(result.getString(6));
                address.setText(result.getString(7));
                profile.setImageURI(Uri.parse(result.getString(8)));
            }
        }

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                tryToOpenImageSelector();
                            }
                        };
                // Show dialog if you do not want to change the image
                showUnsavedChangesDialog(discardButtonClickListener);

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(), ContEdit.class);
                it.putExtra("user_id", id_user);
                startActivity(it);
            }
        });

        return root;
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
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

    public void tryToOpenImageSelector() {
        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
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
                Uri actualUri = resultData.getData();
                String uriImage = Objects.requireNonNull(actualUri).toString();
                profile.setImageURI(actualUri);
                db.updateImageProfile(id_user, uriImage);
                profile.invalidate();
            }
        }
    }


}

