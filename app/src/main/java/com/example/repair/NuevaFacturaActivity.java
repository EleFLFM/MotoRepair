package com.example.repair;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NuevaFacturaActivity extends AppCompatActivity {

    private EditText etCliente, etDescripcion, etTotal;
    private DatabaseReference facturasRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_factura);

        facturasRef = FirebaseDatabase.getInstance().getReference("facturas");

        etCliente = findViewById(R.id.etCliente);
        etDescripcion = findViewById(R.id.etDescripcion);
        etTotal = findViewById(R.id.etTotal);

        Button btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(v -> guardarFactura());
    }

    private void guardarFactura() {
        String cliente = etCliente.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String totalStr = etTotal.getText().toString().trim();

        if (cliente.isEmpty() || descripcion.isEmpty() || totalStr.isEmpty()) {
            // Mostrar error
            return;
        }

        try {
            double total = Double.parseDouble(totalStr);
            String fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

            Factura nuevaFactura = new Factura(cliente, fecha, total, descripcion);
            String facturaId = facturasRef.push().getKey();

            if (facturaId != null) {
                facturasRef.child(facturaId).setValue(nuevaFactura)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                finish(); // Cerrar actividad después de guardar
                            } else {
                                // Mostrar error
                            }
                        });
            }
        } catch (NumberFormatException e) {
            etTotal.setError("Ingrese un monto válido");
        }
    }
}
