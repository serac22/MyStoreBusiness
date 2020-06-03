package com.example.mystorebusiness.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystorebusiness.R;
import com.example.mystorebusiness.account.ui.employees.DetailsEmployees;
import com.example.mystorebusiness.account.ui.employees.Employee;
import com.example.mystorebusiness.data.Db_Employee;

import java.util.ArrayList;

public class AdapterEmployee extends RecyclerView.Adapter<AdapterEmployee.ViewHolder> {

    private final ArrayList<Employee> dataEmployees;
    private final LayoutInflater mInflater;
    private final Context MyContext;

    // data is passed into the constructor
    public AdapterEmployee(Context context, ArrayList<Employee> dataEmployees) {
        this.mInflater = LayoutInflater.from(context);
        this.MyContext=context;
        this.dataEmployees = dataEmployees;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.view_item_employee, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.myTextView.setText(dataEmployees.get(position).getName());
        holder.image.setImageURI(Uri.parse(dataEmployees.get(position).getImage()));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return dataEmployees.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView myTextView;
        private final ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvNameEmployee);
            image = itemView.findViewById(R.id.imgEmployee);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Db_Employee db = new Db_Employee(MyContext);
            Cursor result= db.getEmployeeID(myTextView.getText().toString());
            if(result.getCount()!=0){

                StringBuilder buffer=new StringBuilder();
                while(result.moveToNext()) {
                    buffer.append(result.getString(0));
                }

                Intent it = new Intent(MyContext, DetailsEmployees.class);
                it.putExtra("employee_id", buffer.toString());
                it.putExtra("user_id", "update");
                MyContext.startActivity(it);
            }
        }
    }



}
