package com.example.repair;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateProduct extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final String IMGBB_API_KEY = "0d3365758b4efd71f001eec519683d86";

    private TextInputEditText etProductName, etProductPrice, etProductQuantity;
    private Button btnAddProduct, btnSelectImage, btnTakePhoto;
    private ImageView ivProductImage;
    private DatabaseReference productosRef;
    private String productoId;
    private Bitmap selectedImageBitmap;
    private String currentImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        productosRef = FirebaseDatabase.getInstance().getReference("productos");

        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductQuantity = findViewById(R.id.etProductQuantity);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        ivProductImage = findViewById(R.id.ivProductImage);

        productoId = getIntent().getStringExtra("PRODUCTO_ID");

        if (productoId != null) {
            btnAddProduct.setText("ACTUALIZAR");
            cargarDatosProducto(productoId);
        }

        btnAddProduct.setOnClickListener(v -> guardarProducto());
        btnSelectImage.setOnClickListener(v -> openImageChooser());
        btnTakePhoto.setOnClickListener(v -> takePhoto());
    }

    private void cargarDatosProducto(String id) {
        productosRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nombre = snapshot.child("nombre").getValue(String.class);
                    String precio = String.valueOf(snapshot.child("precio").getValue());
                    String cantidad = String.valueOf(snapshot.child("cantidad").getValue());
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);

                    etProductName.setText(nombre);
                    etProductPrice.setText(precio);
                    etProductQuantity.setText(cantidad);
                    currentImageUrl = imageUrl;

                    Glide.with(getApplicationContext())
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_camera)
                            .error(R.drawable.ic_camera)
                            .into(ivProductImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CreateProduct.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    ivProductImage.setImageBitmap(selectedImageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAMERA_REQUEST && data != null) {
                selectedImageBitmap = (Bitmap) data.getExtras().get("data");
                ivProductImage.setImageBitmap(selectedImageBitmap);
            }
        }
    }

    private void guardarProducto() {
        String nombre = etProductName.getText().toString().trim();
        String precioStr = etProductPrice.getText().toString().trim();
        String cantidadStr = etProductQuantity.getText().toString().trim();

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

            if (selectedImageBitmap != null) {
                uploadImageAndSaveProduct(nombre, precio, cantidad);
            } else {
                saveProductToDatabase(nombre, precio, cantidad, currentImageUrl);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingrese valores numéricos válidos", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageAndSaveProduct(String nombre, double precio, int cantidad) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "product_image.jpg",
                        RequestBody.create(MediaType.parse("image/*jpg"), imageBytes))
                .addFormDataPart("key", IMGBB_API_KEY)
                .build();

        Request request = new Request.Builder()
                .url("https://api.imgbb.com/1/upload")
                .post(requestBody)
                .build();

        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(CreateProduct.this, "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    String imageUrl = parseImageUrlFromResponse(responseData);
                    runOnUiThread(() -> saveProductToDatabase(nombre, precio, cantidad, imageUrl));
                } else {
                    runOnUiThread(() -> Toast.makeText(CreateProduct.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private String parseImageUrlFromResponse(String response) {
        try {
            int startIndex = response.indexOf("\"url\":\"") + 7;
            int endIndex = response.indexOf("\"", startIndex);
            return response.substring(startIndex, endIndex).replace("\\/", "/");
        } catch (Exception e) {
            return "";
        }
    }

    private void saveProductToDatabase(String nombre, double precio, int cantidad, String imageUrl) {
        if (productoId != null) {
            Map<String, Object> updates = new HashMap<>();
            updates.put("nombre", nombre);
            updates.put("precio", precio);
            updates.put("cantidad", cantidad);
            if (imageUrl != null) updates.put("imageUrl", imageUrl);

            productosRef.child(productoId).updateChildren(updates)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al actualizar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Producto producto = new Producto(nombre, precio, cantidad, imageUrl);
            String newProductId = productosRef.push().getKey();

            if (newProductId != null) {
                productosRef.child(newProductId).setValue(producto)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Error al agregar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }
    }

    public void cancelar(View view) {
        finish();
    }
}
