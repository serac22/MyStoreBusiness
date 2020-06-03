package com.example.mystorebusiness.account.ui.employees;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mystorebusiness.R;
import com.example.mystorebusiness.adapters.AdapterEmployee;
import com.example.mystorebusiness.data.Db_Employee;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class EmployeesFragment extends Fragment {


    private RecyclerView rvEmployee;
    private AdapterEmployee eAdapter;
    private Db_Employee db;
    private ArrayList<Employee> dataEmployees;
    private int id_user;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_employees, container, false);

        rvEmployee = v.findViewById(R.id.rvEmployee);
        rvEmployee.setHasFixedSize(true);

        id_user = Integer.parseInt(Objects.requireNonNull(requireActivity().getIntent().getStringExtra("user_id")));
        FloatingActionButton fab = v.findViewById(R.id.fab_employee);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(), DetailsEmployees.class);
                it.putExtra("employee_id", "add");
                it.putExtra("user_id", id_user);
                startActivity(it);
            }
        });

        final SwipeRefreshLayout pullToRefresh = v.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataEmployees= db.getAllEmployees("",id_user);

                eAdapter = new AdapterEmployee(getActivity(),dataEmployees);
                rvEmployee.setAdapter(eAdapter);
                pullToRefresh.setRefreshing(false);
            }
        });

        db = new Db_Employee(getActivity());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvEmployee.setLayoutManager(mLayoutManager);

        dataEmployees= db.getAllEmployees("",id_user);

        eAdapter = new AdapterEmployee(getActivity(),dataEmployees);
        rvEmployee.setAdapter(eAdapter);

        return v;
    }


}