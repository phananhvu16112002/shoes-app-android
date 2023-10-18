package com.example.finalproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Common.Common;
import com.example.finalproject.DataBase.DataBase;
import com.example.finalproject.Model.Order;
import com.example.finalproject.Model.Request;
import com.example.finalproject.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    Button brnPlaceOrder;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = findViewById(R.id.lstCart);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        txtTotalPrice = findViewById(R.id.total);
        brnPlaceOrder = findViewById(R.id.btnPlace);
        brnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showAlertDialog();


            }
        });

        loadListShoes();
    }

    private void showAlertDialog()
    {
        AlertDialog.Builder alertDiaLog = new AlertDialog.Builder(Cart.this);
        alertDiaLog.setTitle("One More Step");
        alertDiaLog.setMessage("Enter your address:");

        final EditText edtAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAddress.setLayoutParams(lp);
        alertDiaLog.setView(edtAddress);
        alertDiaLog.setIcon(R.drawable.ic_baseline_shopping_cart_24);
        alertDiaLog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(
                        Common.userCurrent.getPhone(),
                        Common.userCurrent.getName(),
                        edtAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart
                );

                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                new DataBase(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this,"Thank you,Order Place",Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        alertDiaLog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDiaLog.show();
    }

    private void loadListShoes() {
        cart = new DataBase(this).getCart();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        int total = 0;
        for (Order order : cart){
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
        }
        Locale locale = new Locale("en","EN");
        NumberFormat fmt = NumberFormat.getInstance(locale);
        txtTotalPrice.setText(fmt.format(total));
    }
}