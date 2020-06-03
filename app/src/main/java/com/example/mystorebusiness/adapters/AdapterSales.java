package com.example.mystorebusiness.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystorebusiness.R;
import com.example.mystorebusiness.account.ui.sales.SaleItem;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class AdapterSales extends RecyclerView.Adapter<AdapterSales.ViewHolder> {
    private ArrayList<SaleItem> salesList;
    private ArrayList<SaleItem> salesListFull;

    public AdapterSales(ArrayList<SaleItem>  stockList, Context context) {
        this.salesList = stockList;
        salesListFull = new ArrayList<>(stockList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_item_sale,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.cod.setText(salesList.get(i).getCod());
        viewHolder.data.setText(salesList.get(i).getData());
        viewHolder.total.setText(String.valueOf(salesList.get(i).getTotal()));
        viewHolder.quantity.setText(String.valueOf(salesList.get(i).getQuantity()));
    }


    @Override
    public int getItemCount() {
        return salesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cod,data,quantity,total;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cod = itemView.findViewById(R.id.tvCod);
            data=itemView.findViewById(R.id.tvDataSale);
            total = itemView.findViewById(R.id.tvTotalSale);
            quantity = itemView.findViewById(R.id.tvQuantity);

        }

    }



    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<SaleItem> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(salesListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (SaleItem item : salesListFull) {
                    if (item.getCod().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            salesList.clear();
            //noinspection unchecked
            salesList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
