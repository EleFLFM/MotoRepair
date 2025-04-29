package com.example.repair;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FacturaItemAdapter extends RecyclerView.Adapter<FacturaItemAdapter.ItemViewHolder> {

    private List<FacturaItem> items;

    public FacturaItemAdapter(List<FacturaItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_factura_producto, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        FacturaItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescripcion, tvPrecio, tvCantidad, tvSubtotal, tvTipo;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
            tvTipo = itemView.findViewById(R.id.tvTipo);
        }

        public void bind(FacturaItem item) {
            tvDescripcion.setText(item.getDescripcion());
            tvPrecio.setText(String.format("Precio: $%.2f", item.getPrecio()));
            tvCantidad.setText("Cantidad: " + item.getCantidad());
            tvSubtotal.setText(String.format("Subtotal: $%.2f", item.getSubtotal()));
            tvTipo.setText(item.getTipo().equals("servicio") ? "Servicio" : "Producto");
        }
    }
}