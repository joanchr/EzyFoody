package com.example.ezyfoody.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezyfoody.R;
import com.example.ezyfoody.models.OrderItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyOrderItemAdapter extends RecyclerView.Adapter<MyOrderItemAdapter.MyOrderItemViewHolder> {

    private DatabaseReference database;
    private ArrayList<OrderItem> items;

    public MyOrderItemAdapter(ArrayList<OrderItem> items) {
        this.items = items;
    }

    @Override
    public MyOrderItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        database = FirebaseDatabase.getInstance().getReference();

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_my_order_item, parent, false);
        return new MyOrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyOrderItemViewHolder holder, final int position) {
        holder.itemName.setText(items.get(position).getName());
        holder.itemPrice.setText("Rp ".concat(items.get(position).getPrice().toString()));
        holder.itemQuantity.setText(items.get(position).getQuantity().toString());
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database.child("cart").child(items.get(position).getId().toString()).removeValue();

                items.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, items.size());


                TextView totalTxt = view.getRootView().findViewById(R.id.my_order_text_view_total_money);
                Integer total = 0;
                for (OrderItem orderItem : items) {
                    total += (orderItem.getPrice() * orderItem.getQuantity());
                }
                totalTxt.setText(total.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        else
            return 0;
    }

    public class MyOrderItemViewHolder extends RecyclerView.ViewHolder{
        private TextView itemName, itemPrice, itemQuantity;
        private Button deleteBtn;
        public MyOrderItemViewHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.text_view_item_name);
            itemPrice = (TextView) itemView.findViewById(R.id.text_view_item_price);
            itemQuantity = (TextView) itemView.findViewById(R.id.text_view_item_quantity);
            deleteBtn = (Button) itemView.findViewById(R.id.button_delete_item);
        }
    }
}
