package com.example.repair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FacturacionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FacturaAdapter adapter;
    private List<Factura> facturas = new ArrayList<>();
    private DatabaseReference facturasRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturacion);

        // Inicializar Firebase
        facturasRef = FirebaseDatabase.getInstance().getReference("facturas");

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FacturaAdapter(facturas);
        recyclerView.setAdapter(adapter);

        // Botón para agregar nueva factura
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(FacturacionActivity.this, NuevaFacturaActivity.class));
        });

        // Cargar facturas
        cargarFacturas();
    }

    private void cargarFacturas() {
        facturasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                facturas.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Factura factura = snapshot.getValue(Factura.class);
                    if (factura != null) {
                        factura.setUserId(snapshot.getKey());
                        facturas.add(factura);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FacturacionActivity.this, "Error al cargar items", Toast.LENGTH_SHORT).show();
            }
        });
    }
}