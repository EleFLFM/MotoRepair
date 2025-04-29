package com.example.repair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Obtener referencia a los CardView
        CardView cardInventory = findViewById(R.id.card_inventory);
        CardView cardProducts = findViewById(R.id.card_products);
        CardView cardServices = findViewById(R.id.card_services);
        CardView cardBilling = findViewById(R.id.card_billing);

        // Configurar listeners para cada card
        cardInventory.setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, InventarioActivity.class));
        });

        cardProducts.setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, ProductsActivity.class));
        });

        cardServices.setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, ServiceActivity.class));
        });

        cardBilling.setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, FacturacionActivity.class));
        });
    }
}