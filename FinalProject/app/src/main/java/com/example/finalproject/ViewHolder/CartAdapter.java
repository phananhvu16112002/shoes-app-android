package com.example.finalproject.ViewHolder;



import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.finalproject.Interface.itemClickListener;
import com.example.finalproject.Model.Order;
import com.example.finalproject.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView txt_cart_name;
    public  TextView txt_cart_price;
    public ImageView iv_cart_count;

    private itemClickListener ItemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_cart_name = itemView.findViewById(R.id.cart_item_name);
        txt_cart_price = itemView.findViewById(R.id.cart_item_price);
        iv_cart_count = itemView.findViewById(R.id.cart_item_count);
    }

    @Override
    public void onClick(View view) {
        ItemClickListener.onClick(view,getAdapterPosition(),false);

    }

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }
}
public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {



    private List<Order> lstData = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> lstData, Context context) {
        this.lstData = lstData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        TextDrawable drawable = TextDrawable.builder().buildRound(
                ""+lstData.get(position).getQuantity(), Color.RED);
        holder.iv_cart_count.setImageDrawable(drawable);

        Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getInstance(locale);
        int price = (Integer.parseInt(lstData.get(position).getPrice())) * (Integer.parseInt(lstData.get(position).getQuantity()));
        holder.txt_cart_price.setText(fmt.format(price));
        holder.txt_cart_name.setText(lstData.get(position).getProductName());
    }



    @Override
    public int getItemCount() {
        return lstData.size();
    }
}

