package com.example.atopnsk2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context mContext;
    private List<Order> mOrderList;
    private DatabaseReference mOrdersRef;

    public OrderAdapter(Context context, List<Order> orderList) {
        mContext = context;
        mOrderList = orderList;
        mOrdersRef = FirebaseDatabase.getInstance().getReference("orders");
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, int position) {
        Order order = mOrderList.get(position);
        holder.orderIdTextView.setText("ID заказа: " + order.getOrderId());
        holder.userIdTextView.setText("ID пользователя: " + order.getUserId());
        holder.totalPriceTextView.setText("Общая стоимость: " + String.format("%.2f", order.getTotalPrice()));
        holder.statusSpinner.setSelection(getStatusPosition(order.getStatus()));

        holder.updateStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newStatus = holder.statusSpinner.getSelectedItem().toString();
                updateOrderStatus(order, newStatus);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    private void updateOrderStatus(Order order, String newStatus) {
        mOrdersRef.child(order.getOrderId()).child("status").setValue(newStatus)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(mContext, "Статус заказа обновлен", Toast.LENGTH_SHORT).show();
                    if ("Доставлен".equals(newStatus)) {
                        moveToCompletedOrders(order);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(mContext, "Не удалось обновить статус", Toast.LENGTH_SHORT).show());
    }

    private void moveToCompletedOrders(Order order) {
        order.moveToCompleted();
        mOrdersRef.child(order.getOrderId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    mOrderList.remove(order);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, "Заказ перемещен в выполненные", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(mContext, "Не удалось переместить заказ", Toast.LENGTH_SHORT).show());
    }

    private int getStatusPosition(String status) {
        String[] statuses = mContext.getResources().getStringArray(R.array.order_statuses);
        for (int i = 0; i < statuses.length; i++) {
            if (statuses[i].equals(status)) {
                return i;
            }
        }
        return 0; // Default to first status if not found
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView orderIdTextView;
        public TextView userIdTextView;
        public TextView totalPriceTextView;
        public Spinner statusSpinner;
        public Button updateStatusButton;

        public OrderViewHolder(View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.textViewOrderId);
            userIdTextView = itemView.findViewById(R.id.textViewUserId);
            totalPriceTextView = itemView.findViewById(R.id.textViewTotalPrice);
            statusSpinner = itemView.findViewById(R.id.spinnerOrderStatus);
            updateStatusButton = itemView.findViewById(R.id.buttonUpdateStatus);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(itemView.getContext(),
                    R.array.order_statuses, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            statusSpinner.setAdapter(adapter);
        }
    }
}