package com.example.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.DataBase.DataBase;
import com.example.finalproject.Model.Order;
import com.example.finalproject.Model.Shoes;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ShoesDetail extends AppCompatActivity {

    TextView shoes_name,shoes_price,shoes_description;
    ImageView shoes_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    String shoesId = "";
    FirebaseDatabase database;
    DatabaseReference shoesList;
    Shoes current;
    RecyclerView recyclerView;
    TextView Quantity;
    TextView Size;
    ImageView ivPlus;
    ImageView ivMinus;
    int totalQuantity = 1;
    int totalSize = 36;

    ImageView ivMinusSize;
    ImageView ivPlusSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoes_detail);

        database = FirebaseDatabase.getInstance();
        shoesList = database.getReference("Shoes");

        Size = findViewById(R.id.tvSize);
        Quantity = findViewById(R.id.tvQuantity);
        ivPlus = findViewById(R.id.ivPlus);
        ivMinus = findViewById(R.id.ivMinus);
        ivMinusSize = findViewById(R.id.ivMinusSize);
        ivPlusSize = findViewById(R.id.ivPlusSize);
        shoes_description = findViewById(R.id.shoes_description);
        shoes_name = findViewById(R.id.shoes_name);
        shoes_price = findViewById(R.id.shoes_price);
        shoes_image = findViewById(R.id.img_shoes);
        collapsingToolbarLayout = findViewById(R.id.collapsing);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


        recyclerView = findViewById(R.id.recycler_shoes);
        btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                new DataBase(getBaseContext()).addToCart(new Order(
                        shoesId,
                        current.getName(),
                        getTotal(),
                        current.getPrice(),
                        current.getDiscount()
                ));
                Toast.makeText(ShoesDetail.this,"Add to Cart",Toast.LENGTH_SHORT).show();
            }

        });




        ivPlusSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (totalSize < 45)
                {
                    totalSize++;
                    Size.setText(String.valueOf(totalSize));
                }
            }
        });
        ivMinusSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalSize >36)
                {
                    totalSize--;
                    Size.setText(String.valueOf(totalSize));
                }
            }
        });
        ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (totalQuantity < 10)
                {
                    totalQuantity++;
                    Quantity.setText(String.valueOf(totalQuantity));
                }
            }
        });

        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalQuantity >0)
                {
                    totalQuantity--;
                    Quantity.setText(String.valueOf(totalQuantity));
                }
            }
        });


        if (getIntent() != null)
        {
            shoesId = getIntent().getStringExtra("shoesId");

        }
        if (!shoesId.isEmpty())
        {
            getDetailShoes(shoesId);
        }


    }

    private String getTotal()
    {

        ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (totalQuantity < 10)
                {
                    totalQuantity++;
                    Quantity.setText(String.valueOf(totalQuantity));
                }
            }
        });

        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalQuantity >0)
                {
                    totalQuantity--;
                    Quantity.setText(String.valueOf(totalQuantity));
                }
            }
        });
        return String.valueOf(totalQuantity);
    }

    private void getDetailShoes(String shoesId) {

        shoesList.child(shoesId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                current = snapshot.getValue(Shoes.class);
                assert current != null;
                Picasso.with(getBaseContext()).load(current.getImage()).into(shoes_image);

                collapsingToolbarLayout.setTitle(current.getName());

                shoes_price.setText(current.getPrice());
                shoes_name.setText(current.getName());
                shoes_description.setText(current.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}