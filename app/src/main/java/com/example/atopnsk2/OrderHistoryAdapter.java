package com.example.atopnsk2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {
    private Context mContext;
    private List<Order> mOrderList;

    public OrderHistoryAdapter(Context context, List<Order> orderList) {
        mContext = context;
        mOrderList = orderList;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order_history, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        Order order = mOrderList.get(position);
        holder.orderIdTextView.setText("ID заказа: " + order.getOrderId());
        holder.userIdTextView.setText("ID пользователя: " + order.getUserId());
        holder.totalPriceTextView.setText("Общая стоимость: " + String.format("%.2f", order.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView orderIdTextView;
        public TextView userIdTextView;
        public TextView totalPriceTextView;

        public OrderHistoryViewHolder(View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.textViewOrderIdHistory);
            userIdTextView = itemView.findViewById(R.id.textViewUserIdHistory);
            totalPriceTextView = itemView.findViewById(R.id.textViewTotalPriceHistory);
        }
    }
}