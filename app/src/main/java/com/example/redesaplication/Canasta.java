package com.example.redesaplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.redesaplication.Common.Common;
import com.example.redesaplication.Database.Database;
import com.example.redesaplication.Models.Order;
import com.example.redesaplication.Models.Solicitud;
import com.example.redesaplication.ViewHolder.CartAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Canasta extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request;
    TextView txtTotalPrice;
    Button btnPlace;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;
    int total = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canasta);

        database = FirebaseDatabase.getInstance();
        request = database.getReference("Solicitudes");

        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice  = (TextView) findViewById(R.id.total);
        btnPlace = (Button) findViewById(R.id.btnPlaceOrder);

        cargarLista();

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (total > 0){
                    showAlertDialog();
                }
                else{
                    Toast.makeText(Canasta.this, "Aun no tiene ningún item en la lista", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Canasta.this);
        alertDialog.setTitle("Hay una última cosa que hacer");
        alertDialog.setMessage("Ingrese su dirección");

        final EditText edtAdress = new EditText(Canasta.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAdress.setLayoutParams(lp);
        alertDialog.setView(edtAdress);
        alertDialog.setIcon(R.drawable.ic_baseline_shopping_cart_24);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Solicitud solicitud = new Solicitud(
                        Common.usuario.getTelefono(),
                        Common.usuario.getNombre(),
                        edtAdress.getText().toString(),
                        String.valueOf(total),
                        cart
                );
                request.child(String.valueOf(System.currentTimeMillis())).setValue(solicitud);
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Canasta.this, "Gracias por su orden", Toast.LENGTH_SHORT).show();
                finish();
             }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void cargarLista() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);

        recyclerView.setAdapter(adapter);

        for (Order order:cart){
            total += (Integer.parseInt(order.getPrecio()))*(Integer.parseInt(order.getCantidad()));
        }
        Locale locale = new Locale("es", "CO");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        txtTotalPrice.setText(fmt.format(total));
    }
}