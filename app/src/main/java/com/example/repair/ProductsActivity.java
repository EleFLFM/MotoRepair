package com.example.repair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class ProductsActivity extends AppCompatActivity {

    private RecyclerView productsRecyclerView;
    private ProductAdapter productAdapter;
    private List<Producto> productos = new ArrayList<>();
    private DatabaseReference productosRef;
    private ImageButton addButton, refreshButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        // Inicializar Firebase
        productosRef = FirebaseDatabase.getInstance().getReference("productos");

        // Vincular vistas
        productsRecyclerView = findViewById(R.id.rvRegistros);
        addButton = findViewById(R.id.btnAgregar);
        refreshButton = findViewById(R.id.btnActualizar);
        deleteButton = findViewById(R.id.btnEliminar);

        // Configurar RecyclerView
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(productos, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Producto producto) {
                abrirEditarProducto(producto);
            }

            @Override
            public void onProductLongClick(Producto producto) {
                mostrarOpcionesProducto(producto);
            }
        });
        productsRecyclerView.setAdapter(productAdapter);

        // Configurar botones
        addButton.setOnClickListener(v -> abrirCrearProducto());
        refreshButton.setOnClickListener(v -> cargarProductos());
        deleteButton.setOnClickListener(v -> mostrarModoEliminacion());

        // Cargar productos iniciales
        cargarProductos();
    }

    private void cargarProductos() {
        productosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productos.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Producto producto = snapshot.getValue(Producto.class);
                    if (producto != null) {
                        producto.setId(snapshot.getKey());
                        productos.add(producto);
                    }
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProductsActivity.this, "Error al cargar productos: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirCrearProducto() {
        startActivity(new Intent(this, CreateProduct.class));
    }

    private void abrirEditarProducto(Producto producto) {
        Intent intent = new Intent(this, CreateProduct.class);
        intent.putExtra("PRODUCTO_ID", producto.getId());
        intent.putExtra("PRODUCTO_NOMBRE", producto.getNombre());
        intent.putExtra("PRODUCTO_PRECIO", producto.getPrecio());
        intent.putExtra("PRODUCTO_CANTIDAD", producto.getCantidad());
        startActivity(intent);
    }

    private void mostrarOpcionesProducto(Producto producto) {
        Toast.makeText(this, "Producto seleccionado: " + producto.getNombre(), Toast.LENGTH_SHORT).show();
    }

    private void mostrarModoEliminacion() {
        Toast.makeText(this, "Modo eliminación activado", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarProductos();
    }
}