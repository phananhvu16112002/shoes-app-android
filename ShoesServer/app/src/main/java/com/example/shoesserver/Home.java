package com.example.shoesserver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoesserver.Common.Common;
import com.example.shoesserver.Interface.ItemClickListener;
import com.example.shoesserver.Model.Category;
import com.example.shoesserver.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesserver.databinding.ActivityHomeBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtFullName;
    FirebaseDatabase database;
    DatabaseReference categories;
    FirebaseStorage storage;
    StorageReference storageReference;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;
    Category newCategory;

    EditText edtName;
    Button btnUpLoad,btnSelect;
    DrawerLayout drawer;

    Uri saveUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        categories = database.getReference("Category");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close
        );
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.userCurrent.getName());


        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager  =  new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        
        
        loadMenu();
        



    }

    private void showDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Add new Category");
        alertDialog.setMessage("Please Fill Full Information!!");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_nav_menu_layout,null);

        edtName = add_menu_layout.findViewById(R.id.edtName);
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
                if(newCategory != null){
                    categories.push().setValue(newCategory);
                    Snackbar.make(drawer,"New Category"+newCategory.getName()+"was Added",Snackbar.LENGTH_SHORT).show();

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
                    Toast.makeText(Home.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            newCategory = new Category(edtName.getText().toString(),uri.toString());
                        }
                    });
                }

            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(Home.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            saveUri = data.getData();
            btnSelect.setText("Image Selected");
        }
    }

    private void chooseImage() {

        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),Common.PICK_IMAGE_REQUEST);


    }

    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(
                Category.class,
                R.layout.menu_item,
                MenuViewHolder.class,
                categories
        ) {
            @Override
            protected void populateViewHolder(MenuViewHolder menuViewHolder, Category category, int i) {
                menuViewHolder.txtMenuName.setText(category.getName());
                Picasso.with(Home.this).load(category.getImage()).into(menuViewHolder.imageView);
                menuViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                            Intent shoesList = new Intent(Home.this,ShoesList.class);
                            shoesList.putExtra("CategoryId",adapter.getRef(position).getKey());
                            startActivity(shoesList);
                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
        recycler_menu.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);

        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getTitle().equals(Common.UPADTE)){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else if (item.getTitle().equals(Common.DELETE)){
            deleteCategory(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteCategory(String key) {

        categories.child(key).removeValue();
        Toast.makeText(this, "Item Deleted!!!", Toast.LENGTH_SHORT).show();
    }

    private void showUpdateDialog(String key, Category item) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Update Category");
        alertDialog.setMessage("Please Fill Full Information!!");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_nav_menu_layout,null);

        edtName = add_menu_layout.findViewById(R.id.edtName);
        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpLoad = add_menu_layout.findViewById(R.id.btnUpLoad);

        edtName.setText(item.getName());
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
//                if(newCategory != null){
//                    categories.push().setValue(newCategory);
//                    Snackbar.make(drawer,"New Category"+newCategory.getName()+"was Added",Snackbar.LENGTH_SHORT).show();
//
//                }
                item.setName(edtName.getText().toString());
                categories.child(key).setValue(item);
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


    private void changeImage(final Category item) {

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
                            Toast.makeText(Home.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Home.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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


