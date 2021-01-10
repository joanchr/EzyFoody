package com.example.ezyfoody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.ezyfoody.adapters.ItemAdapter;
import com.example.ezyfoody.models.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DrinksActivity extends AppCompatActivity {

    private Context context;
    private DatabaseReference database;
    public ArrayList<Item> drinks;

    private RecyclerView recyclerView;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinks);

        context = this;
        database = FirebaseDatabase.getInstance().getReference();
        fillData();
    }

    public void redirectToMyOrderActivity(View view){
        Intent intent = new Intent(this, MyOrderActivity.class);
        startActivity(intent);
    }

    private void fillData(){
        drinks = new ArrayList<>();
        database.child("drinks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    drinks.add(new Item(
                            data.child("name").getValue().toString(),
                            Integer.parseInt(data.child("price").getValue().toString()),
                            Integer.parseInt(data.child("stock").getValue().toString()),
                            "drink",
                            Integer.parseInt(data.getKey())
                    ));

                    recyclerView = (RecyclerView) findViewById(R.id.drinks_recyclerview);
                    adapter = new ItemAdapter(drinks);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,2);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}