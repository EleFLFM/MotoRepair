package com.example.repair;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ServiceActivity extends AppCompatActivity {

    private RecyclerView servicesRecyclerView;
    private ServiceAdapter serviceAdapter;
    private List<Servicio> servicios = new ArrayList<>();
    private DatabaseReference serviciosRef;
    private ImageButton addButton, refreshButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_service);

        // Configurar insets para edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar Firebase
        serviciosRef = FirebaseDatabase.getInstance().getReference("servicios");

        // Vincular vistas con los IDs correctos
        servicesRecyclerView = findViewById(R.id.servicesRecyclerView);
        addButton = findViewById(R.id.addButton);
        refreshButton = findViewById(R.id.refreshButton);
        deleteButton = findViewById(R.id.deleteButton);

        // Configurar RecyclerView
        servicesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceAdapter = new ServiceAdapter(servicios, new ServiceAdapter.OnServiceClickListener() {
            @Override
            public void onServiceClick(Servicio servicio) {
                abrirEditarServicio(servicio);
            }

            @Override
            public void onServiceLongClick(Servicio servicio) {
                mostrarOpcionesServicio(servicio);
            }
        });
        servicesRecyclerView.setAdapter(serviceAdapter);

        // Configurar botones con los IDs correctos
        addButton.setOnClickListener(v -> abrirCrearServicio());
        refreshButton.setOnClickListener(v -> cargarServicios());
        deleteButton.setOnClickListener(v -> mostrarModoEliminacion());

        // Cargar servicios iniciales
        cargarServicios();
    }

    private void cargarServicios() {
        serviciosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                servicios.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Servicio servicio = snapshot.getValue(Servicio.class);
                    if (servicio != null) {
                        servicio.setId(snapshot.getKey());
                        servicios.add(servicio);
                    }
                }
                serviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ServiceActivity.this, "Error al cargar servicios: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirCrearServicio() {
        startActivity(new Intent(this, CreateService.class));
    }

    private void abrirEditarServicio(Servicio servicio) {
        Intent intent = new Intent(this, CreateService.class);
        intent.putExtra("SERVICIO_ID", servicio.getId());
        intent.putExtra("SERVICIO_NOMBRE", servicio.getNombre());
        intent.putExtra("SERVICIO_DESCRIPCION", servicio.getDescripcion());
        intent.putExtra("SERVICIO_PRECIO", servicio.getPrecio());
        startActivity(intent);
    }

    private void mostrarOpcionesServicio(Servicio servicio) {
        Toast.makeText(this, "Servicio seleccionado: " + servicio.getNombre(), Toast.LENGTH_SHORT).show();
    }

    private void mostrarModoEliminacion() {
        Toast.makeText(this, "Modo eliminación activado", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarServicios();
    }
}