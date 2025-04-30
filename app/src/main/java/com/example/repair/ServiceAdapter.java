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
    private boolean isSelectMode; // Modo selección para factura

    public interface OnServiceClickListener {
        void onServiceClick(Servicio servicio);
        void onServiceLongClick(Servicio servicio);
    }

    // Constructor para uso en ServiceActivity
    public ServiceAdapter(List<Servicio> servicios, OnServiceClickListener listener) {
        this.servicios = servicios;
        this.listener = listener;
        this.isSelectMode = false;
    }

    // Constructor alternativo para uso en otros contextos (como facturación)
    public ServiceAdapter(List<Servicio> servicios, OnServiceClickListener listener, boolean isSelectMode) {
        this.servicios = servicios;
        this.listener = listener;
        this.isSelectMode = isSelectMode;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = isSelectMode ? R.layout.item_service_select : R.layout.item_service_manage;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new ServiceViewHolder(view, isSelectMode);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Servicio servicio = servicios.get(position);
        holder.bind(servicio);
    }

    @Override
    public int getItemCount() {
        return servicios != null ? servicios.size() : 0;
    }

    public void updateServices(List<Servicio> newServices) {
        this.servicios = newServices;
        notifyDataSetChanged();
    }

    public void addService(Servicio servicio) {
        this.servicios.add(servicio);
        notifyItemInserted(servicios.size() - 1);
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription, tvPrice;
        Button btnSelect, btnEdit, btnDelete;

        public ServiceViewHolder(@NonNull View itemView, boolean isSelectMode) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvServiceName);
            tvDescription = itemView.findViewById(R.id.tvServiceDescription);
            tvPrice = itemView.findViewById(R.id.tvServicePrice);

            // Setup click listeners on the entire item
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onServiceClick(servicios.get(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onServiceLongClick(servicios.get(position));
                    return true;
                }
                return false;
            });

            // Setup buttons if they exist in the layout
            if (isSelectMode) {
                btnSelect = itemView.findViewById(R.id.btnSelectService);
                if (btnSelect != null) {
                    btnSelect.setOnClickListener(v -> {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && listener != null) {
                            listener.onServiceClick(servicios.get(position));
                        }
                    });
                }
            } else {
                btnEdit = itemView.findViewById(R.id.btnEditService);
                btnDelete = itemView.findViewById(R.id.btnDeleteService);

                if (btnEdit != null) {
                    btnEdit.setOnClickListener(v -> {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && listener != null) {
                            listener.onServiceClick(servicios.get(position));
                        }
                    });
                }

                if (btnDelete != null) {
                    btnDelete.setOnClickListener(v -> {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && listener != null) {
                            listener.onServiceLongClick(servicios.get(position));
                        }
                    });
                }
            }
        }

        public void bind(Servicio servicio) {
            tvName.setText(servicio.getNombre());
            tvDescription.setText(servicio.getDescripcion());
            tvPrice.setText(String.format(Locale.getDefault(), "$%.2f", servicio.getPrecio()));
        }
    }
}