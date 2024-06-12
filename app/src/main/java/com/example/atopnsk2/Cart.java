package com.example.atopnsk2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cart extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CartAdapter mAdapter;
    private List<Product> mCartList;
    private DatabaseReference mCartRef;
    private DatabaseReference mOrdersRef;

    private TextView mTotalPriceTextView;
    private Button mPlaceOrderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mRecyclerView = findViewById(R.id.recyclerViewCart);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTotalPriceTextView = findViewById(R.id.textViewTotalPrice);
        mPlaceOrderButton = findViewById(R.id.buttonPlaceOrder);

        mCartList = new ArrayList<>();
        mCartRef = FirebaseDatabase.getInstance().getReference("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mOrdersRef = FirebaseDatabase.getInstance().getReference("orders");

        mCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mCartList.clear();
                double totalPrice = 0.0;
                for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                    Product product = cartSnapshot.getValue(Product.class);
                    if (product != null) {
                        mCartList.add(product);
                        totalPrice += Double.parseDouble(product.getPrice());
                    }
                }
                mAdapter.notifyDataSetChanged();
                mTotalPriceTextView.setText("Общая стоимость: " + String.format("%.2f", totalPrice));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибки
            }
        });

        mAdapter = new CartAdapter(this, mCartList);
        mRecyclerView.setAdapter(mAdapter);

        mPlaceOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });
    }

    private void placeOrder() {
        String orderId = UUID.randomUUID().toString();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        double totalPrice = 0.0;

        for (Product product : mCartList) {
            totalPrice += Double.parseDouble(product.getPrice());
        }

        Order order = new Order(orderId, userId, mCartList, totalPrice, "Новый");

        mOrdersRef.child(orderId).setValue(order).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Cart.this, "Заказ оформлен!", Toast.LENGTH_SHORT).show();
                mCartRef.removeValue();
            } else {
                Toast.makeText(Cart.this, "Не удалось оформить заказ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}