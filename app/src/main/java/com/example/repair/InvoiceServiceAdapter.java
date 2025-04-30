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

public class InvoiceServiceAdapter extends RecyclerView.Adapter<InvoiceServiceAdapter.ServiceViewHolder> {

    private List<ItemServicio> servicios;
    private OnInvoiceServiceListener listener;

    public interface OnInvoiceServiceListener {
        void onDeleteService(int position);
    }

    public InvoiceServiceAdapter(List<ItemServicio> servicios, OnInvoiceServiceListener listener) {
        this.servicios = servicios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_invoice, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ItemServicio servicio = servicios.get(position);
        holder.bind(servicio, position);
    }

    @Override
    public int getItemCount() {
        return servicios.size();
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDescription, tvPrice;
        private Button btnDelete;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvServiceName);
            tvDescription = itemView.findViewById(R.id.tvServiceDescription);
            tvPrice = itemView.findViewById(R.id.tvServicePrice);
            btnDelete = itemView.findViewById(R.id.btnDeleteService);
        }

        public void bind(ItemServicio servicio, int position) {
            tvName.setText(servicio.getNombre());
            tvDescription.setText(servicio.getDescripcion());
            tvPrice.setText(String.format(Locale.getDefault(), "$%.2f", servicio.getPrecio()));

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteService(position);
                }
            });
        }
    }
}