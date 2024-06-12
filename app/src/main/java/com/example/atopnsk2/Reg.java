package com.example.atopnsk2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;

public class Reg extends AppCompatActivity {
    private Button button_enter;
    private EditText editTextEmail, editTextPassword;
    private TextView textViewLogin;
    private RegViewModel regViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        initView();
        regViewModel = new ViewModelProvider(this).get(RegViewModel.class);
        observeViewModel();
        button_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                regViewModel.signUp(email, password);

            }
        });
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reg.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    private void observeViewModel(){
        regViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null){
                    Toast.makeText(Reg.this, errorMessage, Toast.LENGTH_SHORT).show();
                }

            }
        });
        regViewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null){
                    Intent intent = new Intent(Reg.this, Store.class);
                    startActivity(intent);

                }
            }
        });
    }
    private void initView(){
        button_enter = findViewById(R.id.buttonRegister);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewLogin = findViewById(R.id.textViewLogin);
    }
}