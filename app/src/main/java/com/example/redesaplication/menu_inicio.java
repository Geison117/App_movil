package com.example.redesaplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redesaplication.Common.Common;
import com.example.redesaplication.Interface.ItemClickListener;
import com.example.redesaplication.Models.Categoria;
import com.example.redesaplication.Models.Order;
import com.example.redesaplication.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

public class menu_inicio extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;

    FirebaseDatabase database;
    DatabaseReference categoria;
    TextView tvNombre;
    RecyclerView recyclerMenu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Categoria, MenuViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicio);
        Toolbar toolbar = findViewById(R.id.toolbar);

        database = FirebaseDatabase.getInstance();
        categoria = database.getReference("Categoria");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(menu_inicio.this, Canasta.class);
                startActivity(cartIntent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        tvNombre = (TextView) headerView.findViewById(R.id.nombre);
        tvNombre.setText(Common.usuario.getNombre());

        recyclerMenu = (RecyclerView)findViewById(R.id.recycler_menu);
        recyclerMenu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);

        loadMenu();

        toolbar.setTitle("Menú");
        setTitle("Menú");
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadMenu() {
         adapter = new FirebaseRecyclerAdapter<Categoria, MenuViewHolder>(Categoria.class, R.layout.menu_item, MenuViewHolder.class, categoria) {
            @Override
            protected void populateViewHolder(MenuViewHolder menuViewHolder, Categoria categoria, int i) {
                menuViewHolder.txtMenuName.setText(categoria.getNombre());
                Picasso.get().load(categoria.getImagen()).into(menuViewHolder.imageView);
                final Categoria clickItem = categoria;
                menuViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodList = new Intent(menu_inicio.this, menu_comidas.class);
                        foodList.putExtra("idCategoria", adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                });
            }
        };
        recyclerMenu.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicio, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_menu)
        {

        }
        else if (id == R.id.nav_cart){
            Intent intent = new Intent(menu_inicio.this, Canasta.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_orders){
            Intent intent = new Intent(menu_inicio.this, OrderStatus.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_salir){
            Intent intent = new Intent(menu_inicio.this, Acceder.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return true;
    }
}