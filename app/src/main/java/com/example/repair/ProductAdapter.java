package com.example.repair;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

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
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.bind(producto, listener);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio, tvCantidad;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProducto);
            tvPrecio = itemView.findViewById(R.id.tvPrecioProducto);
            tvCantidad = itemView.findViewById(R.id.tvCantidadProducto);
        }

        public void bind(Producto producto, OnProductClickListener listener) {
            tvNombre.setText(producto.getNombre());
            tvPrecio.setText(String.format("$%.2f", producto.getPrecio()));
            tvCantidad.setText("Stock: " + producto.getCantidad());

            itemView.setOnClickListener(v -> listener.onProductClick(producto));
            itemView.setOnLongClickListener(v -> {
                listener.onProductLongClick(producto);
                return true;
            });
        }
    }
}