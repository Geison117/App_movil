package com.example.redesaplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.redesaplication.Common.Common;
import com.example.redesaplication.Database.Database;
import com.example.redesaplication.Interface.ItemClickListener;
import com.example.redesaplication.Models.Solicitud;
import com.example.redesaplication.Models.Sugerencia;
import com.example.redesaplication.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Date;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public LinearLayoutManager layoutManager;

    FirebaseRecyclerAdapter<Solicitud, OrderViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference requests;
    RatingBar ratingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Solicitudes");

        recyclerView = (RecyclerView) findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager  = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.usuario.getTelefono());
    }

    private void loadOrders(String telefono) {
        adapter = new FirebaseRecyclerAdapter<Solicitud, OrderViewHolder>(
                Solicitud.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("telefono").equalTo(telefono)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, Solicitud solicitud, int i) {

                String f = adapter.getRef(i).getKey();
                orderViewHolder.txtOrderId.setText(f);
                orderViewHolder.txtOrderStatus.setText(conseguirStatus(solicitud.getStatus()));
                orderViewHolder.txtOrderAddress.setText(solicitud.getDireccion());
                orderViewHolder.txtOrderPhone.setText(solicitud.getTelefono());

                orderViewHolder.setItemListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        if(solicitud.getStatus().equals("2")){
                            String r = adapter.getRef(i).getKey();
                            DatabaseReference table = database.getReference("Solicitudes/" + r);
                            table.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(!snapshot.child("rating").exists()){
                                        mostrarAlerta(solicitud, i, view);
                                    }
                                    else
                                    {
                                        Toast.makeText(OrderStatus.this, "Ya calificó esta solicitud", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(OrderStatus.this, conseguirStatus(solicitud.getStatus()), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void mostrarAlerta(Solicitud solicitud, int i, View view) {
        Dialog alertDialog = new Dialog(OrderStatus.this, R.style.FullHeightDialog);
        alertDialog.setContentView(R.layout.rating_layout);
        alertDialog.setCancelable(true);

        Button button = (Button) alertDialog.findViewById(R.id.btn_rating);
        RatingBar rb = (RatingBar)  alertDialog.findViewById(R.id.ratingBar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getRef(i).child("rating").setValue(String.valueOf(rb.getRating()));
                alertDialog.dismiss();
                DatabaseReference request = database.getReference("Rating");
                request.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String rating = (String) snapshot.child("rating").getValue();
                        String num = (String) snapshot.child("num_solicitudes").getValue();
                        float r = Float.parseFloat(rating);
                        float n = Float.parseFloat(num);
                        float rs = rb.getRating();
                        if(n == 0){
                            n = n + 1;
                            r = rs;
                        }
                        else{
                            r = (r*n)/(n+1) + (rs/(n+1));
                            n = n+1;
                        }
                        request.child("rating").setValue(String.valueOf(r));
                        request.child("num_solicitudes").setValue(String.valueOf(n));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(OrderStatus.this, "Gracias por evaluar nuestro servicio", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
    }

    private String conseguirStatus(String status) {
        if (status.equals("0")) {
            return "En espera";
        } else if (status.equals("1")) {
            return "Viene en camino";
        } else {
            return "Entregado";

        }
    }
}