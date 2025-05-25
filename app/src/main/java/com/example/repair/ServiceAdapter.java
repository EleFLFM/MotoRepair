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

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private List<Servicio> servicios;
    private OnServiceClickListener listener;
    private boolean deleteMode = false;

    public interface OnServiceClickListener {
        void onServiceClick(Servicio servicio);
        void onServiceLongClick(Servicio servicio);
        void onDeleteService(Servicio servicio, int position);
    }

    public ServiceAdapter(List<Servicio> servicios, OnServiceClickListener listener) {
        this.servicios = servicios;
        this.listener = listener;
    }

    public void toggleDeleteMode() {
        deleteMode = !deleteMode;
        notifyDataSetChanged();
    }

    public boolean isDeleteMode() {
        return deleteMode;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_manage, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Servicio servicio = servicios.get(position);
        holder.bind(servicio, deleteMode);
    }

    @Override
    public int getItemCount() {
        return servicios.size();
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription, tvPrice;
        Button btnEdit, btnDelete;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvServiceName);
            tvDescription = itemView.findViewById(R.id.tvServiceDescription);
            tvPrice = itemView.findViewById(R.id.tvServicePrice);
            btnEdit = itemView.findViewById(R.id.btnEditService);
            btnDelete = itemView.findViewById(R.id.btnDeleteService);
        }

        public void bind(Servicio servicio, boolean deleteMode) {
            tvName.setText(servicio.getNombre());
            tvDescription.setText(servicio.getDescripcion());
            tvPrice.setText(String.format(Locale.getDefault(), "$%.2f", servicio.getPrecio()));

            // Mostrar/ocultar botones según el modo
            btnEdit.setVisibility(deleteMode ? View.GONE : View.VISIBLE);
            btnDelete.setVisibility(deleteMode ? View.VISIBLE : View.GONE);

            // Configurar listeners
            itemView.setOnClickListener(v -> {
                if (!deleteMode && listener != null) {
                    listener.onServiceClick(servicio);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onServiceLongClick(servicio);
                    return true;
                }
                return false;
            });

            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onServiceClick(servicio);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteService(servicio, getAdapterPosition());
                }
            });
        }
    }
}