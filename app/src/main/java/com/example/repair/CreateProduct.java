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

public class CreateProduct extends AppCompatActivity {

    private TextInputEditText etProductName, etProductPrice, etProductQuantity;
    private Button btnAddProduct;
    private DatabaseReference productosRef;
    private String productoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        // Inicializar Firebase
        productosRef = FirebaseDatabase.getInstance().getReference("productos");

        // Vincular vistas
        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductQuantity = findViewById(R.id.etProductQuantity);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        // Verificar si estamos editando
        productoId = getIntent().getStringExtra("PRODUCTO_ID");
        if (productoId != null) {
            // Modo edición
            btnAddProduct.setText("ACTUALIZAR");
            etProductName.setText(getIntent().getStringExtra("PRODUCTO_NOMBRE"));
            etProductPrice.setText(String.valueOf(getIntent().getDoubleExtra("PRODUCTO_PRECIO", 0)));
            etProductQuantity.setText(String.valueOf(getIntent().getIntExtra("PRODUCTO_CANTIDAD", 0)));
        }

        btnAddProduct.setOnClickListener(v -> guardarProducto());
    }

    private void guardarProducto() {
        String nombre = etProductName.getText().toString().trim();
        String precioStr = etProductPrice.getText().toString().trim();
        String cantidadStr = etProductQuantity.getText().toString().trim();

        // Validaciones
        if (nombre.isEmpty()) {
            etProductName.setError("Ingrese el nombre del producto");
            return;
        }

        if (precioStr.isEmpty()) {
            etProductPrice.setError("Ingrese el precio");
            return;
        }

        if (cantidadStr.isEmpty()) {
            etProductQuantity.setError("Ingrese la cantidad");
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            int cantidad = Integer.parseInt(cantidadStr);

            if (productoId != null) {
                // Actualizar producto existente
                Map<String, Object> updates = new HashMap<>();
                updates.put("nombre", nombre);
                updates.put("precio", precio);
                updates.put("cantidad", cantidad);

                productosRef.child(productoId).updateChildren(updates)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Error al actualizar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                // Crear nuevo producto
                Producto producto = new Producto(nombre, precio, cantidad);
                String newProductId = productosRef.push().getKey();

                if (newProductId != null) {
                    productosRef.child(newProductId).setValue(producto)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Error al agregar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingrese valores numéricos válidos", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelar(View view) {
        finish();
    }
}