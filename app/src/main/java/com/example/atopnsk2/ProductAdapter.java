package com.example.atopnsk2;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context mContext;
    private List<Product> mProductList;
    private List<Product> mProductListFull;
    private DatabaseReference mCartRef;

    public ProductAdapter(Context context, List<Product> productList) {
        mContext = context;
        mProductList = productList;
        mProductListFull = new ArrayList<>(productList);
        mCartRef = FirebaseDatabase.getInstance().getReference("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, int position) {
        Product product = mProductList.get(position);
        holder.nameTextView.setText(product.getName());
        holder.descriptionTextView.setText(product.getDescription());
        holder.priceTextView.setText(product.getPrice());

        Picasso.get().load(product.getImageUrl()).fit().centerCrop().into(holder.productImageView);

        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    private void addToCart(Product product) {
        mCartRef.child(product.getId()).setValue(product)
                .addOnSuccessListener(aVoid -> Toast.makeText(mContext, product.getName() + " добавлен в корзину", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(mContext, "Не удалось добавить в корзину", Toast.LENGTH_SHORT).show());
    }
    public void filterList(List<Product> filteredList) {
        mProductList = filteredList;
        notifyDataSetChanged();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView descriptionTextView;
        public TextView priceTextView;
        public ImageView productImageView;
        public Button addToCartButton;

        public ProductViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewProductName);
            descriptionTextView = itemView.findViewById(R.id.textViewProductDescription);
            priceTextView = itemView.findViewById(R.id.textViewProductPrice);
            productImageView = itemView.findViewById(R.id.imageViewProductImage);
            addToCartButton = itemView.findViewById(R.id.buttonAddToCart);
        }
    }
}
