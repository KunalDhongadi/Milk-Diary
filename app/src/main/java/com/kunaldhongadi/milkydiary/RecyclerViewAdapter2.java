package com.kunaldhongadi.milkydiary;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.ViewHolder> {

    private List<Bill> bills;
    private LayoutInflater layoutInflater;

    public RecyclerViewAdapter2(Context context, List<Bill> bills) {
        this.layoutInflater = LayoutInflater.from(context);
        this.bills = bills;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.bill_list_view, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        Bill bill = bills.get(position);
        holder.textMonth.setText(bill.getMonth());
        holder.textCount.setText(bill.getCount() + " day(s)");
        holder.textTotalCost.setText("₹" + bill.getTotalCost());
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textMonth,textCount,textTotalCost;

        ViewHolder(View itemView) {
            super(itemView);
            this.textMonth = itemView.findViewById(R.id.bill_month);
            this.textCount = itemView.findViewById(R.id.bill_milk_count);
            this.textTotalCost = itemView.findViewById(R.id.bill_total_cost);
        }

    }






}
