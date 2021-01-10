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
import android.widget.TextView;

import com.example.ezyfoody.adapters.CompleteOrderItemAdapter;
import com.example.ezyfoody.adapters.TransactionItemAdapter;
import com.example.ezyfoody.models.OrderItem;
import com.example.ezyfoody.models.Transaction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TransactionActivity extends AppCompatActivity {

    private Context context;
    private DatabaseReference database;
    private ArrayList<Transaction> transactions;

    private RecyclerView recyclerView;
    private TransactionItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        context = this;
        database = FirebaseDatabase.getInstance().getReference();
        transactions = new ArrayList<>();
        database.child("transactions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    ArrayList<OrderItem> orderItems = new ArrayList<>();
                    for (DataSnapshot d: data.child("items").getChildren()){
                        orderItems.add(
                                new OrderItem(d.child("name").getValue().toString(),
                                Integer.parseInt(d.child("price").getValue().toString()),
                                Integer.parseInt(d.child("quantity").getValue().toString())
                                )
                        );
                    }
                    transactions.add(
                            new Transaction(
                                    data.child("address").getValue().toString(),
                                    data.child("date").getValue().toString(),
                                    Integer.parseInt(data.child("total_items").getValue().toString()),
                                    Integer.parseInt(data.child("total_price").getValue().toString()),
                                    orderItems
                            )
                    );
                    recyclerView = (RecyclerView) findViewById(R.id.transactions_recyclerview);
                    adapter = new TransactionItemAdapter(transactions);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void redirectToMainActivity(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}