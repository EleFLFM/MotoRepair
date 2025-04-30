package com.example.repair;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Producto> productos;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Producto producto);
        void onProductLongClick(Producto producto);
    }

    public ProductAdapter(List<Producto> productos, OnProductClickListener listener) {
        this.productos = productos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_invoice, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.bind(producto);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvPrice, tvQuantity;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvProductQuantity);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onProductClick(productos.get(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onProductLongClick(productos.get(position));
                    return true;
                }
                return false;
            });
        }

        public void bind(Producto producto) {
            tvName.setText(producto.getNombre());
            tvPrice.setText(String.format(Locale.getDefault(), "$%.2f", producto.getPrecio()));
            tvQuantity.setText(String.valueOf(producto.getCantidad()));
        }
    }
}