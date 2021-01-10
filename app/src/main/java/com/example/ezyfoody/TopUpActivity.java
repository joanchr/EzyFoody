package com.example.ezyfoody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ezyfoody.adapters.CompleteOrderItemAdapter;
import com.example.ezyfoody.models.OrderItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TopUpActivity extends AppCompatActivity {

    private Context context;
    private DatabaseReference database;
    private EditText amountEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        database = FirebaseDatabase.getInstance().getReference();
        context = this;
        amountEt = findViewById(R.id.top_up_edit_text_amount);
    }
    public void redirectToMainActivity(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void topUp(final View view){
        if(Integer.parseInt(amountEt.getText().toString()) > 2000000){
            Toast.makeText(context, "Maximum top up is Rp 2000000", Toast.LENGTH_SHORT).show();
            return;
        }

        database.child("user").child("money").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer old = Integer.parseInt(snapshot.getValue().toString());
                database.child("user").child("money").setValue(old+Integer.parseInt(amountEt.getText().toString()));
                redirectToMainActivity(view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}