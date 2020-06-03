package com.example.mystorebusiness.account.ui.stocks;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mystorebusiness.R;
import com.example.mystorebusiness.adapters.AdapterStocks;
import com.example.mystorebusiness.data.Db_Stocks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class StocksFragment extends Fragment {
    private RecyclerView rvStocks;
    private AdapterStocks mAdapter;
    private Db_Stocks db;
    private ArrayList<StockItem> dataDrinks;
    private int id_user;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_stocks, container, false);
        rvStocks = v.findViewById(R.id.rvStocks);
        rvStocks.setHasFixedSize(true);
        db = new Db_Stocks(getActivity());

        id_user = Integer.parseInt(Objects.requireNonNull(requireActivity().getIntent().getStringExtra("user_id")));
        final SwipeRefreshLayout pullToRefresh = v.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataDrinks = db.getAllItems("",id_user);
                mAdapter = new AdapterStocks(getActivity(),dataDrinks);
                rvStocks.setAdapter(mAdapter);
                pullToRefresh.setRefreshing(false);
            }
        });

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(), DetailsStock.class);
                it.putExtra("codScan", "");
                it.putExtra("nameScan", "");
                it.putExtra("priceScan", "");
                it.putExtra("quantityScan", "");
                it.putExtra("descriptionScan", "");
                it.putExtra("expiration_dateScan", "");
                it.putExtra("user_id", id_user);
                startActivity(it);


            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvStocks.setLayoutManager(layoutManager);

        dataDrinks = db.getAllItems("",id_user);

        mAdapter = new AdapterStocks(getActivity(),dataDrinks);
        rvStocks.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cont_stock_sort, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_sort_name:
                 dataDrinks = db.getAllItems("name",id_user);

                mAdapter = new AdapterStocks(getActivity(),dataDrinks);
                rvStocks.setAdapter(mAdapter);
                return true;
            case R.id.action_sort_price:
                dataDrinks = db.getAllItems("price",id_user);

                mAdapter = new AdapterStocks(getActivity(),dataDrinks);
                rvStocks.setAdapter(mAdapter);
                return true;
            case R.id.action_sort_quantity:
                 dataDrinks = db.getAllItems("quantity",id_user);

                mAdapter = new AdapterStocks(getActivity(),dataDrinks);
                rvStocks.setAdapter(mAdapter);
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