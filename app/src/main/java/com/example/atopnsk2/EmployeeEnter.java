package com.example.atopnsk2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployeeEnter extends AppCompatActivity {

    private EditText editTextEmployeeEmail;
    private EditText editTextEmployeePassword;
    private Button buttonEmployeeLogin;
    private TextView textViewForgotPassword;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_enter);

        editTextEmployeeEmail = findViewById(R.id.editTextEmployeeEmail);
        editTextEmployeePassword = findViewById(R.id.editTextEmployeePassword);
        buttonEmployeeLogin = findViewById(R.id.buttonEmployeeLogin);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Workers");

        buttonEmployeeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEmployee();
            }
        });


    }

    private void loginEmployee() {
        String email = editTextEmployeeEmail.getText().toString().trim();
        String password = editTextEmployeePassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Введите Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
            return;
        }

        mDatabase.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot workerSnapshot : dataSnapshot.getChildren()) {
                        EmployeeProfile worker = workerSnapshot.getValue(EmployeeProfile.class);
                        if (worker != null && worker.getPassword().equals(password)) {
                            if (worker.isDirector()) {
                                Intent intent = new Intent(EmployeeEnter.this, EmployeeDashboardActivity.class);
                                intent.putExtra("isDirector", true);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(EmployeeEnter.this, EmployeeDashboardActivity.class);
                                intent.putExtra("isDirector", false);
                                startActivity(intent);
                            }
                            finish();
                        } else {
                            Toast.makeText(EmployeeEnter.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(EmployeeEnter.this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EmployeeEnter.this, "Ошибка базы данных", Toast.LENGTH_SHORT).show();
            }
        });
    }
}