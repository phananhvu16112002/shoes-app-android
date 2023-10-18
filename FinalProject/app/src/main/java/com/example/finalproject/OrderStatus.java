package com.example.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Common.Common;
import com.example.finalproject.Interface.itemClickListener;
import com.example.finalproject.Model.Request;
import com.example.finalproject.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);


        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = findViewById(R.id.lstOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.userCurrent.getPhone());
    }

    private void loadOrders(String phone) {

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, Request request, int i) {

                orderViewHolder.txtOrderID.setText(adapter.getRef(i).getKey());
                orderViewHolder.txtOrderStatus.setText(convertCodeToStatus(request.getStatus()));
                orderViewHolder.txtOrderAddress.setText(request.getAddress());
                orderViewHolder.txtOrderPhone.setText(request.getPhone());

                Request request1 = request;
                orderViewHolder.setItemClickListener(new itemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongCLick) {
                        Toast.makeText(OrderStatus.this,""+request1.getStatus(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);
    }

    private  String convertCodeToStatus(String status)
    {
        if(status.equals("0"))
        {
            return "Placed";
        }
        else if (status.equals("1"))
        {
            return "On Road";
        }
        else
            return "Shipped";

    }


}
//Phan Anh Vũ - 520H0696 //Phan Anh Vũ - 520H0696 //Phan Anh Vũ - 520H0696
//Phan Anh Vũ - 520H0696 //Phan Anh Vũ - 520H0696 //Phan Anh Vũ - 520H0696
//Phan Anh Vũ - 520H0696 //Phan Anh Vũ - 520H0696 //Phan Anh Vũ - 520H0696