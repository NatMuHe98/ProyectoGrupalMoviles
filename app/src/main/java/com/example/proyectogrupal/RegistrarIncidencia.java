package com.example.proyectogrupal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectogrupal.entidades.IncidenciaDto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegistrarIncidencia extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextTextNombreIncidencia, editTextDescripcionIncidencia;
    private ImageView imageViewAgregarFoto;
    private Button buttonEnviarIncidencia, buttonSubirFoto;
    private ProgressBar progressbar_SubirFoto;

    private Uri myImageUri;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private StorageTask storageTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_incidencia);

        editTextTextNombreIncidencia = findViewById(R.id.editTextTextNombreIncidencia);
        editTextDescripcionIncidencia = findViewById(R.id.editTextDescripcionIncidencia);
        imageViewAgregarFoto = findViewById(R.id.imageViewAgregarFoto);
        buttonEnviarIncidencia = findViewById(R.id.buttonEnviarIncidencia);
        buttonSubirFoto = findViewById(R.id.buttonSubirFoto);
        progressbar_SubirFoto = findViewById(R.id.progressbar_SubirFoto);

        storageReference = FirebaseStorage.getInstance().getReference("Incidencias");
        databaseReference = FirebaseDatabase.getInstance().getReference("Incidencias");

        buttonSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ElegirImagen();
            }
        });

        buttonEnviarIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(storageTask != null && storageTask.isInProgress()){
                    Toast.makeText(RegistrarIncidencia.this, "Envío en proceso", Toast.LENGTH_SHORT).show();
                }else{
                    EnviarIncidencia();
                }
            }
        });

    }

    private String extensionArchivo (Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void EnviarIncidencia() {
        final String nombre = editTextTextNombreIncidencia.getText().toString();
        final String descripcion = editTextDescripcionIncidencia.getText().toString();
        final int ubicacion = 1;
        final boolean estado = false;
        final String comentario = null;

        if (nombre.isEmpty()){
            editTextTextNombreIncidencia.setError("Nombre no debe quedar vacío");
        }else if (descripcion.isEmpty()){
            editTextDescripcionIncidencia.setError("Descripción no debe quedar vacio");
        }else{

            if (myImageUri != null){
                //ASIGNACIÓN DEL NOMBRE DE LA IMAGEN Y SU EXTENSIÓN
                StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                        + "." + extensionArchivo(myImageUri));
                storageTask = fileReference.putFile(myImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressbar_SubirFoto.setProgress(0);
                                    }
                                }, 500);
                                Toast.makeText(RegistrarIncidencia.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                String id = databaseReference.push().getKey();
                                String url = taskSnapshot.getUploadSessionUri().toString();
                                if(id!=null){
                                    //SE AGREGAN LOS DATOS OBTENIDOS A DATABSE 
                                    IncidenciaDto incidenciaDto = new IncidenciaDto(id, nombre, url, descripcion, ubicacion, estado, comentario );
                                    databaseReference.child(id).setValue(incidenciaDto);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegistrarIncidencia.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                                progressbar_SubirFoto.setProgress((int) progress);
                            }
                        });

            }else{
                Toast.makeText(RegistrarIncidencia.this, "No se seleccionó fotografía", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void ElegirImagen() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            myImageUri = data.getData();
            Picasso.with(this).load(myImageUri).into(imageViewAgregarFoto);

        }
    }
}
