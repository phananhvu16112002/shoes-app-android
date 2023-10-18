package com.example.shoesserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.shoesserver.Common.Common;
import com.example.shoesserver.Interface.ItemClickListener;
import com.example.shoesserver.Model.Category;
import com.example.shoesserver.Model.Shoes;
import com.example.shoesserver.ViewHolder.ShoesViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class ShoesList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RelativeLayout rootLayout;
    FloatingActionButton fab;

    Uri saveUri;

    DatabaseReference shoesList;
    FirebaseDatabase db;
    FirebaseStorage storage;
    StorageReference storageReference;

    String categoryId = "";
    FirebaseRecyclerAdapter<Shoes, ShoesViewHolder> adapter;

    EditText edtName,edtDescription,edtPrice,edtDiscount;

    Button btnSelect,btnUpLoad;
    Shoes newShoes;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoes_list);


        db = FirebaseDatabase.getInstance();
        shoesList = db.getReference("Shoes");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        recyclerView = findViewById(R.id.recycler_shoes);
        recyclerView.setHasFixedSize(true);
        layoutManager =  new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        rootLayout = findViewById(R.id.rootLayout);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddShoesDialog();
            }
        });

        if (getIntent() != null)
        {
            categoryId =    getIntent().getStringExtra("CategoryId");
        }
        if (!categoryId.isEmpty())
        {
            loadlistShoes(categoryId);
        }




    }


    private void upLoadImage() {

        if (saveUri != null)
        {
            ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading....");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(ShoesList.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newShoes = new Shoes();
                                    newShoes.setName(edtName.getText().toString());
                                    newShoes.setDescription(edtDescription.getText().toString());
                                    newShoes.setPrice(edtPrice.getText().toString());
                                    newShoes.setDiscount(edtDiscount.getText().toString());
                                    newShoes.setListId(categoryId);
                                    newShoes.setImage(uri.toString());
                                }
                            });
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(ShoesList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progess = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            mDialog.setMessage("Uploaded"+progess+"%");


                        }
                    });
        }
    }

    private void chooseImage() {

        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),Common.PICK_IMAGE_REQUEST);


    }
    private void showAddShoesDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShoesList.this);
        alertDialog.setTitle("Add new Shoes");
        alertDialog.setMessage("Please Fill Full Information!!");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_shoes,null);

        edtName = add_menu_layout.findViewById(R.id.edtName);
        edtDescription = add_menu_layout.findViewById(R.id.edtDescription);
        edtDiscount = add_menu_layout.findViewById(R.id.edtDiscount);
        edtPrice = add_menu_layout.findViewById(R.id.edtPrice );
        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpLoad = add_menu_layout.findViewById(R.id.btnUpLoad);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoadImage();
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_baseline_shopping_cart_24);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(newShoes != null){
                    shoesList.push().setValue(newShoes);
                    Snackbar.make(rootLayout,"New Category"+newShoes.getName()+"was Added",Snackbar.LENGTH_SHORT).show();

                }

            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        alertDialog.show();

    }



    private void loadlistShoes(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Shoes, ShoesViewHolder>(
                Shoes.class,
                R.layout.shoes_item,
                ShoesViewHolder.class,
                shoesList.orderByChild("listId").equalTo(categoryId)
        )  {
            @Override
            protected void populateViewHolder(ShoesViewHolder shoesViewHolder, Shoes shoes, int i) {
                shoesViewHolder.shoes_name.setText(shoes.getName());
                Picasso.with(getBaseContext()).load(shoes.getImage()).into(shoesViewHolder.shoes_image);

                shoesViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            saveUri = data.getData();
            btnSelect.setText("Image Selected");
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.UPADTE))
        {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else if (item.getTitle().equals(Common.DELETE))
        {
            deleteShoes(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteShoes(String key) {
        
        shoesList.child(key).removeValue();
    }

    private void showUpdateDialog(String key, Shoes item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShoesList.this);
        alertDialog.setTitle("Edit Shoes");
        alertDialog.setMessage("Please Fill Full Information!!");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_shoes,null);

        edtName = add_menu_layout.findViewById(R.id.edtName);
        edtDescription = add_menu_layout.findViewById(R.id.edtDescription);
        edtDiscount = add_menu_layout.findViewById(R.id.edtDiscount);
        edtPrice = add_menu_layout.findViewById(R.id.edtPrice );


        edtName.setText(item.getName());
        edtDiscount.setText(item.getDiscount());
        edtDescription.setText(item.getDescription());
        edtPrice.setText(item.getPrice());


        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpLoad = add_menu_layout.findViewById(R.id.btnUpLoad);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_baseline_shopping_cart_24);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                    item.setName(edtName.getText().toString());
                    item.setPrice(edtPrice.getText().toString());
                    item.setDescription(edtDescription.getText().toString());
                    item.setDiscount(edtDiscount.getText().toString());

                    shoesList.child(key).setValue(item);
                    shoesList.push().setValue(newShoes);
                    Snackbar.make(rootLayout,"Shoes"+item.getName()+"was edited",Snackbar.LENGTH_SHORT).show();


            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void changeImage(final Shoes item) {

        if (saveUri != null)
        {
            ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading....");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(ShoesList.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    item.setImage(uri.toString());
                                }
                            });
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(ShoesList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progess = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            mDialog.setMessage("Uploaded"+progess+"%");


                        }
                    });
        }
    }
}