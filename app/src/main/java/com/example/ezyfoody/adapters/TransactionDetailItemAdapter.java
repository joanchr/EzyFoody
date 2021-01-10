package com.example.ezyfoody.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ezyfoody.R;
import com.example.ezyfoody.models.OrderItem;
import com.example.ezyfoody.models.Transaction;

import java.util.ArrayList;

public class TransactionDetailItemAdapter extends RecyclerView.Adapter<TransactionDetailItemAdapter.CompleteOrderItemViewHolder>{

    private ArrayList<OrderItem> items;

    public TransactionDetailItemAdapter(ArrayList<OrderItem> items) {
        this.items = items;
    }

    @Override
    public TransactionDetailItemAdapter.CompleteOrderItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_transaction_detail_item, parent, false);
        return new TransactionDetailItemAdapter.CompleteOrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionDetailItemAdapter.CompleteOrderItemViewHolder holder, int position) {
        holder.text_view_item_name.setText(items.get(position).getName());
        holder.text_view_item_price.setText("Rp ".concat(items.get(position).getPrice().toString()));
        holder.text_view_item_quantity.setText(items.get(position).getQuantity().toString());
    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        else
            return 0;
    }

    public class CompleteOrderItemViewHolder extends RecyclerView.ViewHolder{
        private TextView text_view_item_name, text_view_item_price, text_view_item_quantity;
        public CompleteOrderItemViewHolder(View itemView) {
            super(itemView);
            text_view_item_name = (TextView) itemView.findViewById(R.id.text_view_item_name);
            text_view_item_price = (TextView) itemView.findViewById(R.id.text_view_item_price);
            text_view_item_quantity = (TextView) itemView.findViewById(R.id.text_view_item_quantity);
        }
    }

}
