package com.example.mystorebusiness.account.ui.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystorebusiness.R;

import java.util.List;
import java.util.Objects;

public class StatisticsFragment extends Fragment {
    private int id_user;
    public StatisticsFragment() {
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

        View v = inflater.inflate(R.layout.fragment_statistics, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        id_user = Integer.parseInt(Objects.requireNonNull(requireActivity().getIntent().getStringExtra("user_id")));
        List<Chart> chartList = Chart.createChartList(getResources());
        final ChartsAdapter adapter = new ChartsAdapter(getActivity(), chartList,id_user);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final SearchView searchView = v.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        return v;
    }



}
