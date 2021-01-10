package com.example.ezyfoody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference database;
    private TextView amountEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance().getReference();
        amountEt = findViewById(R.id.balance);
        database.child("user").child("money").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer old = Integer.parseInt(snapshot.getValue().toString());
                amountEt.setText("Balance: " + old);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void redirectToDrinkActivity(View view){
        Intent intent = new Intent(this, DrinksActivity.class);
        startActivity(intent);
    }
    public void redirectToSnacksActivity(View view){
        Intent intent = new Intent(this, SnacksActivity.class);
        startActivity(intent);
    }
    public void redirectToFoodsActivity(View view){
        Intent intent = new Intent(this, FoodsActivity.class);
        startActivity(intent);
    }
    public void redirectToTopUpActivity(View view){
        Intent intent = new Intent(this, TopUpActivity.class);
        startActivity(intent);
    }
    public void redirectToMyOrderActivity(View view){
        Intent intent = new Intent(this, MyOrderActivity.class);
        startActivity(intent);
    }
    public void redirectToTransactionsActivity(View view){
        Intent intent = new Intent(this, TransactionActivity.class);
        startActivity(intent);
    }
}