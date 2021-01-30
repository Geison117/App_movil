package com.example.redesaplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.redesaplication.Common.Common;
import com.example.redesaplication.Models.Solicitud;
import com.example.redesaplication.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Solicitud, OrderViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Solicitudes");

        recyclerView = (RecyclerView) findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager  = new LinearLayoutManager(this);
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
                orderViewHolder.txtOrderId.setText(adapter.getRef(i).getKey());
                orderViewHolder.txtOrderStatus.setText(conseguirStatus(solicitud.getStatus()));
                orderViewHolder.txtOrderAddress.setText(solicitud.getDireccion());
                orderViewHolder.txtOrderPhone.setText(solicitud.getTelefono());
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private String conseguirStatus(String status) {
        if (status.equals("0")){
            return "En espera";
        }
        else if (status.equals("1")){
            return "Viene en camino";
        }
        else{
            return "Entregado";
        }

    }
}