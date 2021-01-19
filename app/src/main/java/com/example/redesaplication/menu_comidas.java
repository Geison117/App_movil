package com.example.redesaplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.redesaplication.Interface.ItemClickListener;
import com.example.redesaplication.Models.Categoria;
import com.example.redesaplication.Models.Comida;
import com.example.redesaplication.ViewHolder.FoodViewHolder;
import com.example.redesaplication.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class menu_comidas extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference listaComida;
    String idCategoria;
    FirebaseRecyclerAdapter<Comida, FoodViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_comidas);

        database = FirebaseDatabase.getInstance();
        listaComida = database.getReference("Comidas");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent() != null){
            idCategoria = getIntent().getStringExtra("idCategoria");
            if(!idCategoria.isEmpty() && idCategoria != null){
                loadListFood();
            }

        }


    }

    private void loadListFood() {
        adapter = new FirebaseRecyclerAdapter<Comida, FoodViewHolder>(Comida.class, R.layout.item_comida, FoodViewHolder.class, listaComida.orderByChild("categoria").equalTo(idCategoria) ) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Comida comida, int i) {
                foodViewHolder.txtFoodName.setText(comida.getNombre());
                Picasso.get().load(comida.getImagen()).into(foodViewHolder.imageView);

                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodDetail = new Intent(menu_comidas.this, DescripcionComida.class);
                        foodDetail.putExtra("idComida", adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
}