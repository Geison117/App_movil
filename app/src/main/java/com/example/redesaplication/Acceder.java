package com.example.redesaplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ShowableListMenu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.redesaplication.Common.Common;
import com.example.redesaplication.Models.Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Acceder extends AppCompatActivity {
    EditText edtPhone, edtPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceder);

        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        // Inicie Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("Usuario");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialogo = new ProgressDialog(Acceder.this);
                dialogo.setMessage("Por favor espera");
                dialogo.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dialogo.dismiss();
                        if(!edtPhone.getText().toString().isEmpty()){
                            if(snapshot.child(edtPhone.getText().toString()).exists()) {
                                // Consigue la información del usuario
                                Usuario usuario = snapshot.child(edtPhone.getText().toString()).getValue(Usuario.class);
                                usuario.setTelefono(edtPhone.getText().toString());
                                if(!edtPassword.getText().toString().isEmpty()){
                                    if (usuario.getContrasena().equals(edtPassword.getText().toString())) {
                                        Intent inicio = new Intent(Acceder.this, menu_inicio.class);
                                        Common.usuario = usuario;
                                        startActivity(inicio);
                                        finish();
                                        Toast.makeText(Acceder.this, "Acceso exitoso", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Acceder.this, "Contraseña errónea", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(Acceder.this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(Acceder.this, "El usuario no existe", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(Acceder.this, "Ingrese un usuario", Toast.LENGTH_SHORT).show();
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