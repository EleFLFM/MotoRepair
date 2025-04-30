package com.example.repair;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
public class NuevaFacturaActivity extends AppCompatActivity {

    private EditText etNumeroFactura, etCliente, etFecha;
    private Button btnAddProduct, btnAddService, btnSaveInvoice;
    private RecyclerView rvProductos, rvServicios;
    private TextView tvSubtotal, tvTotal;

    private List<ItemProduct> productosSeleccionados = new ArrayList<>();
    private List<ItemServicio> serviciosSeleccionados = new ArrayList<>();
    private InvoiceProductAdapter productAdapter;
    private InvoiceServiceAdapter servicioAdapter;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_factura);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("facturas");

        // Vincular vistas
        etNumeroFactura = findViewById(R.id.textInvoiceNumber);
        etCliente = findViewById(R.id.textCliente);
        etFecha = findViewById(R.id.textInvoiceDate);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddService = findViewById(R.id.btnAddService);
        btnSaveInvoice = findViewById(R.id.btnSaveInvoice);
        rvProductos = findViewById(R.id.rvProductos);
        rvServicios = findViewById(R.id.rvServicios);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTotal = findViewById(R.id.tvTotal);

        // Configurar RecyclerViews
        rvProductos.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new InvoiceProductAdapter(productosSeleccionados, new InvoiceProductAdapter.OnInvoiceProductListener() {
            @Override
            public void onDeleteProduct(int position) {
                productosSeleccionados.remove(position);
                productAdapter.notifyItemRemoved(position);
                calcularTotales();
            }

            @Override
            public void onUpdateQuantity(int position, int newQuantity) {
                ItemProduct producto = productosSeleccionados.get(position);
                producto.setQuantity(newQuantity);
                producto.calculateSubtotal();
                productAdapter.notifyItemChanged(position);
                calcularTotales();
            }
        });
        rvProductos.setAdapter(productAdapter);

        rvServicios.setLayoutManager(new LinearLayoutManager(this));
        servicioAdapter = new InvoiceServiceAdapter(serviciosSeleccionados, new InvoiceServiceAdapter.OnInvoiceServiceListener() {
            @Override
            public void onDeleteService(int position) {
                serviciosSeleccionados.remove(position);
                servicioAdapter.notifyItemRemoved(position);
                calcularTotales();
            }
        });
        rvServicios.setAdapter(servicioAdapter);

        // Configurar fecha
        etFecha.setOnClickListener(v -> mostrarSelectorFecha());

        // Configurar botones
        btnAddProduct.setOnClickListener(v -> mostrarDialogoProductos());
        btnAddService.setOnClickListener(v -> mostrarDialogoServicios());
        btnSaveInvoice.setOnClickListener(v -> guardarFactura());

        // Calcular total inicial
        calcularTotales();
    }

    private void mostrarSelectorFecha() {
        Calendar calendario = Calendar.getInstance();
        int año = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String fechaSeleccionada = year + "-" + (month + 1) + "-" + dayOfMonth;
                    etFecha.setText(fechaSeleccionada);
                },
                año, mes, dia
        );
        datePicker.show();
    }

    private void mostrarDialogoProductos() {
        // Mostrar progreso mientras carga
        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Cargando productos...");
        progress.setCancelable(false);
        progress.show();

        // Consultar productos desde Firebase
        DatabaseReference productosRef = FirebaseDatabase.getInstance().getReference("productos");
        productosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progress.dismiss();
                List<Producto> productosDisponibles = new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Producto producto = ds.getValue(Producto.class);
                    producto.setId(ds.getKey()); // Asignar el ID de Firebase
                    productosDisponibles.add(producto);
                }

                if (productosDisponibles.isEmpty()) {
                    Toast.makeText(NuevaFacturaActivity.this,
                            "No hay productos disponibles", Toast.LENGTH_SHORT).show();
                    return;
                }

                mostrarDialogoSeleccionProductos(productosDisponibles);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progress.dismiss();
                Toast.makeText(NuevaFacturaActivity.this,
                        "Error al cargar productos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoSeleccionProductos(List<Producto> productos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar Producto");

        // Convertir la lista a array de Strings para el diálogo
        String[] nombresProductos = new String[productos.size()];
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            nombresProductos[i] = p.getNombre() + " - Stock: " + p.getCantidad() + " - $" + p.getPrecio();
        }

        builder.setItems(nombresProductos, (dialog, which) -> {
            Producto productoSeleccionado = productos.get(which);
            mostrarDialogoCantidadProducto(productoSeleccionado);
        });

        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    private void mostrarDialogoCantidadProducto(Producto producto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cantidad para " + producto.getNombre());

        // Inflar vista personalizada
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_cantidad_producto, null);
        EditText etCantidad = view.findViewById(R.id.etCantidad);
        TextView tvStock = view.findViewById(R.id.tvStock);

        tvStock.setText("Disponible: " + producto.getCantidad());
        etCantidad.setText("1");
        etCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);

        builder.setView(view);
        builder.setPositiveButton("Agregar", (dialog, which) -> {
            try {
                int cantidad = Integer.parseInt(etCantidad.getText().toString());

                if (cantidad <= 0) {
                    Toast.makeText(this, "La cantidad debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (cantidad > producto.getCantidad()) {
                    Toast.makeText(this, "No hay suficiente stock", Toast.LENGTH_SHORT).show();
                    return;
                }

                agregarProductoAFactura(producto, cantidad);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Ingrese una cantidad válida", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    private void agregarProductoAFactura(Producto producto, int cantidad) {
        // Crear ItemProduct para la factura
        ItemProduct item = new ItemProduct(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                cantidad
        );

        // Verificar si el producto ya está en la factura
        for (ItemProduct p : productosSeleccionados) {
            if (p.getProductId().equals(producto.getId())) {
                // Actualizar cantidad si ya existe
                p.setQuantity(p.getQuantity() + cantidad);
                productAdapter.notifyDataSetChanged();
                calcularTotales();
                return;
            }
        }

        // Agregar nuevo producto
        productosSeleccionados.add(item);
        productAdapter.notifyItemInserted(productosSeleccionados.size() - 1);
        calcularTotales();
    }
    private void mostrarDialogoServicios() {
        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Cargando servicios...");
        progress.setCancelable(false);
        progress.show();

        DatabaseReference serviciosRef = FirebaseDatabase.getInstance().getReference("servicios");
        serviciosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progress.dismiss();
                List<Servicio> serviciosDisponibles = new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Servicio servicio = ds.getValue(Servicio.class);
                    servicio.setId(ds.getKey());
                    serviciosDisponibles.add(servicio);
                }

                if (serviciosDisponibles.isEmpty()) {
                    Toast.makeText(NuevaFacturaActivity.this,
                            "No hay servicios disponibles", Toast.LENGTH_SHORT).show();
                    return;
                }

                mostrarDialogoSeleccionServicios(serviciosDisponibles);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progress.dismiss();
                Toast.makeText(NuevaFacturaActivity.this,
                        "Error al cargar servicios: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoSeleccionServicios(List<Servicio> servicios) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar Servicio");

        String[] nombresServicios = new String[servicios.size()];
        for (int i = 0; i < servicios.size(); i++) {
            Servicio s = servicios.get(i);
            nombresServicios[i] = s.getNombre() + " - $" + s.getPrecio();
        }

        builder.setItems(nombresServicios, (dialog, which) -> {
            Servicio servicioSeleccionado = servicios.get(which);
            agregarServicioAFactura(servicioSeleccionado);
        });

        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    private void agregarServicioAFactura(Servicio servicio) {
        // Crear ItemServicio para la factura
        ItemServicio item = new ItemServicio(
                servicio.getId(),
                servicio.getNombre(),
                servicio.getDescripcion(),
                servicio.getPrecio()
        );

        // Verificar si el servicio ya está en la factura
        for (ItemServicio s : serviciosSeleccionados) {
            if (s.getServicioId().equals(servicio.getId())) {
                Toast.makeText(this, "Este servicio ya fue agregado", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Agregar nuevo servicio
        serviciosSeleccionados.add(item);
        servicioAdapter.notifyItemInserted(serviciosSeleccionados.size() - 1);
        calcularTotales();
    }
    private void calcularTotales() {
        double subtotal = 0.0;

        // Sumar productos
        for (ItemProduct producto : productosSeleccionados) {
            subtotal += producto.getSubtotal();
        }

        // Sumar servicios
        for (ItemServicio servicio : serviciosSeleccionados) {
            subtotal += servicio.getPrecio();
        }

        tvSubtotal.setText(String.format(Locale.getDefault(), "%.2f", subtotal));
        tvTotal.setText(String.format(Locale.getDefault(), "%.2f", subtotal));
    }

    private void guardarFactura() {
        // Validaciones mejoradas con feedback visual
        if (!validarCampos()) {
            return;
        }

        // Mostrar progreso
        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Guardando factura...");
        progress.setCancelable(false);
        progress.show();

        // Crear objeto Factura
        Factura factura = new Factura();
        factura.setNumeroFactura(etNumeroFactura.getText().toString().trim());
        factura.setNombreCliente(etCliente.getText().toString().trim());
        factura.setFecha(etFecha.getText().toString().trim());
        factura.setProductos(new ArrayList<>(productosSeleccionados)); // Copia defensiva
        factura.setServicios(new ArrayList<>(serviciosSeleccionados)); // Copia defensiva
//        factura.setUserId("");
        factura.calcularTotal();

        // Guardar en Firebase
        databaseRef.push().setValue(factura)
                .addOnCompleteListener(task -> {
                    progress.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "✅ Factura guardada", Toast.LENGTH_SHORT).show();
                        actualizarInventario();
                        finish();
                    } else {
                        mostrarError("Error al guardar: " + task.getException().getMessage());
                    }
                });
    }

    private boolean validarCampos() {
        boolean valido = true;

        if (etNumeroFactura.getText().toString().trim().isEmpty()) {
            etNumeroFactura.setError("Número requerido");
            valido = false;
        }

        if (etCliente.getText().toString().trim().isEmpty()) {
            etCliente.setError("Cliente requerido");
            valido = false;
        }

        if (etFecha.getText().toString().trim().isEmpty()) {
            etFecha.setError("Fecha requerida");
            valido = false;
        }

        if (productosSeleccionados.isEmpty() && serviciosSeleccionados.isEmpty()) {
            Toast.makeText(this, "Agregue al menos un producto o servicio", Toast.LENGTH_LONG).show();
            valido = false;
        }

        return valido;
    }

    private void mostrarError(String mensaje) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(mensaje)
                .setPositiveButton("OK", null)
                .show();
    }
    private void actualizarInventario() {
        if (productosSeleccionados.isEmpty()) return;

        DatabaseReference productosRef = FirebaseDatabase.getInstance().getReference("productos");

        productosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (ItemProduct item : productosSeleccionados) {
                    String productId = item.getProductId();
                    if (snapshot.hasChild(productId)) {
                        Producto producto = snapshot.child(productId).getValue(Producto.class);
                        if (producto != null) {
                            int nuevaCantidad = producto.getCantidad() - item.getQuantity();
                            if (nuevaCantidad >= 0) {
                                producto.setCantidad(nuevaCantidad);
                                productosRef.child(productId).setValue(producto);
                            } else {
                                Log.e("Inventario", "Stock insuficiente para: " + productId);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error al actualizar inventario: " + error.getMessage());
            }
        });
    }
}