package com.example.atopnsk2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {
    private TextView textViewUserName;
    private Button buttonMyOrders, buttonMyInfo, buttonLogout;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        textViewUserName = findViewById(R.id.textViewUserName);
        buttonMyOrders = findViewById(R.id.buttonMyOrders);

        buttonLogout = findViewById(R.id.buttonLogout);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            textViewUserName.setText(user.getDisplayName());
        }

        buttonMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход на экран Мои заказы
                startActivity(new Intent(Profile.this, UserOrders.class));
            }
        });



        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(Profile.this, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Profile.this, MainActivity.class));
                finish();
            }
        });
    }
}