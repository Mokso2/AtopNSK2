package com.example.atopnsk2;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EmployeeDashboardActivity extends AppCompatActivity {

    private Button buttonOrders;
    private Button buttonOrderHistory;
    private Button buttonAddProduct;
    private Button buttonAddEmployee;
    private boolean isDirector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);

        buttonOrders = findViewById(R.id.buttonOrders);
        buttonOrderHistory = findViewById(R.id.buttonOrderHistory);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);
        buttonAddEmployee = findViewById(R.id.buttonAddEmployee);

        isDirector = getIntent().getBooleanExtra("isDirector", false);

        buttonOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход на экран "Заказы"
                Intent intent = new Intent(EmployeeDashboardActivity.this, OrdersActivity.class);
                startActivity(intent);
            }
        });

        buttonOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход на экран "История заказов"
                Intent intent = new Intent(EmployeeDashboardActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
            }
        });

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход на экран "Добавить товар"
                Intent intent = new Intent(EmployeeDashboardActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        buttonAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход на экран "Добавить сотрудника"
                if (isDirector) {
                    Intent intent = new Intent(EmployeeDashboardActivity.this, AddEmployeeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(EmployeeDashboardActivity.this, "У вас нет прав для добавления сотрудников", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}