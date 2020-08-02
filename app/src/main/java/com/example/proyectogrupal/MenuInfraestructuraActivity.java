package com.example.proyectogrupal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.proyectogrupal.entidades.IncidenciaDto;
import com.example.proyectogrupal.entidades.IncidenciaRV;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuInfraestructuraActivity extends AppCompatActivity {

    //variables globales
    DatabaseReference databaseReference;
    RecyclerView recycler;
    ArrayList<IncidenciaRV> listIncidencia;
    int id;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_infraestructura);

        databaseReference = FirebaseDatabase.getInstance().getReference("incidencias");

        recycler = findViewById(R.id.recyclerViewInfra);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        listIncidencia = new ArrayList<IncidenciaRV>();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot != null) {

                    final IncidenciaDto incidenciaDto = dataSnapshot.getValue(IncidenciaDto.class);
                    Log.d("infoApp", incidenciaDto.getNombre());
                    listIncidencia.add(new IncidenciaRV(incidenciaDto.getId(), incidenciaDto.getNombre(), incidenciaDto.isEstado()));

                    AdapterDatos adapter = new AdapterDatos(listIncidencia);

                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            id = listIncidencia.get(recycler.getChildAdapterPosition(v)).getId();
                            Log.d("infoApp","seleeccion: "+id);

                            Intent intent = new Intent(MenuInfraestructuraActivity.this, DetallesActivity.class);
                            intent.setData(Uri.parse(String.valueOf(id)));
                            int requestCode = 1;
                            startActivityForResult(intent,requestCode);

                        }
                    });

                    recycler.setAdapter(adapter);

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}