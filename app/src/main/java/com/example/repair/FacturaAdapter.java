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
    private OnFacturaClickListener listener;

    public interface OnFacturaClickListener {
        void onFacturaClick(Factura factura);
    }

    public FacturaAdapter(List<Factura> facturas, OnFacturaClickListener listener) {
        this.facturas = facturas;
        this.listener = listener;
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
        return facturas.size();
    }

    public void updateData(List<Factura> nuevasFacturas) {
        facturas = nuevasFacturas;
        notifyDataSetChanged();
    }

    static class FacturaViewHolder extends RecyclerView.ViewHolder {
        TextView tvCliente, tvFecha, tvTotal, tvEstado;

        public FacturaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }

        public void bind(Factura factura, OnFacturaClickListener listener) {
            tvCliente.setText(factura.getCliente());
            tvFecha.setText(factura.getFecha());
            tvTotal.setText(String.format("$%.2f", factura.getTotal()));
            tvEstado.setText(factura.getEstado());

            itemView.setOnClickListener(v -> listener.onFacturaClick(factura));
        }
    }
}