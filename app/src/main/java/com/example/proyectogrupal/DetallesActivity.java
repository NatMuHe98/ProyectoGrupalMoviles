package com.example.proyectogrupal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.proyectogrupal.entidades.IncidenciaDto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetallesActivity extends AppCompatActivity {

    String id;
    String nombre;
    String descripcion;
    int ubicacion;
    boolean estado;
    ToggleButton toggleButton;
    EditText editTextComentario;
    String comentario;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        databaseReference = FirebaseDatabase.getInstance().getReference("incidencias");
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        //Obtener el id del equipo seleccionado en la anterior actividad.
        Intent intent = getIntent();
        id = intent.getData().toString();
        Log.d("infoApp", id);

        StorageReference imagenesRef = storageReference.child("3178.png");

        //Cargar la imagen del equipo sin descargarla
        ImageView imageViewFoto = findViewById(R.id.imageViewFoto);
        Glide.with(this).load(imagenesRef).into(imageViewFoto);



        databaseReference.child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    IncidenciaDto incidenciaDto = dataSnapshot.getValue(IncidenciaDto.class);
                    Log.d("infoApp", incidenciaDto.getNombre()+"hola");

                    TextView textViewTituloNombre = findViewById(R.id.textViewTituloNombre);
                    nombre = incidenciaDto.getNombre();
                    textViewTituloNombre.setText("Detalles de incidencia: "+nombre);

                    TextView textViewDescripcion = findViewById(R.id.textViewDescripcion);
                    descripcion = incidenciaDto.getDescripcion();
                    textViewDescripcion.setText(descripcion);
/*
                    TextView textViewUbicacion = findViewById(R.id.textViewUbicacion);
                    ubicacion = incidenciaDto.getUbicacion();
                    textViewUbicacion.setText(String.valueOf(ubicacion));
*/
                    toggleButton = findViewById(R.id.toggleButton);
                    toggleButton.setChecked(incidenciaDto.isEstado());

                    editTextComentario = findViewById(R.id.editTextComentario);
                    editTextComentario.setText(incidenciaDto.getComentario());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void btnGuardar(View view){
        IncidenciaDto incidenciaDto = new IncidenciaDto();

        //Poner los valores antiguos
        incidenciaDto.setId(id);
        incidenciaDto.setNombre(nombre);
        incidenciaDto.setDescripcion(descripcion);
       // incidenciaDto.setUbicacion(ubicacion);

        //Valor del switch
        estado = toggleButton.isChecked();
        incidenciaDto.setEstado(estado);
        Log.d("infoApp", String.valueOf(estado));

        //Valor del comentario
        comentario = String.valueOf(editTextComentario.getText());
        incidenciaDto.setComentario(comentario);

        databaseReference.child(String.valueOf(id)).setValue(incidenciaDto);

        finish();

    }
}