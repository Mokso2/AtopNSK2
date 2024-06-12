package com.example.atopnsk2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.UserOrderViewHolder> {
    private Context mContext;
    private List<Order> mOrderList;

    public UserOrderAdapter(Context context, List<Order> orderList) {
        mContext = context;
        mOrderList = orderList;
    }

    @NonNull
    @Override
    public UserOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user_order, parent, false);
        return new UserOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrderViewHolder holder, int position) {
        Order order = mOrderList.get(position);
        holder.orderIdTextView.setText("ID заказа: " + order.getOrderId());
        holder.totalPriceTextView.setText("Общая стоимость: " + String.format("%.2f", order.getTotalPrice()));
        holder.statusTextView.setText("Статус: " + order.getStatus());
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public static class UserOrderViewHolder extends RecyclerView.ViewHolder {
        public TextView orderIdTextView;
        public TextView totalPriceTextView;
        public TextView statusTextView;

        public UserOrderViewHolder(View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.textViewOrderId);
            totalPriceTextView = itemView.findViewById(R.id.textViewTotalPrice);
            statusTextView = itemView.findViewById(R.id.textViewStatus);
        }
    }
}