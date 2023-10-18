package com.example.finalproject.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Interface.itemClickListener;
import com.example.finalproject.R;

public class ShoesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView shoes_name;
    public ImageView shoes_image;
    private itemClickListener ItemClickListener;

    public ShoesViewHolder(@NonNull View itemView) {
        super(itemView);

        shoes_image = (ImageView) itemView.findViewById(R.id.shoes_image);
        shoes_name = (TextView) itemView.findViewById(R.id.shoes_name);

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
