package com.example.mystorebusiness.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystorebusiness.R;
import com.example.mystorebusiness.account.ui.employees.DetailsEmployees;
import com.example.mystorebusiness.account.ui.stocks.DetailsStock;
import com.example.mystorebusiness.account.ui.stocks.StockItem;
import com.example.mystorebusiness.account.ui.stocks.UpdateStock;
import com.example.mystorebusiness.data.Db_Employee;
import com.example.mystorebusiness.data.Db_Stocks;

import java.util.ArrayList;
import java.util.List;


public class AdapterStocks extends RecyclerView.Adapter<AdapterStocks.ViewHolder> {
    private ArrayList<StockItem> stocksList;
    private ArrayList<StockItem> stocksListFull;
    private final LayoutInflater mInflater;
    private final Context MyContext;

    public AdapterStocks(Context context,ArrayList<StockItem>  stockList) {
        this.stocksList = stockList;
        this.mInflater = LayoutInflater.from(context);
        stocksListFull = new ArrayList<>(stockList);
        this.MyContext=context;
    }


    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.view_item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.cod.setText(stocksList.get(i).getCod());
        viewHolder.name.setText(stocksList.get(i).getProductName());
        viewHolder.price.setText(stocksList.get(i).getFinal_price());
        viewHolder.quantity.setText(stocksList.get(i).getQuantity());
        viewHolder.image.setImageURI(Uri.parse(stocksList.get(i).getImage()));
    }


    @Override
    public int getItemCount() {
        return stocksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name,price,quantity,cod;
        public ImageView image;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvNameProduct);
            cod=itemView.findViewById(R.id.tvCodProduct);
            price = itemView.findViewById(R.id.tvPriceProduct);
            image = itemView.findViewById(R.id.imgProduct);
            quantity = itemView.findViewById(R.id.tvQuantity);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Db_Stocks db = new Db_Stocks(MyContext);
            Cursor result= db.getProductID(cod.getText().toString());
            if(result.getCount()!=0){

                StringBuilder buffer=new StringBuilder();
                while(result.moveToNext()) {
                    buffer.append(result.getString(0));
                }

                Intent it = new Intent(MyContext, UpdateStock.class);
                it.putExtra("product_id", buffer.toString());
                MyContext.startActivity(it);
            }
        }


    }




    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<StockItem> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(stocksListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (StockItem item : stocksListFull) {
                    if (item.getProductName().toLowerCase().contains(filterPattern)) {
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
            stocksList.clear();
            //noinspection unchecked
            stocksList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };



}


