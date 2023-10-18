package com.example.finalproject.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Interface.itemClickListener;
import com.example.finalproject.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtMenuName;
    public ImageView imageView;

    private itemClickListener ItemClickListener;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        txtMenuName = (TextView)itemView.findViewById(R.id.menu_name);
        imageView = (ImageView) itemView.findViewById(R.id.menu_image);

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
