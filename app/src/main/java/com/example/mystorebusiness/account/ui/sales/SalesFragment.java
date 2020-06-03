package com.example.mystorebusiness.account.ui.sales;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.mystorebusiness.R;
import com.example.mystorebusiness.account.ui.stocks.ScanCodeActivity;
import com.example.mystorebusiness.adapters.AdapterSales;
import com.example.mystorebusiness.data.Db_Sales;

import java.util.ArrayList;
import java.util.Objects;

public class SalesFragment extends Fragment {
    private RecyclerView rvSales;
    private AdapterSales mAdapter;
    private Db_Sales db;
    private ArrayList<SaleItem> saleList;
    private int id_user;

    public SalesFragment() {
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

        View v = inflater.inflate(R.layout.fragment_sale, container, false);
        rvSales = v.findViewById(R.id.rvSales);
        rvSales.setHasFixedSize(true);

        db = new Db_Sales(getActivity());

        id_user = Integer.parseInt(Objects.requireNonNull(requireActivity().getIntent().getStringExtra("user_id")));
        final SwipeRefreshLayout pullToRefresh = v.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                saleList = db.getAllItems("",id_user);

                mAdapter = new AdapterSales(saleList,getActivity());
                rvSales.setAdapter(mAdapter);
                pullToRefresh.setRefreshing(false);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvSales.setLayoutManager(layoutManager);

        saleList = db.getAllItems("",id_user);

        mAdapter = new AdapterSales(saleList,getActivity());
        rvSales.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cont_sales, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_scan:

                Intent it = new Intent(getActivity(), ScanCodeActivity.class);
                it.putExtra("user_id", id_user);
                it.putExtra("scope", 1);
                startActivity(it);

                return true;
            case R.id.action_search:
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        mAdapter.getFilter().filter(newText);
                        return false;
                    }
                });
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
