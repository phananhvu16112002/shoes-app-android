package com.example.shoesserver.ViewHolder;


import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesserver.Common.Common;
import com.example.shoesserver.Interface.ItemClickListener;
import com.example.shoesserver.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener{
    public TextView txtMenuName;
    public ImageView imageView;

    private ItemClickListener ItemClickListener;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        txtMenuName = (TextView)itemView.findViewById(R.id.menu_name);
        imageView = (ImageView) itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);


    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        ItemClickListener = itemClickListener;
    }



    @Override
    public void onClick(View view) {
        ItemClickListener.onClick(view,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("Select the action");
        menu.add(0,0,getAdapterPosition(), Common.UPADTE);
        menu.add(0,1,getAdapterPosition(), Common.DELETE);

    }
}

