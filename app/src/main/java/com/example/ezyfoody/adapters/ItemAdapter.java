package com.example.ezyfoody.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezyfoody.OrderActivity;
import com.example.ezyfoody.R;
import com.example.ezyfoody.models.Item;
import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private ArrayList<Item> items;

    public ItemAdapter(ArrayList<Item> items) {
        this.items = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        holder.itemName.setText(items.get(position).getName());
        holder.itemPrice.setText("Rp ".concat(items.get(position).getPrice().toString()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), OrderActivity.class);
                intent.putExtra("Item", items.get(position));
                view.getContext().startActivity(intent);
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

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView itemName, itemPrice;
        public ItemViewHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.text_view_item_name);
            itemPrice = (TextView) itemView.findViewById(R.id.text_view_item_price);
        }
    }
}
