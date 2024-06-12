package com.example.atopnsk2;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Store extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ProductAdapter mAdapter;
    private ImageView imageViewProfile, imageViewHome, imageViewCart;
    private List<Product> mProductList;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    private EditText mSearchEditText;
    private Button mButtonAllProducts;
    private Button mButtonCategorySmallNetwork;
    private Button mButtonCategoryCCTV;
    private Button mButtonCategoryProEquipment;
    private Button mButtonCategoryProtection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        // Инициализация FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Проверка состояния входа
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Пользователь не вошел в систему, переход на экран входа
            Intent loginIntent = new Intent(Store.this, MainActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }
        mSearchEditText = findViewById(R.id.searchView);
        mButtonCategorySmallNetwork = findViewById(R.id.button_small_seti);
        mButtonCategoryCCTV = findViewById(R.id.button_video);
        mButtonAllProducts = findViewById(R.id.buttonAllProducts);
        mButtonCategoryProEquipment = findViewById(R.id.button_prof);
        mButtonCategoryProtection = findViewById(R.id.button_guard);
        imageViewCart = findViewById(R.id.navigation_cart);
        imageViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Store.this, Cart.class);
                startActivity(intent);
            }
        });
        imageViewProfile = findViewById(R.id.navigation_profile);
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Store.this, Profile.class);
                startActivity(intent);
            }
        });

        // Инициализация RecyclerView
        mRecyclerView = findViewById(R.id.popularProductsRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Инициализация списка товаров
        mProductList = new ArrayList<>();

        // Инициализация базы данных Firebase
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Products");

        // Загрузка данных из Firebase
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mProductList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        mProductList.add(product);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Store", "Failed to read value.", databaseError.toException());
                Toast.makeText(Store.this, "Failed to load products.", Toast.LENGTH_SHORT).show();
            }
        });

        // Инициализация адаптера и присоединение его к RecyclerView
        mAdapter = new ProductAdapter(Store.this, mProductList);
        mRecyclerView.setAdapter(mAdapter);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterByName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterByName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        mButtonAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.filterList(mProductList);
            }
        });

        mButtonCategorySmallNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterByCategory("Оборудование для малых сетей");
            }
        });

        mButtonCategoryCCTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterByCategory("Видеонаблюдение");
            }
        });

        mButtonCategoryProEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterByCategory("Профессиональное оборудование");
            }
        });

        mButtonCategoryProtection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterByCategory("Защита и элементы питания");
            }
        });
    }

    private void filterByName(String text) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : mProductList) {
            if (product.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(product);
            }
        }
        mAdapter.filterList(filteredList);
    }

    private void filterByCategory(String category) {
        try {
            List<Product> filteredList = new ArrayList<>();
            for (Product product : mProductList) {
                if (product.getCategories() != null && product.getCategories().contains(category)) {
                    filteredList.add(product);
                }
            }
            mAdapter.filterList(filteredList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}