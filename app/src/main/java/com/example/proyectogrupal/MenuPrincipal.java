package com.example.proyectogrupal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuPrincipal extends AppCompatActivity {
    EditText Email, pass;
    private FirebaseAuth mAuth;
    String usuario;
    FirebaseUser currentUser;
    int resultcode = 5;
    ArrayList<Object> objectArrayList1;
    private DatabaseReference mDatabase;
    ArrayList<String> infraestructura = new ArrayList<String>();
    GenericTypeIndicator<HashMap<String, Object>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Object>>() {};
    int c=0,a=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        Email = findViewById(R.id.editUser);
        pass = findViewById(R.id.editPass);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void acceder(View view) {
        final String validarEmail = Email.getText().toString();
        String password = pass.getText().toString();

        if (validarEmail.isEmpty()) {
            Toast.makeText(MenuPrincipal.this, "ERROR AL ingresar con un CORREO VACIO", Toast.LENGTH_SHORT).show();
            Email.requestFocus();
            return;
        } else {
            final String[] dominio = validarEmail.split("@");
            if (dominio[1].equals("pucp.pe") || dominio[1].equals("pucp.edu.pe")) {

                mAuth.signInWithEmailAndPassword(validarEmail, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("pruebiña", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    currentUser = mAuth.getCurrentUser();

                                    if (!currentUser.isEmailVerified()) {
                                        currentUser.sendEmailVerification()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(MenuPrincipal.this,
                                                                "Se le ha enviado un correo para validar su cuenta",
                                                                Toast.LENGTH_SHORT).show();
                                                        Log.d("infoApp", "Envío de correo exitoso");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("infoApp", "error al enviar el correo");
                                                    }
                                                });


                                    } else {
                                        updateUI(user);
                                        mDatabase.child("INFRAESTRUCTURA").addValueEventListener(new ValueEventListener(){

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                int contar=0;
                                                if(dataSnapshot.exists()){
                                                    GenericTypeIndicator<HashMap<String, Object>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Object>>() {};
                                                    Map<String, Object> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                                                    ArrayList<Object> objectArrayList = new ArrayList<Object>(objectHashMap.values());
                                                    objectArrayList1=objectArrayList;
                                                    Log.d("infraestr", String.valueOf(objectArrayList));
                                                    for(int i=0;i<objectArrayList1.size();i++){
                                                        Log.d("esta validado?", validarEmail);
                                                        if(validarEmail.equals(objectArrayList.get(i))){
                                                            Log.d("esta validado?", String.valueOf(objectArrayList.get(i)));
                                                            contar=contar+1;
                                                            Log.d("esta validado?", String.valueOf(c));
                                                        }
                                                    }
                                                    if(contar==0){



                                                        Intent intent = new Intent(MenuPrincipal.this, MainActivity3.class);
                                                        intent.putExtra("uid", usuario);
                                                        startActivityForResult(intent, 3);
                                                    }else{


                                                        Intent intent = new Intent(MenuPrincipal.this, MenuInfraestructuraActivity.class);
                                                        intent.putExtra("uid", usuario);
                                                        startActivityForResult(intent, 4);
                                                    }


                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("pruebiña 2", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MenuPrincipal.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);

                                }


                            }
                        });


            } else {
                Toast.makeText(MenuPrincipal.this, "SOLO DOMINIO PUCP", Toast.LENGTH_SHORT).show();

                Email.requestFocus();
                return;
            }
        }

    }



    public void crear(View view) {
        Intent intent = new Intent(MenuPrincipal.this, CrearAcc.class);
        startActivityForResult(intent, 1);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        updateUI(currentUser);
        if (currentUser != null && resultcode > 4) {
            Intent intent = new Intent(MenuPrincipal.this, MainActivity3.class);
            intent.putExtra("uid", usuario);
            Log.d("usuario", currentUser.getUid());
            startActivityForResult(intent, 2);

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        Email.setText("");
        pass.setText("");
        if (requestCode == 2 || requestCode == 3 || requestCode == 4) {
            updateUI(null);
            Log.d("salida exitosa", mAuth.getUid());
            resultCode = requestCode;
            FirebaseAuth.getInstance().signOut();

            currentUser = mAuth.getCurrentUser();

            updateUI(currentUser);
            Log.d("usuariossss", String.valueOf(currentUser));


        }

    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            usuario = user.getUid();
        } else {
            usuario = null;
        }

    }
}