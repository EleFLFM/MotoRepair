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

public class InvoiceProductAdapter extends RecyclerView.Adapter<InvoiceProductAdapter.ProductViewHolder> {

    private List<ItemProduct> products;
    private OnInvoiceProductListener listener;

    public interface OnInvoiceProductListener {
        void onDeleteProduct(int position);
        void onUpdateQuantity(int position, int newQuantity);
    }

    public InvoiceProductAdapter(List<ItemProduct> products, OnInvoiceProductListener listener) {
        this.products = products;
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
        ItemProduct product = products.get(position);
        holder.bind(product, position);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvPrice, tvQuantity, tvSubtotal;
        private Button btnDelete, btnDecrease, btnIncrease;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvSubtotal = itemView.findViewById(R.id.tvProductSubtotal);
            btnDelete = itemView.findViewById(R.id.btnDeleteProduct);
//            btnDecrease = itemView.findViewById(R.id.btnDecreaseQuantity);
//            btnIncrease = itemView.findViewById(R.id.btnIncreaseQuantity);
        }

        public void bind(ItemProduct product, int position) {
            tvName.setText(product.getName());
            tvPrice.setText(String.format(Locale.getDefault(), "$%.2f", product.getUnitPrice()));
            tvQuantity.setText(String.valueOf(product.getQuantity()));
            tvSubtotal.setText(String.format(Locale.getDefault(), "$%.2f", product.getSubtotal()));

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteProduct(position);
                }
            });

            btnDecrease.setOnClickListener(v -> {
                int newQuantity = product.getQuantity() - 1;
                if (newQuantity > 0 && listener != null) {
                    listener.onUpdateQuantity(position, newQuantity);
                }
            });

            btnIncrease.setOnClickListener(v -> {
                int newQuantity = product.getQuantity() + 1;
                if (listener != null) {
                    listener.onUpdateQuantity(position, newQuantity);
                }
            });
        }
    }
}