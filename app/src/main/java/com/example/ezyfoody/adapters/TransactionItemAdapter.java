package com.example.ezyfoody.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezyfoody.R;
import com.example.ezyfoody.models.OrderItem;
import com.example.ezyfoody.models.Transaction;

import java.util.ArrayList;

public class TransactionItemAdapter extends RecyclerView.Adapter<TransactionItemAdapter.CompleteOrderItemViewHolder>{

    private ArrayList<Transaction> items;

    public TransactionItemAdapter(ArrayList<Transaction> items) {
        this.items = items;
    }

    @Override
    public TransactionItemAdapter.CompleteOrderItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_transaction_item, parent, false);
        return new TransactionItemAdapter.CompleteOrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionItemAdapter.CompleteOrderItemViewHolder holder, int position) {
        holder.text_view_purchase_date.setText(items.get(position).getDate());
        holder.text_view_total_price.setText("Rp ".concat(items.get(position).getTotalPrice().toString()));
        holder.text_view_total_items.setText(items.get(position).getTotalItems().toString());
        holder.text_view_address.setText(items.get(position).getAddress());

        holder.recyclerView.setAdapter(new TransactionDetailItemAdapter(items.get(position).getItems()));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.recyclerView.getContext()));
    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        else
            return 0;
    }

    public class CompleteOrderItemViewHolder extends RecyclerView.ViewHolder{
        private TextView text_view_purchase_date, text_view_total_price, text_view_total_items, text_view_address;
        private RecyclerView recyclerView;
        public CompleteOrderItemViewHolder(View itemView) {
            super(itemView);
            text_view_purchase_date = (TextView) itemView.findViewById(R.id.text_view_purchase_date);
            text_view_total_price = (TextView) itemView.findViewById(R.id.text_view_total_price);
            text_view_total_items = (TextView) itemView.findViewById(R.id.text_view_total_items);
            text_view_address = (TextView) itemView.findViewById(R.id.text_view_address);

            recyclerView = (RecyclerView) itemView.findViewById(R.id.transaction_details_recyclerview);
        }
    }

}
