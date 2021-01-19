package com.example.redesaplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.redesaplication.Models.Comida;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DescripcionComida extends AppCompatActivity {

    TextView foodName, foodPrice, food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
    String foodId = "";

    FirebaseDatabase database;
    DatabaseReference foods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_comida);

        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Comidas");

        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);

        food_description = (TextView) findViewById(R.id.food_description);
        foodName = (TextView) findViewById(R.id.food_name);
        foodPrice = (TextView) findViewById(R.id.food_price);
        food_image = (ImageView) findViewById(R.id.img_food);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        if(getIntent() != null){
            foodId = getIntent().getStringExtra("idComida");
        }
        if (!foodId.isEmpty()){
            getDetalleComidas(foodId);
        }
    }

    private void getDetalleComidas(String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Comida comida = snapshot.getValue(Comida.class);

                Picasso.get().load(comida.getImagen()).into(food_image);
                collapsingToolbarLayout.setTitle(comida.getNombre());
                foodPrice.setText(comida.getPrecio());

                foodName.setText(comida.getNombre());

                food_description.setText(comida.getDescripcion());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}