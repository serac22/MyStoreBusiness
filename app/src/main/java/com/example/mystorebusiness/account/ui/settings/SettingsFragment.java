package com.example.mystorebusiness.account.ui.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.mystorebusiness.R;
import com.example.mystorebusiness.data.Db_Employee;
import com.example.mystorebusiness.data.Db_Sales;
import com.example.mystorebusiness.data.Db_Stocks;
import com.example.mystorebusiness.data.Db_Users;

import java.util.Objects;

public class SettingsFragment extends Fragment {
    private ImageView delete;
    private Db_Users db;
    private Db_Sales db_s;
    private Db_Stocks db_st;
    private Db_Employee db__e;
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        delete=v.findViewById(R.id.image_delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db = new Db_Users(getActivity());
                                db.deleteUser(Integer.parseInt(Objects.requireNonNull(requireActivity().getIntent().getStringExtra("user_id"))));
                                db_s.deleteSale(Integer.parseInt(Objects.requireNonNull(requireActivity().getIntent().getStringExtra("user_id"))));
                                db_st.deleteStocks(Integer.parseInt(Objects.requireNonNull(requireActivity().getIntent().getStringExtra("user_id"))));
                                db__e.deleteEmployees(Integer.parseInt(Objects.requireNonNull(requireActivity().getIntent().getStringExtra("user_id"))));
                                getActivity().finish();
                                System.exit(0);
                            }
                        };
                // Show dialog if you do not want to exit
                showUnsavedChangesDialog(discardButtonClickListener);

            }
        });
        return v;
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.do_delete_cont);
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