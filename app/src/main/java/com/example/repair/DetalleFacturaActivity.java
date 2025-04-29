package com.example.repair;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DetalleFacturaActivity extends AppCompatActivity {

    private TextView tvCliente, tvFecha, tvEstado, tvTotal, tvDescripcion;
    private Button btnEditar, btnEliminar, btnAgregarItem;
    private RecyclerView rvItems;
    private DatabaseReference facturasRef;
    private String facturaId;
    private FacturaItemAdapter itemAdapter;
    private List<FacturaItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_factura);

        // Obtener ID de la factura del Intent
        facturaId = getIntent().getStringExtra("FACTURA_ID");
        if (facturaId == null) {
            Toast.makeText(this, "Error: No se encontró la factura", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar Firebase
        facturasRef = FirebaseDatabase.getInstance().getReference("facturas").child(facturaId);

        // Vincular vistas
        tvCliente = findViewById(R.id.tvCliente);
        tvFecha = findViewById(R.id.tvFecha);
        tvEstado = findViewById(R.id.tvEstado);
        tvTotal = findViewById(R.id.tvTotal);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnAgregarItem = findViewById(R.id.btnAgregarItem);
        rvItems = findViewById(R.id.rvItems);

        // Configurar RecyclerView para items
        itemAdapter = new FacturaItemAdapter(items);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(itemAdapter);

        // Configurar botones
        btnEditar.setOnClickListener(v -> editarFactura());
        btnEliminar.setOnClickListener(v -> confirmarEliminacion());
        btnAgregarItem.setOnClickListener(v -> agregarItem());

        // Cargar datos de la factura
        cargarFactura();
    }

    private void cargarFactura() {
        facturasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Factura factura = snapshot.getValue(Factura.class);
                if (factura != null) {
                    mostrarFactura(factura);
                    cargarItems();
                } else {
                    Toast.makeText(DetalleFacturaActivity.this, "Factura no encontrada", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetalleFacturaActivity.this, "Error al cargar factura", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarFactura(Factura factura) {
        tvCliente.setText("Cliente: " + factura.getCliente());
        tvFecha.setText("Fecha: " + factura.getFecha());
        tvEstado.setText(factura.getEstado());
        tvTotal.setText("Total: $" + String.format("%.2f", factura.getTotal()));
        tvDescripcion.setText("Descripción: " + factura.getDescripcion());

        // Cambiar color según estado
        int estadoColor = factura.getEstado().equals("pagada") ? R.color.green :
                factura.getEstado().equals("cancelada") ? R.color.red : R.color.orange;
        tvEstado.setBackgroundColor(getResources().getColor(estadoColor));
    }

    private void cargarItems() {
        facturasRef.child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    FacturaItem item = itemSnapshot.getValue(FacturaItem.class);
                    if (item != null) {
                        item.setId(itemSnapshot.getKey());
                        items.add(item);
                    }
                }
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetalleFacturaActivity.this, "Error al cargar items", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editarFactura() {
        // Implementar lógica para editar
        Toast.makeText(this, "Editar factura", Toast.LENGTH_SHORT).show();
    }

    private void confirmarEliminacion() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de eliminar esta factura?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarFactura())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarFactura() {
        facturasRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Factura eliminada", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al eliminar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void agregarItem() {
        // Implementar lógica para agregar items
        Toast.makeText(this, "Agregar nuevo item", Toast.LENGTH_SHORT).show();
    }
}