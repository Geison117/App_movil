package com.example.redesaplication.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redesaplication.Interface.ItemClickListener;
import com.example.redesaplication.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId, txtOrderStatus, txtOrderPhone, txtOrderAddress;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        txtOrderAddress = (TextView) itemView.findViewById(R.id.order_address);
        txtOrderStatus =  (TextView) itemView.findViewById(R.id.order_status);
        txtOrderId =  (TextView) itemView.findViewById(R.id.order_id);
        txtOrderPhone =  (TextView) itemView.findViewById(R.id.order_phone);

        itemView.setOnClickListener(this);
    }
    public void setItemListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View v) {

    }
}
