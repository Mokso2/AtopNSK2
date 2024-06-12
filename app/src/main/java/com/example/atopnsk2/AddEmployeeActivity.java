package com.example.atopnsk2;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddEmployeeActivity extends AppCompatActivity {

    private EditText editTextEmployeeEmail;
    private EditText editTextEmployeePassword;
    private CheckBox checkBoxIsDirector;
    private Button buttonAddEmployee;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        editTextEmployeeEmail = findViewById(R.id.editTextEmployeeEmail);
        editTextEmployeePassword = findViewById(R.id.editTextEmployeePassword);
        checkBoxIsDirector = findViewById(R.id.checkBoxIsDirector);
        buttonAddEmployee = findViewById(R.id.buttonAddEmployee);

        mDatabase = FirebaseDatabase.getInstance().getReference("Workers");

        buttonAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmployee();
            }
        });
    }

    private void addEmployee() {
        String email = editTextEmployeeEmail.getText().toString().trim();
        String password = editTextEmployeePassword.getText().toString().trim();
        boolean isDirector = checkBoxIsDirector.isChecked();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Введите Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
            return;
        }

        EmployeeProfile employee = new EmployeeProfile(email, password, isDirector);
        mDatabase.push().setValue(employee);
        Toast.makeText(this, "Сотрудник добавлен", Toast.LENGTH_SHORT).show();
        finish();
    }
}