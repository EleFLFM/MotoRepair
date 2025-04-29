package com.example.repair;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FacturacionActivity extends AppCompatActivity implements FacturaAdapter.OnFacturaClickListener {

    private RecyclerView recyclerView;
    private FacturaAdapter adapter;
    private List<Factura> facturas = new ArrayList<>();
    private DatabaseReference facturasRef;
    private TextInputEditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturacion);

        // Inicializar Firebase
        facturasRef = FirebaseDatabase.getInstance().getReference("facturas");

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FacturaAdapter(facturas, this);
        recyclerView.setAdapter(adapter);

        // Configurar búsqueda
        etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarFacturas(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Botón para agregar nueva factura
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(FacturacionActivity.this, NuevaFacturaActivity.class);
            startActivity(intent);
        });

        // Cargar facturas desde Firebase
        cargarFacturas();
    }

    private void cargarFacturas() {
        facturasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                facturas.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Factura factura = dataSnapshot.getValue(Factura.class);
                    if (factura != null) {
                        factura.setId(dataSnapshot.getKey());
                        facturas.add(factura);
                    }
                }
                adapter.updateData(facturas);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar error
            }
        });
    }

    private void filtrarFacturas(String texto) {
        List<Factura> facturasFiltradas = new ArrayList<>();
        for (Factura factura : facturas) {
            if (factura.getCliente().toLowerCase().contains(texto.toLowerCase()) ||
                    factura.getDescripcion().toLowerCase().contains(texto.toLowerCase())) {
                facturasFiltradas.add(factura);
            }
        }
        adapter.updateData(facturasFiltradas);
    }

    @Override
    public void onFacturaClick(Factura factura) {
        // Abrir actividad de detalle de factura
        Intent intent = new Intent(this, DetalleFacturaActivity.class);
        intent.putExtra("FACTURA_ID", factura.getId());
        startActivity(intent);
    }
}