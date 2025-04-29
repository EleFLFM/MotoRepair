package com.example.repair;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private List<Servicio> servicios;
    private OnServiceClickListener listener;

    public interface OnServiceClickListener {
        void onServiceClick(Servicio servicio);
        void onServiceLongClick(Servicio servicio);
    }

    public ServiceAdapter(List<Servicio> servicios, OnServiceClickListener listener) {
        this.servicios = servicios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Servicio servicio = servicios.get(position);
        holder.bind(servicio, listener);
    }

    @Override
    public int getItemCount() {
        return servicios.size();
    }

    public void updateData(List<Servicio> nuevosServicios) {
        servicios = nuevosServicios;
        notifyDataSetChanged();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDescripcion, tvPrecio;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreServicio);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionServicio);
            tvPrecio = itemView.findViewById(R.id.tvPrecioServicio);
        }

        public void bind(Servicio servicio, OnServiceClickListener listener) {
            tvNombre.setText(servicio.getNombre());
            tvDescripcion.setText(servicio.getDescripcion());
            tvPrecio.setText(String.format("$%.2f", servicio.getPrecio()));

            itemView.setOnClickListener(v -> listener.onServiceClick(servicio));
            itemView.setOnLongClickListener(v -> {
                listener.onServiceLongClick(servicio);
                return true;
            });
        }
    }
}