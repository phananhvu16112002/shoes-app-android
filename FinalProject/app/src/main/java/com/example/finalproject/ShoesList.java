package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Interface.itemClickListener;
import com.example.finalproject.Model.Shoes;
import com.example.finalproject.ViewHolder.ShoesViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShoesList extends AppCompatActivity {

    RecyclerView recycler_shoes;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference shoeslist;

    String categoryId = "";
    FirebaseRecyclerAdapter<Shoes, ShoesViewHolder> adapter;

    FirebaseRecyclerAdapter<Shoes, ShoesViewHolder> searchAdapter;

    List<String> suggestList =  new ArrayList<>();
    MaterialSearchBar materialSearchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoes_list);

        database = FirebaseDatabase.getInstance();
        shoeslist = database.getReference("Shoes");

        recycler_shoes = (RecyclerView) findViewById(R.id.recycler_shoes);
        recycler_shoes.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_shoes.setLayoutManager(layoutManager);

        if (getIntent() != null)
        {
            categoryId = getIntent().getStringExtra("CategoryId");

        }
        if (!categoryId.isEmpty())
        {
            loadlistshoes(categoryId);
        }

        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter you Shoes");

        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<String> suggest = new ArrayList<String>();
                for(String search: suggestList)
                {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled)
                {
                    recycler_shoes.setAdapter(adapter);

                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {


                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {

        searchAdapter = new FirebaseRecyclerAdapter<Shoes, ShoesViewHolder>(
                Shoes.class,
                R.layout.shoes_item,
                ShoesViewHolder.class,
                shoeslist.orderByChild("name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(ShoesViewHolder shoesViewHolder, Shoes shoes, int i) {
                shoesViewHolder.shoes_name.setText(shoes.getName());
                Picasso.with(getBaseContext()).load(shoes.getImage()).into(shoesViewHolder.shoes_image);
                final Shoes local = shoes;
                shoesViewHolder.setItemClickListener(new itemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongCLick) {
                        Toast.makeText(ShoesList.this,""+local.getName(),Toast.LENGTH_SHORT).show();
                        Intent shoesDetail = new Intent(ShoesList.this,ShoesDetail.class);
                        shoesDetail.putExtra("shoesId",searchAdapter.getRef(position).getKey());
                        startActivity(shoesDetail);
                    }
                });

            }
        };
        recycler_shoes.setAdapter(searchAdapter);
    }

    private void loadSuggest() {
        shoeslist.orderByChild("listId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot:snapshot.getChildren())
                {
                    Shoes item = postSnapshot.getValue(Shoes.class);
                    suggestList.add(item.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadlistshoes(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Shoes, ShoesViewHolder>
                (Shoes.class,
                            R.layout.shoes_item,ShoesViewHolder.class,
                        shoeslist.orderByChild("listId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(ShoesViewHolder shoesViewHolder, Shoes shoes, int i) {
                shoesViewHolder.shoes_name.setText(shoes.getName());
                Picasso.with(getBaseContext()).load(shoes.getImage()).into(shoesViewHolder.shoes_image);
                final Shoes local = shoes;
                shoesViewHolder.setItemClickListener(new itemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongCLick) {
                        Toast.makeText(ShoesList.this,""+local.getName(),Toast.LENGTH_SHORT).show();
                        Intent shoesDetail = new Intent(ShoesList.this,ShoesDetail.class);
                        shoesDetail.putExtra("shoesId",adapter.getRef(position).getKey());
                        startActivity(shoesDetail);
                    }
                });
            }
        };
        Log.d("TAG",""+adapter.getItemCount());
        recycler_shoes.setAdapter(adapter);
    }
}