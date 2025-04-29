package com.example.repair;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnRegister;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validaciones
        if(email.isEmpty()){
            etEmail.setError("El correo electrónico es requerido");
            etEmail.requestFocus();
            return;
        }



        if(password.isEmpty()){
            etPassword.setError("La contraseña es requerida");
            etPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            etPassword.setError("La contraseña debe tener al menos 6 caracteres");
            etPassword.requestFocus();
            return;
        }

        // Crear objeto User
        User user = new User(email, password);

        // Guardar en Firebase Database
        // Usamos el email como clave (reemplazando caracteres especiales)
        String key = email.replace(".", ","); // Firebase no permite puntos en las claves

        mDatabase.child(key).setValue(user)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,
                                "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                        finish(); // Cierra la actividad de registro
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Error al registrar: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}