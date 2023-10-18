package com.example.finalproject.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Interface.itemClickListener;
import com.example.finalproject.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView txtOrderID,txtOrderStatus,txtOrderPhone,txtOrderAddress;
    private itemClickListener ItemClickListener;
    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderAddress = itemView.findViewById(R.id.order_address);
        txtOrderID = itemView.findViewById(R.id.orderID);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderPhone = itemView.findViewById(R.id.order_phone);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(itemClickListener itemClickListener) {
        ItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {

        ItemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
