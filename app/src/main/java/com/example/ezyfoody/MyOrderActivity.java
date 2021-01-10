package com.example.ezyfoody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ezyfoody.adapters.ItemAdapter;
import com.example.ezyfoody.adapters.MyOrderItemAdapter;
import com.example.ezyfoody.models.Item;
import com.example.ezyfoody.models.OrderItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyOrderActivity extends AppCompatActivity {

    private Context context;
    private DatabaseReference database;
    private ArrayList<OrderItem> cart;

    private RecyclerView recyclerView;
    private MyOrderItemAdapter adapter;
    private TextView totalTxt;
    private TextView restaurantLocationTxt;

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        context = this;
        database = FirebaseDatabase.getInstance().getReference();
        cart = new ArrayList<>();
        totalTxt = findViewById(R.id.my_order_text_view_total_money);
        restaurantLocationTxt = findViewById(R.id.restaurant_location);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //get my order data
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

                    recyclerView = (RecyclerView) findViewById(R.id.my_order_recyclerview);
                    adapter = new MyOrderItemAdapter(cart);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);

                    Integer total = 0;
                    for (OrderItem orderItem : cart ) {
                        total += (orderItem.getPrice() * orderItem.getQuantity());
                    }
                    totalTxt.setText(total.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        locationInit();
    }

    public void redirectToOrderCompleteActivity(View view){
        if(Integer.parseInt(totalTxt.getText().toString()) == 0){
            Toast toast = Toast.makeText(this, getString(R.string.no_order), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        database.child("user").child("money").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer balance = Integer.parseInt(snapshot.getValue().toString());
                if(Integer.parseInt(totalTxt.getText().toString()) > balance){
                    Toast.makeText(context, "Balance is not enough!", Toast.LENGTH_SHORT).show();
                    return;
                }
                database.child("user").child("money").setValue(balance - Integer.parseInt(totalTxt.getText().toString()));
                redirect();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void redirect(){
        Intent intent = new Intent(this, OrderCompleteActivity.class);
        intent.putExtra("restaurant_location", restaurantLocationTxt.getText().toString());
        startActivity(intent);
    }

    public void redirectToMaps(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                final String returnString = data.getStringExtra("restaurant");
                restaurantLocationTxt.setText(returnString);
            }
        }
    }

    public void locationInit(){

        //request permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                //get current location
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){
                            getClosestRestaurant(location.getLatitude(), location.getLongitude());
                        }
                    }
                });
            }else{
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }
    }

    public void getClosestRestaurant(final Double currentlat, final Double currentlong){
        //get closest restaurant
        database.child("restaurants").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float minDistance = 0;
                Integer closestRestaurantId = 1;
                for (DataSnapshot data: snapshot.getChildren()) {
                    LatLng marker = new LatLng(Double.parseDouble(data.child("latitude").getValue().toString()), Double.parseDouble(data.child("longitude").getValue().toString()));

                    float[] distance = new float[1];
                    Location.distanceBetween(currentlat, currentlong, marker.latitude, marker.longitude, distance);
                    if(distance[0] < minDistance){
                        minDistance=distance[0];
                        closestRestaurantId = Integer.parseInt(data.getKey());
                    }
                }
                database.child("user").child("preferred_restaurant").setValue(snapshot.child(closestRestaurantId.toString()).child("name").getValue().toString());
                restaurantLocationTxt.setText(snapshot.child(closestRestaurantId.toString()).child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}