package com.example.ezyfoody.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ezyfoody.R;
import com.example.ezyfoody.models.OrderItem;

import java.util.ArrayList;

public class CompleteOrderItemAdapter extends RecyclerView.Adapter<CompleteOrderItemAdapter.CompleteOrderItemViewHolder>{

    private ArrayList<OrderItem> items;

    public CompleteOrderItemAdapter(ArrayList<OrderItem> items) {
        this.items = items;
    }

    @Override
    public CompleteOrderItemAdapter.CompleteOrderItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recylerview_complete_order_item, parent, false);
        return new CompleteOrderItemAdapter.CompleteOrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompleteOrderItemAdapter.CompleteOrderItemViewHolder holder, int position) {
        holder.itemName.setText(items.get(position).getName());
        holder.itemPrice.setText("Rp ".concat(items.get(position).getPrice().toString()));
        holder.itemQuantity.setText(items.get(position).getQuantity().toString());
    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        else
            return 0;
    }

    public class CompleteOrderItemViewHolder extends RecyclerView.ViewHolder{
        private TextView itemName, itemPrice, itemQuantity;
        public CompleteOrderItemViewHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.text_view_item_name);
            itemPrice = (TextView) itemView.findViewById(R.id.text_view_item_price);
            itemQuantity = (TextView) itemView.findViewById(R.id.text_view_item_quantity);
        }
    }

}
