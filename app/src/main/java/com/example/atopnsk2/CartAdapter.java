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

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context mContext;
    private List<Product> mCartList;
    private DatabaseReference mCartRef;

    public CartAdapter(Context context, List<Product> cartList) {
        mContext = context;
        mCartList = cartList;
        mCartRef = FirebaseDatabase.getInstance().getReference("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, int position) {
        Product product = mCartList.get(position);
        holder.nameTextView.setText(product.getName());
        holder.descriptionTextView.setText(product.getDescription());
        holder.priceTextView.setText(product.getPrice());

        Picasso.get().load(product.getImageUrl()).fit().centerCrop().into(holder.productImageView);

        holder.removeFromCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromCart(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCartList.size();
    }

    private void removeFromCart(Product product) {
        mCartRef.child(product.getId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    mCartList.remove(product);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, product.getName() + " удален из корзины", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(mContext, "Не удалось удалить из корзины", Toast.LENGTH_SHORT).show());
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView descriptionTextView;
        public TextView priceTextView;
        public ImageView productImageView;
        public Button removeFromCartButton;

        public CartViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewProductName);
            descriptionTextView = itemView.findViewById(R.id.textViewProductDescription);
            priceTextView = itemView.findViewById(R.id.textViewProductPrice);
            productImageView = itemView.findViewById(R.id.imageViewProductImage);
            removeFromCartButton = itemView.findViewById(R.id.buttonRemoveFromCart);
        }
    }
}