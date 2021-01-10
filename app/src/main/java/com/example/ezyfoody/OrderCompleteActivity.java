package com.example.ezyfoody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ezyfoody.adapters.CompleteOrderItemAdapter;
import com.example.ezyfoody.adapters.MyOrderItemAdapter;
import com.example.ezyfoody.models.OrderItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class OrderCompleteActivity extends AppCompatActivity {

    private Context context;
    private DatabaseReference database;
    private ArrayList<OrderItem> cart;

    private RecyclerView recyclerView;
    private CompleteOrderItemAdapter adapter;

    private String restaurantLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);

        restaurantLocation = getIntent().getStringExtra("restaurant_location");

        context = this;
        database = FirebaseDatabase.getInstance().getReference();
        cart = new ArrayList<>();
        database.child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<OrderItem> transactionItems = new ArrayList<>();
                Integer total = 0;
                for (DataSnapshot data: snapshot.getChildren()) {
                    cart.add(
                            new OrderItem(
                                    data.child("name").getValue().toString(),
                                    Integer.parseInt(data.child("price").getValue().toString()),
                                    Integer.parseInt(data.child("quantity").getValue().toString()),
                                    Integer.parseInt(data.getKey())
                            )
                    );
                    transactionItems.add(
                            new OrderItem(
                                    data.child("name").getValue().toString(),
                                    Integer.parseInt(data.child("price").getValue().toString()),
                                    Integer.parseInt(data.child("quantity").getValue().toString())
                            )
                    );
                    reduceQuantity(data.child("type").getValue().toString(),data.child("id").getValue().toString(),Integer.parseInt(data.child("quantity").getValue().toString()));

                    recyclerView = (RecyclerView) findViewById(R.id.order_complete_recyclerview);
                    adapter = new CompleteOrderItemAdapter(cart);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);

                    TextView totalTxt = findViewById(R.id.order_complete_text_view_total_money);

                    total = 0;
                    for (OrderItem orderItem : cart) {
                        total += (orderItem.getPrice() * orderItem.getQuantity());
                    }
                    totalTxt.setText(total.toString());
                }


                insertToTransactions(transactionItems, total);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void insertToTransactions(final ArrayList<OrderItem> transactionItems, final Integer totalPrice){
        database.child("transactions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long dataCount = snapshot.getChildrenCount();
                dataCount++;
                database.child("transactions").child(String.valueOf(dataCount)).child("address").setValue(restaurantLocation);
                database.child("transactions").child(String.valueOf(dataCount)).child("date").setValue(new Date().toString());
                database.child("transactions").child(String.valueOf(dataCount)).child("total_items").setValue(transactionItems.size());
                database.child("transactions").child(String.valueOf(dataCount)).child("total_price").setValue(totalPrice);
                Integer counter = 0;
                for (OrderItem oi: transactionItems) {
                    counter++;
                    database.child("transactions").child(String.valueOf(dataCount)).child("items").child(counter.toString()).child("name").setValue(oi.getName());
                    database.child("transactions").child(String.valueOf(dataCount)).child("items").child(counter.toString()).child("price").setValue(oi.getPrice());
                    database.child("transactions").child(String.valueOf(dataCount)).child("items").child(counter.toString()).child("quantity").setValue(oi.getQuantity());
                }
                database.child("cart").removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void reduceQuantity(String type, final String id, final Integer quantity){
        if(type.equals("food")){
            database.child("foods").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer old = Integer.parseInt(snapshot.child("stock").getValue().toString());
                    database.child("foods").child(id).child("stock").setValue(old - quantity);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(type.equals("drink")){
            database.child("drinks").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer old = Integer.parseInt(snapshot.child("stock").getValue().toString());
                    database.child("drinks").child(id).child("stock").setValue(old - quantity);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(type.equals("snack")){
            database.child("snacks").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer old = Integer.parseInt(snapshot.child("stock").getValue().toString());
                    database.child("snacks").child(id).child("stock").setValue(old - quantity);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void redirectToMainActivity(View view){

//        OrderItem.cart.clear();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}