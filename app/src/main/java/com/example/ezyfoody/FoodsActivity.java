package com.example.ezyfoody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class FoodsActivity extends AppCompatActivity {

    private Context context;
    private DatabaseReference database;
    public ArrayList<Item> foods;

    private RecyclerView recyclerView;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foods);

        context = this;
        database = FirebaseDatabase.getInstance().getReference();
        fillData();


    }

    public void redirectToMyOrderActivity(View view){
        Intent intent = new Intent(this, MyOrderActivity.class);
        startActivity(intent);
    }

    private void fillData(){
        foods = new ArrayList<>();
        database.child("foods").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    foods.add(new Item(data.child("name").getValue().toString(), Integer.parseInt(data.child("price").getValue().toString())));

                    recyclerView = (RecyclerView) findViewById(R.id.foods_recyclerview);
                    adapter = new ItemAdapter(foods);
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