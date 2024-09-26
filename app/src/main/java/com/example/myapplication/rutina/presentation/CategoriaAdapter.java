package com.example.myapplication.rutina.presentation;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.rutina.data.models.Categoria;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {

    private List<Categoria> categoriaList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Categoria categoria);
        void onDeleteClick(Categoria categoria);
    }

    public CategoriaAdapter(List<Categoria> categoriaList, OnItemClickListener listener) {
        this.categoriaList = categoriaList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categoria_ejercicio, parent, false);
        return new CategoriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        Categoria categoria = categoriaList.get(position);
        holder.tvNombre.setText(categoria.getNombre());
        holder.tvDescripcion.setText(categoria.getDescripcion());

        // Acción para el botón de editar
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(categoria));

        // Acción para el botón de eliminar
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(categoria));
    }

    @Override
    public int getItemCount() {
        return categoriaList.size();
    }

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre, tvDescripcion;
        ImageButton btnEdit, btnDelete;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_nombre_categoria);
            tvDescripcion = itemView.findViewById(R.id.tv_descripcion_categoria);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
    public void updateData(List<Categoria> nuevasCategorias) {
        this.categoriaList = nuevasCategorias;
        notifyDataSetChanged(); // Notificar al RecyclerView que los datos han cambiado
    }

}
