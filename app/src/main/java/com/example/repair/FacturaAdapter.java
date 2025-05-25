package com.example.repair;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class FacturaAdapter extends RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder> {

    private List<Factura> facturas;
    private OnFacturaClickListener listener;

    public interface OnFacturaClickListener {
        void onFacturaClick(Factura factura);
        void onFacturaLongClick(Factura factura);
    }

    public FacturaAdapter(List<Factura> facturas, OnFacturaClickListener listener) {
        this.facturas = facturas;
        this.listener = listener;
    }

    public void updateData(List<Factura> nuevasFacturas) {
        this.facturas = nuevasFacturas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FacturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_factura, parent, false);
        return new FacturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacturaViewHolder holder, int position) {
        Factura factura = facturas.get(position);
        holder.bind(factura, listener);
    }

    @Override
    public int getItemCount() {
        return facturas != null ? facturas.size() : 0;
    }

    static class FacturaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumero, tvCliente, tvFecha, tvTotal, tvProductos, tvServicios;

        public FacturaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumero = itemView.findViewById(R.id.tvInvoiceNumber);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvProductos = itemView.findViewById(R.id.tvProductosCount);
            tvServicios = itemView.findViewById(R.id.tvServiciosCount);
        }

        public void bind(Factura factura, OnFacturaClickListener listener) {
            tvNumero.setText(String.format("Factura #%s", factura.getNumeroFactura()));
            tvCliente.setText(factura.getNombreCliente());
            tvFecha.setText(factura.getFecha());
            tvTotal.setText(String.format(Locale.getDefault(), "$%.2f", factura.getTotal()));

            // Mostrar conteo de productos y servicios
            int numProductos = factura.getProductos() != null ? factura.getProductos().size() : 0;
            int numServicios = factura.getServicios() != null ? factura.getServicios().size() : 0;

            tvProductos.setText(String.format("%d productos", numProductos));
            tvServicios.setText(String.format("%d servicios", numServicios));

            // Configurar clicks
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFacturaClick(factura);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onFacturaLongClick(factura);
                }
                return true;
            });
        }
    }
}