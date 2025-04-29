package com.example.repair;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateService extends AppCompatActivity {

    private TextInputEditText etNombre, etDescripcion, etPrecio;
    private Button btnCrearServicio;
    private DatabaseReference serviciosRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);

        // Inicializar Firebase
        serviciosRef = FirebaseDatabase.getInstance().getReference("servicios");

        // Vincular vistas
        etNombre = findViewById(R.id.etNombreServicio);
        etDescripcion = findViewById(R.id.etDescripcionServicio);
        etPrecio = findViewById(R.id.etPrecioServicio);
        btnCrearServicio = findViewById(R.id.btnCrearServicio);

        // Configurar botón
        btnCrearServicio.setOnClickListener(v -> crearServicio());
        // En el método onCreate de CreateService:
        String servicioId = getIntent().getStringExtra("SERVICIO_ID");
        if (servicioId != null) {
            // Modo edición
            String nombre = getIntent().getStringExtra("SERVICIO_NOMBRE");
            String descripcion = getIntent().getStringExtra("SERVICIO_DESCRIPCION");
            double precio = getIntent().getDoubleExtra("SERVICIO_PRECIO", 0);

            etNombre.setText(nombre);
            etDescripcion.setText(descripcion);
            etPrecio.setText(String.valueOf(precio));
            btnCrearServicio.setText("Actualizar Servicio");

            btnCrearServicio.setOnClickListener(v -> actualizarServicio(servicioId));
        }

    }
    private void actualizarServicio(String servicioId) {
        // Similar a crearServicio() pero usando updateChildren()
        Map<String, Object> updates = new HashMap<>();
        updates.put("nombre", etNombre.getText().toString().trim());
        updates.put("descripcion", etDescripcion.getText().toString().trim());
        updates.put("precio", Double.parseDouble(etPrecio.getText().toString().trim()));

        serviciosRef.child(servicioId).updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Servicio actualizado", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
    private void crearServicio() {
        // Obtener valores de los campos
        String nombre = Objects.requireNonNull(etNombre.getText()).toString().trim();
        String descripcion = Objects.requireNonNull(etDescripcion.getText()).toString().trim();
        String precioStr = Objects.requireNonNull(etPrecio.getText()).toString().trim();

        // Validar campos
        if (nombre.isEmpty()) {
            etNombre.setError("Ingrese el nombre del servicio");
            etNombre.requestFocus();
            return;
        }

        if (descripcion.isEmpty()) {
            etDescripcion.setError("Ingrese una descripción");
            etDescripcion.requestFocus();
            return;
        }

        if (precioStr.isEmpty()) {
            etPrecio.setError("Ingrese el precio");
            etPrecio.requestFocus();
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);

            // Crear objeto Servicio
            Servicio servicio = new Servicio();
            servicio.setNombre(nombre);
            servicio.setDescripcion(descripcion);
            servicio.setPrecio(precio);

            // Generar ID único y guardar en Firebase
            String servicioId = serviciosRef.push().getKey();
            if (servicioId != null) {
                servicio.setId(servicioId);
                serviciosRef.child(servicioId).setValue(servicio)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(CreateService.this, "Servicio creado exitosamente", Toast.LENGTH_SHORT).show();
                            finish(); // Cerrar la actividad después de crear
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(CreateService.this, "Error al crear servicio: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        } catch (NumberFormatException e) {
            etPrecio.setError("Ingrese un precio válido");
            etPrecio.requestFocus();
        }
    }

    // Método para cancelar/volver
    public void cancelarCreacion(View view) {
        finish();
    }
}