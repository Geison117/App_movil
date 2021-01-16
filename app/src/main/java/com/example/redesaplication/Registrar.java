package com.example.redesaplication;


import android.app.ProgressDialog;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.redesaplication.Models.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Registrar extends AppCompatActivity {

    EditText edtPhone, edtNombre, edtContrasena;
    Button registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtNombre = (EditText) findViewById(R.id.edtName);
        edtContrasena = (EditText) findViewById(R.id.edtPassword);

        registrar = (Button) findViewById(R.id.btnSignUp);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("Usuario");

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialogo = new ProgressDialog(Registrar.this);
                dialogo.setMessage("Por favor espera");
                dialogo.show();

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(edtPhone.getText().toString()).exists()){
                            dialogo.dismiss();
                            Toast.makeText(Registrar.this, "El número de teléfono ya está registrado", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            dialogo.dismiss();
                            Usuario usuario = new Usuario(edtNombre.getText().toString(), edtContrasena.getText().toString());
                            table_user.child(edtPhone.getText().toString()).setValue(usuario);
                            Toast.makeText(Registrar.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}