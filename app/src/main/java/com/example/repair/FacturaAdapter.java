package com.example.repair;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FacturaAdapter extends RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder> {

    private List<Factura> facturas;

    public FacturaAdapter(List<Factura> facturas) {
        this.facturas = facturas;
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
        holder.bind(factura);
    }

    @Override
    public int getItemCount() {
        return facturas.size();
    }

    static class FacturaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumero, tvCliente, tvFecha, tvTotal;

        public FacturaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumero = itemView.findViewById(R.id.tvInvoiceNumber);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }

        public void bind(Factura factura) {
            tvNumero.setText("Factura #" + factura.getNumeroFactura());
            tvCliente.setText("Cliente: " + factura.getNombreCliente());
            tvFecha.setText("Fecha: " + factura.getFecha());
            tvTotal.setText("Total: $" + String.format("%.2f", factura.getTotal()));
        }
    }
}