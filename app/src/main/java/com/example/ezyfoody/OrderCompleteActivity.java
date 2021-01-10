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

public class OrderCompleteActivity extends AppCompatActivity {

    private Context context;
    private DatabaseReference database;
    private ArrayList<OrderItem> cart;

    private RecyclerView recyclerView;
    private CompleteOrderItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);

        context = this;
        database = FirebaseDatabase.getInstance().getReference();
        cart = new ArrayList<>();
        database.child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    cart.add(
                            new OrderItem(
                                    data.child("name").getValue().toString(),
                                    Integer.parseInt(data.child("price").getValue().toString()),
                                    Integer.parseInt(data.child("quantity").getValue().toString()),
                                    Integer.parseInt(data.getKey())
                            )
                    );

                    recyclerView = (RecyclerView) findViewById(R.id.order_complete_recyclerview);
                    adapter = new CompleteOrderItemAdapter(cart);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);

                    TextView totalTxt = findViewById(R.id.order_complete_text_view_total_money);

                    Integer total = 0;
                    for (OrderItem orderItem : cart) {
                        total += (orderItem.getPrice() * orderItem.getQuantity());
                    }
                    totalTxt.setText(total.toString());
                }
                database.child("cart").removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void redirectToMainActivity(View view){

//        OrderItem.cart.clear();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}