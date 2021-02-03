package com.example.redesaplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.redesaplication.Common.Common;
import com.example.redesaplication.Database.Database;
import com.example.redesaplication.Interface.ItemClickListener;
import com.example.redesaplication.Models.Comentario;
import com.example.redesaplication.Models.Comida;
import com.example.redesaplication.Models.Order;
import com.example.redesaplication.Models.Usuario;
import com.example.redesaplication.ViewHolder.ComentarioViewHolder;
import com.example.redesaplication.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
    Toolbar toolbar;
    Button btnComentario;
    EditText txtComentario;
    RecyclerView listaComentarios;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Comentario, ComentarioViewHolder> adapter;

    Usuario persona;
    Comida comida;

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

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Database(getBaseContext()).addToCart(new Order(
                        foodId, comida.getNombre(),
                        numberButton.getNumber(),
                        comida.getPrecio()
                ));

                Toast.makeText(DescripcionComida.this, "Añadido a la canasta", Toast.LENGTH_SHORT).show();
            }
        });

        food_description = (TextView) findViewById(R.id.food_description);
        foodName = (TextView) findViewById(R.id.food_name);
        foodPrice = (TextView) findViewById(R.id.food_price);
        food_image = (ImageView) findViewById(R.id.img_food);

        btnComentario = (Button) findViewById(R.id.btn_comentario);
        txtComentario = (EditText) findViewById(R.id.comentario);
        listaComentarios = (RecyclerView) findViewById(R.id.lista_comentarios);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        listaComentarios.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        listaComentarios.setLayoutManager(layoutManager);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        if(getIntent() != null){
            foodId = getIntent().getStringExtra("idComida");
        }
        if (!foodId.isEmpty()){
            getDetalleComidas(foodId);
        }

        btnComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comentar();
            }
        });


        numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                int total = Integer.parseInt(comida.getPrecio())*Integer.parseInt(numberButton.getNumber());
                foodPrice.setText(String.valueOf(total));
            }
        });


        cargarComentarios();
    }

    private void comentar() {
        if(!txtComentario.getText().toString().equals("")){
            DatabaseReference Comdb = database.getReference("Comentarios");

            Comentario comentario = new Comentario();
            comentario.setIdcomida(foodId);
            comentario.setIdusuario(Common.usuario.getTelefono());
            String mensaje = txtComentario.getText().toString();
            comentario.setMensaje(mensaje);
            Comdb.child((String.valueOf(System.currentTimeMillis()))).setValue(comentario);

            txtComentario.setText("");
        }
    }

    private void cargarComentarios() {
        DatabaseReference Comdb = database.getReference("Comentarios");

        adapter = new FirebaseRecyclerAdapter<Comentario, ComentarioViewHolder>(Comentario.class, R.layout.comentario_individuo, ComentarioViewHolder.class, Comdb.orderByChild("idcomida").equalTo(foodId) ) {
            @Override
            protected void populateViewHolder(ComentarioViewHolder comentarioViewHolder, Comentario comentario, int i) {
                DatabaseReference personas = database.getReference("Usuario");
                String idUsuario =comentario.getIdusuario();
                personas.child(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            persona = snapshot.getValue(Usuario.class);
                            comentarioViewHolder.txtNombre.setText(persona.getNombre());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                comentarioViewHolder.txtComentario.setText(comentario.getMensaje());
                if (comentario.getRespuesta() == null){
                    comentarioViewHolder.estado.setText("Comentario enviado");
                    comentarioViewHolder.txtRespuesta.setVisibility(View.GONE);
                }
                else{
                    comentarioViewHolder.estado.setText("Respuesta del administrador");
                    comentarioViewHolder.txtRespuesta.setText(comentario.getRespuesta());
                }

            }
        };
        listaComentarios.setAdapter(adapter);


    }

    private void getDetalleComidas(String foodId) {

        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comida = snapshot.getValue(Comida.class);

                Picasso.get().load(comida.getImagen()).into(food_image);
                collapsingToolbarLayout.setTitle(comida.getNombre());
                toolbar.setTitle(comida.getNombre());

                int total = Integer.parseInt(comida.getPrecio())*Integer.parseInt(numberButton.getNumber());
                foodPrice.setText(String.valueOf(total));


                foodName.setText(comida.getNombre());

                food_description.setText(comida.getDescripcion());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}