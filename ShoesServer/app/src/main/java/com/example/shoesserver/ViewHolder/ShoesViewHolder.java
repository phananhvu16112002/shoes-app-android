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

public class ShoesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener{
    public TextView shoes_name;
    public ImageView shoes_image;

    private ItemClickListener ItemClickListener;

    public ShoesViewHolder(@NonNull View itemView) {
        super(itemView);
        shoes_name = (TextView)itemView.findViewById(R.id.shoes_name);
        shoes_image = (ImageView) itemView.findViewById(R.id.shoes_image);

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
