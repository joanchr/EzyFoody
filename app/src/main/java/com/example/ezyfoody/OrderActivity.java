package com.example.ezyfoody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ezyfoody.adapters.MyOrderItemAdapter;
import com.example.ezyfoody.models.Item;
import com.example.ezyfoody.models.OrderItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderActivity extends AppCompatActivity {

    private Context context;
    private DatabaseReference database;

    private Item item;
    private TextView itemName;
    private TextView itemPrice;
    private EditText itemQuantity;
    private Integer exisitingCartPosition;
    private long itemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        context = this;
        database = FirebaseDatabase.getInstance().getReference();

        itemName =  findViewById(R.id.order_text_view_item_name);
        itemPrice =  findViewById(R.id.order_text_view_item_price);
        itemQuantity =  findViewById(R.id.order_edit_text_item_quantity);

        item = getIntent().getExtras().getParcelable("Item");
        itemName.setText(item.getName());
        itemPrice.setText(item.getPrice().toString());


        itemQuantity.setText("1");
        checkExistingOnCart(item.getName());
    }

    private void checkExistingOnCart(final String name){

        database.child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                itemCount = 0;
                for (DataSnapshot data: snapshot.getChildren()) {
                    if(name.equals(data.child("name").getValue().toString())){
                        itemQuantity.setText(data.child("quantity").getValue().toString());
                        exisitingCartPosition = Integer.parseInt(data.getKey());
                    }
                    itemCount = Integer.parseInt(data.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        for(int i = 0; i < OrderItem.cart.size(); i++){
//            if(name.equals(OrderItem.cart.get(i).getName())){
//                itemQuantity.setText(OrderItem.cart.get(i).getQuantity().toString());
//                exisitingCartPosition = i;
//            }
//        }
    }

    private void addToCart(){
        if(exisitingCartPosition != null){
            database.child("cart").child(exisitingCartPosition.toString()).child("quantity").setValue(Integer.parseInt(itemQuantity.getText().toString()));
//            OrderItem.cart.get(exisitingCartPosition).setQuantity( Integer.parseInt(itemQuantity.getText().toString()) );
        }else{
            long newId = itemCount+1;
            database.child("cart").child(String.valueOf(newId)).child("quantity").setValue(Integer.parseInt(itemQuantity.getText().toString()));
            database.child("cart").child(String.valueOf(newId)).child("name").setValue(item.getName());
            database.child("cart").child(String.valueOf(newId)).child("price").setValue(item.getPrice());
//            OrderItem.cart.add(new OrderItem(item.getName(), item.getPrice(), Integer.parseInt(itemQuantity.getText().toString()) ));
        }
    }

    public void redirectToMyOrderActivity(View view){
        addToCart();
        Intent intent = new Intent(this, MyOrderActivity.class);
        startActivity(intent);
        finish();
    }

    public void redirectToPreviousActivity(View view){
        addToCart();
        finish();
    }

}