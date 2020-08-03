package com.example.proyectogrupal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CrearAcc extends AppCompatActivity {
    EditText editemail,editpass,edituser;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_acc);
        editemail=findViewById(R.id.email);
        editpass=findViewById(R.id.pass);
        edituser=findViewById(R.id.user);

        mAuth = FirebaseAuth.getInstance();
    }
    public void validarPucp(View view){

        String validarEmail=editemail.getText().toString();
        String password=editpass.getText().toString();

        if(validarEmail.isEmpty()){
            Toast.makeText(CrearAcc.this,"ERROR AL REGISTRAR UN CORREO VACIO",Toast.LENGTH_SHORT).show();
            editemail.requestFocus();
            return;
        }else {
            String[] dominio=validarEmail.split("@");
            if(dominio[1].equals("pucp.pe")||dominio[1].equals("pucp.edu.pe")){

                //
              crear();

                //


            }else{
                Toast.makeText(CrearAcc.this,"solo dominiio pucp",Toast.LENGTH_SHORT).show();
                editemail.setText(" ");
                editemail.requestFocus();
                return;
            }
        }





    }
    public void crear(){

        String validarEmail=editemail.getText().toString();
        String password=editpass.getText().toString();
        mAuth.createUserWithEmailAndPassword(validarEmail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("prueba", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(CrearAcc.this,"registro completado",Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("prueba", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CrearAcc.this, "usuario ya EXISTE",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });





    }

}