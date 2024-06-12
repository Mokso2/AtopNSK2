package com.example.atopnsk2;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    private EditText editTextProductName, editTextProductDescription, editTextProductPrice, editTextProductImageUrl;
    private CheckBox checkBoxSmallNetwork, checkBoxCCTV, checkBoxProEquipment, checkBoxProtection;
    private Button buttonAddProduct;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        editTextProductName = findViewById(R.id.editTextProductName);
        editTextProductDescription = findViewById(R.id.editTextProductDescription);
        editTextProductPrice = findViewById(R.id.editTextProductPrice);
        editTextProductImageUrl = findViewById(R.id.editTextProductImageUrl);
        checkBoxSmallNetwork = findViewById(R.id.checkBoxSmallNetwork);
        checkBoxCCTV = findViewById(R.id.checkBoxCCTV);
        checkBoxProEquipment = findViewById(R.id.checkBoxProEquipment);
        checkBoxProtection = findViewById(R.id.checkBoxProtection);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);
        mDatabase = FirebaseDatabase.getInstance().getReference("Products");

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
    }

    private void addProduct() {
        String name = editTextProductName.getText().toString().trim();
        String description = editTextProductDescription.getText().toString().trim();
        String price = editTextProductPrice.getText().toString().trim();
        String imageUrl = editTextProductImageUrl.getText().toString().trim();
        List<String> categories = new ArrayList<>();
        if (checkBoxSmallNetwork.isChecked()) categories.add("Оборудование для малых сетей");
        if (checkBoxCCTV.isChecked()) categories.add("Видеонаблюдение");
        if (checkBoxProEquipment.isChecked()) categories.add("Профессиональное оборудование");
        if (checkBoxProtection.isChecked()) categories.add("Защита и элементы питания");

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(price) && !TextUtils.isEmpty(imageUrl) && !categories.isEmpty()) {
            String id = mDatabase.push().getKey();
            Product product = new Product(id, name, description, price, imageUrl, categories);
            mDatabase.child(id).setValue(product).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(AddProductActivity.this, "Товар добавлен", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddProductActivity.this, "Ошибка при добавлении товара", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Пожалуйста, заполните все поля и выберите хотя бы одну категорию", Toast.LENGTH_SHORT).show();
        }
    }
}